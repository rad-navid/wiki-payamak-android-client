package com.peaceworld.wikisms.view.preference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.controller.utility.Utility;

public class SettingPreferenceActivity extends SherlockPreferenceActivity  implements SharedPreferences.OnSharedPreferenceChangeListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_setting);
		
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		CharSequence[] entries = new CharSequence[30];
		CharSequence[] entryValues = new CharSequence[30];
		for(int i=10;i<40;i++)
		{
			entries[i-10]=i+"";
			entryValues[i-10]=i+"";
		}
		
		ListPreference sizes = (ListPreference)findPreference("TEXTSIZE");
		sizes.setEntries(entries);
		sizes.setEntryValues(entryValues);
		sizes.setNegativeButtonText(null);
		
		ListPreference detailSizes = (ListPreference)findPreference("DETAILTEXTSIZE");
		detailSizes.setEntries(entries);
		detailSizes.setEntryValues(entryValues);
		detailSizes.setNegativeButtonText(null);
		
		UserIdentifierPreference userId = (UserIdentifierPreference)findPreference("USERID");
		String userIdentifier=Utility.getUserIdentifier(this)+"";
		System.out.println("user identifier:"+userIdentifier);
		userId.setSummary("شناسه کاربری شما: "+userIdentifier);
		
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String key) {
		
		if(key.equalsIgnoreCase("USERNAME"))
		{
			Settings.setUserRegistered(getApplicationContext(), false);
		}
		
	}

}
