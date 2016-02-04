package com.peaceworld.wikisms.model.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.peaceworld.wikisms.model.entity.dao.ContentTable;
import com.peaceworld.wikisms.model.entity.dao.UserTableDao;

public class UserFullDao extends UserTableDao {
	private Context ctx;
	public UserFullDao(Context ctx) {
		super(ctx);
		this.ctx=ctx;
	}
	
	public List<Long> getDistinctAvailableUserIds()
	{
		ArrayList<Long>resultList=new ArrayList<Long>();
		String query="SELECT DISTINCT "+ContentTable.Columns.CREATORUSER+" FROM Content WHERE "
				+ContentTable.Columns.CREATORUSER+" NOT IN (SELECT _id FROM UserTable )";
		Cursor curs=getDbHelper(ctx).getWritableDatabase().rawQuery(query, null);
		while(curs.moveToNext())
		{
			long id=curs.getLong(0);
			resultList.add(id);
		}
		
		curs.close();
		return resultList;
		
	}

}
