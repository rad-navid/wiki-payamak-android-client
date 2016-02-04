package com.peaceworld.wikisms.controller.rs.common;

public interface URLs {
	//static final String MAIN_URL = "http://wiki-payamak.rhcloud.com";
	//static final String SERVICES_URL = "http://wiki-payamak.rhcloud.com/rs";
	static final String MAIN_URL = "http://www.wiki-sms.com";
	static final String SERVICES_URL = "http://www.wiki-sms.com/rs";
	

	static final String DATA_GET_ALL_ENCRYPTED_URL=SERVICES_URL+"/data/getallencrypted";
	static final String DATA_GET_BY_CATEGORY_ENCRYPTED_URL=SERVICES_URL+"/data/getcontentsbycategory";
	
	static final String USER_CREATION_URL=SERVICES_URL+"/user/create";
	static final String USER_GET_URL=SERVICES_URL+"/user/getall";
	
	static final String COMMENTS_NEW_URL=SERVICES_URL+"/comments/new";
	
	static final String CONTENT_CATEGORY_GET_ALL_ENCRYPTED_URL=SERVICES_URL+"/category/getallencrypted";
	
	static final String CATEGORY_NOTIFICATION_GET_ALL_URL=SERVICES_URL+"/ccn/getallccns";
	static final String CATEGORY_NOTIFICATION_ADD_URL=SERVICES_URL+"/ccn/addccns";
	static final String CATEGORY_NOTIFICATION_AVAILABLE_URL=SERVICES_URL+"/ccn/getallavailable";
	static final String CATEGORY_NOTIFICATION_UPDATE_URL=SERVICES_URL+"/ccn/updateccns";
	
	static final String CONTENT_NOTIFICATION_GET_ALL_URL=SERVICES_URL+"/cn/getallcns";
	static final String CONTENT_NOTIFICATION_GET_ALL_BY_CREATOR_URL=SERVICES_URL+"/cn/getallcnsbycreator";
	static final String CONTENT_NOTIFICATION_ADD_URL=SERVICES_URL+"/cn/addcns";
	static final String CONTENT_NOTIFICATION_AVAILABLE_URL=SERVICES_URL+"/cn/getallavailable";
	static final String CONTENT_NOTIFICATION_UPDATE_URL=SERVICES_URL+"/cn/updatecns";
	
	static final String CHANGE_TABLE_GENERAL_LOG_URL=SERVICES_URL+"/changelog/changelog";
	static final String CHANGE_TABLE_CONTENT_LOG_URL=SERVICES_URL+"/changelog/cnchangelog";
	static final String CHANGE_TABLE_CATEGORY_LOG_URL=SERVICES_URL+"/changelog/ccnchangelog";
	static final String SYSTEM_TIME_URL=SERVICES_URL+"/changelog/systemtime";
	static final String CHANGE_CONTENT_STATE_SEND_URL=SERVICES_URL+"/changelog/setcontentsstate";
	static final String CHANGE_CONTENT_STATE_GET_URL=SERVICES_URL+"/changelog/getlastcontentsstate";
	
	static final String SYSTEM_CHECK_CONNECTION_POST_URL=SERVICES_URL+"/system/postconnection";
	static final String SYSTEM_CHECK_CONNECTION_GET_URL=SERVICES_URL+"/system/getconnection";
	
	
	
	
}
