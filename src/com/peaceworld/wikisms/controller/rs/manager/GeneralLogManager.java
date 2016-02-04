package com.peaceworld.wikisms.controller.rs.manager;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.controller.rs.Utilities;
import com.peaceworld.wikisms.controller.rs.common.ChangeLogServices;
import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.GeneralChangeLog;

public class GeneralLogManager {
	
	private static final String LAST_GENERAL_LOG_UPDATE_TIME_STAMP = "LASTGENERALLOGUPDATETIMESTAMP";
	
	public static void handleGeneralLog(Context context) {
		
		long lastChangeTime = getLastUpdateTime(context);
		
		try {
			SQLiteDatabase db= new ContentFullDao(context).getDbHelper(context).getWritableDatabase();
			long startId = 0;
			List<GeneralChangeLog> changeList = null;
			do {
				String encryped = ChangeLogServices.getChangeLog(startId, lastChangeTime, 20000);
				
				if(encryped.equalsIgnoreCase(Settings.SERVER_EMPTY_LOG_LIST_MESSAGE))
					return;
				
				if(encryped.length()<=0)
					return;
				
				String decrypted = Utilities.gzipUncompress(encryped);
				Gson gson = new Gson();
				changeList = (List<GeneralChangeLog>) gson.fromJson(decrypted, new TypeToken<List<GeneralChangeLog>>() {}.getType());
			
				for (GeneralChangeLog changeLog : changeList) {
					
					startId=changeLog.getId()>startId ? changeLog.getId():startId;
					lastChangeTime=changeLog.getTimeStamp()>lastChangeTime ? changeLog.getTimeStamp():lastChangeTime;
					
					db.delete(changeLog.getTableName(), "_id=?", new String[]{changeLog.getRecordId()+""});
				}
			} while (changeList!=null && changeList.size()>0);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
			setLastUpdateTime(context, lastChangeTime);
		}
	}
	
	private static long getLastUpdateTime(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		return pref.getLong(LAST_GENERAL_LOG_UPDATE_TIME_STAMP, 0);
	}

	private static void setLastUpdateTime(Context context, long timeStaamp) {
		SharedPreferences pref = context.getSharedPreferences(
				Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putLong(LAST_GENERAL_LOG_UPDATE_TIME_STAMP, timeStaamp);
		editor.commit();
	}

}
