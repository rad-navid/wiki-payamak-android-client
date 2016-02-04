package com.peaceworld.wikisms.view.newui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryDao;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryTable;

public class ContentCategoryManipulationAdapter extends BaseAdapter  {
	
	public enum CATEGORY_TYPE {basic}
	protected ArrayList<CategoryObject>items;
	protected Context context;
	private ContentCategoryDao dao;
	private long currentParentId;
	private ArrayList<ContentCategory> selectedCategories;
	private boolean singleSelectionMod,showEmptyCategory;
	
	public ContentCategoryManipulationAdapter(Context context,long parentLocalId)
	{
		this.context=context;
		dao=new ContentCategoryDao(context);
		selectedCategories=new ArrayList<ContentCategory>();
		loadItems(parentLocalId);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	protected List<ContentCategory> loadChiled(long categoryId)
	{
		String condition=ContentCategoryTable.Columns.PARENTCATEGORY+" = "
				+categoryId+" AND "
				+"( "+ContentCategoryTable.Columns.ACCEPTED+" = 1 OR "+ 
				ContentCategoryTable.Columns.SELFDEFINED+ " = 1 )";
		return dao.asList(dao.query(condition, null));
	}
	
	protected boolean hasChild(long categoryId)
	{
		List<ContentCategory> list= loadChiled(categoryId);
		if(list==null || list.size()<1)
			return false;
		else return true;
	}
	
	public void refresh() {
		loadItems(currentParentId);
		notifyDataSetChanged();
	}
	
	protected boolean loadItems(long parentId)
	{
		if(items==null)
			items=new ArrayList<CategoryObject>(30);
		items.clear();
		
		currentParentId=parentId;
		
		List<ContentCategory> list=loadChiled(parentId);
		if(list==null || list.size()<1)
		{
			Toast.makeText(context, "زیرگروهی وجود ندارد", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		for(ContentCategory ctg:list)
		{
			CategoryObject obj=new  CategoryObject(CATEGORY_TYPE.basic, ctg);
			items.add(obj);
		}
		
		return true;
	}
	

	public class CategoryObject{
		
		public CATEGORY_TYPE type;
		public ContentCategory contentCategory;
		
		public CategoryObject(CATEGORY_TYPE type,
				ContentCategory contentCategory) {
			
			this.type = type;
			this.contentCategory = contentCategory;
		}
		
	}



	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		
		View view=View.inflate(context,R.layout.adapter_content_category_manipulation_item, null);
		CategoryObject obj=items.get(index);
		ImageView image=(ImageView)view.findViewById(R.id.categoryItemImageView);
		image.setImageResource(R.drawable.folder);
		
		TextView name=(TextView)view.findViewById(R.id.categoryItemNameTextView);
		name.setText(obj.contentCategory.getName());
		
		image.setOnClickListener(onClickListener);
		name.setOnClickListener(onClickListener);
		
		CheckBox selectionCheckBox=(CheckBox)view.findViewById(R.id.categorySelectCheckBox);
		
		if(isCategorySelected(obj.contentCategory))
			selectionCheckBox.setChecked(true);
		else
			selectionCheckBox.setChecked(false);
		
		
		selectionCheckBox.setOnCheckedChangeListener(onCheckedChangedListener);
		
		name.setTag(index);
		image.setTag(index);
		selectionCheckBox.setTag(obj);
		view.setTag(obj.contentCategory.getParentCategory());
		
		return view;
	}
	
	private boolean isCategorySelected(ContentCategory category) {
		if(selectedCategories==null || selectedCategories.size()<=0)
			return false;
		for(ContentCategory ctg:selectedCategories)
			if(ctg.getId()==category.getId())
				return true;
		return false;
	}
	private void unselectCategory(ContentCategory category)
	{
		for(int i=0;i<selectedCategories.size();i++)
		{
			if(selectedCategories.get(i).getId()==category.getId())
			{
				selectedCategories.remove(i);
				break;
			}
		}
	}

	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			int index=(Integer)v.getTag();
			ContentCategory clickedItem= items.get(index).contentCategory;
			if(clickedItem!=null)
			{
				loadItems(clickedItem.getId());
				notifyDataSetChanged();
			}
			
		}
	};
	
	private OnCheckedChangeListener onCheckedChangedListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			CategoryObject obj=(CategoryObject)buttonView.getTag();
			if(!isCategorySelected(obj.contentCategory))
				selectedCategories.add(obj.contentCategory);
			else
				unselectCategory(obj.contentCategory);
		}
	};
	
	public boolean isSingleSelectionMod() {
		return singleSelectionMod;
	}

	public void setSingleSelectionMod(boolean singleSelectionMod) {
		this.singleSelectionMod = singleSelectionMod;
	}

	public boolean isShowEmptyCategory() {
		return showEmptyCategory;
	}

	public void setShowEmptyCategory(boolean showEmptyCategory) {
		this.showEmptyCategory = showEmptyCategory;
	}

	public void moveUp()
	{
		ContentCategory parentCategory=dao.get(currentParentId);
		if(parentCategory==null)
			return;
		loadItems(parentCategory.getParentCategory());
		notifyDataSetChanged();
	}
	
	public long getCurrentParentLocalId()
	{
		return currentParentId;
	}
	public ContentCategory getSelected()
	{
		return selectedCategories.get(0);
	}
	public ArrayList<ContentCategory> getAllSelected()
	{
		return (ArrayList<ContentCategory>)selectedCategories.clone();
	}
	public void clearSelected()
	{
		selectedCategories.clear();
		refresh();
	}
	


	
}
