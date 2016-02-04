package com.peaceworld.wikisms.model.entity.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import com.turbomanage.storm.query.FilterBuilder;
import com.turbomanage.storm.TableHelper;
import java.util.Map;
import java.util.HashMap;
import com.peaceworld.wikisms.model.entity.UserTable;
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
public class UserTableTable extends TableHelper<UserTable> {

	public enum Columns implements TableHelper.Column {
		_id,
		USERNAME
	}

	@Override
	public String getTableName() {
		return "UserTable";
	}

	@Override
	public Column[] getColumns() {
		return Columns.values();
	}
	
	@Override
	public long getId(UserTable obj) {
		return obj.getUserIdentifier();
	}
	
	@Override
	public void setId(UserTable obj, long id) {
		obj.setUserIdentifier(id);
	}

	@Override
	public Column getIdCol() {
		return Columns._id;
	}

	@Override
	public String createSql() {
		return
			"CREATE TABLE IF NOT EXISTS UserTable(" +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"USERNAME TEXT" +
			")";
	}

	@Override
	public String dropSql() {
		return "DROP TABLE IF EXISTS UserTable";
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
		colIdx = c.getColumnIndex("USERNAME"); values[1] = (colIdx < 0) ? defaultValues[1] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		return values;
	}

	@Override
	public void bindRowValues(InsertHelper insHelper, String[] rowValues) {
		if (rowValues[0] == null) insHelper.bindNull(1); else insHelper.bind(1, LongConverter.GET.fromString(rowValues[0]));
		if (rowValues[1] == null) insHelper.bindNull(2); else insHelper.bind(2, StringConverter.GET.fromString(rowValues[1]));
	}

	@Override
	public String[] getDefaultValues() {
		String[] values = new String[getColumns().length];
		UserTable defaultObj = new UserTable();
		values[0] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getUserIdentifier()));
		values[1] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getUsername()));
		return values;
	}

	@Override
	public UserTable newInstance(Cursor c) {
		UserTable obj = new UserTable();
		obj.setUserIdentifier(c.getLong(0));
		obj.setUsername(c.getString(1));
		return obj;
	}

	@Override
	public ContentValues getEditableValues(UserTable obj) {
		ContentValues cv = new ContentValues();
		cv.put("_id", obj.getUserIdentifier());
		cv.put("USERNAME", obj.getUsername());
		return cv;
	}

	@Override
	public FilterBuilder buildFilter(FilterBuilder filter, UserTable obj) {
		UserTable defaultObj = new UserTable();
		// Include fields in query if they differ from the default object
		if (obj.getUserIdentifier() != defaultObj.getUserIdentifier())
			filter = filter.eq(Columns._id, LongConverter.GET.toSql(obj.getUserIdentifier()));
		if (obj.getUsername() != defaultObj.getUsername())
			filter = filter.eq(Columns.USERNAME, StringConverter.GET.toSql(obj.getUsername()));
		return filter;
	}

}