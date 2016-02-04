package com.peaceworld.wikisms.model.entity;

import com.google.gson.Gson;
import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;

@Entity

public class ContentNotification {

	public static enum ACTION 
		{	CONTENT_CREATE,CONTENT_DELETE,CONTENT_EDIT,CONTENT_TAG_ADDED,CONTENT_TAG_CHANGED}
	   
	@Id	
	private long id;
	/**
	 * id of the content to be affected
	 */
	private long contentId;
	private String action;
	/**
	 * action CONTENT_CREATE : new content value
	 * action CONTENT_EDIT : edited content value
	 */
	private String  metaInfo;
	
	/**
	 * action CONTENT_TAG_CHANGED : id of the category to be changed
	 * action CONTENT_CATEGORY_ADDED : id of the category to be added
	 * action CONTENT_CREATE : id of the parent category (main and probably unaccepted parent)
	 */
	private long operand1;
	
	/**
	 * action CONTENT_TAG_CHANGED : id of the category to be replaced
	 * action CONTENT_CREATE : id of the parent category (first accepted parent)
	 * 
	 */
	private long operand2;
	
	private int administrationLevel;
	private boolean accept;
	private boolean deny;
	
	private String comment;
	private long creatorUser;
	private boolean sentToServer;
	private boolean sentFromServer;
	
	/**
	 * this field is to handle content change log
	 */
	private long timeStamp;
	
		
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getContentId() {
		return contentId;
	}



	public void setContentId(long contentId) {
		this.contentId = contentId;
	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}



	public String getMetaInfo() {
		return metaInfo;
	}



	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}



	public long getOperand1() {
		return operand1;
	}



	public void setOperand1(long operand1) {
		this.operand1 = operand1;
	}



	public long getOperand2() {
		return operand2;
	}



	public void setOperand2(long operand2) {
		this.operand2 = operand2;
	}



	public int getAdministrationLevel() {
		return administrationLevel;
	}



	public void setAdministrationLevel(int administrationLevel) {
		this.administrationLevel = administrationLevel;
	}

	public String getComment() {
		return comment;
	}



	public void setComment(String comment) {
		this.comment = comment;
	}


	public long getCreatorUser() {
		return creatorUser;
	}



	public void setCreatorUser(long creatorUser) {
		this.creatorUser = creatorUser;
	}



	public boolean isSentToServer() {
		return sentToServer;
	}



	public void setSentToServer(boolean sentToServer) {
		this.sentToServer = sentToServer;
	}



	public boolean isSentFromServer() {
		return sentFromServer;
	}



	public boolean isAccept() {
		return accept;
	}



	public void setAccept(boolean accept) {
		this.accept = accept;
	}



	public boolean isDeny() {
		return deny;
	}



	public void setDeny(boolean deny) {
		this.deny = deny;
	}



	public void setSentFromServer(boolean sentFromServer) {
		this.sentFromServer = sentFromServer;
	}



	public long getTimeStamp() {
		return timeStamp;
	}



	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}



	public String toString()
	{
		Gson gson=new Gson();
		return gson.toJson(this);
	}

		
}
