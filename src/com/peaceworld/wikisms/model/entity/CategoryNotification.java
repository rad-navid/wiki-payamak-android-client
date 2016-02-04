package com.peaceworld.wikisms.model.entity;

import com.google.gson.Gson;
import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;

/**
 * Entity implementation class for Entity: CategoryNotification
 *
 */
@Entity

public class CategoryNotification  {

	public static enum ACTION 
	{CATEGORY_CREATE,CATEGORY_DELETE,CATEGORY_CHANGED,CATEGORY_MERGE,CATEGORY_MOVE}

	public CategoryNotification() {
		super();
	}
	
	/**
	 * Auto Generated global unique id  
	 */
	
	@Id
	private long id;
	
	/**
	 * action CATEGORY_CREATE : new category Auto generated global unique id 
	 * action CATEGORY_MOVE: id of the category to be moved
	 * action CATEGORY_MERGE: id of the first category
	 * action CATEGORY_CHANGED : id of the category to be changed
	 * action CATEGORY_DELETE : id of the category to be deleted
	 */
	private long operand1;
	
	/**
	 * action CATEGORY_CREATE : new category parent id
	 * action CATEGORY_MOVE: id of the moved category parent
	 * action CATEGORY_MERGE: id of the second category
	 */
	private long operand2;
	
	/**
	 *  action CATEGORY_CREATE : new category name
	 *  action CATEGORY_MERGE: name of the new category
	 *  action CATEGORY_CHANGED : the new name of the changed category
	 */
	private String metadata;
	private String action;
	private long creatorUser;
	
	private int administrationLevel;
	private boolean accept;
	private boolean deny;
	private boolean sentToServer;
	private boolean sentFromServer;
	
	private String comment;

	/**
	 * this field is to handle category change log
	 */
	private long timeStamp;
		
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
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



	public String getMetadata() {
		return metadata;
	}



	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
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



	public boolean isSentToServer() {
		return sentToServer;
	}



	public void setSentToServer(boolean sentToServer) {
		this.sentToServer = sentToServer;
	}



	public long getCreatorUser() {
		return creatorUser;
	}



	public void setCreatorUser(long creatorUser) {
		this.creatorUser = creatorUser;
	}



	public boolean isSentFromServer() {
		return sentFromServer;
	}



	public void setSentFromServer(boolean sentFromServer) {
		this.sentFromServer = sentFromServer;
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
