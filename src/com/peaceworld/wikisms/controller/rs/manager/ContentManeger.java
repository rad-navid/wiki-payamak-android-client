package com.peaceworld.wikisms.controller.rs.manager;

import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.controller.rs.Utilities;
import com.peaceworld.wikisms.controller.rs.common.ContentService;
import com.peaceworld.wikisms.controller.services.ContentDownloaderListener;
import com.peaceworld.wikisms.model.dao.CategoryFullDao;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.dao.ContentDao;

public class ContentManeger {
	
	private static final String SERVER_EMPTY_CONTENT_LIST_MESSAGE="EMPTYCONTENTLIST";
	public enum ContentDownloaderMessages{NO_MORE_CONTENT,SERVER_CONNECTION_ERROR,SUCCESSFUL,UNDIFINED_CATEGORY,INTERNET_CONNECTION_ERROR}
	public static List<Content> downloadAndReturnNewContents(long categoryId,int limit,Context context, ContentDownloaderListener indexerListener)
 {
		ContentDao contentDao = new ContentDao(context);
		CategoryFullDao catFullDao=new CategoryFullDao(context);
		ContentCategory cat=catFullDao.get(categoryId);
		if(cat==null)
		{
			indexerListener.finished(ContentDownloaderMessages.UNDIFINED_CATEGORY);
			return null;
		}
		long lastContentId=cat.getLastContentId();

		Gson gson = new Gson();

		String encryped = ContentService.getByCategory(lastContentId,categoryId, limit);
		if(encryped.equalsIgnoreCase(SERVER_EMPTY_CONTENT_LIST_MESSAGE))
		{
			indexerListener.finished(ContentDownloaderMessages.NO_MORE_CONTENT);
			return null;
		}
			
		String decrypted = Utilities.gzipUncompress(encryped);
		List<Content> contents = null;
		contents = (List<Content>) gson.fromJson(decrypted,new TypeToken<List<Content>>() {}.getType());
		if (contents == null || contents.size() == 0) 
		{
			indexerListener.finished(ContentDownloaderMessages.SERVER_CONNECTION_ERROR);
			System.out.println(encryped);
			return null;
		}
		
		
		
		for (Content c : contents)
		 {
			if(c.getId()>lastContentId)
				lastContentId=c.getId();
			try{
			contentDao.insert(c);
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		 }
		cat.setLastContentId(lastContentId);
		catFullDao.update(cat);
		
		indexerListener.finished(ContentDownloaderMessages.SUCCESSFUL);
		return contents;

	}

}
