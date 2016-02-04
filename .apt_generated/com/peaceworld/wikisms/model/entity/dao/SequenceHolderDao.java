package com.peaceworld.wikisms.model.entity.dao;

import android.content.Context;
import com.turbomanage.storm.DatabaseHelper;
import com.turbomanage.storm.TableHelper;
import com.peaceworld.wikisms.model.entity.SequenceHolder;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.LongConverter;
import com.turbomanage.storm.types.StringConverter;

/**
 * GENERATED CODE
 *
 * @author David M. Chandler
 */
public class SequenceHolderDao extends SQLiteDao<SequenceHolder>{

    @Override
	public DatabaseHelper getDbHelper(Context ctx) {
		return com.peaceworld.wikisms.model.dao.Wikisms_dbFactory.getDatabaseHelper(ctx);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public TableHelper getTableHelper() {
		return new com.peaceworld.wikisms.model.entity.dao.SequenceHolderTable();
	}

	/**
	 * @see SQLiteDao#SQLiteDao(Context)
	 */
	public SequenceHolderDao(Context ctx) {
		super(ctx);
	}

}