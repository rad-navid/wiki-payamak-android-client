package com.peaceworld.wikisms.model.entity.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import com.turbomanage.storm.query.FilterBuilder;
import com.turbomanage.storm.TableHelper;
import java.util.Map;
import java.util.HashMap;
import com.peaceworld.wikisms.model.entity.ContentDirectChange;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.LongConverter;
import com.turbomanage.storm.types.IntegerConverter;

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
public class ContentDirectChangeTable extends TableHelper<ContentDirectChange> {

	public enum Columns implements TableHelper.Column {
		_id,
		LIKED,
		TIMESTAMP,
		VIEWD
	}

	@Override
	public String getTableName() {
		return "ContentDirectChange";
	}

	@Override
	public Column[] getColumns() {
		return Columns.values();
	}
	
	@Override
	public long getId(ContentDirectChange obj) {
		return obj.getId();
	}
	
	@Override
	public void setId(ContentDirectChange obj, long id) {
		obj.setId(id);
	}

	@Override
	public Column getIdCol() {
		return Columns._id;
	}

	@Override
	public String createSql() {
		return
			"CREATE TABLE IF NOT EXISTS ContentDirectChange(" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"LIKED INTEGER NOT NULL," +
				"TIMESTAMP INTEGER NOT NULL," +
				"VIEWD INTEGER NOT NULL" +
			")";
	}

	@Override
	public String dropSql() {
		return "DROP TABLE IF EXISTS ContentDirectChange";
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
		colIdx = c.getColumnIndex("LIKED"); values[1] = (colIdx < 0) ? defaultValues[1] : IntegerConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("TIMESTAMP"); values[2] = (colIdx < 0) ? defaultValues[2] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("VIEWD"); values[3] = (colIdx < 0) ? defaultValues[3] : IntegerConverter.GET.toString(getIntOrNull(c, colIdx));
		return values;
	}

	@Override
	public void bindRowValues(InsertHelper insHelper, String[] rowValues) {
		if (rowValues[0] == null) insHelper.bindNull(1); else insHelper.bind(1, LongConverter.GET.fromString(rowValues[0]));
		if (rowValues[1] == null) insHelper.bindNull(2); else insHelper.bind(2, IntegerConverter.GET.fromString(rowValues[1]));
		if (rowValues[2] == null) insHelper.bindNull(3); else insHelper.bind(3, LongConverter.GET.fromString(rowValues[2]));
		if (rowValues[3] == null) insHelper.bindNull(4); else insHelper.bind(4, IntegerConverter.GET.fromString(rowValues[3]));
	}

	@Override
	public String[] getDefaultValues() {
		String[] values = new String[getColumns().length];
		ContentDirectChange defaultObj = new ContentDirectChange();
		values[0] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getId()));
		values[1] = IntegerConverter.GET.toString(IntegerConverter.GET.toSql(defaultObj.getLiked()));
		values[2] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getTimeStamp()));
		values[3] = IntegerConverter.GET.toString(IntegerConverter.GET.toSql(defaultObj.getViewd()));
		return values;
	}

	@Override
	public ContentDirectChange newInstance(Cursor c) {
		ContentDirectChange obj = new ContentDirectChange();
		obj.setId(c.getLong(0));
		obj.setLiked(c.getInt(1));
		obj.setTimeStamp(c.getLong(2));
		obj.setViewd(c.getInt(3));
		return obj;
	}

	@Override
	public ContentValues getEditableValues(ContentDirectChange obj) {
		ContentValues cv = new ContentValues();
		cv.put("_id", obj.getId());
		cv.put("LIKED", obj.getLiked());
		cv.put("TIMESTAMP", obj.getTimeStamp());
		cv.put("VIEWD", obj.getViewd());
		return cv;
	}

	@Override
	public FilterBuilder buildFilter(FilterBuilder filter, ContentDirectChange obj) {
		ContentDirectChange defaultObj = new ContentDirectChange();
		// Include fields in query if they differ from the default object
		if (obj.getId() != defaultObj.getId())
			filter = filter.eq(Columns._id, LongConverter.GET.toSql(obj.getId()));
		if (obj.getLiked() != defaultObj.getLiked())
			filter = filter.eq(Columns.LIKED, IntegerConverter.GET.toSql(obj.getLiked()));
		if (obj.getTimeStamp() != defaultObj.getTimeStamp())
			filter = filter.eq(Columns.TIMESTAMP, LongConverter.GET.toSql(obj.getTimeStamp()));
		if (obj.getViewd() != defaultObj.getViewd())
			filter = filter.eq(Columns.VIEWD, IntegerConverter.GET.toSql(obj.getViewd()));
		return filter;
	}

}