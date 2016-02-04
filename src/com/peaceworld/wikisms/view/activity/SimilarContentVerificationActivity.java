package com.peaceworld.wikisms.view.activity;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.controller.utility.Utility;
import com.peaceworld.wikisms.model.dao.ContentNotificationFullDao;
import com.peaceworld.wikisms.model.entity.ContentNotification;
import com.peaceworld.wikisms.model.entity.SimilarContent;
import com.peaceworld.wikisms.view.newui.MenuAnimManager;
import com.peaceworld.wikisms.view.newui.dialog.CustomListDialog;


public class SimilarContentVerificationActivity extends SherlockActivity {

	protected ContentViewerAdapter adapter;
	private Drawable itemBg1, itemBg2;	
	private List<SimilarContent>similarContentList;
	private int currentIndex;
	private View currentView;  
	
	private int TextColor=0, TextSize=0;
	private Typeface typeface;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_similarity_check_list);
		
		TextView header=(TextView)findViewById(R.id.similarityCheckHeaderTextView);
		header.setText(getResources().getString(R.string.similarity_check_activity_header));
		
		itemBg1 = getResources().getDrawable(
				R.drawable.content_view_list_item_background1);
		itemBg2 = getResources().getDrawable(
				R.drawable.content_view_list_item_background2);
		ListView gridView = (ListView) findViewById(R.id.contentViewListView);
		
		adapter = new ContentViewerAdapter();
		gridView.setAdapter(adapter);
		
		onNewIntent(getIntent());
		
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		List<SimilarContent> tmp_similarContentList=Settings.poolSililarContentList(this);
		if(similarContentList==null)
		similarContentList=tmp_similarContentList;
		else if(tmp_similarContentList!=null)
			similarContentList.addAll(tmp_similarContentList);
			
		adapter.notifyDataSetChanged();

	}
	
	@Override
	public void onResume() {
		super.onResume();

		SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(this);
		String textFont = shpref.getString("TEXTFONT", "BTabssom.ttf");
		TextColor = shpref.getInt("TEXTCOLOR",getResources().getColor(R.color.default_text_color));
		TextSize = Integer.parseInt(shpref.getString("TEXTSIZE", "20"));
		typeface = Typeface.createFromAsset(getAssets(),"fonts/" + textFont);
	}
	
	private void accepted()
	{
		
		ContentNotification cn = similarContentList.get(currentIndex).Cn;
		cn.setAction(ContentNotification.ACTION.CONTENT_CREATE.name());
		cn.setCreatorUser(Utility.getUserIdentifier(this));
		ContentNotificationFullDao cnDao=new ContentNotificationFullDao(this);
		cnDao.insert(cn);
		denied();
		
	}
	
	private void denied()
	{
		similarContentList.remove(currentIndex);
		adapter.notifyDataSetChanged();
	}
	
	public void removeView(final boolean accept)
	{
		AnimationSet animSet=MenuAnimManager.getSmsSentAnimation();
		if(currentView==null)
			return;
		animSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(accept)
					accepted();
				else
					denied();
			}
		});
		currentView.startAnimation(animSet);
		
		
	}

	public class ContentViewerAdapter extends BaseAdapter {

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public int getCount() {
			if (similarContentList != null)
				return similarContentList.size();
			return 0;
		}

		@Override
		public Object getItem(int index) {
			if (similarContentList != null)
				return similarContentList.get(index);
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {

			view = getLayoutInflater().inflate(
					R.layout.similar_content_verification_list_itme, null);
			TextView contentTextView = (TextView) view
					.findViewById(R.id.ContentViewListItemTextView);
			TextView metaInfoTextView = (TextView) view
					.findViewById(R.id.metaInfoTextView);
			if (index % 2 == 0)
				contentTextView.setBackgroundDrawable(itemBg1);
			else
				contentTextView.setBackgroundDrawable(itemBg2);
			
			contentTextView.setTextColor(TextColor);
			contentTextView.setTextSize(TextSize);
			
			if (typeface != null)
				contentTextView.setTypeface(typeface);
			
			contentTextView.setText(similarContentList.get(index).Cn.getMetaInfo()+"\r\n\r\n");
			String info= " با "+similarContentList.get(index).SimilarContentList.size()+
					" پیامک موجود شباهت دارد. برای مشاهده جزییات کلیک کنید ";
			metaInfoTextView.setText(info);
			view.setTag(index);
			view.setOnClickListener(onClickListener);
			
			return view;
		}
		
		OnClickListener onClickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				int index=(Integer)v.getTag();
				CustomListDialog diaog = new CustomListDialog(SimilarContentVerificationActivity.this, similarContentList.get(index),SimilarContentVerificationActivity.this);
				diaog.show();
				currentIndex=index;
				currentView=v;
			}
		};

	}



}
