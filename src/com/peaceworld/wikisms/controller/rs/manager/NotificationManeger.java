package com.peaceworld.wikisms.controller.rs.manager;

import com.peaceworld.wikisms.controller.utility.Settings;

import android.content.Context;

public class NotificationManeger {
	
	
	public static  boolean uploadNotificationChangesToServer(Context context)
	{
		try{
			boolean c1=CategoryNotificationManeger.uploadCCNChangesToServer(context);
			boolean c2=ContentNotificationManeger.uploadCNChangesToServer(context);
			return c2 && c1;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}	
	
	public static  boolean sendNewNotificationsToServer(Context context)
	{
		try{
			boolean c1=CategoryNotificationManeger.sendNewNotificationsToServer(context);
			boolean c2=ContentNotificationManeger.sendNewNotificationsToServer(context);
			return c1 && c2;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
	public static boolean downloadNewNotifications(Context context)
	{
		try{
			
			int max=Settings.getNotificationLimit(context);
			int ccnLimit=(max/2)<5?5:(max/2);
			int cnLimit=max-ccnLimit;
			long ccn=CategoryNotificationManeger.downloadNewNotifications(context, ccnLimit);
			long cn=ContentNotificationManeger.downloadNewNotifications(context,cnLimit);
			return true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
	
}
