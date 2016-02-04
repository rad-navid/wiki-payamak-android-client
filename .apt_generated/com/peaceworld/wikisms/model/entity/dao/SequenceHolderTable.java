package com.peaceworld.wikisms.model.entity.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import com.turbomanage.storm.query.FilterBuilder;
import com.turbomanage.storm.TableHelper;
import java.util.Map;
import java.util.HashMap;
import com.peaceworld.wikisms.model.entity.SequenceHolder;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.LongConverter;
import com.turbomanage.storm.types.StringConverter;

/**
 * GENERATED CODE
 *
 * This class contains the SQL DDL for the named entity / table.
 * These methods are not included in the EntityDao class because
 * they must be executed before the Dao can be instantiated, and
 * they are instance methods vs. static so that they can be
 * overridden in a typesafe manner.
 *
 * @author David M. Chandler
 */
public class SequenceHolderTable extends TableHelper<SequenceHolder> {

	public enum Columns implements TableHelper.Column {
		_id,
		SEQUENCE,
		TABLENAME
	}

	@Override
	public String getTableName() {
		return "SequenceHolder";
	}

	@Override
	public Column[] getColumns() {
		return Columns.values();
	}
	
	@Override
	public long getId(SequenceHolder obj) {
		return obj.getId();
	}
	
	@Override
	public void setId(SequenceHolder obj, long id) {
		obj.setId(id);
	}

	@Override
	public Column getIdCol() {
		return Columns._id;
	}

	@Override
	public String createSql() {
		return
			"CREATE TABLE IF NOT EXISTS SequenceHolder(" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"SEQUENCE INTEGER NOT NULL," +
				"TABLENAME TEXT" +
			")";
	}

	@Override
	public String dropSql() {
		return "DROP TABLE IF EXISTS SequenceHolder";
	}

	@Override
	public String upgradeSql(int oldVersion, int newVersion) {
		return null;
	}

	@Override
	public String[] getRowValues(Cursor c) {
		String[] values = new String[this.getColumns().length];
		String[] defaultValues = getDefaultValues();
		int colIdx; // entity field's position in the cursor
		colIdx = c.getColumnIndex("_id"); values[0] = (colIdx < 0) ? defaultValues[0] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("SEQUENCE"); values[1] = (colIdx < 0) ? defaultValues[1] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("TABLENAME"); values[2] = (colIdx < 0) ? defaultValues[2] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		return values;
	}

	@Override
	public void bindRowValues(InsertHelper insHelper, String[] rowValues) {
		if (rowValues[0] == null) insHelper.bindNull(1); else insHelper.bind(1, LongConverter.GET.fromString(rowValues[0]));
		if (rowValues[1] == null) insHelper.bindNull(2); else insHelper.bind(2, LongConverter.GET.fromString(rowValues[1]));
		if (rowValues[2] == null) insHelper.bindNull(3); else insHelper.bind(3, StringConverter.GET.fromString(rowValues[2]));
	}

	@Override
	public String[] getDefaultValues() {
		String[] values = new String[getColumns().length];
		SequenceHolder defaultObj = new SequenceHolder();
		values[0] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getId()));
		values[1] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getSequence()));
		values[2] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getTableName()));
		return values;
	}

	@Override
	public SequenceHolder newInstance(Cursor c) {
		SequenceHolder obj = new SequenceHolder();
		obj.setId(c.getLong(0));
		obj.setSequence(c.getLong(1));
		obj.setTableName(c.getString(2));
		return obj;
	}

	@Override
	public ContentValues getEditableValues(SequenceHolder obj) {
		ContentValues cv = new ContentValues();
		cv.put("_id", obj.getId());
		cv.put("SEQUENCE", obj.getSequence());
		cv.put("TABLENAME", obj.getTableName());
		return cv;
	}

	@Override
	public FilterBuilder buildFilter(FilterBuilder filter, SequenceHolder obj) {
		SequenceHolder defaultObj = new SequenceHolder();
		// Include fields in query if they differ from the default object
		if (obj.getId() != defaultObj.getId())
			filter = filter.eq(Columns._id, LongConverter.GET.toSql(obj.getId()));
		if (obj.getSequence() != defaultObj.getSequence())
			filter = filter.eq(Columns.SEQUENCE, LongConverter.GET.toSql(obj.getSequence()));
		if (obj.getTableName() != defaultObj.getTableName())
			filter = filter.eq(Columns.TABLENAME, StringConverter.GET.toSql(obj.getTableName()));
		return filter;
	}

}