package com.peaceworld.wikisms.model.dao;

import android.content.Context;

import com.peaceworld.wikisms.controller.utility.Utility;
import com.peaceworld.wikisms.model.entity.ContentNotification;
import com.peaceworld.wikisms.model.entity.dao.ContentNotificationDao;
import com.peaceworld.wikisms.model.entity.dao.ContentNotificationTable;

public class ContentNotificationFullDao extends ContentNotificationDao {
	
	private Context context;

	public ContentNotificationFullDao(Context context)
	{
		super(context);
		this.context=context;
	}
	
	public void createNotificationTagChanged(long contentId,long oldCategoryId,long newCategoryId,String comment)
	{
		ContentNotification contentNotification=new ContentNotification();
		contentNotification.setId(generateNewId());
		contentNotification.setAccept(false);
		contentNotification.setAction(ContentNotification.ACTION.CONTENT_TAG_CHANGED.name());
		contentNotification.setOperand1(oldCategoryId);
		contentNotification.setOperand2(newCategoryId);
		contentNotification.setContentId(contentId);
		contentNotification.setComment(comment);
		contentNotification.setCreatorUser(Utility.getUserIdentifier(context));
		insert(contentNotification);
		
	}
	

	
	
	
	public void createNotificationTagAdd(long contentId,long newCategoryId , String comment)
	{
		ContentNotification contentNotification=new ContentNotification();
		contentNotification.setId(generateNewId());
		contentNotification.setAccept(false);
		contentNotification.setAction(ContentNotification.ACTION.CONTENT_TAG_ADDED.name());
		contentNotification.setOperand1(newCategoryId);
		contentNotification.setContentId(contentId);
		contentNotification.setComment(comment);
		contentNotification.setCreatorUser(Utility.getUserIdentifier(context));
		insert(contentNotification);
		
	}
	
	
	public void createNotificationContentAdded(String newContent,long parentCategoryId,String comment)
	{
		ContentNotification contentNotification=new ContentNotification();
		contentNotification.setId(generateNewId());
		contentNotification.setAccept(false);
		contentNotification.setAction(ContentNotification.ACTION.CONTENT_CREATE.name());
		contentNotification.setOperand1(parentCategoryId);
		CategoryFullDao categoryFullDao=new CategoryFullDao(context);
		contentNotification.setOperand2(categoryFullDao.getFirstAcceptedParent(parentCategoryId));
		contentNotification.setMetaInfo(newContent);
		contentNotification.setComment(comment);
		contentNotification.setCreatorUser(Utility.getUserIdentifier(context));
		insert(contentNotification);
		
	}

	
	public void createNotificationContentDelete(long contenId,String comment)
	{
		ContentNotification contentNotification=new ContentNotification();
		contentNotification.setId(generateNewId());
		contentNotification.setAccept(false);
		contentNotification.setAction(ContentNotification.ACTION.CONTENT_DELETE.name());
		contentNotification.setContentId(contenId);
		contentNotification.setComment(comment);
		contentNotification.setCreatorUser(Utility.getUserIdentifier(context));
		insert(contentNotification);
	}
	
	public void createNotificationContentEdit(long contenId,String newValue,String comment)
	{
		ContentNotification contentNotification=new ContentNotification();
		contentNotification.setId(generateNewId());
		contentNotification.setAccept(false);
		contentNotification.setAction(ContentNotification.ACTION.CONTENT_EDIT.name());
		contentNotification.setMetaInfo(newValue);
		contentNotification.setContentId(contenId);
		contentNotification.setComment(comment);
		contentNotification.setCreatorUser(Utility.getUserIdentifier(context));
		insert(contentNotification);
	}
	
	private long generateNewId()
	{
		return Utility.generateUniqueIdForTable(new ContentNotificationTable().getTableName(), context);
	}
	
}
