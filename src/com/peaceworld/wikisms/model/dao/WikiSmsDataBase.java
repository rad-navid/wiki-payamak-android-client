package com.peaceworld.wikisms.model.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.GridLayout.Spec;

import com.peaceworld.wikisms.controller.utility.Settings;
import com.turbomanage.storm.DatabaseHelper;
import com.turbomanage.storm.api.Database;
import com.turbomanage.storm.api.DatabaseFactory;

@Database(name = WikiSmsDataBase.DB_NAME, version = WikiSmsDataBase.DB_VERSION)
public class WikiSmsDataBase extends DatabaseHelper {

	public static final String DB_NAME = "wikisms_db";
	public static final int DB_VERSION = 2;
	private Context context;

	public WikiSmsDataBase(Context ctx, DatabaseFactory dbFactory) {
		
		super(ctx, dbFactory);
		this.context = ctx;
	}

	@Override
	public UpgradeStrategy getUpgradeStrategy() {
		return UpgradeStrategy.DROP_CREATE;
	}

	public static String getExternalDatabasePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()+
				"/wikipayamak";	
	}
	
	public static void pushDB(Context context, boolean force) throws Exception {
		//if(1==1)
		//	return;
		
		String destPath =WikiSmsDataBase.getExternalDatabasePath();

		File f2 = new File(destPath);
		if (!f2.exists())
			f2.mkdirs();
		String destPathdb = WikiSmsDataBase.getExternalDatabasePath()+"/"+WikiSmsDataBase.DB_NAME;
		File f = new File(destPathdb);
		if (force || !f.exists()) {
			InputStream in = context.getAssets().open(WikiSmsDataBase.DB_NAME);
			OutputStream out = new FileOutputStream(f);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
		
		
		context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE).edit().putString("memo1", "DBmovedSDcart").commit();
	}
	
		
}
