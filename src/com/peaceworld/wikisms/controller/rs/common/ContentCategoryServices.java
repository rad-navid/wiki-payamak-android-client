package com.peaceworld.wikisms.controller.rs.common;

import org.apache.http.client.methods.HttpPost;

import com.peaceworld.wikisms.controller.rs.Utilities;

public class ContentCategoryServices implements URLs {

	public static String getAllEncrypted()
	{
		try {
			HttpPost httppost = new HttpPost(CONTENT_CATEGORY_GET_ALL_ENCRYPTED_URL);
			return Utilities.SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
}
