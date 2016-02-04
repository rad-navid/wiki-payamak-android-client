package com.peaceworld.wikisms.controller.rs.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.controller.rs.Utilities;
import com.peaceworld.wikisms.controller.rs.common.ChangeLogServices;
import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentDirectChange;
import com.peaceworld.wikisms.model.entity.dao.ContentDirectChangeDao;

public class ContentsStateManeger {

	
	private static final String LAST_CONTENT_STATE_UPDATE_TIME_STAMP = "LAST_CONTENT_STATE_UPDATE_TIME_STAMP";

	public static boolean sendContantStateChanges(Context context) {
		try {
			ContentDirectChangeDao cdcDao = new ContentDirectChangeDao(context);
			ArrayList<ContentDirectChange> changeList = (ArrayList<ContentDirectChange>) cdcDao
					.listAll();
			Gson gson = new Gson();
			String gsonString = gson.toJson(changeList);
			
			String compressed = Utilities.gzipCompress(gsonString);
			String result = ChangeLogServices.sendContentStateChenges(compressed);
			if (result != null && result.equalsIgnoreCase("successful")) {
				cdcDao.deleteAll();
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static void getLastContentsState(Context context) {
		
		long lastChangeTime = getLastUpdateTime(context);
		try {
			ContentFullDao contentDao = new ContentFullDao(context);
			long indexId = 0;
			List<ContentDirectChange> changeList = null;
			do {
				String encryped = ChangeLogServices.getLastContentsState(indexId, lastChangeTime, 20000);
				if(encryped.equalsIgnoreCase(Settings.SERVER_EMPTY_LOG_LIST_MESSAGE))
					break;
				
				String decrypted = Utilities.gzipUncompress(encryped);
				Gson gson = new Gson();
				changeList = (List<ContentDirectChange>) gson.fromJson(decrypted, new TypeToken<List<ContentDirectChange>>() {}.getType());

				for (ContentDirectChange chng : changeList) {
					Content cnt = contentDao.get(chng.getId());
					if (cnt != null) {
						cnt.setViewedCounter(chng.getViewd());
						cnt.setLikedCounter(chng.getLiked());
						contentDao.update(cnt);
					}
					indexId = chng.getId() > indexId ? chng.getId() : indexId;
					lastChangeTime = chng.getTimeStamp() > lastChangeTime ? chng.getTimeStamp() : lastChangeTime;
				}
				
			} while (changeList != null && changeList.size() > 0);
			
			
		} catch (Exception ex) 
		{
			ex.printStackTrace();
		}finally
		{
			setLastUpdateTime(context, lastChangeTime);
		}
	}

	private static long getLastUpdateTime(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		return pref.getLong(LAST_CONTENT_STATE_UPDATE_TIME_STAMP, 0);
	}

	private static void setLastUpdateTime(Context context, long timeStaamp) {
		SharedPreferences pref = context.getSharedPreferences(
				Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putLong(LAST_CONTENT_STATE_UPDATE_TIME_STAMP, timeStaamp);
		editor.commit();
	}

}
