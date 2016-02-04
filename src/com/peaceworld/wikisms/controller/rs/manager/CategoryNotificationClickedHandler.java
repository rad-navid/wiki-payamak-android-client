package com.peaceworld.wikisms.controller.rs.manager;

import android.content.Context;

import com.peaceworld.wikisms.model.dao.CategoryFullDao;
import com.peaceworld.wikisms.model.entity.CategoryNotification;
import com.peaceworld.wikisms.model.entity.ContentCategory;


public class CategoryNotificationClickedHandler {
	
	private CategoryFullDao categoryDao;
	private Context context;
	
	public CategoryNotificationClickedHandler(Context context)
	{
		this.context=context;
	}
	
	public void classifyNotifications(CategoryNotification ccnotif, boolean accepted) {

		CategoryNotification.ACTION action = CategoryNotification.ACTION
				.valueOf(ccnotif.getAction());
		switch (action) {
		case CATEGORY_CHANGED:
			notification_action_CATEGORY_CHANGED(ccnotif);
			break;
		case CATEGORY_CREATE:
			notification_action_CATEGORY_CREATE(ccnotif,accepted);
			break;
		case CATEGORY_DELETE:
			notification_action_CATEGORY_DELETE(ccnotif);
			break;
		case CATEGORY_MERGE:
			notification_action_CATEGORY_MERGE(ccnotif);
			break;
		case CATEGORY_MOVE:
			notification_action_CATEGORY_MOVE(ccnotif);
			break;

		default:
			break;
		}

	}

	private void notification_action_CATEGORY_MOVE(CategoryNotification ccnotif) {
		
	}

	private void notification_action_CATEGORY_MERGE(CategoryNotification ccnotif) {
		
	}

	private void notification_action_CATEGORY_DELETE(
			CategoryNotification ccnotif) {

	}

	private void notification_action_CATEGORY_CREATE(
			CategoryNotification ccnotif, boolean accepted) {
		
		categoryDao=new CategoryFullDao(context);
		
		if(categoryDao.get(ccnotif.getOperand1())!=null)
		{
			categoryDao.get(ccnotif.getOperand1()).setAccepted(true);
			return;
		}
		
		ContentCategory ccn=new ContentCategory();
		ccn.setName(ccnotif.getMetadata());
		ccn.setAccepted(false);
		ccn.setSelfDefined(false);
		ccn.setDenied(!accepted);
		ccn.setId(ccnotif.getOperand1());
		ccn.setParentCategory(ccnotif.getOperand2());
		categoryDao.createNewCategory(ccn);
		
	}

	private void notification_action_CATEGORY_CHANGED(
			CategoryNotification ccnotif) {
		
	}

}
