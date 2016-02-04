package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
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

public class ContentDetailViewActivity extends Activity {
	
	private ArrayList<Content> ContentList;
	private ArrayList<Content> PreviewList;
	private ContentCategoryDao categoryDao;
	private ContentDao contentDao;
	private ContentViewerAdapter adapter;
	private long groupId;
	private int pointer;
	private int previewCounter=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_content_view_gallery);
		groupId=getIntent().getExtras().getLong("GroupId");
		pointer=getIntent().getExtras().getInt("Pointer");
		contentDao=new ContentDao(this);
		categoryDao=new ContentCategoryDao(this);
		loadContentList(groupId);
		GridView gridView=(GridView)findViewById(R.id.contentViewGridView); 
		PreviewList=new ArrayList<Content>(previewCounter);
		
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
			if(PreviewList!=null)
				return PreviewList.size();
			return 0;
		}

		@Override
		public Object getItem(int index) {
			if(PreviewList!=null)
				return PreviewList.get(index);
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			if(PreviewList!=null)
				return PreviewList.get(arg0).getId();
			return 0;
		}

		@Override
		public View getView(int index, View view, ViewGroup arg2) {
			
			Content content=PreviewList.get(index);
			view=getLayoutInflater().inflate(R.layout.fragment_content_view_gallery_itme, null);
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
