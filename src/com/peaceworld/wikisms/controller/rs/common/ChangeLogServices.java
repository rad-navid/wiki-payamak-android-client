package com.peaceworld.wikisms.controller.rs.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.peaceworld.wikisms.controller.rs.Utilities;

public class ChangeLogServices implements URLs {

	public static String getChangeLog(long indexId,long timeStamp, int limit)
	{
		try {
			HttpPost httppost = new HttpPost(CHANGE_TABLE_GENERAL_LOG_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("IndexId", indexId+""));
			nameValuePairs.add(new BasicNameValuePair("timestamp", timeStamp+""));
			nameValuePairs.add(new BasicNameValuePair("limit", limit+""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	public static String getContentChangeLog(long indexId, long timeStamp, int limit)
	{
		try {
			HttpPost httppost = new HttpPost(CHANGE_TABLE_CONTENT_LOG_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("indexId", indexId+""));
			nameValuePairs.add(new BasicNameValuePair("timeStamp", timeStamp+""));
			nameValuePairs.add(new BasicNameValuePair("limit", limit+""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getCategoryChangeLog(long indexId,long timeStamp, int limit)
	{
		try {
			HttpPost httppost = new HttpPost(CHANGE_TABLE_CATEGORY_LOG_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("indexId", indexId+""));
			nameValuePairs.add(new BasicNameValuePair("timeStamp", timeStamp+""));
			nameValuePairs.add(new BasicNameValuePair("limit", limit+""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}


	public static String getSystemTime()
	{
		try {
			HttpPost httppost = new HttpPost(SYSTEM_TIME_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static String getLastContentsState(long indexId,long lastChangeTime, int limit)
	{
		try {
			HttpPost httppost = new HttpPost(CHANGE_CONTENT_STATE_GET_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("indexId", indexId+""));
			nameValuePairs.add(new BasicNameValuePair("lastChangeTime", lastChangeTime+""));
			nameValuePairs.add(new BasicNameValuePair("limit", limit+""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static String sendContentStateChenges(String contentChangeLog)
	{
		try {
			HttpPost httppost = new HttpPost(CHANGE_CONTENT_STATE_SEND_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("contentChangeLog", contentChangeLog));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
}
