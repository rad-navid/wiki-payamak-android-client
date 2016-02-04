package com.peaceworld.wikisms.controller.utility;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.model.entity.SimilarContent;

public class Settings {
	
	private static final String USER_SETTING_PREFERENCES="USERSETTINGPREFERENCES";
	public static final String APPLICATION_SETTING_PREFERENCES="APPLICATIONSETTINGPREFERENCES";
	public static final String SERVER_EMPTY_LOG_LIST_MESSAGE="EMPTYLOGLIST";
	
	private static final String USER_REGISTRATION="USERREGISTRATION";
	private static final String STARTUP_DIALOG_STATE="STARTUPDIALOGSTATE";
	private static final String IMPORT_SMS_SIMILAR_CONTENTS="IMPORTSMSSIMILARCONTENTS";
	private static final String IMPORT_SMS_SIMILAR_CONTENTS_VALIDATE="IMPORTSMSSIMILARCONTENTSVALIDATE";
	
	// Filter params
	private static String FILTER_VIEW="FILTERVIEW",FILTER_ORDER="FILTERORDER";
	public static String ORDER_BY_DATE="ORDERBYDATE",ORDER_BY_LIKED="ORDERBYLIKED",ORDER_BY_VIEWED="ORDERBYVIEWED";
	public static String VIEW_UNVIEWED="VIEWUNVIEWED" ,VIEW_VIEWED="VIEWVIEWED",VIEW_ALL="VIEWALL";
	
	public static boolean isUserRegistered(Context context)
	{
		SharedPreferences spref=context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		return spref.getBoolean(Settings.USER_REGISTRATION, false);
	}
	
	public static void setUserRegistered(Context context, boolean state)
	{
		SharedPreferences spref=context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		spref.edit().putBoolean(Settings.USER_REGISTRATION, state).commit();
	}
	
	public static boolean isNotStartupDialogActive(Context context)
	{
		SharedPreferences spref=context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		return spref.getBoolean(Settings.STARTUP_DIALOG_STATE, false);
	}
	
	public static void setStartupDialogState(Context context, boolean state)
	{
		SharedPreferences spref=context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		spref.edit().putBoolean(Settings.STARTUP_DIALOG_STATE, state).commit();
	}
	
	public static String getUserName(Context context)
	{
		SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(context);
		String Username=shpref.getString("USERNAME", "");
		return Username;
	}
	public static int getNotificationLimit(Context context)
	{
		SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(context);
		String stringValue=shpref.getString("NOTIFICATIONMAXSIZE", "50");
		int limit=50;
		try{
			limit=Integer.parseInt(stringValue);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return limit;
	}
	
	
	
	public static void setOrderFilter(Context context, String filter)
	{
		SharedPreferences shpref= context.getSharedPreferences(Settings.USER_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = shpref.edit();
		editor.putString(Settings.FILTER_ORDER, filter);
		editor.commit();
	}
	public static String getOrderFilter(Context context)
	{
		SharedPreferences shpref= context.getSharedPreferences(Settings.USER_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		return shpref.getString(Settings.FILTER_ORDER, ORDER_BY_DATE);
	}
	public static void setViewFilter(Context context, String filter)
	{
		SharedPreferences shpref= context.getSharedPreferences(Settings.USER_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = shpref.edit();
		editor.putString(Settings.FILTER_VIEW, filter);
		editor.commit();
	}
	public static String getViewFilter(Context context)
	{
		SharedPreferences shpref= context.getSharedPreferences(Settings.USER_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		return shpref.getString(Settings.FILTER_VIEW, VIEW_ALL );
	}
	
	public static byte[] getSalt()
	{
		byte[] salt = {
	            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
	            (byte)0x56, (byte)0x34, (byte)0xE3, (byte)0x03
	        };
		return salt;
	}

	public static void pushSililarContentList(Context context,ArrayList<SimilarContent> similarContentList) {
		
		SharedPreferences pref= context.getSharedPreferences(APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		boolean isValid=pref.getBoolean(IMPORT_SMS_SIMILAR_CONTENTS_VALIDATE, false);
		
		Editor editor=pref.edit();
		if(!isValid)
		{
			Gson gson=new Gson();
			String gsonString=gson.toJson(similarContentList);
			editor.putString(IMPORT_SMS_SIMILAR_CONTENTS, gsonString);
			editor.putBoolean(IMPORT_SMS_SIMILAR_CONTENTS_VALIDATE, true);
		}
		else
		{
			String gsonString= pref.getString(IMPORT_SMS_SIMILAR_CONTENTS, "");
			List<SimilarContent> MainsimilarContentList = null;
			Gson gson=new Gson();
			MainsimilarContentList = null;
			try
			{
				MainsimilarContentList=(List<SimilarContent>)gson.
					fromJson(gsonString, new TypeToken<List<SimilarContent>>() {}.getType());
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			if(MainsimilarContentList!=null)
				MainsimilarContentList.addAll(similarContentList);
			else
				MainsimilarContentList=similarContentList;
			
			gsonString=gson.toJson(MainsimilarContentList);
			editor.putString(IMPORT_SMS_SIMILAR_CONTENTS, gsonString);
			editor.putBoolean(IMPORT_SMS_SIMILAR_CONTENTS_VALIDATE, true);
		}
		editor.commit();
		
	}
	
	public static List<SimilarContent> poolSililarContentList(Context context) {
		
		SharedPreferences pref= context.getSharedPreferences(APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		boolean isValid=pref.getBoolean(IMPORT_SMS_SIMILAR_CONTENTS_VALIDATE, false);
		
		
		List<SimilarContent> similarContentList = null;
		if(isValid)
		{
			String gsonString= pref.getString(IMPORT_SMS_SIMILAR_CONTENTS, "");
			Gson gson=new Gson();
			
			try
			{
				similarContentList=(List<SimilarContent>)gson.
					fromJson(gsonString, new TypeToken<List<SimilarContent>>() {}.getType());
			
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			Editor editor=pref.edit();
			editor.putBoolean(IMPORT_SMS_SIMILAR_CONTENTS_VALIDATE, false);
			editor.remove(IMPORT_SMS_SIMILAR_CONTENTS);
			editor.commit();
		}
		
		return similarContentList;
		
	}
	
	
	
	
	

}
