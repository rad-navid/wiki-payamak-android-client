package com.peaceworld.wikisms.controller.rs.manager;

import java.util.List;

import android.content.Context;

import com.peaceworld.wikisms.controller.rs.Utilities;
import com.peaceworld.wikisms.controller.rs.common.UserService;
import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.controller.utility.Utility;
import com.peaceworld.wikisms.model.dao.UserFullDao;
import com.peaceworld.wikisms.model.entity.UserTable;

public class UserManager {
	
	public static void registerUser(Context context)
	{
		try{
			
			if(!Settings.isUserRegistered(context))
			{
				boolean result=UserService.CreateUser(Utility.getUserIdentifier(context), Settings.getUserName(context), Utility.getInfo(context));
				Settings.setUserRegistered(context, result);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void getAllUsers(Context context)
	{
		try{
			
			UserFullDao userFullDao=new UserFullDao(context);
			List<Long> availableUserIds=userFullDao.getDistinctAvailableUserIds();
			String queryParam="";
			for(Long id:availableUserIds)
				queryParam+=id+",";
			
			String compressed=UserService.GetAll(queryParam);
			String result=Utilities.gzipUncompress(compressed);
			if(result.length()<=0)
				return;
			String users[]=result.split(";");
			if(users.length>0)
				userFullDao.deleteAll();
			for(String user:users)
			{
				String tmp[]=user.split(":");
				long identifier=Long.parseLong(tmp[0]);
				String userName="";
				if(tmp.length>1)
					userName=tmp[1];
				UserTable newUser=new UserTable();
				newUser.setUserIdentifier(identifier);
				newUser.setUsername(userName);
				userFullDao.insert(newUser);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	
	

}
