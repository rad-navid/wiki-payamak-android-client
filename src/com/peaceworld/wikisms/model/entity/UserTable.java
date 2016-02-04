package com.peaceworld.wikisms.model.entity;

import com.turbomanage.storm.api.Entity;
import com.turbomanage.storm.api.Id;


@Entity
public class UserTable  {

	public UserTable() {
		super();
	}
	
	@Id
	private long userIdentifier;
	private String username;
	
	public long getUserIdentifier() {
		return userIdentifier;
	}
	public void setUserIdentifier(long userIdentifier) {
		this.userIdentifier = userIdentifier;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
		
   
	
}
