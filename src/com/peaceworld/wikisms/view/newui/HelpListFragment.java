package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.peaceworld.wikisms.R;

public class HelpListFragment extends Fragment {

	private ArrayList<Integer> iconList,infoList;
	private HelpAdapter adapter;
	private Drawable itemBg1, itemBg2;
	private int TextColor=0, TextSize=0;
	private Typeface typeface;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		initLists();
		return inflater.inflate(R.layout.fragment_content_view_list, container,	false);
	}

	private void initLists() {
		infoList=new ArrayList<Integer>();
		iconList=new ArrayList<Integer>();
		
		iconList.add(R.drawable.edit);
		infoList.add(R.string.edit_content_help);
		
		iconList.add(R.drawable.change);
		infoList.add(R.string.change_content_help);
		
		iconList.add(R.drawable.extension);
		infoList.add(R.string.extension_content_help);
		
		iconList.add(R.drawable.ic_action_new_email);
		infoList.add(R.string.new_content_help);
		
		iconList.add(R.drawable.favorite);
		infoList.add(R.string.favorite_content_help);
		
		iconList.add(R.drawable.compress);
		infoList.add(R.string.compress_content_help);
		
		iconList.add(R.drawable.share);
		infoList.add(R.string.share_content_help);
		
		iconList.add(R.drawable.like);
		infoList.add(R.string.like_content_help);
		
		iconList.add(R.drawable.view_blue);
		infoList.add(R.string.view_content_help);
		
		iconList.add(R.drawable.send_sms);
		infoList.add(R.string.send_sms_content_help);
		
		iconList.add(R.drawable.ic_action_new);
		infoList.add(R.string.new_category_help);
		
		iconList.add(R.drawable.ic_action_merge);
		infoList.add(R.string.merge_category_help);
		
		iconList.add(R.drawable.ic_action_cut);
		infoList.add(R.string.cut_category_help);
		
		iconList.add(R.drawable.ic_action_unselect_all);
		infoList.add(R.string.unselect_all_category_help);
		

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		itemBg1 = getResources().getDrawable(
				R.drawable.content_view_list_item_background1);
		itemBg2 = getResources().getDrawable(
				R.drawable.content_view_list_item_background2);
		ListView ListView = (ListView) getActivity().findViewById(
				R.id.contentViewListView);
		
		ListView.setOnScrollListener(new OnScrollListener() {
			
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
		
		adapter = new HelpAdapter();
		ListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putInt("mColorRes", mColorRes);
	}

	public class HelpAdapter extends BaseAdapter {

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public int getCount() {
			if (infoList != null)
				return infoList.size();
			return 0;
		}

		@Override
		public Object getItem(int index) {
			if (infoList != null)
				return infoList.get(index);
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {

			view = getActivity().getLayoutInflater().inflate(R.layout.fragment_help_list_itme, null);
			TextView infoTextView = (TextView) view.findViewById(R.id.infoTextView);
			ImageView icon = (ImageView) view.findViewById(R.id.iconImageView);
			
			if (index % 2 == 0)
				infoTextView.setBackgroundDrawable(itemBg1);
			else
				infoTextView.setBackgroundDrawable(itemBg2);
			
			infoTextView.setTextColor(TextColor);
			infoTextView.setTextSize(TextSize);
			
			if (typeface != null)
				infoTextView.setTypeface(typeface);

			infoTextView.setText(getResources().getString(infoList.get(index)));
			icon.setImageResource(iconList.get(index));
			
			return view;
		}

	}

}
