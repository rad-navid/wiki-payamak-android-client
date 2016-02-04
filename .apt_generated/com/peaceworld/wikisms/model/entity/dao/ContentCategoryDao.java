package com.peaceworld.wikisms.model.entity.dao;

import android.content.Context;
import com.turbomanage.storm.DatabaseHelper;
import com.turbomanage.storm.TableHelper;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.BooleanConverter;
import com.turbomanage.storm.types.LongConverter;
import com.turbomanage.storm.types.StringConverter;

/**
 * GENERATED CODE
 *
 * @author David M. Chandler
 */
public class ContentCategoryDao extends SQLiteDao<ContentCategory>{

    @Override
	public DatabaseHelper getDbHelper(Context ctx) {
		return com.peaceworld.wikisms.model.dao.Wikisms_dbFactory.getDatabaseHelper(ctx);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public TableHelper getTableHelper() {
		return new com.peaceworld.wikisms.model.entity.dao.ContentCategoryTable();
	}

	/**
	 * @see SQLiteDao#SQLiteDao(Context)
	 */
	public ContentCategoryDao(Context ctx) {
		super(ctx);
	}

}