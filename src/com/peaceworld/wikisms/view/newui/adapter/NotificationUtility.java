package com.peaceworld.wikisms.view.newui.adapter;

import android.content.Context;

import com.peaceworld.wikisms.controller.rs.manager.CategoryNotificationClickedHandler;
import com.peaceworld.wikisms.model.dao.CategoryFullDao;
import com.peaceworld.wikisms.model.dao.CategoryNotificationFullDao;
import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.CategoryNotification;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.ContentNotification;
import com.peaceworld.wikisms.view.newui.adapter.NotificationListAdapter.NotificationHelper;
import com.peaceworld.wikisms.view.newui.adapter.NotificationListAdapter.NotificationType;

public class NotificationUtility {
	
	public enum NotificationVerificationType{valid,invalid,validButPreviouslyDenied}
	
	private CategoryFullDao categoryDao;
	private ContentFullDao contentDao;
	private CategoryNotificationFullDao ccnDao;
	private CategoryNotificationClickedHandler categoryNotificationClickedHandler;
	

	public NotificationUtility(Context context)
	{
		this.categoryDao=new CategoryFullDao(context);
		this.contentDao=new ContentFullDao(context);
		ccnDao=new CategoryNotificationFullDao(context);
		categoryNotificationClickedHandler=new CategoryNotificationClickedHandler(context);
	}
	
	public NotificationVerificationType VerifyNotification(NotificationHelper notification)
	{

		if(notification.type==NotificationType.Content)
		{
			ContentNotification cn=(ContentNotification)notification.notification;
			return VerifyContentNotification(cn);
		}
		else if(notification.type==NotificationType.Category)
		{
			CategoryNotification ccn=(CategoryNotification)notification.notification;
			return VerifyCategoryNotification(ccn);
		}
		else return NotificationVerificationType.invalid;
	}
	
	
	private NotificationVerificationType VerifyCategoryNotification(CategoryNotification ccn) {
		
		try{
			CategoryNotification.ACTION action = CategoryNotification.ACTION.valueOf(ccn.getAction());
			switch (action) {
			case CATEGORY_CHANGED:
				return notification_action_CATEGORY_CHANGED(ccn);
			case CATEGORY_CREATE:
				return notification_action_CATEGORY_CREATE(ccn);
			case CATEGORY_DELETE:
				return notification_action_CATEGORY_DELETE(ccn);
			case CATEGORY_MERGE:
				return notification_action_CATEGORY_MERGE(ccn);
			case CATEGORY_MOVE:
				return notification_action_CATEGORY_MOVE(ccn);
			default:
				return NotificationVerificationType.invalid;
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return NotificationVerificationType.invalid;
		}
	}
	
	
	private NotificationVerificationType VerifyContentNotification(ContentNotification cn) {
		
		try{
		
			ContentNotification.ACTION action = ContentNotification.ACTION.valueOf(cn.getAction());
			switch (action) {
			case CONTENT_DELETE:
				return notification_action_CONTENT_DELETE(cn);
			case CONTENT_EDIT:
				return notification_action_CONTENT_EDIT(cn);
			case CONTENT_TAG_ADDED:
				return notification_action_CONTENT_TAG_ADDED(cn);
			case CONTENT_TAG_CHANGED:
				return notification_action_CONTENT_TAG_CHANGED(cn);
			case CONTENT_CREATE:
				return notification_action_CONTENT_CREATE(cn);
			default:
				return NotificationVerificationType.invalid;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return NotificationVerificationType.invalid;
		}
	}
	
	
	private NotificationVerificationType notification_action_CATEGORY_MOVE(CategoryNotification ccnotif) {
		
		ContentCategory c1= categoryDao.get(ccnotif.getOperand1());
		ContentCategory c2= categoryDao.get(ccnotif.getOperand2());
		if(c1 ==null || c2 == null)
			return NotificationVerificationType.invalid;
		else if(c1.isDenied() || c2.isDenied())
		{
			ccnotif.setDeny(true);
			ccnDao.update(ccnotif);
			return NotificationVerificationType.validButPreviouslyDenied;
		}
		return NotificationVerificationType.valid;
		   
	}

	private NotificationVerificationType notification_action_CATEGORY_MERGE(CategoryNotification ccnotif) {
		
		ContentCategory c1= categoryDao.get(ccnotif.getOperand1());
		ContentCategory c2= categoryDao.get(ccnotif.getOperand2());
		if(c1 ==null || c2 == null)
			return NotificationVerificationType.invalid;
		else if(c1.isDenied() || c2.isDenied())
		{
			ccnotif.setDeny(true);
			ccnDao.update(ccnotif);
			return NotificationVerificationType.validButPreviouslyDenied;
		}
		return NotificationVerificationType.valid;
		
	}

	private NotificationVerificationType notification_action_CATEGORY_DELETE(CategoryNotification ccnotif) {
		ContentCategory cc= categoryDao.get(ccnotif.getOperand1());
		if(cc==null)
			return NotificationVerificationType.invalid;
		else if(cc.isDenied())
		{
			ccnotif.setDeny(true);
			ccnDao.update(ccnotif);
			return NotificationVerificationType.validButPreviouslyDenied;
		}
		return NotificationVerificationType.valid;
	}

	private NotificationVerificationType notification_action_CATEGORY_CREATE(CategoryNotification ccnotif) {

		if(categoryDao.get(ccnotif.getOperand1())!=null)
			return NotificationVerificationType.invalid;
		ContentCategory pc= categoryDao.get(ccnotif.getOperand2());
		if(pc==null && ccnotif.getOperand2()!=0 )
			return NotificationVerificationType.invalid;
		if(pc!=null && pc.isDenied())
		{
			categoryNotificationClickedHandler.classifyNotifications(ccnotif, false);
			ccnotif.setDeny(true);
			ccnDao.update(ccnotif);
			return NotificationVerificationType.validButPreviouslyDenied;
		}
		boolean c1=categoryDao.haveSameChiled(ccnotif.getMetadata(), ccnotif.getOperand2());
		if(c1)
			return NotificationVerificationType.invalid;
	
		else
			return NotificationVerificationType.valid;
	}

	private NotificationVerificationType notification_action_CATEGORY_CHANGED(CategoryNotification ccnotif) {
		ContentCategory c1= categoryDao.get(ccnotif.getOperand1());
		if(c1==null)
			return NotificationVerificationType.invalid;
		else if(c1.isDenied())
		{
			ccnotif.setDeny(true);
			ccnDao.update(ccnotif);
			return NotificationVerificationType.validButPreviouslyDenied;
		}
		return NotificationVerificationType.valid;
	}

	private NotificationVerificationType notification_action_CONTENT_CREATE(ContentNotification cnotif) {
		
		Content c=contentDao.get(cnotif.getContentId());
		if(c!=null)
			return NotificationVerificationType.invalid;
		else
			return NotificationVerificationType.valid;
		
	}

	private NotificationVerificationType notification_action_CONTENT_TAG_CHANGED(ContentNotification cnotif) {
		
		Content c=contentDao.get(cnotif.getContentId());
		ContentCategory newCategory= categoryDao.get(cnotif.getOperand2());
		ContentCategory oldCategory=categoryDao.get(cnotif.getOperand1());
		if(c==null || newCategory==null || oldCategory==null)
			return NotificationVerificationType.invalid;
		else
			return NotificationVerificationType.valid;
		
	}

	private NotificationVerificationType notification_action_CONTENT_TAG_ADDED(	ContentNotification cnotif) {
		Content c=contentDao.get(cnotif.getContentId());
		ContentCategory newCategory= categoryDao.get(cnotif.getOperand1());
		if(c==null || newCategory == null)
			return NotificationVerificationType.invalid;
		else 
			return NotificationVerificationType.valid;
		
	}


	private NotificationVerificationType notification_action_CONTENT_EDIT(ContentNotification cnotif) {
		Content content=contentDao.get(cnotif.getContentId());
		if(content==null)
			return NotificationVerificationType.invalid;
		else
			return NotificationVerificationType.valid;
	}

	private NotificationVerificationType notification_action_CONTENT_DELETE(ContentNotification cnotif) {
		Content content=contentDao.get(cnotif.getContentId());
		if(content == null)
			return NotificationVerificationType.invalid;
		else 
			return NotificationVerificationType.valid;
		
	}


}
