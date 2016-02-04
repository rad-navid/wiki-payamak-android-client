package com.peaceworld.wikisms.model.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentDirectChange;
import com.peaceworld.wikisms.model.entity.dao.ContentDao;
import com.peaceworld.wikisms.model.entity.dao.ContentDirectChangeDao;
import com.peaceworld.wikisms.model.entity.dao.ContentTable;

public class ContentFullDao extends ContentDao {
	private Context context;
	private ContentDirectChangeDao contentDirectChangeDao;
	private static CryptoMessage crypto;

	public ContentFullDao(Context context)
	{
		super(context);
		this.context=context;
		contentDirectChangeDao =new ContentDirectChangeDao(context);
		try {
			if(crypto==null)
				crypto = new CryptoMessage(context,context.getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE).getString("memo2", ""));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	public Content get(Long id)
	{
		Content content=super.get(id);
		return normalizeContent(content);
	}
	
	public boolean isMemberOf( long contentId,long categoryId)
	{
		Content content=get(contentId); 
		return content.getContentTag().contains(";"+categoryId+";");
	}
	
	public void changeContentCategoryTag(long contentId, long oldCategoryId,long newCategoryId, String comment, boolean createNotification)
	{
		
		Content content=get(contentId);     
		
		String newContentTag=content.getContentTag().replace(";"+oldCategoryId+";"	, ";")
				.replace(";"+newCategoryId+";", ";")+newCategoryId+";";
		
		content.setContentTag(newContentTag);
		content.setPlainText("");
		update(content);
		
		if(createNotification)
			new ContentNotificationFullDao(context).createNotificationTagChanged(contentId, oldCategoryId, newCategoryId,comment);
		
		
	}
	
	public void addContentCategoryTag(long contentId,long newCategoryId, String comment, boolean createNotification)
	{
		Content content=get(contentId);
		String newContentTag=content.getContentTag().
				replace(";"+newCategoryId+";", ";")+newCategoryId+";";
		content.setContentTag(newContentTag);
		content.setPlainText("");
		update(content);
		
		if(createNotification)
			new ContentNotificationFullDao(context).createNotificationTagAdd(contentId, newCategoryId,comment);
		
		
	}
	
	public void createContent(String newContent,long newCategoryId,String comment)
	{
		new ContentNotificationFullDao(context).createNotificationContentAdded(newContent, newCategoryId,comment);
		
	}
	
	public boolean editContent(long contentId,String newValue,String comment,boolean createNotifiction)
	{
		
		Content content=get(contentId);
		String cryptedValue="";
		try {
			cryptedValue = crypto.encrypt(newValue);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		content.setEncriptedText(cryptedValue);
		content.setPlainText("");
		update(content);
		if(createNotifiction)
			new ContentNotificationFullDao(context).createNotificationContentEdit(contentId, newValue, comment);
		
		return true;
	}

	public boolean deleteContent(long contentId,String comment, boolean createNotification)
	{

		if(createNotification)
			new ContentNotificationFullDao(context).createNotificationContentDelete(contentId, comment);
		
		ContentFullDao contentDao=new ContentFullDao(context);
		int result=contentDao.delete(contentId);
		return result!=0;
		
	}

	public void contentViewed(long contentId)
	{
		Content content=get(contentId);
		content.setViewed(true);
		content.setPlainText("");
		update(content);
		
		ContentDirectChange directChanege=contentDirectChangeDao.get(contentId);
		if(directChanege==null)
		{
			directChanege=new ContentDirectChange();
			directChanege.setId(contentId);
			directChanege.setLiked(0);
			directChanege.setViewd(1);
			contentDirectChangeDao.insert(directChanege);
		}
	}
	
	public void contentLiked(long contentId)
	{
		Content content=get(contentId);
		content.setLiked(true);
		content.setPlainText("");
		update(content);
		
		updateContentState(contentId);
	}
	
	public void updateContentState(long contentId)
	{
		ContentDirectChange directChanege=contentDirectChangeDao.get(contentId);
		if(directChanege==null)
		{
			directChanege=new ContentDirectChange();
			directChanege.setId(contentId);
			directChanege.setLiked(1);
			directChanege.setViewd(1);
			contentDirectChangeDao.insert(directChanege);
		}
		else
		{
			directChanege.setLiked(1);
			directChanege.setViewd(1);
			contentDirectChangeDao.update(directChanege);
		}
	}
	
	public void contentUnLiked(long contentId)
	{
		Content content=get(contentId);
		content.setLiked(false);
		content.setPlainText("");
		update(content);
		
		ContentDirectChange directChanege=contentDirectChangeDao.get(contentId);
		if(directChanege==null)
		{
			directChanege=new ContentDirectChange();
			directChanege.setId(contentId);
			directChanege.setLiked(0);
			contentDirectChangeDao.insert(directChanege);
		}
		else
		{
			directChanege.setLiked(0);
			contentDirectChangeDao.update(directChanege);
		}	
	}

	public void contentMarkedAsFavorite(long contentId)
	{
		Content content=get(contentId);
		content.setFavorite(true);
		content.setPlainText("");
		update(content);
	}
	
	public void contentMarkedAsUnFavorite(long contentId)
	{
		Content content=get(contentId);
		content.setFavorite(false);
		content.setPlainText("");
		update(content);
	}
	
	public ArrayList<Content> getAllContents(long fromId, long toId)
	{
		String queryString=ContentTable.Columns._id+" >="+ fromId+" AND " 
				+ContentTable.Columns._id+" <= "+toId;
		Cursor cursor=query(queryString, null );
		ArrayList<Content> result= (ArrayList<Content>) asList(cursor);
		return normalizeContentList(result);
	}
	
	public ArrayList<Content> getAllContentByCategoryId(long categoryId)
	{
		String tmpQuery=addCategoryIdToSQLQuery(categoryId);
		Cursor cursor=query(tmpQuery, null );
		ArrayList<Content> result= (ArrayList<Content>) asList(cursor);
		return normalizeContentList(result);
	}
	public ArrayList<Content> getAllContentFilteredSorted(ArrayList<Content> exceptionList, int limit, boolean orderByView,boolean orderByLiked,boolean orderByDate)
	{
		String query="1==1"+ addExceptionQuery(exceptionList);
		query+= addFilterViewQuery();
		if(orderByDate)
		query+= " ORDER BY "+ContentTable.Columns.INSERTIONDATETIME+" DESC   ";
		else if(orderByLiked)
			query+= " ORDER BY "+ContentTable.Columns.LIKEDCOUNTER+" DESC   ";
		else if(orderByView)
			query+= " ORDER BY "+ContentTable.Columns.VIEWEDCOUNTER+" DESC   ";
		query+= " LIMIT "+ limit;
		Cursor cursor=query(query, null );
		ArrayList<Content> result= (ArrayList<Content>) asList(cursor);
		return normalizeContentList(result);
	}

	public ArrayList<Content> getAllContentByCategoryIdSortedFiltered(long categoryId, ArrayList<Content> exceptionList, int limit)
	{
		String query="  ";
		query+=addCategoryIdToSQLQuery(categoryId)+"  ";
		query+= addExceptionQuery(exceptionList);
		query+= addFilterViewQuery();
		query+= addOrderQuery();
		query+= " LIMIT "+ limit;
		Cursor cursor=query(query, null );
		ArrayList<Content> result= (ArrayList<Content>) asList(cursor);
		return normalizeContentList(result);
	}
	public List<Content> getAllFavoritsContent(ArrayList<Content> exceptionList, int limit) {
		String query =ContentTable.Columns.FAVORITE+" = 1   " ;
		query += addExceptionQuery(exceptionList);
		query += " LIMIT "+ limit;
		Cursor cursor=query(query, null );
		ArrayList<Content> result= (ArrayList<Content>) asList(cursor);
		return normalizeContentList(result);
	}
	
	public List<Content> getRandomContents(ArrayList<Content> exceptionList, int limit) {
		
		String sql="SELECT * FROM Content ORDER BY RANDOM() LIMIT "+limit;
		Cursor curs=getReadableDb().rawQuery(sql, null);
		ArrayList<Content> result = (ArrayList<Content>) asList(curs);
		normalizeContentList(result);
		return result;
	}
	
	private String addFilterViewQuery() {
		
		String viewFilter=Settings.getViewFilter(context);
		if(viewFilter.equalsIgnoreCase(Settings.VIEW_ALL))
			return "";
		else if(viewFilter.equalsIgnoreCase(Settings.VIEW_UNVIEWED))
			return " AND  "+ContentTable.Columns.VIEWED+" = 0  ";
		else if(viewFilter.equalsIgnoreCase(Settings.VIEW_VIEWED))
			return " AND  "+ContentTable.Columns.VIEWED+" = 1  ";
		
		return null;
	}

	private String addOrderQuery() {
		
		String orderFilter=Settings.getOrderFilter(context);
		if(orderFilter.equalsIgnoreCase(Settings.ORDER_BY_DATE))
			 return " ORDER BY "+ContentTable.Columns.INSERTIONDATETIME+" DESC   ";
		else if(orderFilter.equalsIgnoreCase(Settings.ORDER_BY_LIKED))
			return " ORDER BY "+ContentTable.Columns.LIKEDCOUNTER+" DESC   ";
		else if(orderFilter.equalsIgnoreCase(Settings.ORDER_BY_VIEWED))
			return " ORDER BY "+ContentTable.Columns.VIEWEDCOUNTER+" DESC   ";
		return "";
	}

	private String addExceptionQuery(ArrayList<Content> exceptionList) {
		
		String query="";

		if(exceptionList!=null && exceptionList.size()>0)
		{
			query+=" AND  "+ContentTable.Columns._id+" NOT IN ( ";
			for(int i=0;i<exceptionList.size()-1;i++)
				query+=exceptionList.get(i).getId()+",";
			query+=exceptionList.get(exceptionList.size()-1).getId()+" )";
		}
		return query;
	}

	private String addCategoryIdToSQLQuery(long categoryId)
	{
		return ContentTable.Columns.CONTENTTAG+" LIKE \'%;"+categoryId+";%\' ";
	}
	
	private ArrayList<Content> normalizeContentList(ArrayList<Content> contents)
	{
		for(Content c:contents)
			normalizeContent(c);
		return contents;
	}
	
	private Content normalizeContent(Content content)
	{
		if(content==null)
			return null;
		try {
			content.setPlainText(crypto.decrypt(content.getEncriptedText()));
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
