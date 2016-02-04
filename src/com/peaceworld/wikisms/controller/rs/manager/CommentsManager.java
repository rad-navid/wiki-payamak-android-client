package com.peaceworld.wikisms.controller.rs.manager;

import java.util.List;

import android.content.Context;

import com.peaceworld.wikisms.controller.rs.common.CommentsService;
import com.peaceworld.wikisms.controller.utility.Utility;
import com.peaceworld.wikisms.model.entity.Comments;
import com.peaceworld.wikisms.model.entity.dao.CommentsDao;

public class CommentsManager {
	
	public static void SendAllComments(Context context)
	{
		try{
			CommentsDao dao=new CommentsDao(context);
			List<Comments> commentsList=dao.listAll();
			for(Comments c:commentsList)
			{
				boolean result=CommentsService.createNewComments(Utility.getUserIdentifier(context), c.getComment());
				if(result)
					dao.delete(c.getId());
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	

}
