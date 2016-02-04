package com.peaceworld.wikisms.model.entity.dao;

import android.content.Context;
import com.turbomanage.storm.DatabaseHelper;
import com.turbomanage.storm.TableHelper;
import com.peaceworld.wikisms.model.entity.ContentDirectChange;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.LongConverter;
import com.turbomanage.storm.types.IntegerConverter;

/**
 * GENERATED CODE
 *
 * @author David M. Chandler
 */
public class ContentDirectChangeDao extends SQLiteDao<ContentDirectChange>{

    @Override
	public DatabaseHelper getDbHelper(Context ctx) {
		return com.peaceworld.wikisms.model.dao.Wikisms_dbFactory.getDatabaseHelper(ctx);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public TableHelper getTableHelper() {
		return new com.peaceworld.wikisms.model.entity.dao.ContentDirectChangeTable();
	}

	/**
	 * @see SQLiteDao#SQLiteDao(Context)
	 */
	public ContentDirectChangeDao(Context ctx) {
		super(ctx);
	}

}