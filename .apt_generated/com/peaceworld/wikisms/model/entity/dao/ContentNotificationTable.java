package com.peaceworld.wikisms.model.entity.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import com.turbomanage.storm.query.FilterBuilder;
import com.turbomanage.storm.TableHelper;
import java.util.Map;
import java.util.HashMap;
import com.peaceworld.wikisms.model.entity.ContentNotification;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.BooleanConverter;
import com.turbomanage.storm.types.StringConverter;
import com.turbomanage.storm.types.IntegerConverter;
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
public class ContentNotificationTable extends TableHelper<ContentNotification> {

	public enum Columns implements TableHelper.Column {
		ACCEPT,
		ACTION,
		ADMINISTRATIONLEVEL,
		COMMENT,
		CONTENTID,
		CREATORUSER,
		DENY,
		_id,
		METAINFO,
		OPERAND1,
		OPERAND2,
		SENTFROMSERVER,
		SENTTOSERVER,
		TIMESTAMP
	}

	@Override
	public String getTableName() {
		return "ContentNotification";
	}

	@Override
	public Column[] getColumns() {
		return Columns.values();
	}
	
	@Override
	public long getId(ContentNotification obj) {
		return obj.getId();
	}
	
	@Override
	public void setId(ContentNotification obj, long id) {
		obj.setId(id);
	}

	@Override
	public Column getIdCol() {
		return Columns._id;
	}

	@Override
	public String createSql() {
		return
			"CREATE TABLE IF NOT EXISTS ContentNotification(" +
				"ACCEPT INTEGER NOT NULL," +
				"ACTION TEXT," +
				"ADMINISTRATIONLEVEL INTEGER NOT NULL," +
				"COMMENT TEXT," +
				"CONTENTID INTEGER NOT NULL," +
				"CREATORUSER INTEGER NOT NULL," +
				"DENY INTEGER NOT NULL," +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"METAINFO TEXT," +
				"OPERAND1 INTEGER NOT NULL," +
				"OPERAND2 INTEGER NOT NULL," +
				"SENTFROMSERVER INTEGER NOT NULL," +
				"SENTTOSERVER INTEGER NOT NULL," +
				"TIMESTAMP INTEGER NOT NULL" +
			")";
	}

	@Override
	public String dropSql() {
		return "DROP TABLE IF EXISTS ContentNotification";
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
		colIdx = c.getColumnIndex("ACCEPT"); values[0] = (colIdx < 0) ? defaultValues[0] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("ACTION"); values[1] = (colIdx < 0) ? defaultValues[1] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		colIdx = c.getColumnIndex("ADMINISTRATIONLEVEL"); values[2] = (colIdx < 0) ? defaultValues[2] : IntegerConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("COMMENT"); values[3] = (colIdx < 0) ? defaultValues[3] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		colIdx = c.getColumnIndex("CONTENTID"); values[4] = (colIdx < 0) ? defaultValues[4] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("CREATORUSER"); values[5] = (colIdx < 0) ? defaultValues[5] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("DENY"); values[6] = (colIdx < 0) ? defaultValues[6] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("_id"); values[7] = (colIdx < 0) ? defaultValues[7] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("METAINFO"); values[8] = (colIdx < 0) ? defaultValues[8] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		colIdx = c.getColumnIndex("OPERAND1"); values[9] = (colIdx < 0) ? defaultValues[9] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("OPERAND2"); values[10] = (colIdx < 0) ? defaultValues[10] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("SENTFROMSERVER"); values[11] = (colIdx < 0) ? defaultValues[11] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("SENTTOSERVER"); values[12] = (colIdx < 0) ? defaultValues[12] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("TIMESTAMP"); values[13] = (colIdx < 0) ? defaultValues[13] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		return values;
	}

	@Override
	public void bindRowValues(InsertHelper insHelper, String[] rowValues) {
		if (rowValues[0] == null) insHelper.bindNull(1); else insHelper.bind(1, BooleanConverter.GET.fromString(rowValues[0]));
		if (rowValues[1] == null) insHelper.bindNull(2); else insHelper.bind(2, StringConverter.GET.fromString(rowValues[1]));
		if (rowValues[2] == null) insHelper.bindNull(3); else insHelper.bind(3, IntegerConverter.GET.fromString(rowValues[2]));
		if (rowValues[3] == null) insHelper.bindNull(4); else insHelper.bind(4, StringConverter.GET.fromString(rowValues[3]));
		if (rowValues[4] == null) insHelper.bindNull(5); else insHelper.bind(5, LongConverter.GET.fromString(rowValues[4]));
		if (rowValues[5] == null) insHelper.bindNull(6); else insHelper.bind(6, LongConverter.GET.fromString(rowValues[5]));
		if (rowValues[6] == null) insHelper.bindNull(7); else insHelper.bind(7, BooleanConverter.GET.fromString(rowValues[6]));
		if (rowValues[7] == null) insHelper.bindNull(8); else insHelper.bind(8, LongConverter.GET.fromString(rowValues[7]));
		if (rowValues[8] == null) insHelper.bindNull(9); else insHelper.bind(9, StringConverter.GET.fromString(rowValues[8]));
		if (rowValues[9] == null) insHelper.bindNull(10); else insHelper.bind(10, LongConverter.GET.fromString(rowValues[9]));
		if (rowValues[10] == null) insHelper.bindNull(11); else insHelper.bind(11, LongConverter.GET.fromString(rowValues[10]));
		if (rowValues[11] == null) insHelper.bindNull(12); else insHelper.bind(12, BooleanConverter.GET.fromString(rowValues[11]));
		if (rowValues[12] == null) insHelper.bindNull(13); else insHelper.bind(13, BooleanConverter.GET.fromString(rowValues[12]));
		if (rowValues[13] == null) insHelper.bindNull(14); else insHelper.bind(14, LongConverter.GET.fromString(rowValues[13]));
	}

	@Override
	public String[] getDefaultValues() {
		String[] values = new String[getColumns().length];
		ContentNotification defaultObj = new ContentNotification();
		values[0] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isAccept()));
		values[1] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getAction()));
		values[2] = IntegerConverter.GET.toString(IntegerConverter.GET.toSql(defaultObj.getAdministrationLevel()));
		values[3] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getComment()));
		values[4] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getContentId()));
		values[5] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getCreatorUser()));
		values[6] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isDeny()));
		values[7] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getId()));
		values[8] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getMetaInfo()));
		values[9] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getOperand1()));
		values[10] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getOperand2()));
		values[11] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isSentFromServer()));
		values[12] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isSentToServer()));
		values[13] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getTimeStamp()));
		return values;
	}

	@Override
	public ContentNotification newInstance(Cursor c) {
		ContentNotification obj = new ContentNotification();
		obj.setAccept(c.getInt(0) == 1 ? true : false);
		obj.setAction(c.getString(1));
		obj.setAdministrationLevel(c.getInt(2));
		obj.setComment(c.getString(3));
		obj.setContentId(c.getLong(4));
		obj.setCreatorUser(c.getLong(5));
		obj.setDeny(c.getInt(6) == 1 ? true : false);
		obj.setId(c.getLong(7));
		obj.setMetaInfo(c.getString(8));
		obj.setOperand1(c.getLong(9));
		obj.setOperand2(c.getLong(10));
		obj.setSentFromServer(c.getInt(11) == 1 ? true : false);
		obj.setSentToServer(c.getInt(12) == 1 ? true : false);
		obj.setTimeStamp(c.getLong(13));
		return obj;
	}

	@Override
	public ContentValues getEditableValues(ContentNotification obj) {
		ContentValues cv = new ContentValues();
		cv.put("ACCEPT", obj.isAccept() ? 1 : 0);
		cv.put("ACTION", obj.getAction());
		cv.put("ADMINISTRATIONLEVEL", obj.getAdministrationLevel());
		cv.put("COMMENT", obj.getComment());
		cv.put("CONTENTID", obj.getContentId());
		cv.put("CREATORUSER", obj.getCreatorUser());
		cv.put("DENY", obj.isDeny() ? 1 : 0);
		cv.put("_id", obj.getId());
		cv.put("METAINFO", obj.getMetaInfo());
		cv.put("OPERAND1", obj.getOperand1());
		cv.put("OPERAND2", obj.getOperand2());
		cv.put("SENTFROMSERVER", obj.isSentFromServer() ? 1 : 0);
		cv.put("SENTTOSERVER", obj.isSentToServer() ? 1 : 0);
		cv.put("TIMESTAMP", obj.getTimeStamp());
		return cv;
	}

	@Override
	public FilterBuilder buildFilter(FilterBuilder filter, ContentNotification obj) {
		ContentNotification defaultObj = new ContentNotification();
		// Include fields in query if they differ from the default object
		if (obj.isAccept() != defaultObj.isAccept())
			filter = filter.eq(Columns.ACCEPT, BooleanConverter.GET.toSql(obj.isAccept()));
		if (obj.getAction() != defaultObj.getAction())
			filter = filter.eq(Columns.ACTION, StringConverter.GET.toSql(obj.getAction()));
		if (obj.getAdministrationLevel() != defaultObj.getAdministrationLevel())
			filter = filter.eq(Columns.ADMINISTRATIONLEVEL, IntegerConverter.GET.toSql(obj.getAdministrationLevel()));
		if (obj.getComment() != defaultObj.getComment())
			filter = filter.eq(Columns.COMMENT, StringConverter.GET.toSql(obj.getComment()));
		if (obj.getContentId() != defaultObj.getContentId())
			filter = filter.eq(Columns.CONTENTID, LongConverter.GET.toSql(obj.getContentId()));
		if (obj.getCreatorUser() != defaultObj.getCreatorUser())
			filter = filter.eq(Columns.CREATORUSER, LongConverter.GET.toSql(obj.getCreatorUser()));
		if (obj.isDeny() != defaultObj.isDeny())
			filter = filter.eq(Columns.DENY, BooleanConverter.GET.toSql(obj.isDeny()));
		if (obj.getId() != defaultObj.getId())
			filter = filter.eq(Columns._id, LongConverter.GET.toSql(obj.getId()));
		if (obj.getMetaInfo() != defaultObj.getMetaInfo())
			filter = filter.eq(Columns.METAINFO, StringConverter.GET.toSql(obj.getMetaInfo()));
		if (obj.getOperand1() != defaultObj.getOperand1())
			filter = filter.eq(Columns.OPERAND1, LongConverter.GET.toSql(obj.getOperand1()));
		if (obj.getOperand2() != defaultObj.getOperand2())
			filter = filter.eq(Columns.OPERAND2, LongConverter.GET.toSql(obj.getOperand2()));
		if (obj.isSentFromServer() != defaultObj.isSentFromServer())
			filter = filter.eq(Columns.SENTFROMSERVER, BooleanConverter.GET.toSql(obj.isSentFromServer()));
		if (obj.isSentToServer() != defaultObj.isSentToServer())
			filter = filter.eq(Columns.SENTTOSERVER, BooleanConverter.GET.toSql(obj.isSentToServer()));
		if (obj.getTimeStamp() != defaultObj.getTimeStamp())
			filter = filter.eq(Columns.TIMESTAMP, LongConverter.GET.toSql(obj.getTimeStamp()));
		return filter;
	}

}