package com.peaceworld.wikisms.controller.rs.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.peaceworld.wikisms.controller.rs.Utilities;

public class ContentNotificationServices implements URLs {

	public static String getAllCNs(String exclude, long identifier, int limit)
	{
		try {
			HttpPost httppost = new HttpPost(CONTENT_NOTIFICATION_GET_ALL_URL);
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
	
	public static String getAllCNsByCreator(String exclude, String creatorsId)
	{
		try {
			HttpPost httppost = new HttpPost(CONTENT_NOTIFICATION_GET_ALL_BY_CREATOR_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("exclude", exclude));
			nameValuePairs.add(new BasicNameValuePair("creatorsId", creatorsId));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static boolean addCNs(String cns)
	{
		try {
			HttpPost httppost = new HttpPost(CONTENT_NOTIFICATION_ADD_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("cns", cns));
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
	
	public static String getAllAvailable(String cns)
	{
		try {
			HttpPost httppost = new HttpPost(CONTENT_NOTIFICATION_AVAILABLE_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("cns", cns));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean updateCNs(String cnsstate)
	{
		try {
			HttpPost httppost = new HttpPost(CONTENT_NOTIFICATION_UPDATE_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("cnsstate", cnsstate));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			String result = Utilities.SendHttpRequest(httppost);
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
