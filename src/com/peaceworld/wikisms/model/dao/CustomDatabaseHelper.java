package com.peaceworld.wikisms.model.dao;

import android.content.Context;

import com.turbomanage.storm.DatabaseHelper;
import com.turbomanage.storm.api.DatabaseFactory;

public class CustomDatabaseHelper  extends DatabaseHelper{

	public CustomDatabaseHelper(Context ctx, DatabaseFactory dbFactory) {
		super(ctx, dbFactory);
		
		
	}

	@Override
	public UpgradeStrategy getUpgradeStrategy() {
		return UpgradeStrategy.UPGRADE;
	}

}
