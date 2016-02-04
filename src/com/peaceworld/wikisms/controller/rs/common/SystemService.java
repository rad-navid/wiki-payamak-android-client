package com.peaceworld.wikisms.controller.rs.common;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import com.peaceworld.wikisms.controller.rs.Utilities;

public class SystemService implements URLs {

	public static boolean CheckConnectionPost() {
		try {
			HttpPost httppost = new HttpPost(SYSTEM_CHECK_CONNECTION_POST_URL);
			String result= Utilities.SendHttpRequest(httppost);
			if(result.equalsIgnoreCase("successful"))
				return true;
			else
			{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public static boolean CheckConnectionGet() {
		try {
			HttpGet httpGet = new HttpGet(SYSTEM_CHECK_CONNECTION_GET_URL);
			String result= Utilities.SendHttpRequest(httpGet);
			if(result.equalsIgnoreCase("successful"))
				return true;
			else
			{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	
}
