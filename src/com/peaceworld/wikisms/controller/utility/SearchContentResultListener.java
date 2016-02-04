package com.peaceworld.wikisms.controller.utility;

import java.util.ArrayList;

import com.peaceworld.wikisms.model.entity.Content;

public interface SearchContentResultListener {
	
	public void searchContentDone(ArrayList<Content>searchResult, String searchKey);
}
