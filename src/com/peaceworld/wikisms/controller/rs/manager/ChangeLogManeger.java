package com.peaceworld.wikisms.controller.rs.manager;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.controller.rs.Utilities;
import com.peaceworld.wikisms.controller.rs.common.ChangeLogServices;
import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.model.dao.CategoryFullDao;
import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.CategoryNotification;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.ContentNotification;

public class ChangeLogManeger {
	
	private static final String CATEGORY_TABLE_UPDATE_TIMESTAMP="CATEGORYTABLEUPDATETIMESTAMP";
	private static final String CONTENT_TABLE_UPDATE_TIMESTAMP="CONTENTTABLEUPDATETIMESTAMP";
	private ContentFullDao contentDao;
	private CategoryFullDao categoryDao;
	private Context context;
	
	public ChangeLogManeger(Context context)
	{
		this.context=context;
		contentDao=new ContentFullDao(context);
		categoryDao=new CategoryFullDao(context);
	}
	
	public void updateDataBase()
	{
		try
		{
			updateCategoryTable();
			updateContentTable();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void updateCategoryTable()
 {
		long timeStamp = getLastCategoryTableUpdateTimeStamp(context);
		try {
			long indexId = 0;
			List<CategoryNotification> CCNList = null;
			do {
				String encryped = ChangeLogServices.getCategoryChangeLog(indexId, timeStamp, 500);

				if (encryped.equalsIgnoreCase(Settings.SERVER_EMPTY_LOG_LIST_MESSAGE))
					break;

				String decrypted = Utilities.gzipUncompress(encryped);
				Gson gson = new Gson();

				CCNList = (List<CategoryNotification>) gson.fromJson(decrypted,	new TypeToken<List<CategoryNotification>>(){}.getType());

				for (CategoryNotification ccn : CCNList) {
					try {
						indexId = ccn.getId() > indexId ? ccn.getId() : indexId;
						timeStamp = ccn.getTimeStamp() > timeStamp ? ccn.getTimeStamp() : timeStamp;

						classifyCCNs(ccn);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} while (CCNList != null && CCNList.size() > 0);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			setLastCategoryTableUpdateTimeStamp(context, timeStamp);
		}

	}
	
	private void updateContentTable()
 {
		long timeStamp = getLastContentTableUpdateTimeStamp(context);
		try {
			long indexId = 0;
			List<ContentNotification> CNList = null;
			do {
				String encryped = ChangeLogServices.getContentChangeLog(indexId, timeStamp, 400);

				if (encryped.equalsIgnoreCase(Settings.SERVER_EMPTY_LOG_LIST_MESSAGE))
					break;

				String decrypted = Utilities.gzipUncompress(encryped);
				Gson gson = new Gson();

				CNList = (List<ContentNotification>) gson.fromJson(decrypted,new TypeToken<List<ContentNotification>>() {}.getType());
				for (ContentNotification cn : CNList) {
					try {

						indexId = cn.getId() > indexId ? cn.getId() : indexId;
						timeStamp = cn.getTimeStamp() > timeStamp ? cn.getTimeStamp() : timeStamp;

						classifyCNs(cn);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} while (CNList != null && CNList.size() > 0);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setLastContentTableUpdateTimeStamp(context, timeStamp);
		}

	}

	
	private void classifyCCNs(CategoryNotification CCN)
	{
			CategoryNotification.ACTION action=CategoryNotification.ACTION.valueOf(CCN.getAction());
			switch(action)
			{
			case CATEGORY_CHANGED:
				notification_action_CATEGORY_CHANGED(CCN);
				break;
			case CATEGORY_CREATE:
				notification_action_CATEGORY_CREATE(CCN);
				break;
			case CATEGORY_DELETE:
				notification_action_CATEGORY_DELETE(CCN);
				break;
			case CATEGORY_MERGE:
				notification_action_CATEGORY_MERGE(CCN);
				break;
			case CATEGORY_MOVE:
				notification_action_CATEGORY_MOVE(CCN);
				break;
				
			default:
				break;
			}
	}
	
	private void classifyCNs(ContentNotification CN)
	{
			ContentNotification.ACTION action=ContentNotification.ACTION.valueOf(CN.getAction());
			switch(action)
			{
			case CONTENT_DELETE:
				notification_action_CONTENT_DELETE(CN);
				break;
			case CONTENT_EDIT:
				notification_action_CONTENT_EDIT(CN);
				break;
			case CONTENT_TAG_ADDED:
				notification_action_CONTENT_TAG_ADDED(CN);
				break;
			case CONTENT_TAG_CHANGED:
				notification_action_CONTENT_TAG_CHANGED(CN);
				break;
			case CONTENT_CREATE:
				notification_action_CONTENT_CREATE(CN);
				break;
				
			default:
				break;
			
			}
	
	}
	
	
	private void notification_action_CATEGORY_MOVE(CategoryNotification ccnotif) {
		
		categoryDao.moveCategory(ccnotif.getOperand1(), ccnotif.getOperand2(), "", false);
	}

	private void notification_action_CATEGORY_MERGE(CategoryNotification ccnotif) {
		
		categoryDao.mergeCategory(ccnotif.getOperand1(), ccnotif.getOperand2(), ccnotif.getMetadata(), "", false);
	}

	private void notification_action_CATEGORY_DELETE(CategoryNotification ccnotif) {
		
		categoryDao.delete(ccnotif.getOperand1());
	}

	private void notification_action_CATEGORY_CREATE(CategoryNotification ccnotif) {
		
		if(categoryDao.get(ccnotif.getOperand1())!=null)
		{
			ContentCategory ctg=categoryDao.get(ccnotif.getOperand1());
			ctg.setAccepted(true);
			categoryDao.update(ctg);
			return;
		}
		
		ContentCategory ccn=new ContentCategory();
		ccn.setName(ccnotif.getMetadata());
		ccn.setAccepted(true);
		ccn.setId(ccnotif.getOperand1());
		ccn.setParentCategory(ccnotif.getOperand2());
		categoryDao.createNewCategory(ccn);
	}

	private void notification_action_CATEGORY_CHANGED(CategoryNotification ccnotif) {
		
		categoryDao.editCategory(ccnotif.getOperand1(), ccnotif.getMetadata(), "", false);
	}

	private void notification_action_CONTENT_CREATE(ContentNotification cnotif) {
		Content c=new Content();
		c.setId(cnotif.getContentId());
		c.setEncriptedText(cnotif.getMetaInfo());
		c.setContentTag(cnotif.getComment());
		c.setCreatorUser(cnotif.getCreatorUser());
		c.setInsertionDateTime(cnotif.getTimeStamp());
		c.setApproved(true);
		contentDao.insert(c);
		
	}

	private void notification_action_CONTENT_TAG_CHANGED(
			ContentNotification cnotif) {
		contentDao.changeContentCategoryTag(cnotif.getContentId(), cnotif.getOperand1(), cnotif.getOperand2(), "", false);
	}

	private void notification_action_CONTENT_TAG_ADDED(ContentNotification cnotif) {
		contentDao.addContentCategoryTag(cnotif.getContentId(), cnotif.getOperand1(), "", false);
		
	}

	
	private void notification_action_CONTENT_EDIT(ContentNotification cnotif) {
		contentDao.editContent(cnotif.getContentId(), cnotif.getMetaInfo(), "", false);
	}

	private void notification_action_CONTENT_DELETE(ContentNotification cnotif) {
		
		contentDao.delete(cnotif.getContentId());
	}
	
	
	
	public static long getLastContentTableUpdateTimeStamp(Context context)
	{
		SharedPreferences pref= context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		return pref.getLong(CONTENT_TABLE_UPDATE_TIMESTAMP, 0);
	}
	
	public static void setLastContentTableUpdateTimeStamp(Context context, long timeStaamp)
	{
		SharedPreferences pref= context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor=pref.edit();
		editor.putLong(CONTENT_TABLE_UPDATE_TIMESTAMP, timeStaamp);
		editor.commit();
	}
	
	public static long getLastCategoryTableUpdateTimeStamp(Context context)
	{
		SharedPreferences pref= context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		return pref.getLong(CATEGORY_TABLE_UPDATE_TIMESTAMP, 0);
	}
	
	public static void setLastCategoryTableUpdateTimeStamp(Context context, long timeStaamp)
	{
		SharedPreferences pref= context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE);
		Editor editor=pref.edit();
		editor.putLong(CATEGORY_TABLE_UPDATE_TIMESTAMP, timeStaamp);
		editor.commit();
	}
	
	
	
	
}
