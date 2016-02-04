package com.peaceworld.wikisms.controller.utility;

import java.util.ArrayList;
import java.util.regex.Pattern;

import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.Content;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class SearchContents extends AsyncTask<String, Integer, ArrayList<Content>> {

	private ProgressDialog progressDialog;
	private Context context;
	private int limit;
	private long fromId;
	private SearchContentResultListener resultListener;
	private String keyWord="";
	
	public SearchContents(Context context, long fromId, SearchContentResultListener resultListener)
	{
		this.context=context;
		this.fromId=fromId;
		limit=10;
		this.resultListener=resultListener;
		
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog=new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setTitle("در حال جستجو");
		progressDialog.setMessage("لطفا تا اتمام جستجو شکیبا باشید");
		progressDialog.show();
		
	}
	
	@Override
	protected ArrayList<Content> doInBackground(String... params) {
		
		String keyWord=params[0].trim();
		if(keyWord==null  || keyWord.equalsIgnoreCase(""))
			return null;
		this.keyWord=keyWord;
		Pattern pattern=Pattern.compile("\\b"+keyWord+"\\b");
		
		ArrayList<Content> searchResult=new ArrayList<Content>(limit);
		ContentFullDao dao=new ContentFullDao(context);
		long from=fromId;
		int len=50;
		int counter=0;
		ArrayList<Content> contentList;
		do
		{
			contentList =dao.getAllContents(from,from+len);
			from+=(len+1);
			for(Content c:contentList)
				if(pattern.matcher(c.getPlainText()).find())
				{
					searchResult.add(c);
					if(searchResult.size()>limit)
						break;
				}
				
			
			counter+=len;
			publishProgress(counter);

		}while(contentList!=null && contentList.size()>0 && searchResult.size()<limit);
		return searchResult;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		progressDialog.setMessage("لطفا تا اتمام جستجو شکیبا باشید"+"\r\n"+values[0]+" پیامک جستجو شده");
	}
	
	@Override
	protected void onPostExecute(ArrayList<Content> result) {		
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(resultListener!=null)
			resultListener.searchContentDone(result, keyWord);
	}

}
