package com.peaceworld.wikisms.controller.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
	
		
		Intent BgSyncIntent=new Intent(context,BackgroundSyncService.class);
		context.startService(BgSyncIntent);

	}
	
}