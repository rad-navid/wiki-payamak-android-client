package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryDao;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryTable;
import com.peaceworld.wikisms.view.newui.ContentViewListFragment.StaredContentType;

public class ContentCategoryExplorerFragment extends ListFragment {
	
	private List<ContentCategory> CategoryList;
	private List<String> GroupList;
	private List<Integer> GroupListIcons;
	private CategoryListExpelorerAdpter adapter;
	private ContentCategoryDao dao;
	private long currentCategoryId=-1;
	private ContentCategory dummyContentCategory=new ContentCategory();
	private boolean preDefinedList=false;
	
	public ContentCategoryExplorerFragment() {
		setRetainInstance(true);
	}
	
	public ContentCategoryExplorerFragment(long initParentId) {
		this();
		currentCategoryId=initParentId;
	}
	
	public ContentCategoryExplorerFragment(boolean preDefinedList) {
		this();
		this.preDefinedList=true;
		GroupList=new ArrayList<String>(6);
		GroupList.add("...");
		GroupList.add("پسندیده شده ترین ها");
		GroupList.add("جدید ترین ها");
		GroupList.add("پر بازدید ترین ها");
		GroupList.add("قاطی پاتی");
		GroupList.add("مورد علاقه من");
		
		
		GroupListIcons = new ArrayList<Integer>(GroupList.size());
		GroupListIcons.add(R.drawable.ic_action_sort_by_like);
		GroupListIcons.add(R.drawable.ic_action_sort_by_like);
		GroupListIcons.add(R.drawable.ic_action_sort_by_time);
		GroupListIcons.add(R.drawable.ic_action_sort_by_view);
		GroupListIcons.add(R.drawable.ic_action_random);
		GroupListIcons.add(R.drawable.favorite);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v=inflater.inflate(R.layout.fragment_menu_simple_list, container,false);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(!preDefinedList)
			CategoryList=loadContentCategoryList(currentCategoryId);
		adapter=new CategoryListExpelorerAdpter();
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if(position==0)
			moveUp();
		else
			moveForwarde(position);
	}
	
	// the meat of switching the above fragment
		private void switchFragment(Fragment fragment) {
			if (getActivity() == null)
				return;

			if (getActivity() instanceof ContentPreviewFragmentActivity) {
				ContentPreviewFragmentActivity ra = (ContentPreviewFragmentActivity) getActivity();
				ra.switchContent(fragment, false);
			}
		}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	
	private ArrayList<ContentCategory> loadContentCategoryList(long parentCategoryId)
	{
		if(dao==null)
			dao= new ContentCategoryDao(getActivity());
		
		String where=ContentCategoryTable.Columns.PARENTCATEGORY+"="+ parentCategoryId;
		if(parentCategoryId<=0)
			where=ContentCategoryTable.Columns.PARENTCATEGORY+"="+ 0 ;
		
		where+=" AND ("+ContentCategoryTable.Columns.ACCEPTED+"=1 OR "
		+ContentCategoryTable.Columns.SELFDEFINED+"=1)";
		
		ArrayList<ContentCategory> catsList=(ArrayList<ContentCategory>) dao.asList(dao.query(where, null));
		
		catsList.add(0, dummyContentCategory);
		return catsList;
	}
	
	public class CategoryListExpelorerAdpter extends BaseAdapter{
		
		@Override
		public int getCount() {
			if(CategoryList!=null && !preDefinedList)
				return CategoryList.size();
			else if(preDefinedList)
				return GroupList.size();
			return 0;
		}

		@Override
		public Object getItem(int index) {
			if(CategoryList!=null && !preDefinedList)
				return CategoryList.get(index);
			else if(preDefinedList)
				return GroupList.get(index);
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			if(CategoryList!=null && !preDefinedList)
				return CategoryList.get(arg0).getId();
			else if(preDefinedList)
				return arg0;
			
			return 0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			
			String name="";
			if(CategoryList!=null && !preDefinedList)
				name=CategoryList.get(index).getName();
			else if(preDefinedList)
				name=GroupList.get(index);
			
			view=View.inflate(getActivity(), R.layout.fragment_menu_content_category_explorer_list_itme, null);
			TextView categoryName=(TextView)view.findViewById(R.id.CategoryNameListItemTextView);
			int textSize=getResources().getInteger(R.integer.content_category__browser_font_size);
			categoryName.setTextSize(textSize);
			
			ImageView imgRight=(ImageView)view.findViewById(R.id.rightArrowImageView);
			imgRight.setImageResource(R.drawable.ic_action_forward);
			if(index!=0 && !preDefinedList)
				imgRight.setVisibility(View.INVISIBLE);
			else if(index!=0 && preDefinedList)
				imgRight.setImageResource(GroupListIcons.get(index));
			
			if(index==0)
			{
				name=" . . . ";
				ImageView imgLeft  =(ImageView)view.findViewById(R.id.leftArrowImageView);
				imgLeft.setImageResource(R.drawable.home);
				imgLeft.setVisibility(View.VISIBLE);
				imgLeft.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						switchMenu(new MainMenuFragment());
					}
				});
			}
			
			categoryName.setText(name);
			
			Animation animation = MenuAnimManager.getAnimation(view);
			animation.setStartOffset(index*50);
			view.startAnimation(animation);
			return view;
		}
		
		
	}	

	
	private void moveForwarde(int position)
	{
		try{
			switchContact(position);
			
			if(!preDefinedList)
			{
				ContentCategory category=CategoryList.get(position);
				((ContentPreviewFragmentActivity)(getActivity())).getSupportActionBar().setTitle(category.getName());
				
				ArrayList<ContentCategory> result=loadContentCategoryList(category.getId());
				if(result!=null && result.size()>1)
				{
					currentCategoryId=category.getId();
					CategoryList=result;
					adapter.notifyDataSetChanged();
				}
			}
			
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	private void moveUp()
	{
		if(preDefinedList)
		{
			switchMenu(new MainMenuFragment());
			return;
		}
		
		ContentCategory parent=dao.get(currentCategoryId);
		if(parent==null)
		{
			switchMenu(new MainMenuFragment());
			((ContentPreviewFragmentActivity)(getActivity())).getSupportActionBar().setTitle(R.string.app_name);
			return;
		}
		
		try{
			ArrayList<ContentCategory> result=null;
			if(parent.getParentCategory()==0)
			{
				result=loadContentCategoryList(0);
				((ContentPreviewFragmentActivity)(getActivity())).getSupportActionBar().setTitle(R.string.app_name);
			}
			else
			{
				result=loadContentCategoryList(parent.getParentCategory());
				((ContentPreviewFragmentActivity)(getActivity())).getSupportActionBar().setTitle(parent.getName());
			}
			
			if(result!=null && result.size()>0)
			{
				currentCategoryId=parent.getParentCategory();
				CategoryList=result;
				adapter.notifyDataSetChanged();
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		
	}
	
	private void switchMenu(Fragment fragment) {

		if (getActivity() == null)
			return;
		if (getActivity() instanceof ContentPreviewFragmentActivity) {
			ContentPreviewFragmentActivity ra = (ContentPreviewFragmentActivity) getActivity();
			ra.switchMenu(fragment);
		}
	}
	
	private void switchContact(int position)
	{
		Fragment newContent;
		if(!preDefinedList)
		{
			ContentCategory category=CategoryList.get(position);
			newContent = new ContentViewListFragment(category.getId());
		}
		else
		{
			StaredContentType mod = null;
			if(position==1)
			mod=StaredContentType.MostLiked;
			else if(position==2)
				mod=StaredContentType.Newest;
			else if(position==3)
				mod=StaredContentType.MostVisited;
			else if(position==4)
				mod=StaredContentType.randomContents;
			else if(position==5)
				mod=StaredContentType.favorits;
			newContent = new ContentViewListFragment(mod);
		}
			
		if (newContent != null)
			switchFragment(newContent);
	}
}
