package com.peaceworld.wikisms.controller.rs.manager;

import java.util.List;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.controller.rs.common.CategoryNotificationServices;
import com.peaceworld.wikisms.controller.utility.Utility;
import com.peaceworld.wikisms.model.dao.CategoryNotificationFullDao;
import com.peaceworld.wikisms.model.entity.CategoryNotification;
import com.peaceworld.wikisms.model.entity.dao.CategoryNotificationTable;

public class CategoryNotificationManeger {
	
	
	public static boolean uploadCCNChangesToServer(Context context)
	{
		CategoryNotificationFullDao ccnDao=new CategoryNotificationFullDao(context);
		String where=CategoryNotificationTable.Columns.SENTTOSERVER.name()+" = 0 AND ( "+
				CategoryNotificationTable.Columns.DENY.name()+" = 1 OR "+
				CategoryNotificationTable.Columns.ACCEPT.name()+" = 1 )";
		List<CategoryNotification>ccns=ccnDao.asList(ccnDao.query(where, null));
		
		String ccnsState="";
		for(CategoryNotification ccn:ccns)
			ccnsState+=ccn.getId()+":"+ccn.isAccept()+";";
		boolean result= CategoryNotificationServices.updateCCNs(ccnsState);
		if(result)
		{
			for(CategoryNotification ccn:ccns)
			{
				ccn.setSentToServer(true);
				ccnDao.update(ccn);	
			}
			return true;
		}
		else
			return false;
	}
	
	public static boolean sendNewNotificationsToServer(Context context)
	{
		CategoryNotificationFullDao ccnDao=new CategoryNotificationFullDao(context);
		String where=CategoryNotificationTable.Columns.SENTFROMSERVER.name()+" = 0 ";
		List<CategoryNotification>ccns=ccnDao.asList(ccnDao.query(where, null));
		Gson gson=new Gson();
		String gsonString=gson.toJson(ccns);
		boolean result=CategoryNotificationServices.addCCNs(gsonString);
		if(result)
		{
			for(CategoryNotification ccn:ccns)
				ccnDao.delete(ccn.getId());
			return true;
		}
		else
			return false;
		
	}
	
	
	public static long downloadNewNotifications(Context context, int limit)
	{
		
		CategoryNotificationFullDao ccnDao=new CategoryNotificationFullDao(context);
		List<CategoryNotification> excludeCCNs =ccnDao.filter().eq(CategoryNotificationTable.Columns.SENTFROMSERVER, 1).list();
		String excludeSet=" ( 0 ) ";
		if(excludeCCNs.size()>0)
			excludeSet=createSetString(excludeCCNs);
		
		long identifier=Utility.getUserIdentifier(context);
		
		String gsonString=CategoryNotificationServices.getAllCCNS(excludeSet, identifier, limit);
		
		Gson gson=new Gson();
		List<CategoryNotification> ccns = null;
		ccns = (List<CategoryNotification>)gson.
				fromJson(gsonString, new TypeToken<List<CategoryNotification>>() {}.getType());
		
		for(CategoryNotification ccn:ccns)
			ccn.setSentFromServer(true);
		
		long ccnCounter= ccnDao.insertMany(ccns);
		
		long cnCounter=ContentNotificationManeger.downloadNewNotifications(context, ccns);
		
		return ccnCounter+cnCounter;
		
	}

	
	private static String createSetString(List<CategoryNotification>excludeCCNs)
	{
		String excludeSet=" ( ";
		for(int i=0;i<excludeCCNs.size();i++)
		{
			excludeSet+=excludeCCNs.get(i).getId();
			if(i<excludeCCNs.size()-1)
				excludeSet+=",";
		}
		excludeSet+=" ) ";
		return excludeSet;
	}
	
}
