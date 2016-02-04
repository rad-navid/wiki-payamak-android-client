package com.peaceworld.wikisms.view.newui.dialog;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.dao.CategoryFullDao;
import com.peaceworld.wikisms.model.dao.CategoryNotificationFullDao;
import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.Comments;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.dao.CommentsDao;
import com.peaceworld.wikisms.view.activity.DialogConfirmationListener;

public class SimpleOneInputDialog extends DialogFragment {
	
	private Button okButton,cancleButton;
	private EditText inputTextBox;
	private long Id1;
	private ArrayList<ContentCategory>categoryList;
	private TYPE type;
	public enum TYPE{ CREATE_CATEGORY,MERGE_CATEGORY,DELETE_CATEGORY,EDIT_CONTENT,DELETE_CONTENT,RENAME_CATEGORY,COMMENTS, CREATE_CONTENT}
	private DialogConfirmationListener listener;
	
	
	public SimpleOneInputDialog(long Id,TYPE type, DialogConfirmationListener listener) {
		setRetainInstance(true);
		this.Id1=Id;
		this.type=type;
		this.listener=listener;
	}
	public SimpleOneInputDialog(ArrayList<ContentCategory> categoryList,TYPE type, DialogConfirmationListener listener) {
		setRetainInstance(true);
		this.categoryList=categoryList;
		this.type=type;
		this.listener=listener;
	}
	public SimpleOneInputDialog(TYPE type, DialogConfirmationListener listener) {
		setRetainInstance(true);
		this.type=type;
		this.listener=listener;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.dialog_simple_input, null);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		okButton=(Button)view.findViewById(R.id.OkButton);
		cancleButton=(Button)view.findViewById(R.id.CancelButton);
		inputTextBox=(EditText)view.findViewById(R.id.inputEditText);
		
		okButton.setOnClickListener(onClickListener_ok);
		cancleButton.setOnClickListener(onClickListener_cancle);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		inputTextBox.setText("");
		setupDialogTitles();
	}
	
	private void setupDialogTitles()
	{
		if(type==TYPE.COMMENTS)
		{
			inputTextBox.setHint(getResources().getString(R.string.comments_suggestions_descroption));
			inputTextBox.setMinHeight(200);
			inputTextBox.setMaxLines(20);
		}
		else if(type==TYPE.DELETE_CATEGORY)
		{
			CategoryFullDao categoryDao=new CategoryFullDao(getActivity());
			String name=categoryDao.get(Id1).getName();
			String info="گروه "+name+" در سیستم شما حذف خواهد شد"+
			"\r\n"+"\t\t آیا تایید میکنید؟"+
			"\r\n"+"در صورت نیاز به بازیابی مجدد ان از منو اصلی گزینه همگام سازی با سرور را انتخاب نمایید.";
			//inputTextBox.setHint(info);
			inputTextBox.setText(info);
			inputTextBox.setMaxLines(10);
			inputTextBox.setMinHeight(200);
			inputTextBox.setEnabled(false);
		}
		else if(type==TYPE.CREATE_CONTENT)
		{
			String hint="متن پیامک را با دقت بنویسید.";
			inputTextBox.setHint(hint);
			inputTextBox.setMaxLines(30);
			inputTextBox.setMinHeight(200);
			inputTextBox.setEnabled(true);
		}
		else if(type==TYPE.DELETE_CONTENT)
		{
			inputTextBox.setHint("دلیل حذف این پیامک را بنویسید");
		}
		else
			inputTextBox.setHint("نام گروه جدید را با دقت بنویسید");
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	
	OnClickListener onClickListener_ok=new OnClickListener() {
		
		boolean result=true;
		
		@Override
		public void onClick(View v) {
			
			boolean actionPerformed=true;
			switch (type) {
			case CREATE_CATEGORY:
				result=createCategory();
				break;
			case MERGE_CATEGORY:
				actionPerformed=false;
				CategoryManipulationAsyncTask async=new CategoryManipulationAsyncTask();
				async.execute();
				break;
			case DELETE_CATEGORY:
				result=deleteCategory();
				break;
			case RENAME_CATEGORY:
				result=renameCategory();
				break;
			case DELETE_CONTENT:
				result=deleteContent();
				break;
			case COMMENTS:
				result=Comments();   
				break;
			case CREATE_CONTENT:
				if(getContent().split("\\s+").length<7)
				{
					Toast.makeText(getActivity(), "متن پیامک خیلی کوتاه است", Toast.LENGTH_SHORT).show();
					return;
				}
				result=true;  
				break;
			default:
				result=false;
				break;
			}
			
			if(listener!=null && actionPerformed)
			{
				listener.actionPerformed(result, type);
				dismissAllowingStateLoss();
			}
			else if(type==TYPE.COMMENTS)
			{
				dismissAllowingStateLoss();
			}
		}
	};
	
	OnClickListener onClickListener_cancle=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dismissAllowingStateLoss();
			if(listener!=null)
				listener.actionPerformed(false, type);
		}
	};

	protected boolean mergeCategory() {
		
		try {
			CategoryFullDao categoryDao = new CategoryFullDao(getActivity());
			String newName = inputTextBox.getText().toString();
			if (newName.length() <= 0)
				for (ContentCategory c : categoryList)
					newName += c.getName() + "_";
			for (int i = 1; i < categoryList.size(); i++) {
				categoryDao.mergeCategory(categoryList.get(0).getId(),
						categoryList.get(i).getId(), newName, "", true);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
	}
	protected boolean Comments() {
		
		String value=inputTextBox.getText().toString();
		if(value.length()<5)
			return false;
		CommentsDao cDao=new CommentsDao(getActivity());
		com.peaceworld.wikisms.model.entity.Comments c=new Comments();
		c.setComment(value);
		long result=cDao.insert(c);
		if(result!=-1)
			Toast.makeText(getActivity(), "ارسال شد", Toast.LENGTH_LONG).show();
		return result!=-1;
	}
	protected boolean deleteContent() {
		
		ContentFullDao contentFullDao = new ContentFullDao(getActivity());
		if(inputTextBox.getText().toString().trim().length()<=0 ||
				inputTextBox.getText().toString().split("\\s+").length<2 )
		{
			Toast.makeText(getActivity(), "در قسمت توضیحات حداقل با 2 کلمه دلیل حذف را ذکر کنید", Toast.LENGTH_SHORT).show();
			return false;
		}
		boolean result=contentFullDao.deleteContent(Id1, inputTextBox.getText().toString(),true);
		if(result)
			Toast.makeText(getActivity(), "پیامک حذف شد", Toast.LENGTH_LONG).show();
		return result;
	}
	protected boolean deleteCategory() {
		
		CategoryFullDao categoryDao=new CategoryFullDao(getActivity());
		String name=categoryDao.get(Id1).getName();
		int result=categoryDao.delete(Id1);
		Toast.makeText(getActivity(), "گروه "+name+" حذف شد", Toast.LENGTH_LONG).show();
		return result!=0;
		
	}
	protected boolean renameCategory() {
	
		String newCategoryName=inputTextBox.getText().toString();
		if(newCategoryName.trim().length()<2)
		{
			Toast.makeText(getActivity(), newCategoryName+"نام گروه نا معتبر است", Toast.LENGTH_LONG).show();
			return false;
		}
		
		CategoryFullDao categoryDao=new CategoryFullDao(getActivity());
		ContentCategory category=categoryDao.get(Id1);
		category.setName(newCategoryName);
		categoryDao.update(category);
	
		new CategoryNotificationFullDao(getActivity()).createChangeCatgoryNotification(category.getId(), category.getName(), "no comment");
		
		Toast.makeText(getActivity(), newCategoryName+" تغییر کرد", Toast.LENGTH_LONG).show();
		return true;
		
	}
	protected boolean createCategory() {
		
		CategoryFullDao categoryDao=new CategoryFullDao(getActivity());
		
		String newCategoryName=inputTextBox.getText().toString();
		if(newCategoryName.trim().length()<2)
		{
			Toast.makeText(getActivity(), newCategoryName+"نام گروه نا معتبر است", Toast.LENGTH_LONG).show();
			return false;
		}
		ContentCategory cc=new ContentCategory();
		cc.setName(newCategoryName);
		cc.setId(CategoryFullDao.generateNewId(getActivity()));
		cc.setParentCategory(Id1);
		cc.setSelfDefined(true);
		
		boolean result=categoryDao.createNewCategory(cc);
		if(result)
			new CategoryNotificationFullDao(getActivity()).createNewCategoryNofication(cc.getId(), "");
		Toast.makeText(getActivity(), newCategoryName+" ایجاد شد ", Toast.LENGTH_LONG).show();
		
		return result;
		
	}
	public String getContent() {
		return inputTextBox.getText().toString();
		
	}
	
	class CategoryManipulationAsyncTask extends AsyncTask<Void,Boolean, Boolean>
	{
		private ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			if(dialog==null)
				dialog=new ProgressDialog(getActivity());
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("لطفا چند لحظه صبر کنید...");
			dialog.show();
			
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			
			switch (type) {
			case MERGE_CATEGORY:
				return mergeCategory();
			default:
				break;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			dialog.dismiss();
			
			if(result!=null)
			{
				if(result)
				{
					Toast.makeText(getActivity(), "با موفقیت ادغام شد", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getActivity(), "متاسفانه ادغام نشد", Toast.LENGTH_LONG).show();
				}
				
				if(listener!=null)
					listener.actionPerformed(result, type);
			}
			
			dismissAllowingStateLoss();
				
		}
		
	}
	
}