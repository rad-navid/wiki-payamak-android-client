package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.view.activity.DialogConfirmationListener;
import com.peaceworld.wikisms.view.newui.dialog.SimpleOneInputDialog;
import com.peaceworld.wikisms.view.newui.dialog.SimpleOneInputDialog.TYPE;

public class SearchSmsMenuFragment extends Fragment implements DialogConfirmationListener {

	private AutoCompleteTextView contactSelector;
	private CheckBox income, outcome;
	private ImageView search,returnToContent,returnToMainMenu;
	private RadioGroup importSmsRG;
	private  LinearLayout searchSmsLinearLayout;
	private SimpleOneInputDialog inputDialog;
	

	private ArrayAdapter<String> adapter;

	private ArrayList<String> allContacts;
	
	public SearchSmsMenuFragment() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_import_sms_menu, container,
				false);
		contactSelector = (AutoCompleteTextView) v
				.findViewById(R.id.contactSelectorEditText);
		
		outcome = (CheckBox) v.findViewById(R.id.outcomeCheckBox);
		income = (CheckBox) v.findViewById(R.id.incomeCheckBox);
		search = (ImageView) v.findViewById(R.id.searchSmsButton);
		importSmsRG = (RadioGroup)v.findViewById(R.id.importSmsRG);
		searchSmsLinearLayout = (LinearLayout)v.findViewById(R.id.searchSmsLinearLayout);
		returnToContent = (ImageView) v.findViewById(R.id.ContentMenuReturnButton);
		returnToMainMenu = (ImageView) v.findViewById(R.id.MainMenuReturnButton);
		search.setOnClickListener(onClickListener_search);
		returnToContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switchMenu(new ContentCategoryExplorerFragment());
			}
		});
		returnToMainMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				switchMenu(new MainMenuFragment());
			}
		});
		
		importSmsRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
					if(arg1==R.id.searchForSmsRadio)
						searchSmsLinearLayout.setVisibility(View.VISIBLE);
					else
					{
						searchSmsLinearLayout.setVisibility(View.GONE);
						if(inputDialog==null)
							inputDialog=new SimpleOneInputDialog(SimpleOneInputDialog.TYPE.CREATE_CONTENT, SearchSmsMenuFragment.this);
						inputDialog.show(getFragmentManager(), "");
					}
				
			}
		});
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		loadAllContacts();
		
		// Create adapter
		adapter = new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_list_item_1,
				allContacts);
		contactSelector.setThreshold(1);

		// Set adapter to AutoCompleteTextView
		contactSelector.setAdapter(adapter);

	}

	// the meat of switching the above fragment
	private void switchContent(Fragment fragment) {

		if (getActivity() == null)
			return;
		if (getActivity() instanceof ContentPreviewFragmentActivity) {
			ContentPreviewFragmentActivity ra = (ContentPreviewFragmentActivity) getActivity();
			ra.switchContent(fragment, true);
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void loadAllContacts() {

		try {

			Uri contacts = ContactsContract.Contacts.CONTENT_URI;

			ContentResolver cr1 = this.getActivity().getContentResolver();
			String []projection={ContactsContract.Contacts.DISPLAY_NAME} ;
			String selection=ContactsContract.Contacts.HAS_PHONE_NUMBER +" = 1 ";
			Cursor cur = cr1.query(contacts, projection, selection, null, null);

			if(cur.moveToFirst())
			{
				allContacts=new ArrayList<String>(cur.getCount());
				do
				{
					allContacts.add(cur.getString(0));
				}while(cur.moveToNext());
			}
			
			
		} catch (Exception e) {
			Log.i("AutocompleteContacts", "Exception : " + e);
		}

	}
	
	private ArrayList<String> getAllPhonNumbers(String NAME)
	{
		ArrayList<String>allNumbers=new ArrayList<String>(10);
		try {
			//
	        //  Find contact based on name.
	        //
	        ContentResolver cr = this.getActivity().getContentResolver();
	        String selection=ContactsContract.Contacts.DISPLAY_NAME+" = '" + NAME + "' ";
	        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
	            selection, null, null);
	        if (cursor.moveToFirst()) {
	            String contactId =
	                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

	            Cursor phones = cr.query(Phone.CONTENT_URI, null,
	                Phone.CONTACT_ID + " = " + contactId, null, null);
	            while (phones.moveToNext()) {
	                String number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
	                allNumbers.add(number);
	            }
	            phones.close();
	         }
	    cursor.close();
	    } catch (Exception e) {
			Log.i("AutocompleteContacts", "Exception : " + e);
		}
		return allNumbers;
	}
	
	OnClickListener onClickListener_search=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ArrayList<String> numbers= getAllPhonNumbers(contactSelector.getText().toString());
			SmsViewListFragment fragment=new SmsViewListFragment(numbers, income.isChecked(), outcome.isChecked());
			switchContent(fragment);
		}
	};

	@Override
	public void actionPerformed(boolean confirmed, TYPE actionType) {
		if(actionType==TYPE.CREATE_CONTENT && confirmed)
		{
			String sms=inputDialog.getContent();
			ArrayList<String>SmsList=new ArrayList<String>(2);
			SmsList.add(sms);
			SmsViewListFragment fragment=new SmsViewListFragment(SmsList);
			switchContent(fragment);
		}
		
	}

}
