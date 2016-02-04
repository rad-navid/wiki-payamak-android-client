package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.view.activity.CategoryManipulationainActivity;
import com.peaceworld.wikisms.view.newui.dialog.SimpleOneInputDialog;
import com.peaceworld.wikisms.view.preference.SettingPreferenceActivity;

public class MainMenuFragment extends ListFragment {
	
	private List<String> mainMenuList;
	private List<String> ActionList;
	private List<Integer> mainMenuListIcon;
	private CategoryListExpelorerAdpter adapter;
	
	public MainMenuFragment() {
		setRetainInstance(true);
		mainMenuList=new ArrayList<String>(7);
		mainMenuList.add("پیامک های منتخب");
		mainMenuList.add("مرور پیامک ها");
		mainMenuList.add("ویرایش گروه ها");
		mainMenuList.add("ارسال پیامک");
		mainMenuList.add("نظر ها / پیشنهاد ها");
		mainMenuList.add("رای به ما در بازار");
		mainMenuList.add("تنظیمات");
		mainMenuList.add("راهنما");
		ActionList=mainMenuList;
		
		mainMenuListIcon=new ArrayList<Integer>(mainMenuList.size());
		mainMenuListIcon.add(R.drawable.ic_action_important);
		mainMenuListIcon.add(R.drawable.ic_action_view_as_list);
		mainMenuListIcon.add(R.drawable.ic_action_edit);
		mainMenuListIcon.add(R.drawable.ic_action_new_email);
		mainMenuListIcon.add(R.drawable.ic_action_new_label);
		mainMenuListIcon.add(R.drawable.ic_action_favorite);
		mainMenuListIcon.add(R.drawable.ic_action_settings);
		mainMenuListIcon.add(R.drawable.ic_action_help);
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v=inflater.inflate(R.layout.fragment_menu_simple_list, container,false);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter=new CategoryListExpelorerAdpter();
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		switch(position)
		{
		case 0:
			switchMenu(new ContentCategoryExplorerFragment(true));
			break;
		case 1:
			switchMenu(new ContentCategoryExplorerFragment());
			break;
		case 2:
			Intent intent=new Intent(getActivity(),CategoryManipulationainActivity.class);
			startActivity(intent);
			break;
		case 3:
			switchMenu(new SearchSmsMenuFragment());break;
		case 4:
			SimpleOneInputDialog commentDialog= new SimpleOneInputDialog(0, SimpleOneInputDialog.TYPE.COMMENTS, null);
			commentDialog.show(getFragmentManager(), "");
			break;
		case 5:
			try
			{
				Intent voteIntent=new Intent(Intent.ACTION_EDIT,Uri.parse("http://cafebazaar.ir/app/?id=com.peaceworld.wikisms"));
				startActivity(voteIntent);
			}
			catch (Exception e) {
				//e.printStackTrace();
				try{
					
					Intent voteIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://cafebazaar.ir/app/?id=com.peaceworld.wikisms"));
					startActivity(voteIntent);
					
					e.printStackTrace();
					
				}catch(Exception ex)
				{
					
				}
			}
			break;
		case 6:
			
			ArrayList<String>sharedList=new ArrayList<String>(0);
			Fragment blankFrag = new SmsViewListFragment(sharedList);
			switchContent(blankFrag, true);
			
			Intent settingIntent=new Intent(getActivity(),SettingPreferenceActivity.class);
			startActivity(settingIntent);
			break;
			
		case 7:
			switchContent(new HelpListFragment(), true);
			
		}
	}
	
	// the meat of switching the above fragment
		private void switchMenu(Fragment fragment) {
			
			if (getActivity() == null)
				return;

			if (getActivity() instanceof ContentPreviewFragmentActivity) {
				ContentPreviewFragmentActivity ra = (ContentPreviewFragmentActivity) getActivity();
				ra.switchMenu(fragment);
			}
		}
		private void switchContent(Fragment fragment, boolean toggle) {
			
			if (getActivity() == null)
				return;

			if (getActivity() instanceof ContentPreviewFragmentActivity) {
				ContentPreviewFragmentActivity ra = (ContentPreviewFragmentActivity) getActivity();
				ra.switchContent(fragment, toggle);
			}
		}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	
	
	public class CategoryListExpelorerAdpter extends BaseAdapter{

		
		@Override
		public int getCount() {
			if(ActionList!=null)
				return ActionList.size();
			return 0;
		}

		@Override
		public Object getItem(int index) {
			if(ActionList!=null)
				return ActionList.get(index);
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			
			view=View.inflate(getActivity(), R.layout.fragment_main_menu_list_itme, null);
			TextView actionName=(TextView)view.findViewById(R.id.actionNameTextView);
			int textSize=getResources().getInteger(R.integer.content_category__browser_font_size);
			actionName.setTextSize(textSize);
			actionName.setText(ActionList.get(index));
			ImageView actionImage=(ImageView)view.findViewById(R.id.actionImageView);
			actionImage.setImageResource(mainMenuListIcon.get(index));
			if(index==0)
				actionName.setTextColor(getResources().getColor(R.color.blue_divider));
			
			Animation animation = MenuAnimManager.getAnimation(view);
			animation.setStartOffset(index*50);
			view.startAnimation(animation);
			return view;
		}
		
		
	}

}
