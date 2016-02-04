package com.peaceworld.wikisms.model.dao;

import android.content.Context;

import com.peaceworld.wikisms.controller.utility.Utility;
import com.peaceworld.wikisms.model.entity.CategoryNotification;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.dao.CategoryNotificationDao;
import com.peaceworld.wikisms.model.entity.dao.CategoryNotificationTable;

public class CategoryNotificationFullDao extends CategoryNotificationDao{
	
	private Context context;

	public CategoryNotificationFullDao(Context context)
	{
		super(context);
		this.context=context;
	}

	public void createNewCategoryNofication(long localId,String comment) 
	{
		CategoryFullDao categoryFullDao=new CategoryFullDao(context);
		ContentCategory newCatgory=categoryFullDao.get(localId);
		CategoryNotification categoryNotifiction=new CategoryNotification();
		categoryNotifiction.setAction(CategoryNotification.ACTION.CATEGORY_CREATE.name());
		categoryNotifiction.setCreatorUser(Utility.getUserIdentifier(context));
		categoryNotifiction.setId(generateNewId());
		categoryNotifiction.setMetadata(newCatgory.getName());
		categoryNotifiction.setOperand1(localId);
		categoryNotifiction.setOperand2(newCatgory.getParentCategory());
		categoryNotifiction.setAdministrationLevel(0);
		categoryNotifiction.setComment(comment);
		insert(categoryNotifiction);
		
	}
	
	public void createMoveCategoryNotification(long movedCategoryId, long parentCategoryId,String comment)
	{
		CategoryNotification categoryNotifiction=new CategoryNotification();
		categoryNotifiction.setAction(CategoryNotification.ACTION.CATEGORY_MOVE.name());
		categoryNotifiction.setCreatorUser(Utility.getUserIdentifier(context));
		categoryNotifiction.setId(generateNewId());
		categoryNotifiction.setOperand1(movedCategoryId);
		categoryNotifiction.setOperand2(parentCategoryId);
		categoryNotifiction.setAdministrationLevel(0);
		categoryNotifiction.setComment(comment);
		insert(categoryNotifiction);
	}

	public void createMergeCatgoryNotification(long category1Id, long category2Id,String newCategoryName,String comment)
	{
		
		CategoryNotification categoryNotifiction=new CategoryNotification();
		categoryNotifiction.setAction(CategoryNotification.ACTION.CATEGORY_MERGE.name());
		categoryNotifiction.setCreatorUser(Utility.getUserIdentifier(context));
		categoryNotifiction.setId(generateNewId());
		categoryNotifiction.setOperand1(category1Id);
		categoryNotifiction.setOperand2(category2Id);
		categoryNotifiction.setMetadata(newCategoryName);
		categoryNotifiction.setAdministrationLevel(0);
		categoryNotifiction.setComment(comment);
		insert(categoryNotifiction);
	}
	
	public void createChangeCatgoryNotification(long categoryId, String newCategoryName,String comment)
	{
		
		CategoryNotification categoryNotifiction=new CategoryNotification();
		categoryNotifiction.setAction(CategoryNotification.ACTION.CATEGORY_CHANGED.name());
		categoryNotifiction.setCreatorUser(Utility.getUserIdentifier(context));
		categoryNotifiction.setId(generateNewId());
		categoryNotifiction.setOperand1(categoryId);
		categoryNotifiction.setMetadata(newCategoryName);
		categoryNotifiction.setAdministrationLevel(0);
		categoryNotifiction.setComment(comment);
		insert(categoryNotifiction);
	}
	
	public void createDeleteCatgoryNotification(long categoryId,String comment)
	{
		CategoryNotification categoryNotifiction=new CategoryNotification();
		categoryNotifiction.setAction(CategoryNotification.ACTION.CATEGORY_DELETE.name());
		categoryNotifiction.setCreatorUser(Utility.getUserIdentifier(context));
		categoryNotifiction.setId(generateNewId());
		categoryNotifiction.setOperand1(categoryId);
		categoryNotifiction.setAdministrationLevel(0);
		categoryNotifiction.setComment(comment);
		insert(categoryNotifiction);
	}
	
	private long generateNewId()
	{
		return Utility.generateUniqueIdForTable(new CategoryNotificationTable().getTableName(), context);
	}
	
}
