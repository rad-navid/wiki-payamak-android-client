package com.peaceworld.wikisms.model.entity.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import com.turbomanage.storm.query.FilterBuilder;
import com.turbomanage.storm.TableHelper;
import java.util.Map;
import java.util.HashMap;
import com.peaceworld.wikisms.model.entity.Comments;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.StringConverter;
import com.turbomanage.storm.types.LongConverter;

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
public class CommentsTable extends TableHelper<Comments> {

	public enum Columns implements TableHelper.Column {
		COMMENT,
		_id
	}

	@Override
	public String getTableName() {
		return "Comments";
	}

	@Override
	public Column[] getColumns() {
		return Columns.values();
	}
	
	@Override
	public long getId(Comments obj) {
		return obj.getId();
	}
	
	@Override
	public void setId(Comments obj, long id) {
		obj.setId(id);
	}

	@Override
	public Column getIdCol() {
		return Columns._id;
	}

	@Override
	public String createSql() {
		return
			"CREATE TABLE IF NOT EXISTS Comments(" +
				"COMMENT TEXT," +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
			")";
	}

	@Override
	public String dropSql() {
		return "DROP TABLE IF EXISTS Comments";
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
		colIdx = c.getColumnIndex("COMMENT"); values[0] = (colIdx < 0) ? defaultValues[0] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		colIdx = c.getColumnIndex("_id"); values[1] = (colIdx < 0) ? defaultValues[1] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		return values;
	}

	@Override
	public void bindRowValues(InsertHelper insHelper, String[] rowValues) {
		if (rowValues[0] == null) insHelper.bindNull(1); else insHelper.bind(1, StringConverter.GET.fromString(rowValues[0]));
		if (rowValues[1] == null) insHelper.bindNull(2); else insHelper.bind(2, LongConverter.GET.fromString(rowValues[1]));
	}

	@Override
	public String[] getDefaultValues() {
		String[] values = new String[getColumns().length];
		Comments defaultObj = new Comments();
		values[0] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getComment()));
		values[1] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getId()));
		return values;
	}

	@Override
	public Comments newInstance(Cursor c) {
		Comments obj = new Comments();
		obj.setComment(c.getString(0));
		obj.setId(c.getLong(1));
		return obj;
	}

	@Override
	public ContentValues getEditableValues(Comments obj) {
		ContentValues cv = new ContentValues();
		cv.put("COMMENT", obj.getComment());
		cv.put("_id", obj.getId());
		return cv;
	}

	@Override
	public FilterBuilder buildFilter(FilterBuilder filter, Comments obj) {
		Comments defaultObj = new Comments();
		// Include fields in query if they differ from the default object
		if (obj.getComment() != defaultObj.getComment())
			filter = filter.eq(Columns.COMMENT, StringConverter.GET.toSql(obj.getComment()));
		if (obj.getId() != defaultObj.getId())
			filter = filter.eq(Columns._id, LongConverter.GET.toSql(obj.getId()));
		return filter;
	}

}