package com.peaceworld.wikisms.controller.rs.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.peaceworld.wikisms.controller.rs.Utilities;

public class ContentService implements URLs{

	public static String getByCategory(long lastContentId, long categoryId,int limit)
	{
		try {
			HttpPost httppost = new HttpPost(DATA_GET_BY_CATEGORY_ENCRYPTED_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("maxContentId", lastContentId+""));
			nameValuePairs.add(new BasicNameValuePair("CategoryId", categoryId+""));
			nameValuePairs.add(new BasicNameValuePair("limit",limit+""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
