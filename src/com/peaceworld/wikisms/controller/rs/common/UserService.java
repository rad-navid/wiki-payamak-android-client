package com.peaceworld.wikisms.controller.rs.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.peaceworld.wikisms.controller.rs.Utilities;

public class UserService implements URLs {

	public static boolean CreateUser(long UserIdentifier, String UserName,String deviceInf) {
		try {
			HttpPost httppost = new HttpPost(USER_CREATION_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");


			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("UserIdentifier",	UserIdentifier+""));
			nameValuePairs.add(new BasicNameValuePair("UserName", UserName));
			nameValuePairs.add(new BasicNameValuePair("DeviceInfo", deviceInf));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			String result= Utilities.SendHttpRequest(httppost);
			if(result.equalsIgnoreCase("successful"))
				return true;
			else
			{
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public static String GetAll(String RequestIds) {
		try {
			HttpPost httppost = new HttpPost(USER_GET_URL);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("RequestIds",	RequestIds));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
			
		}

	}

}
