package com.peaceworld.wikisms.model.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.peaceworld.wikisms.controller.utility.Utility;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryDao;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryTable;

public class CategoryFullDao  extends ContentCategoryDao{
		
	private Context context;
	

	public CategoryFullDao(Context context)
	{
		super(context);
		this.context=context;
	}
		
	public boolean createNewCategory(ContentCategory cc)
	{
		if(haveSameChiled(cc.getName(), cc.getParentCategory() ))
			return false;
		if(cc.getParentCategory() != 0 && get(cc.getParentCategory())==null)
			return false;
		
		long id=insert(cc);
		if(id==-1)
			return false;
		return true;
	}
	
	public boolean mergeCategory(long category1Id,long category2Id, String newName ,String comment, boolean createNotification)
	{
		ContentCategory c1=get(category1Id);
		ContentCategory c2=get(category2Id);
		if(c1==null || c2==null || c1.getParentCategory() !=c2.getParentCategory())
			return false;
		
		List<ContentCategory>subCategories=getAllSubCategories(category2Id);
		for(ContentCategory subCat:subCategories)
		{
			subCat.setParentCategory(c1.getId());
			update(subCat);
		}
		delete(category2Id);
		c1.setName(newName);
		update(c1);
		
		ContentFullDao contentFullDao=new ContentFullDao(context);
		ArrayList<Content>updateList=contentFullDao.getAllContentByCategoryId(category2Id);
		for(Content c:updateList)
			contentFullDao.changeContentCategoryTag(c.getId(), category2Id, category1Id, "", false);
		
		//creating category notification
		if(createNotification)
			new CategoryNotificationFullDao(context).createMergeCatgoryNotification(category1Id, category2Id, newName, comment);
		
		return true;
	}
	
	public boolean moveCategory(long movedCategoryId,long parentCategoryId, String comment, boolean createNotification)
	{
		ContentCategory c=get(movedCategoryId);
		ContentCategory cp=get(parentCategoryId);
		if(c==null || (parentCategoryId!=0 && cp==null) )
			return false;
		
		boolean isAncestor=isAncestorOf(parentCategoryId, movedCategoryId);
		if(isAncestor)
			return false;
		
		c.setParentCategory(parentCategoryId);
		update(c);
		
		//creating category notification
		if(createNotification)
			new CategoryNotificationFullDao(context).createMoveCategoryNotification(movedCategoryId, parentCategoryId, comment);
		
		return true;
	}
	
	public boolean isAncestorOf(long categoryId,long ancestorId)
	{
		if(ancestorId==categoryId)
			return true;
		
		List<ContentCategory>subs= getAllSubCategories(ancestorId);
		if(subs==null || subs.size()<1)
			return false;
		
		for(ContentCategory sub: subs)
		{
			boolean is=isAncestorOf(categoryId, sub.getId());
			if(is)
				return true;
		}
		
		return false;
		
	}
	
	public boolean editCategory(long categoryId,String newCategoryName, String comment, boolean createNotification)
	{
		ContentCategory c=get(categoryId);
		if(c==null || haveSameChiled(newCategoryName, c.getParentCategory()) )
			return false;
		
		c.setName(newCategoryName);
		update(c);
		
		//creating category notification
		if(createNotification)
			new CategoryNotificationFullDao(context).createChangeCatgoryNotification(categoryId, newCategoryName, comment);
		
		return true;
	}
	
	
	public boolean deleteCategory(long categoryId, String comment, boolean createNotification)
	{

		delete(categoryId);
		//creating category notification
		if(createNotification)
			new CategoryNotificationFullDao(context).createDeleteCatgoryNotification(categoryId, comment);
		
		return true;
	}

	
	
	
	
	public boolean haveSameChiled(String name, long parentId)
	{
		ContentCategory example=new ContentCategory();
		example.setName(name);
		example.setParentCategory(parentId);	
		return getByExample(example)!=null;
	}
	
	public long getFirstAcceptedParent(long CategoryId)
	{
		while(true)
		{
			ContentCategory cc=get(CategoryId);
			if(cc==null && CategoryId==0)
				return 0;
			else if(cc==null )
				return -1;
			else if(cc.isAccepted())
				return cc.getId();
			else
			CategoryId=cc.getParentCategory();
		}
	}

	public static long generateNewId(Context context)
	{
		return Utility.generateUniqueIdForTable(new ContentCategoryTable().getTableName(), context);
	}
	
	public List<ContentCategory> getAllSubCategories(long ParentId)
	{
		List<ContentCategory>list=filter().eq(ContentCategoryTable.Columns.PARENTCATEGORY, ParentId).list();
		return list;
	}
	
	
}
