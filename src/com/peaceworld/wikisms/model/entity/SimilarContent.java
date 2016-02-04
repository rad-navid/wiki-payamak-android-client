package com.peaceworld.wikisms.model.entity;

import java.util.ArrayList;

public class SimilarContent {
	
	public ContentNotification Cn;
	public ArrayList<String>SimilarContentList;
	public ArrayList<Double>SimilarityList;
	
	public SimilarContent(ContentNotification cn) {
		super();
		Cn = cn;
		SimilarContentList= new ArrayList<String>();
		SimilarityList = new ArrayList<Double>() ;
	}
	

}
