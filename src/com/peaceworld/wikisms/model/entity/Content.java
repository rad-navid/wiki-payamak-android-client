package com.peaceworld.wikisms.model.entity;

import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;

/**
 * Entity implementation class for Entity: MessagesTable
 *
 */
@Entity

public class Content  {


	public Content() {
		super();
	}
	
	@Id
	private long id;
	
	private String encriptedText;
	private String plainText;
	
	private int administrationLevel;
	private String contentTag;
	
	private long insertionDateTime;
	
	private long likedCounter;
	private long viewedCounter;
	
	private boolean approved;
	
	private long creatorUser;
	
	// these fields are for local device usage
	boolean liked,favorite,viewed;
	
	public int getAdministrationLevel() {
		return administrationLevel;
	}
	public void setAdministrationLevel(int administrationLevel) {
		this.administrationLevel = administrationLevel;
	}

	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContentTag() {
		return contentTag;
	}
	public void setContentTag(String contentTag) {
		this.contentTag = contentTag;
	}
	
	public long getLikedCounter() {
		return likedCounter;
	}
	public void setLikedCounter(long likedCounter) {
		this.likedCounter = likedCounter;
	}
	public long getViewedCounter() {
		return viewedCounter;
	}
	public void setViewedCounter(long viewedCounter) {
		this.viewedCounter = viewedCounter;
	}

	public boolean isLiked() {
		return liked;
	}
	public void setLiked(boolean liked) {
		this.liked = liked;
	}
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public String getEncriptedText() {
		return encriptedText;
	}
	public void setEncriptedText(String encriptedText) {
		this.encriptedText = encriptedText;
	}
	public String getPlainText() {
		return plainText;
	}
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
	public boolean isViewed() {
		return viewed;
	}
	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}
	public long getCreatorUser() {
		return creatorUser;
	}
	public void setCreatorUser(long creatorUser) {
		this.creatorUser = creatorUser;
	}
	public long getInsertionDateTime() {
		return insertionDateTime;
	}
	public void setInsertionDateTime(long insertionDateTime) {
		this.insertionDateTime = insertionDateTime;
	}
	public String toString()
	{
		
		String result= "id:"+getId()+" tag:"+getContentTag()+" like:"+getLikedCounter()+
				" User:"+getCreatorUser()+" adminLevel:"+getAdministrationLevel()+
				" value:"+getPlainText();
		return  result;
	}
	
}
