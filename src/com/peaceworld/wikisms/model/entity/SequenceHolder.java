package com.peaceworld.wikisms.model.entity;

import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;

@Entity
public class SequenceHolder {
	
	@Id
	private long id;
	private String tableName;
	private long sequence;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public long getSequence() {
		return sequence;
	}
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}
	

}
