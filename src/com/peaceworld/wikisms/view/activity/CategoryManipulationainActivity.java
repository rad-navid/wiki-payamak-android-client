package com.peaceworld.wikisms.view.activity;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.dao.CategoryFullDao;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.view.newui.adapter.ContentCategoryManipulationAdapter;
import com.peaceworld.wikisms.view.newui.dialog.SimpleOneInputDialog;
import com.peaceworld.wikisms.view.newui.dialog.SimpleOneInputDialog.TYPE;


public class CategoryManipulationainActivity extends SherlockFragmentActivity  implements DialogConfirmationListener{

	protected ContentCategoryManipulationAdapter adapter;
	private GridView gridView;
	protected String parenCategory="";
	private ImageView creatImageView, moveUpImageView;
	private Button createBtn, deleteBtn,moveBtn,mergeBtn,pasteBtn,renameBtn,unselectBtn;
	private ArrayList<ContentCategory> clipBoard;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_manipulation);
		gridView = (GridView) findViewById(R.id.contentBrowserGridView);
		
		creatImageView = (ImageView) findViewById(R.id.categoryManipulationButton);
		moveUpImageView = (ImageView) findViewById(R.id.UpButton);
		
		createBtn = (Button) findViewById(R.id.createButton);
		deleteBtn = (Button) findViewById(R.id.deleteButton);
		moveBtn = (Button) findViewById(R.id.moveButton);
		mergeBtn = (Button) findViewById(R.id.mergeButton);
		pasteBtn = (Button) findViewById(R.id.pasteButton);
		renameBtn = (Button) findViewById(R.id.renameButton);
		unselectBtn= (Button) findViewById(R.id.unSelectAllButton);
		
		creatImageView.setOnClickListener(onClickListener);
		moveUpImageView.setOnClickListener(onClickListener);
		createBtn.setOnClickListener(onClickListener);
		deleteBtn.setOnClickListener(onClickListener);
		moveBtn.setOnClickListener(onClickListener);
		mergeBtn.setOnClickListener(onClickListener);
		pasteBtn.setOnClickListener(onClickListener);
		renameBtn.setOnClickListener(onClickListener);
		unselectBtn.setOnClickListener(onClickListener);
		
		
		
		adapter = new ContentCategoryManipulationAdapter(this, 0);
		gridView.setAdapter(adapter);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.categoryManipulationButton:
				break;
			case R.id.UpButton:
				 adapter.moveUp();
				 return;
			case R.id.createButton:
				 createCategory();
				 return;
			case R.id.deleteButton:
				 deleteCategory();
				 return;
			case R.id.moveButton:
				 moveCategory();
				 return;
			case R.id.mergeButton:
				 mergeCategory();
				 return;
			case R.id.pasteButton:
				pasteCategory();
				return;
			case R.id.renameButton:
				 renameCategory();
				 return;
			case R.id.unSelectAllButton:
				 unSelectAll();
				 return;
			default:
				break;
			}
		
		}
	};
	
	
	protected void unSelectAll()
	{
		adapter.clearSelected();
		Toast.makeText(this, "همه گروه ها از حالت انتخاب خارج شد", Toast.LENGTH_LONG).show();
	}
	protected void renameCategory() {
		
		if(adapter.getAllSelected().size()<=0)
		{
			Toast.makeText(this, "گروهی انتخاب نشده ", Toast.LENGTH_LONG).show();
			return;
		}
		else if(adapter.getAllSelected().size()>1)
		{
			Toast.makeText(this, "فقط یک گروه را برای تغییر نام انتخاب کنید", Toast.LENGTH_LONG).show();
			return;
		}
		SimpleOneInputDialog deleteDialog=new SimpleOneInputDialog(adapter.getSelected().getId(),SimpleOneInputDialog.TYPE.RENAME_CATEGORY, this);
		deleteDialog.show(getSupportFragmentManager(), "");
	}
	
	protected void createCategory() {
		SimpleOneInputDialog createDalog=new SimpleOneInputDialog(adapter.getCurrentParentLocalId(),SimpleOneInputDialog.TYPE.CREATE_CATEGORY,this);
		createDalog.show(getSupportFragmentManager(), "");
	}

	protected void deleteCategory() {
		if(adapter.getAllSelected().size()<=0)
		{
			Toast.makeText(this, "یک گروه را برای حذف انتخاب کنید", Toast.LENGTH_LONG).show();
			return;
		}
		else if(adapter.getAllSelected().size()>1)
		{
			Toast.makeText(this, "فقط یک گروه را برای حذف انتخاب کنید", Toast.LENGTH_LONG).show();
			return;
		}
		SimpleOneInputDialog deleteDialog=new SimpleOneInputDialog(adapter.getSelected().getId(),SimpleOneInputDialog.TYPE.DELETE_CATEGORY, this);
		deleteDialog.show(getSupportFragmentManager(), "");
		
	}

	protected void moveCategory() {
		
		clipBoard=(ArrayList<ContentCategory>)adapter.getAllSelected().clone();
		if(clipBoard==null || clipBoard.size()<=0)
		{
			Toast.makeText(this, " حداقل یک گروه را برای جابجایی انتخاب کنید", Toast.LENGTH_LONG).show();
			adapter.clearSelected();
		}
		Toast.makeText(this, "در گروه مقصد بچسبانید", Toast.LENGTH_LONG).show();
	}

	protected void mergeCategory() {
		if(adapter.getAllSelected().size() <2)
			Toast.makeText(this, "برای ادغام کردن ، حداقل دو  گروه را انتخاب کنید", Toast.LENGTH_LONG).show();
		else
		{
			SimpleOneInputDialog mergeDialog=new SimpleOneInputDialog(
					adapter.getAllSelected(),SimpleOneInputDialog.TYPE.MERGE_CATEGORY, this);
			mergeDialog.show(getSupportFragmentManager(), "");
		}
	}

	protected void pasteCategory() {
	
		CategoryFullDao categoryDao=new CategoryFullDao(this);
		if(clipBoard==null || clipBoard.size()<=0)
		{
			Toast.makeText(this, " یک گروه را برای جابجایی انتخاب کنید", Toast.LENGTH_LONG).show();
			adapter.clearSelected();
			return;
		}
		
		int successful=0;
		for(ContentCategory c:clipBoard)
		{
			boolean result=categoryDao.moveCategory(c.getId(), adapter.getCurrentParentLocalId() , "", true);
			if(result)
				successful++;
		}
		ContentCategory	tmpCat=categoryDao.get( adapter.getCurrentParentLocalId());
		String tmpName=" گروه اصلی ";
		if(tmpCat!=null)
			tmpName=tmpCat.getName();
		String msg=successful+" گروه به "+tmpName+" منتقل شد";
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		adapter.clearSelected();
		clipBoard.clear();
	}
	
	@Override
	public void actionPerformed(boolean confirmed, TYPE actionType) {

		
		if(confirmed && 
				(actionType==TYPE.DELETE_CATEGORY || 
				actionType==TYPE.RENAME_CATEGORY ||
				actionType==TYPE.MERGE_CATEGORY )
		  )
		{
			adapter.clearSelected();
		}
		
		adapter.refresh();
	}
	
}