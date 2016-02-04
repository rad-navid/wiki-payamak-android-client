package com.peaceworld.wikisms.controller.rs.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.peaceworld.wikisms.controller.rs.Utilities;

public class CategoryNotificationServices implements URLs {

	public static String getAllCCNS(String exclude,long identifier,int limit)
	{
		try {
			HttpPost httppost = new HttpPost(CATEGORY_NOTIFICATION_GET_ALL_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("exclude", exclude));
			nameValuePairs.add(new BasicNameValuePair("identifier", identifier+""));
			nameValuePairs.add(new BasicNameValuePair("limit", limit+""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static boolean addCCNs(String ccns)
	{
		try {
			HttpPost httppost = new HttpPost(CATEGORY_NOTIFICATION_ADD_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("ccns", ccns));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			String result= Utilities.SendHttpRequest(httppost);
			if(result==null || !result.equalsIgnoreCase("successful"))
			{
				return false;
			}
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public static String getAllAvailable(String ccns)
	{
		try {
			HttpPost httppost = new HttpPost(CATEGORY_NOTIFICATION_AVAILABLE_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("ccns", ccns));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static boolean updateCCNs(String ccnsState)
	{
		try {
			HttpPost httppost = new HttpPost(CATEGORY_NOTIFICATION_UPDATE_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("ccnsstate", ccnsState));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			String result=Utilities.SendHttpRequest(httppost);
			if(result==null || !result.equalsIgnoreCase("successful"))
			{
				System.out.println(result);
				return false;
			}
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
}
