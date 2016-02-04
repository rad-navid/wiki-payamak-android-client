package com.peaceworld.wikisms.model.entity;

import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;

@Entity
public class Comments {
 
	@Id
	private long id;
	private String comment;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	

}
