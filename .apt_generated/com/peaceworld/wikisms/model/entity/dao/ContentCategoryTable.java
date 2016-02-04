package com.peaceworld.wikisms.model.entity.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import com.turbomanage.storm.query.FilterBuilder;
import com.turbomanage.storm.TableHelper;
import java.util.Map;
import java.util.HashMap;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.BooleanConverter;
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
public class ContentCategoryTable extends TableHelper<ContentCategory> {

	public enum Columns implements TableHelper.Column {
		ACCEPTED,
		DENIED,
		_id,
		LASTCONTENTID,
		NAME,
		PARENTCATEGORY,
		SELFDEFINED
	}

	@Override
	public String getTableName() {
		return "ContentCategory";
	}

	@Override
	public Column[] getColumns() {
		return Columns.values();
	}
	
	@Override
	public long getId(ContentCategory obj) {
		return obj.getId();
	}
	
	@Override
	public void setId(ContentCategory obj, long id) {
		obj.setId(id);
	}

	@Override
	public Column getIdCol() {
		return Columns._id;
	}

	@Override
	public String createSql() {
		return
			"CREATE TABLE IF NOT EXISTS ContentCategory(" +
				"ACCEPTED INTEGER NOT NULL," +
				"DENIED INTEGER NOT NULL," +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"LASTCONTENTID INTEGER NOT NULL," +
				"NAME TEXT," +
				"PARENTCATEGORY INTEGER NOT NULL," +
				"SELFDEFINED INTEGER NOT NULL" +
			")";
	}

	@Override
	public String dropSql() {
		return "DROP TABLE IF EXISTS ContentCategory";
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
		colIdx = c.getColumnIndex("ACCEPTED"); values[0] = (colIdx < 0) ? defaultValues[0] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("DENIED"); values[1] = (colIdx < 0) ? defaultValues[1] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("_id"); values[2] = (colIdx < 0) ? defaultValues[2] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("LASTCONTENTID"); values[3] = (colIdx < 0) ? defaultValues[3] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("NAME"); values[4] = (colIdx < 0) ? defaultValues[4] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		colIdx = c.getColumnIndex("PARENTCATEGORY"); values[5] = (colIdx < 0) ? defaultValues[5] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("SELFDEFINED"); values[6] = (colIdx < 0) ? defaultValues[6] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		return values;
	}

	@Override
	public void bindRowValues(InsertHelper insHelper, String[] rowValues) {
		if (rowValues[0] == null) insHelper.bindNull(1); else insHelper.bind(1, BooleanConverter.GET.fromString(rowValues[0]));
		if (rowValues[1] == null) insHelper.bindNull(2); else insHelper.bind(2, BooleanConverter.GET.fromString(rowValues[1]));
		if (rowValues[2] == null) insHelper.bindNull(3); else insHelper.bind(3, LongConverter.GET.fromString(rowValues[2]));
		if (rowValues[3] == null) insHelper.bindNull(4); else insHelper.bind(4, LongConverter.GET.fromString(rowValues[3]));
		if (rowValues[4] == null) insHelper.bindNull(5); else insHelper.bind(5, StringConverter.GET.fromString(rowValues[4]));
		if (rowValues[5] == null) insHelper.bindNull(6); else insHelper.bind(6, LongConverter.GET.fromString(rowValues[5]));
		if (rowValues[6] == null) insHelper.bindNull(7); else insHelper.bind(7, BooleanConverter.GET.fromString(rowValues[6]));
	}

	@Override
	public String[] getDefaultValues() {
		String[] values = new String[getColumns().length];
		ContentCategory defaultObj = new ContentCategory();
		values[0] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isAccepted()));
		values[1] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isDenied()));
		values[2] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getId()));
		values[3] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getLastContentId()));
		values[4] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getName()));
		values[5] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getParentCategory()));
		values[6] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isSelfDefined()));
		return values;
	}

	@Override
	public ContentCategory newInstance(Cursor c) {
		ContentCategory obj = new ContentCategory();
		obj.setAccepted(c.getInt(0) == 1 ? true : false);
		obj.setDenied(c.getInt(1) == 1 ? true : false);
		obj.setId(c.getLong(2));
		obj.setLastContentId(c.getLong(3));
		obj.setName(c.getString(4));
		obj.setParentCategory(c.getLong(5));
		obj.setSelfDefined(c.getInt(6) == 1 ? true : false);
		return obj;
	}

	@Override
	public ContentValues getEditableValues(ContentCategory obj) {
		ContentValues cv = new ContentValues();
		cv.put("ACCEPTED", obj.isAccepted() ? 1 : 0);
		cv.put("DENIED", obj.isDenied() ? 1 : 0);
		cv.put("_id", obj.getId());
		cv.put("LASTCONTENTID", obj.getLastContentId());
		cv.put("NAME", obj.getName());
		cv.put("PARENTCATEGORY", obj.getParentCategory());
		cv.put("SELFDEFINED", obj.isSelfDefined() ? 1 : 0);
		return cv;
	}

	@Override
	public FilterBuilder buildFilter(FilterBuilder filter, ContentCategory obj) {
		ContentCategory defaultObj = new ContentCategory();
		// Include fields in query if they differ from the default object
		if (obj.isAccepted() != defaultObj.isAccepted())
			filter = filter.eq(Columns.ACCEPTED, BooleanConverter.GET.toSql(obj.isAccepted()));
		if (obj.isDenied() != defaultObj.isDenied())
			filter = filter.eq(Columns.DENIED, BooleanConverter.GET.toSql(obj.isDenied()));
		if (obj.getId() != defaultObj.getId())
			filter = filter.eq(Columns._id, LongConverter.GET.toSql(obj.getId()));
		if (obj.getLastContentId() != defaultObj.getLastContentId())
			filter = filter.eq(Columns.LASTCONTENTID, LongConverter.GET.toSql(obj.getLastContentId()));
		if (obj.getName() != defaultObj.getName())
			filter = filter.eq(Columns.NAME, StringConverter.GET.toSql(obj.getName()));
		if (obj.getParentCategory() != defaultObj.getParentCategory())
			filter = filter.eq(Columns.PARENTCATEGORY, LongConverter.GET.toSql(obj.getParentCategory()));
		if (obj.isSelfDefined() != defaultObj.isSelfDefined())
			filter = filter.eq(Columns.SELFDEFINED, BooleanConverter.GET.toSql(obj.isSelfDefined()));
		return filter;
	}

}