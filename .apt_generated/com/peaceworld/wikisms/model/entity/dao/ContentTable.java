package com.peaceworld.wikisms.model.entity.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import com.turbomanage.storm.query.FilterBuilder;
import com.turbomanage.storm.TableHelper;
import java.util.Map;
import java.util.HashMap;
import com.peaceworld.wikisms.model.entity.Content;
import com.turbomanage.storm.SQLiteDao;
import com.turbomanage.storm.types.IntegerConverter;
import com.turbomanage.storm.types.BooleanConverter;
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
public class ContentTable extends TableHelper<Content> {

	public enum Columns implements TableHelper.Column {
		ADMINISTRATIONLEVEL,
		APPROVED,
		CONTENTTAG,
		CREATORUSER,
		ENCRIPTEDTEXT,
		FAVORITE,
		_id,
		INSERTIONDATETIME,
		LIKED,
		LIKEDCOUNTER,
		PLAINTEXT,
		VIEWED,
		VIEWEDCOUNTER
	}

	@Override
	public String getTableName() {
		return "Content";
	}

	@Override
	public Column[] getColumns() {
		return Columns.values();
	}
	
	@Override
	public long getId(Content obj) {
		return obj.getId();
	}
	
	@Override
	public void setId(Content obj, long id) {
		obj.setId(id);
	}

	@Override
	public Column getIdCol() {
		return Columns._id;
	}

	@Override
	public String createSql() {
		return
			"CREATE TABLE IF NOT EXISTS Content(" +
				"ADMINISTRATIONLEVEL INTEGER NOT NULL," +
				"APPROVED INTEGER NOT NULL," +
				"CONTENTTAG TEXT," +
				"CREATORUSER INTEGER NOT NULL," +
				"ENCRIPTEDTEXT TEXT," +
				"FAVORITE INTEGER NOT NULL," +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
				"INSERTIONDATETIME INTEGER NOT NULL," +
				"LIKED INTEGER NOT NULL," +
				"LIKEDCOUNTER INTEGER NOT NULL," +
				"PLAINTEXT TEXT," +
				"VIEWED INTEGER NOT NULL," +
				"VIEWEDCOUNTER INTEGER NOT NULL" +
			")";
	}

	@Override
	public String dropSql() {
		return "DROP TABLE IF EXISTS Content";
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
		colIdx = c.getColumnIndex("ADMINISTRATIONLEVEL"); values[0] = (colIdx < 0) ? defaultValues[0] : IntegerConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("APPROVED"); values[1] = (colIdx < 0) ? defaultValues[1] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("CONTENTTAG"); values[2] = (colIdx < 0) ? defaultValues[2] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		colIdx = c.getColumnIndex("CREATORUSER"); values[3] = (colIdx < 0) ? defaultValues[3] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("ENCRIPTEDTEXT"); values[4] = (colIdx < 0) ? defaultValues[4] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		colIdx = c.getColumnIndex("FAVORITE"); values[5] = (colIdx < 0) ? defaultValues[5] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("_id"); values[6] = (colIdx < 0) ? defaultValues[6] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("INSERTIONDATETIME"); values[7] = (colIdx < 0) ? defaultValues[7] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("LIKED"); values[8] = (colIdx < 0) ? defaultValues[8] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("LIKEDCOUNTER"); values[9] = (colIdx < 0) ? defaultValues[9] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		colIdx = c.getColumnIndex("PLAINTEXT"); values[10] = (colIdx < 0) ? defaultValues[10] : StringConverter.GET.toString(getStringOrNull(c, colIdx));
		colIdx = c.getColumnIndex("VIEWED"); values[11] = (colIdx < 0) ? defaultValues[11] : BooleanConverter.GET.toString(getIntOrNull(c, colIdx));
		colIdx = c.getColumnIndex("VIEWEDCOUNTER"); values[12] = (colIdx < 0) ? defaultValues[12] : LongConverter.GET.toString(getLongOrNull(c, colIdx));
		return values;
	}

	@Override
	public void bindRowValues(InsertHelper insHelper, String[] rowValues) {
		if (rowValues[0] == null) insHelper.bindNull(1); else insHelper.bind(1, IntegerConverter.GET.fromString(rowValues[0]));
		if (rowValues[1] == null) insHelper.bindNull(2); else insHelper.bind(2, BooleanConverter.GET.fromString(rowValues[1]));
		if (rowValues[2] == null) insHelper.bindNull(3); else insHelper.bind(3, StringConverter.GET.fromString(rowValues[2]));
		if (rowValues[3] == null) insHelper.bindNull(4); else insHelper.bind(4, LongConverter.GET.fromString(rowValues[3]));
		if (rowValues[4] == null) insHelper.bindNull(5); else insHelper.bind(5, StringConverter.GET.fromString(rowValues[4]));
		if (rowValues[5] == null) insHelper.bindNull(6); else insHelper.bind(6, BooleanConverter.GET.fromString(rowValues[5]));
		if (rowValues[6] == null) insHelper.bindNull(7); else insHelper.bind(7, LongConverter.GET.fromString(rowValues[6]));
		if (rowValues[7] == null) insHelper.bindNull(8); else insHelper.bind(8, LongConverter.GET.fromString(rowValues[7]));
		if (rowValues[8] == null) insHelper.bindNull(9); else insHelper.bind(9, BooleanConverter.GET.fromString(rowValues[8]));
		if (rowValues[9] == null) insHelper.bindNull(10); else insHelper.bind(10, LongConverter.GET.fromString(rowValues[9]));
		if (rowValues[10] == null) insHelper.bindNull(11); else insHelper.bind(11, StringConverter.GET.fromString(rowValues[10]));
		if (rowValues[11] == null) insHelper.bindNull(12); else insHelper.bind(12, BooleanConverter.GET.fromString(rowValues[11]));
		if (rowValues[12] == null) insHelper.bindNull(13); else insHelper.bind(13, LongConverter.GET.fromString(rowValues[12]));
	}

	@Override
	public String[] getDefaultValues() {
		String[] values = new String[getColumns().length];
		Content defaultObj = new Content();
		values[0] = IntegerConverter.GET.toString(IntegerConverter.GET.toSql(defaultObj.getAdministrationLevel()));
		values[1] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isApproved()));
		values[2] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getContentTag()));
		values[3] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getCreatorUser()));
		values[4] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getEncriptedText()));
		values[5] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isFavorite()));
		values[6] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getId()));
		values[7] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getInsertionDateTime()));
		values[8] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isLiked()));
		values[9] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getLikedCounter()));
		values[10] = StringConverter.GET.toString(StringConverter.GET.toSql(defaultObj.getPlainText()));
		values[11] = BooleanConverter.GET.toString(BooleanConverter.GET.toSql(defaultObj.isViewed()));
		values[12] = LongConverter.GET.toString(LongConverter.GET.toSql(defaultObj.getViewedCounter()));
		return values;
	}

	@Override
	public Content newInstance(Cursor c) {
		Content obj = new Content();
		obj.setAdministrationLevel(c.getInt(0));
		obj.setApproved(c.getInt(1) == 1 ? true : false);
		obj.setContentTag(c.getString(2));
		obj.setCreatorUser(c.getLong(3));
		obj.setEncriptedText(c.getString(4));
		obj.setFavorite(c.getInt(5) == 1 ? true : false);
		obj.setId(c.getLong(6));
		obj.setInsertionDateTime(c.getLong(7));
		obj.setLiked(c.getInt(8) == 1 ? true : false);
		obj.setLikedCounter(c.getLong(9));
		obj.setPlainText(c.getString(10));
		obj.setViewed(c.getInt(11) == 1 ? true : false);
		obj.setViewedCounter(c.getLong(12));
		return obj;
	}

	@Override
	public ContentValues getEditableValues(Content obj) {
		ContentValues cv = new ContentValues();
		cv.put("ADMINISTRATIONLEVEL", obj.getAdministrationLevel());
		cv.put("APPROVED", obj.isApproved() ? 1 : 0);
		cv.put("CONTENTTAG", obj.getContentTag());
		cv.put("CREATORUSER", obj.getCreatorUser());
		cv.put("ENCRIPTEDTEXT", obj.getEncriptedText());
		cv.put("FAVORITE", obj.isFavorite() ? 1 : 0);
		cv.put("_id", obj.getId());
		cv.put("INSERTIONDATETIME", obj.getInsertionDateTime());
		cv.put("LIKED", obj.isLiked() ? 1 : 0);
		cv.put("LIKEDCOUNTER", obj.getLikedCounter());
		cv.put("PLAINTEXT", obj.getPlainText());
		cv.put("VIEWED", obj.isViewed() ? 1 : 0);
		cv.put("VIEWEDCOUNTER", obj.getViewedCounter());
		return cv;
	}

	@Override
	public FilterBuilder buildFilter(FilterBuilder filter, Content obj) {
		Content defaultObj = new Content();
		// Include fields in query if they differ from the default object
		if (obj.getAdministrationLevel() != defaultObj.getAdministrationLevel())
			filter = filter.eq(Columns.ADMINISTRATIONLEVEL, IntegerConverter.GET.toSql(obj.getAdministrationLevel()));
		if (obj.isApproved() != defaultObj.isApproved())
			filter = filter.eq(Columns.APPROVED, BooleanConverter.GET.toSql(obj.isApproved()));
		if (obj.getContentTag() != defaultObj.getContentTag())
			filter = filter.eq(Columns.CONTENTTAG, StringConverter.GET.toSql(obj.getContentTag()));
		if (obj.getCreatorUser() != defaultObj.getCreatorUser())
			filter = filter.eq(Columns.CREATORUSER, LongConverter.GET.toSql(obj.getCreatorUser()));
		if (obj.getEncriptedText() != defaultObj.getEncriptedText())
			filter = filter.eq(Columns.ENCRIPTEDTEXT, StringConverter.GET.toSql(obj.getEncriptedText()));
		if (obj.isFavorite() != defaultObj.isFavorite())
			filter = filter.eq(Columns.FAVORITE, BooleanConverter.GET.toSql(obj.isFavorite()));
		if (obj.getId() != defaultObj.getId())
			filter = filter.eq(Columns._id, LongConverter.GET.toSql(obj.getId()));
		if (obj.getInsertionDateTime() != defaultObj.getInsertionDateTime())
			filter = filter.eq(Columns.INSERTIONDATETIME, LongConverter.GET.toSql(obj.getInsertionDateTime()));
		if (obj.isLiked() != defaultObj.isLiked())
			filter = filter.eq(Columns.LIKED, BooleanConverter.GET.toSql(obj.isLiked()));
		if (obj.getLikedCounter() != defaultObj.getLikedCounter())
			filter = filter.eq(Columns.LIKEDCOUNTER, LongConverter.GET.toSql(obj.getLikedCounter()));
		if (obj.getPlainText() != defaultObj.getPlainText())
			filter = filter.eq(Columns.PLAINTEXT, StringConverter.GET.toSql(obj.getPlainText()));
		if (obj.isViewed() != defaultObj.isViewed())
			filter = filter.eq(Columns.VIEWED, BooleanConverter.GET.toSql(obj.isViewed()));
		if (obj.getViewedCounter() != defaultObj.getViewedCounter())
			filter = filter.eq(Columns.VIEWEDCOUNTER, LongConverter.GET.toSql(obj.getViewedCounter()));
		return filter;
	}

}