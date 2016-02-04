package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryDao;
import com.peaceworld.wikisms.model.entity.dao.ContentDao;
import com.peaceworld.wikisms.model.entity.dao.ContentTable;


public class ContentViewGalleryFragment extends Fragment {
	
	private ArrayList<Content> ContentList;
	private ContentCategoryDao categoryDao;
	private ContentDao contentDao;
	private ContentViewerAdapter adapter;
	private long groupId;
	
	public ContentViewGalleryFragment() {
		this(0);
	}
	
	public ContentViewGalleryFragment(long groupId) {
		setRetainInstance(true);
		contentDao=new ContentDao(getActivity());
		categoryDao=new ContentCategoryDao(getActivity());
		this.groupId=groupId;
		loadContentList(this.groupId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_content_view_gallery, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		GridView gridView=(GridView)getActivity().findViewById(R.id.contentViewGridView); 
		adapter=new ContentViewerAdapter();
		gridView.setAdapter(adapter);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	//	outState.putInt("mColorRes", mColorRes);
	}
	
	private void loadContentList(long categoryId)
	{
		ContentCategory category=categoryDao.get(categoryId);
		Cursor cursor=contentDao.query(ContentTable.Columns.CONTENTTAG+" LIKE \'%"+category.getName()+"%\'", null );
		ContentList=(ArrayList<Content>) contentDao.asList(cursor);
	}
	
	public class ContentViewerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			if(ContentList!=null)
				return ContentList.size();
			return 0;
		}

		@Override
		public Object getItem(int index) {
			if(ContentList!=null)
				return ContentList.get(index);
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			if(ContentList!=null)
				return ContentList.get(arg0).getId();
			return 0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			
			Content content=ContentList.get(index);
			view=getActivity().getLayoutInflater().inflate(R.layout.fragment_content_view_gallery_itme, null);
			WebView webview=(WebView)view.findViewById(R.id.ContentWebView);
			String contentBody=formatContent(content.getPlainText());
			webview.loadData(contentBody, "text/html; charset=UTF-8", "UTF-8");
			return view;
		}
		
		
	}

	private String formatContent(String value) {
		String result=
		"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"+
		"<html lang=\'fa\' xml:lang=\'fa\' xmlns=\"http://www.w3.org/1999/xhtml\">"+
		"<head>"+
		"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\""+
		"</head><body  dir=\"rtl\" > <p align=\"justify\"> ";
		result+=value;
		
		result+=" </p></body></html>";
		return result;
	}
	
}
