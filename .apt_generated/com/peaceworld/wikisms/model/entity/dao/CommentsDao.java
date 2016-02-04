package com.peaceworld.wikisms.model.entity.dao;

import android.content.Context;
import com.turbomanage.storm.DatabaseHelper;
import com.turbomanage.storm.TableHelper;
import com.peaceworld.wikisms.model.entity.Comments;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.StringConverter;
import com.turbomanage.storm.types.LongConverter;

/**
 * GENERATED CODE
 *
 * @author David M. Chandler
 */
public class CommentsDao extends SQLiteDao<Comments>{

    @Override
	public DatabaseHelper getDbHelper(Context ctx) {
		return com.peaceworld.wikisms.model.dao.Wikisms_dbFactory.getDatabaseHelper(ctx);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public TableHelper getTableHelper() {
		return new com.peaceworld.wikisms.model.entity.dao.CommentsTable();
	}

	/**
	 * @see SQLiteDao#SQLiteDao(Context)
	 */
	public CommentsDao(Context ctx) {
		super(ctx);
	}

}