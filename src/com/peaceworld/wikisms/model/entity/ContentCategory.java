package com.peaceworld.wikisms.model.entity;

import com.google.gson.Gson;
import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;

@Entity
public class ContentCategory{

	@Id
	private long  id;
	private String name;
	private long parentCategory;
	private boolean accepted=false;
	private boolean denied=false;
	private boolean selfDefined=false;
	private long lastContentId=0;
	

	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public long getParentCategory() {
		return parentCategory;
	}



	public void setParentCategory(long parentCategory) {
		this.parentCategory = parentCategory;
	}



	public boolean isAccepted() {
		return accepted;
	}



	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}



	public boolean isDenied() {
		return denied;
	}



	public void setDenied(boolean denied) {
		this.denied = denied;
	}



	public boolean isSelfDefined() {
		return selfDefined;
	}



	public void setSelfDefined(boolean selfDefined) {
		this.selfDefined = selfDefined;
	}



	public long getLastContentId() {
		return lastContentId;
	}



	public void setLastContentId(long lastContentId) {
		this.lastContentId = lastContentId;
	}



	public String toString()
	{
		Gson gson=new Gson();
		return gson.toJson(this);
	}
}
