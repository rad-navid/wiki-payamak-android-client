package com.peaceworld.wikisms.controller.rs.manager;

import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.peaceworld.wikisms.controller.rs.Utilities;
import com.peaceworld.wikisms.controller.rs.common.ContentCategoryServices;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryDao;

public class ContentCategoryManeger {
	
	public static boolean ReIndexCategories(Context context)
	{
		String encryped=ContentCategoryServices.getAllEncrypted();
		String decrypted=Utilities.gzipUncompress(encryped);
		Gson gson=new Gson();
		List<ContentCategory> cateories = null;
		cateories = 
				(List<ContentCategory>)gson.
				fromJson(decrypted, new TypeToken<List<ContentCategory>>() {}.getType());

		ContentCategoryDao dao=new ContentCategoryDao(context);
		dao.deleteAll();
		
		for(ContentCategory cat:cateories)
		{
			cat.setAccepted(true);
		}

		boolean successfull= dao.insertMany(cateories)==-1;
		cateories=dao.listAll();
		return successfull;
	}

}
