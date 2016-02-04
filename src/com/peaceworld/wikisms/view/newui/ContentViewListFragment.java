package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.rs.common.SystemService;
import com.peaceworld.wikisms.controller.rs.manager.ContentManeger;
import com.peaceworld.wikisms.controller.rs.manager.ContentManeger.ContentDownloaderMessages;
import com.peaceworld.wikisms.controller.services.ContentDownloaderListener;
import com.peaceworld.wikisms.controller.utility.SearchContentResultListener;
import com.peaceworld.wikisms.controller.utility.SearchContents;
import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.view.newui.dialog.ContentDetailViewDialog;


public class ContentViewListFragment extends Fragment implements SearchContentResultListener {
	
	public static enum MOD{normal, searchResult, staredContent}
	public static enum StaredContentType{Newest,MostLiked,MostVisited,favorits,randomContents}
	
	private ArrayList<Content> ContentList;
	private Content dummyContent=new Content();
	private ContentFullDao contentFullDao;
	private ContentViewerAdapter adapter;
	private long categoryLocalId;
	private Drawable itemBg1,itemBg2;
	private int TextColor=0, TextSize=0;
	private Typeface typeface;
	private MOD mod;
	private StaredContentType staredContentType;
	private String searchKey="";
	private ListView listView;
	private boolean showToasts=false;
	private int loadListCounter=10;
	
	public ContentViewListFragment() {
		this(0);
	}
	
	public ContentViewListFragment(StaredContentType staredContentType) {
		setRetainInstance(true);
		mod=MOD.staredContent;
		this.staredContentType=staredContentType;
	}
	
	public ContentViewListFragment(long categoryLocalId) {
		setRetainInstance(true);
		this.categoryLocalId=categoryLocalId;
		mod=MOD.normal;
	}
	public ContentViewListFragment(ArrayList<Content>contentList,String searchKey) {
		setRetainInstance(true);
		mod=MOD.searchResult;
		this.ContentList=contentList;
		this.searchKey=searchKey;
		this.ContentList.add(dummyContent);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();

		SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String textFont = shpref.getString("TEXTFONT", "BTabssom.ttf");
		TextColor = shpref.getInt("TEXTCOLOR",getResources().getColor(R.color.default_text_color));
		TextSize = Integer.parseInt(shpref.getString("TEXTSIZE", "20"));
		typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/" + textFont);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_content_view_list, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		itemBg1=getResources().getDrawable(R.drawable.content_view_list_item_background1);
		itemBg2=getResources().getDrawable(R.drawable.content_view_list_item_background2);
		listView=(ListView)getActivity().findViewById(R.id.contentViewListView); 
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int status) {
				switch (status) {
				case SCROLL_STATE_IDLE:
					((ContentPreviewFragmentActivity)(getActivity())).getSupportActionBar().show();
					break;
				default:
					((ContentPreviewFragmentActivity)(getActivity())).getSupportActionBar().hide();
					break;
				}
				
			}
			
			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				
			}
		});
		
		contentFullDao=new ContentFullDao(getActivity());
		
		showToasts=false;
		if(mod!=MOD.searchResult)
		{
			AsyncTaskLoadMoreContents contentLoader=new AsyncTaskLoadMoreContents(categoryLocalId,getActivity());
			contentLoader.execute();
		}
		//loadMoreToContentList();
		
		adapter=new ContentViewerAdapter();
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	//	outState.putInt("mColorRes", mColorRes);
	}
	
	public void notifyDataSetChanged(int position)
	{
		
		if(adapter!=null)
			adapter.notifyDataSetChanged();
		
		if(position<0)
			listView.smoothScrollToPosition(0);
		if(position>=ContentList.size())
			listView.smoothScrollToPosition(ContentList.size());
	}
	
	public boolean loadMoreToContentList()
	{
		if(ContentList==null || ContentList.size()==1)
		{
			ContentList=new ArrayList<Content>(100);
		}
		List<Content> moreList = null;
		if(mod==MOD.normal)
			moreList=contentFullDao.getAllContentByCategoryIdSortedFiltered(categoryLocalId,ContentList,loadListCounter);
		else if(mod==MOD.staredContent)
		{
			switch (staredContentType) {
			case MostLiked:
				moreList=contentFullDao.getAllContentFilteredSorted(ContentList, 20, false, true, false);
				break;
			case MostVisited:
				moreList=contentFullDao.getAllContentFilteredSorted(ContentList,20,true,false,false);
				break;
			case Newest:
				moreList=contentFullDao.getAllContentFilteredSorted(ContentList,20,false,false,true);
				break;
			case favorits:
				moreList=contentFullDao.getAllFavoritsContent(ContentList,100);
				break;
			case randomContents:
				moreList=contentFullDao.getRandomContents(ContentList,100);
				break;
			}
		}
		if(moreList==null || moreList.size()<1)
			return false;
		ContentList.remove(dummyContent);
		ContentList.addAll(moreList);
		ContentList.add(dummyContent);
		
		return true;
	}	
		
	public boolean searchMoreToContentList()
	{
		
		long fromId=0;
		for(Content c:ContentList)
			if(c.getId()>fromId)
				fromId=c.getId();

		SearchContents searchContents=new SearchContents(getActivity(), fromId+1, this);
		searchContents.execute(searchKey);
		return true;
	}
	
	public class ContentViewerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(ContentList!=null)
				return ContentList.size();
			return 0;
		}

		@Override
		public Object getItem(int index) {
			if(ContentList!=null)
				return ContentList.get(index);
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			if(ContentList!=null)
				return ContentList.get(arg0).getId();
			return 0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			
			Content content = ContentList.get(index);
			view = getActivity().getLayoutInflater().inflate(
					R.layout.fragment_content_view_list_itme, null);
			TextView contentTextView = (TextView) view
					.findViewById(R.id.ContentViewListItemTextView);

			if (index % 2 == 0)
				contentTextView.setBackgroundDrawable(itemBg1);
			else
				contentTextView.setBackgroundDrawable(itemBg2);

			if (content==dummyContent) {
				
				String info="برای نمایش ادامه پیامک ها کلیک کنید";
				contentTextView.setText(Html.fromHtml("<b><em>" + info + "</b></em>"));
				contentTextView.setTextColor(getResources().getColor(R.color.blue_text));
				contentTextView.setOnClickListener(onclickListener_loadMore);
			}
			else
			{
				
				contentTextView.setTextColor(TextColor);
				contentTextView.setTextSize(TextSize);
				
				if (typeface != null)
					contentTextView.setTypeface(typeface);

				contentTextView.setText(content.getPlainText());
				contentTextView.setOnClickListener(onclickListener);
				contentTextView.setTag(index);
			}
			return view;
		}
		
		
		private OnClickListener onclickListener_loadMore= new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AsyncTaskLoadMoreContents contentLoader=new AsyncTaskLoadMoreContents(categoryLocalId,getActivity());
				contentLoader.execute();
			}
		};
	private OnClickListener onclickListener= new OnClickListener() {
			
			@Override      
			public void onClick(View v) {      
    
				
				 FragmentTransaction ft = getFragmentManager().beginTransaction();
				 
				 int index=(Integer)v.getTag();
				 
				 // Create and show the dialog.
				 ContentDetailViewDialog newFragment = 
						 new ContentDetailViewDialog(ContentViewListFragment.this,ContentList,ContentViewListFragment.this.categoryLocalId,index);
				 newFragment.show(ft, "dialog");
				
				
			}
		};
	}

	@Override
	public void searchContentDone(ArrayList<Content> searchResult,
			String searchKey) {
		
		ContentList.remove(dummyContent);
		ContentList.addAll(searchResult);
		ContentList.add(dummyContent);
		
		if(adapter!=null && searchResult!=null && searchResult.size()>0)
			adapter.notifyDataSetChanged();
	}
	
	class AsyncTaskLoadMoreContents extends AsyncTask<Void,Void,Void>
	{
		private long categoryId;
		private Context context;
		private int limit=100;
		private ContentDownloaderMessages message;
		public AsyncTaskLoadMoreContents(long categoryId,Context context)
		{
			this.categoryId=categoryId;
			this.context=context;
		}
		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog=new ProgressDialog(context);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("لطفا چند لحظه صبر کنید...");
			dialog.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			

			boolean hasMore=true;
			if(mod==MOD.normal || mod==MOD.staredContent)
				hasMore=loadMoreToContentList();
			else if(mod==MOD.searchResult )
			{
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						searchMoreToContentList();						
					}
				});
				return null;
			}
			
			
			if(hasMore)
				return null;
			boolean serverConnection= SystemService.CheckConnectionGet();
			if(!serverConnection){
				message=ContentDownloaderMessages.INTERNET_CONNECTION_ERROR;
				return null;
			}
			
			ContentManeger.downloadAndReturnNewContents(categoryId, limit, context, contentDownloaderListener);
			
			if(this.message!=null && message==ContentDownloaderMessages.SUCCESSFUL)
				loadMoreToContentList();
			return null;
		}
		
		protected void onPostExecute(Void result) {
			
			dialog.dismiss();
			if(message!=null && showToasts)
			{
				switch (message) {
				case NO_MORE_CONTENT:
					Toast.makeText(getActivity(), "پیامک های بیشتری در این گروه موجود نیست", Toast.LENGTH_LONG).show();
					break;
				case UNDIFINED_CATEGORY :
					System.out.println("big error while downloading contents");
					break;
				case SERVER_CONNECTION_ERROR :
					Toast.makeText(getActivity(), "خطا در برقراری ارتباط با سرور، لطفا مجددا تلاش نمایید", Toast.LENGTH_LONG).show();
					break;
				case INTERNET_CONNECTION_ERROR :
					Toast.makeText(getActivity(), "برای مشاهده پیامک های بیشتر، اتصال به اینترنت را فعال نمایید", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}
			if(adapter!=null)
				adapter.notifyDataSetChanged();
			
			showToasts=true;
		
		};
		
		ContentDownloaderListener contentDownloaderListener=new ContentDownloaderListener() {
			
			@Override
			public void finished(ContentDownloaderMessages message) {
				AsyncTaskLoadMoreContents.this.message=message;
			}
		};
	}

}
