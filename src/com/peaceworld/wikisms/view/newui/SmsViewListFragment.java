package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.utility.Utility;
import com.peaceworld.wikisms.model.dao.ContentNotificationFullDao;
import com.peaceworld.wikisms.model.entity.ContentNotification;
import com.peaceworld.wikisms.view.newui.dialog.CategorySelectionDialog;

public class SmsViewListFragment extends Fragment {

	private ArrayList<String> SmsList, numbers;
	private ContentViewerAdapter adapter;
	private Drawable itemBg1, itemBg2;
	private boolean income, outcome;
	private HashMap<Integer, ContentNotification> CNs;
	private HashMap<Integer, View>Views;
	private ContentNotificationFullDao cnFullDao;
	private boolean handleSharedSms=false;
	private int TextColor=0, TextSize=0;
	private Typeface typeface;

	public SmsViewListFragment() {
		this(null, true, true);
	}

	public SmsViewListFragment(ArrayList<String> numbers, boolean income,
			boolean outcome) {
		setRetainInstance(true);
		this.numbers = numbers;
		this.income = income;
		this.outcome = outcome;
		CNs = new HashMap<Integer, ContentNotification>();
	}

	public SmsViewListFragment(ArrayList<String> sharedSms) {
		setRetainInstance(true);
		
		handleSharedSms=true;
		if (sharedSms!=null && sharedSms.size()>0) {
			SmsList = sharedSms;
		} else {
			SmsList = new ArrayList<String>(0);
		}
		CNs = new HashMap<Integer, ContentNotification>();
	}
	
	private void loadAllSms(ArrayList<String> numbers, boolean income,
			boolean outcome) {
		Uri uri = null;
		if ((income && outcome) || !(income || outcome))
			uri = Uri.parse("content://sms/");
		else if (income)
			uri = Uri.parse("content://sms/inbox");
		else if (outcome)
			uri = Uri.parse("content://sms/sent");

		String[] projection = new String[] { "body" };
		String selection = null;
		if (numbers != null && numbers.size() > 0) {
			selection = "";
			for (int i = 0; i < numbers.size(); i++) {
				selection += " address LIKE \'%" + numbers.get(i) + "%\' ";
				if (i < numbers.size() - 1)
					selection += " OR ";
			}
		}
		ContentResolver cr = this.getActivity().getContentResolver();
		Cursor c = cr.query(uri, projection, selection, null, null);
		if (c.moveToFirst()) {
			SmsList = new ArrayList<String>(c.getCount());
			do {
				SmsList.add(c.getString(0));
			} while (c.moveToNext());
		} else {
			SmsList = new ArrayList<String>(0);
			Toast.makeText(getActivity(), "no sms", Toast.LENGTH_SHORT).show();

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_content_view_list, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(!handleSharedSms)
			loadAllSms(numbers, income, outcome);
		
		itemBg1 = getResources().getDrawable(
				R.drawable.content_view_list_item_background1);
		itemBg2 = getResources().getDrawable(
				R.drawable.content_view_list_item_background2);
		ListView gridView = (ListView) getActivity().findViewById(
				R.id.contentViewListView);
		Views=new HashMap<Integer, View>();
		
		adapter = new ContentViewerAdapter();
		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		cnFullDao = new ContentNotificationFullDao(getActivity());
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

	public class ContentViewerAdapter extends BaseAdapter {

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public int getCount() {
			if (SmsList != null)
				return SmsList.size();
			return 0;
		}

		@Override
		public Object getItem(int index) {
			if (SmsList != null)
				return SmsList.get(index);
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {

			view = getActivity().getLayoutInflater().inflate(
					R.layout.fragment_import_sms_list_itme, null);
			TextView contentTextView = (TextView) view
					.findViewById(R.id.ContentViewListItemTextView);
			if (index % 2 == 0)
				contentTextView.setBackgroundDrawable(itemBg1);
			else
				contentTextView.setBackgroundDrawable(itemBg2);
			
			contentTextView.setTextColor(TextColor);
			contentTextView.setTextSize(TextSize);
			
			if (typeface != null)
				contentTextView.setTypeface(typeface);

			contentTextView.setText(SmsList.get(index) + "\r\n\r\n");

			ImageView sendImage = (ImageView) view
					.findViewById(R.id.sendSmsImageView);
			ImageView addCategoryImae = (ImageView) view
					.findViewById(R.id.addCategoryImageView);

			sendImage.setOnClickListener(onclickListener_send);
			addCategoryImae.setOnClickListener(onclickListener_add);

			sendImage.setTag(index);
			addCategoryImae.setTag(index);

			Views.put(index, view);
			return view;
		}
		private void sendSms(final int index)
		{

			try {
				ContentNotification cn = CNs.get(index);
				if (cn == null)
					cn = new ContentNotification();
				cn.setMetaInfo(SmsList.get(index));
				cn.setAdministrationLevel(0);

				cn.setAction(ContentNotification.ACTION.CONTENT_CREATE.name());
				cn.setCreatorUser(Utility.getUserIdentifier(SmsViewListFragment.this.getActivity()));
				cnFullDao.insert(cn);
				
				CNs.remove(index);
				
				boolean viewAvailable=Views.get(index)!=null;
				if(!viewAvailable)
				{
					SmsList.remove(index);
					notifyDataSetChanged();
					return;
				}
				
				AnimationSet animSet=MenuAnimManager.getSmsSentAnimation();
				Views.get(index).startAnimation(animSet);
				animSet.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						SmsList.remove(index);
						notifyDataSetChanged();
						
					}
				});

			} catch (Exception ex) {

				ex.printStackTrace();
			}

		
		}
		private OnClickListener onclickListener_send = new OnClickListener() {

			@Override
			public void onClick(final View v) {
				AlertDialog dialog= new AlertDialog.Builder(SmsViewListFragment.this.getActivity())
				.setTitle("توجه!!!") 
				.setMessage("این پیامک پس از ارسال، برای رأی‌گیری در معرض دید عموم کاربران قرار می‌گیرد. آیا می‌خواهید آن را ارسال کنید؟")
		           .setCancelable(false)
		           .setPositiveButton("بله", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		            	   final int index = (Integer) v.getTag();
		            	   sendSms(index);
		               }
		           })
		           .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		            	   dialog.dismiss();
		               }
		           })
		           .show();
				
			}
		};

		private OnClickListener onclickListener_add = new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {

					int index = (Integer) v.getTag();
					ContentNotification cn = CNs.get(index);
					if (cn == null)
					{
						cn = new ContentNotification();
						CNs.put(index, cn);
					}
					CategorySelectionDialog categorySelectionDialog = new CategorySelectionDialog(
							cn);
					categorySelectionDialog.show(getFragmentManager(), "");
					

				} catch (Exception ex) {

					ex.printStackTrace();
				}

			}
		};

	}

}
