package com.peaceworld.wikisms.controller.rs.manager;

import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.controller.rs.common.ContentNotificationServices;
import com.peaceworld.wikisms.controller.utility.Utility;
import com.peaceworld.wikisms.model.dao.ContentNotificationFullDao;
import com.peaceworld.wikisms.model.entity.CategoryNotification;
import com.peaceworld.wikisms.model.entity.ContentNotification;
import com.peaceworld.wikisms.model.entity.dao.ContentNotificationTable;

public class ContentNotificationManeger {
	
	
	public static boolean uploadCNChangesToServer(Context context)
	{
		ContentNotificationFullDao cnDao=new ContentNotificationFullDao(context);
		String where=ContentNotificationTable.Columns.SENTTOSERVER.name()+" = 0 AND ( "+
				ContentNotificationTable.Columns.DENY.name()+" = 1 OR "+
				ContentNotificationTable.Columns.ACCEPT.name()+" = 1 )";
		List<ContentNotification>cns=cnDao.asList(cnDao.query(where, null));
		
		String cnsState="";
		for(ContentNotification cn:cns)
			cnsState+=cn.getId()+":"+cn.isAccept()+";";
		boolean result= ContentNotificationServices.updateCNs(cnsState);
		if(result)
		{
			for(ContentNotification cn:cns)
			{
				cn.setSentToServer(true);
				cnDao.update(cn);	
			}
			return true;
		}
		else
			return false;
	}
	
	
	public static boolean sendNewNotificationsToServer(Context context)
	{
		ContentNotificationFullDao cnDao=new ContentNotificationFullDao(context);
		String where=ContentNotificationTable.Columns.SENTFROMSERVER.name()+" = 0 ";
		List<ContentNotification>cns=cnDao.asList(cnDao.query(where, null));
		Gson gson=new Gson();
		String gsonString=gson.toJson(cns);
		boolean result=ContentNotificationServices.addCNs(gsonString);
		if(result)
		{
			for(ContentNotification cn:cns)
				cnDao.delete(cn.getId());
			return true;
		}
		else
			return false;
		
	}
	
	
	public static long downloadNewNotifications(Context context, int limit)
	{
		
		ContentNotificationFullDao cnDao=new ContentNotificationFullDao(context);
		List<ContentNotification> excludeCNs =cnDao.filter().eq(ContentNotificationTable.Columns.SENTFROMSERVER, 1).list();
		String excludeSet=" ( 0 ) ";
		if(excludeCNs.size()>0)
			excludeSet=createSetString(excludeCNs);
		
		long identifier=Utility.getUserIdentifier(context);
		
		String gsonString=ContentNotificationServices.getAllCNs(excludeSet, identifier, limit);
		Gson gson=new Gson();
		List<ContentNotification> cns = null;
		cns = (List<ContentNotification>)gson.
				fromJson(gsonString, new TypeToken<List<ContentNotification>>() {}.getType());
		
		for(ContentNotification cn:cns)
			cn.setSentFromServer(true);
		
		return cnDao.insertMany(cns);
		
	}
	
	public static long downloadNewNotifications(Context context, List<CategoryNotification>creatorsCCNs)
	{
		if(creatorsCCNs==null ||creatorsCCNs.size()<1)
			return 0;
		
		ContentNotificationFullDao cnDao=new ContentNotificationFullDao(context);
		List<ContentNotification> excludeCNs =cnDao.filter().eq(ContentNotificationTable.Columns.SENTFROMSERVER, 1).list();
		String excludeSet=" ( 0) ";
		if(excludeCNs.size() >0)
			excludeSet=createSetString(excludeCNs);
		
		String creatorsIdSet=" ( ";
		for(int i=0;i<creatorsCCNs.size();i++)
		{
			creatorsIdSet+=creatorsCCNs.get(i).getCreatorUser();
			if(i<creatorsCCNs.size()-1)
				creatorsIdSet+=",";
		}
		creatorsIdSet+=" ) ";
		
		String gsonString=ContentNotificationServices.getAllCNsByCreator(excludeSet, creatorsIdSet);
		
		Gson gson=new Gson();
		List<ContentNotification> cns = null;
		cns = (List<ContentNotification>)gson.
				fromJson(gsonString, new TypeToken<List<ContentNotification>>() {}.getType());
		
		for(ContentNotification cn:cns)
			cn.setSentFromServer(true);
		
		return cnDao.insertMany(cns);
		
	}


	private static String createSetString(List<ContentNotification>excludeCNs)
	{
		String excludeSet=" ( ";
		for(int i=0;i<excludeCNs.size();i++)
		{
			excludeSet+=excludeCNs.get(i).getId();
			if(i<excludeCNs.size()-1)
				excludeSet+=",";
		}
		excludeSet+=" ) ";
		return excludeSet;
	}


}
