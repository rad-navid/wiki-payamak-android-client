package com.peaceworld.wikisms.controller.rs.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.peaceworld.wikisms.controller.rs.Utilities;

public class CommentsService implements URLs {

	public static boolean createNewComments(long UserIdentifier, String comments) {
		try {
			HttpPost httppost = new HttpPost(COMMENTS_NEW_URL);
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("userIdentifier",	UserIdentifier+""));
			nameValuePairs.add(new BasicNameValuePair("comments", comments));
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


}
