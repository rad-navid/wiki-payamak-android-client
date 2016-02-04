package com.peaceworld.wikisms.controller.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.peaceworld.wikisms.controller.rs.common.SystemService;
import com.peaceworld.wikisms.controller.rs.manager.ChangeLogManeger;
import com.peaceworld.wikisms.controller.rs.manager.CommentsManager;
import com.peaceworld.wikisms.controller.rs.manager.ContentsStateManeger;
import com.peaceworld.wikisms.controller.rs.manager.GeneralLogManager;
import com.peaceworld.wikisms.controller.rs.manager.NotificationManeger;
import com.peaceworld.wikisms.controller.rs.manager.UserManager;
import com.peaceworld.wikisms.view.newui.adapter.NotificationListAdapter;

public class BackgroundSyncService extends Service {
	
	private Timer timer;
	private Task task;
	@Override
	public void onCreate() {
		super.onCreate();
		
		//Toast.makeText(getApplicationContext(), "Back Sync Call", Toast.LENGTH_SHORT).show();
		
		timer=new Timer();
		task =new Task();
		timer.schedule(task, 20000);
		
	}
	
	@Override

	public int onStartCommand(Intent intent, int flags, int startId) {
		int result=super.onStartCommand(intent, flags, startId);
		
		
		return result;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	public boolean CanConnectToServer()
	{
		try{
			return SystemService.CheckConnectionGet();
		}
		catch(Exception ex)
		{
			return false;
		}
	}
	
	class Task extends TimerTask
	{

		@Override
		public void run() {
				
			try{
				if(CanConnectToServer())
				{
					//IMPORTANT NOTE
					// It is important to regard orders
					
					UserManager.registerUser(getApplicationContext());
					CommentsManager.SendAllComments(getApplicationContext());
					
					GeneralLogManager.handleGeneralLog(getApplicationContext());
					UserManager.getAllUsers(getApplicationContext());
			
					NotificationManeger.sendNewNotificationsToServer(getApplicationContext());
					NotificationManeger.uploadNotificationChangesToServer(getApplicationContext());
					
					boolean haveNewNotification=NotificationManeger.downloadNewNotifications(getApplicationContext());
					if(haveNewNotification)
						NotificationListAdapter.getInstance().refreshNotifications();
					
					ChangeLogManeger changeManager=new ChangeLogManeger(getApplicationContext());
					changeManager.updateDataBase();
					
					ContentsStateManeger.getLastContentsState(getApplicationContext());
					ContentsStateManeger.sendContantStateChanges(getApplicationContext());
				}
				else
				{
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			stopSelf();
		}
		
	}	
	
}
