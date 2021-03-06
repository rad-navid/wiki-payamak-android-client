package com.peaceworld.wikisms.model.entity;


public class GeneralChangeLog {
	
	public static enum ChangeLogActions{USER_USERNAME_UPDATED,CONTENT_NOTIFICATION_REMOVED,CATEGORY_NOTIFICATION_REMOVED}
	
	private int id;
	private long recordId;
	private String tableName;
	private String action;
	private long timeStamp;
	
	public GeneralChangeLog()
	{
		super();
	}
	
	public GeneralChangeLog(long recordId, String tableName, String action,
			long timeStamp) {
		super();
		this.recordId = recordId;
		this.tableName = tableName;
		this.action = action;
		this.timeStamp = timeStamp;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getRecordId() {
		return recordId;
	}
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	

}
