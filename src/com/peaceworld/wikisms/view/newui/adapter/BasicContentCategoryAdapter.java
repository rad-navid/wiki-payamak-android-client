package com.peaceworld.wikisms.view.newui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryDao;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryTable;

public class BasicContentCategoryAdapter extends BaseAdapter implements OnItemClickListener  {
	
	public enum CATEGORY_TYPE {basic}
	protected ArrayList<CategoryObject>items;
	protected Context context;
	private ContentCategoryDao dao;
	private long currentParentId;
	private ArrayList<ContentCategory> selectedCategories;
	private ArrayList<ImageView>selectedViews;
	private boolean singleSelectionMod;
	
	public BasicContentCategoryAdapter(Context context,long parentLocalId,boolean singelSelectionMod)
	{
		this.context=context;
		dao=new ContentCategoryDao(context);
		this.singleSelectionMod=singelSelectionMod;
		selectedCategories=new ArrayList<ContentCategory>();
		selectedViews=new ArrayList<ImageView>();
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
	
	protected boolean loadItems(long parentId)
	{

		List<ContentCategory> list=loadChiled(parentId);
		if(list==null || list.size()<1)
		{
			if(items== null)
				items=new ArrayList<CategoryObject>(1);
		
			return false;
		}
		
		if(items==null)
			items=new ArrayList<CategoryObject>(list.size());
		items.clear();
		
		for(ContentCategory ctg:list)
		{
			CategoryObject obj=new  CategoryObject(CATEGORY_TYPE.basic, ctg);
			items.add(obj);
		}
		currentParentId=parentId;
		return true;
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {

		ContentCategory clickedItem= items.get(index).contentCategory;
		if(clickedItem!=null)
		{
			if(hasChild(clickedItem.getId()))
			{
				loadItems(clickedItem.getId());
				selectedCategories.clear();
				selectedViews.clear();
				notifyDataSetChanged();
				return;
			}
			
			ImageView image=(ImageView)view.findViewById(R.id.categoryItemImageView);
			boolean select=false;
			try{
				
				String tag=((String)view.getTag());
				select=tag.equalsIgnoreCase("select");
			}
			catch(Exception ex)
			{			}
			
			if(singleSelectionMod)
			{
				for(ImageView imagev:selectedViews)
					imagev.setImageResource(R.drawable.folder);
				selectedCategories.clear();
				
				if(selectedViews.contains(image))
				{
					selectedViews.clear();
					view.setTag("");
					image.setImageResource(R.drawable.folder);
				}
				else
				{
					selectedViews.clear();
					view.setTag("select");
					image.setImageResource(R.drawable.folder_select);
					selectedCategories.add(clickedItem);
					selectedViews.add(image);
				}
				
			}
			else
			{
				if(select)
				{
					view.setTag("");
					image.setImageResource(R.drawable.folder);
					selectedCategories.remove(clickedItem);
					selectedViews.add(image);
				}
				else
				{
					view.setTag("select");
					image.setImageResource(R.drawable.folder_select);
					selectedCategories.add(clickedItem);
				}
			}
		}
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		View view=View.inflate(context,R.layout.adapter_basic_cotent_category, null);
		CategoryObject obj=items.get(arg0);
		ImageView image=(ImageView)view.findViewById(R.id.categoryItemImageView);
		image.setImageResource(R.drawable.folder);
		
		TextView name=(TextView)view.findViewById(R.id.categoryItemNameTextView);
		name.setText(obj.contentCategory.getName());
		
		view.setTag(obj.contentCategory.getParentCategory());
		
		return view;
	}
	
	public boolean isSingleSelectionMod() {
		return singleSelectionMod;
	}

	public void setSingleSelectionMod(boolean singleSelectionMod) {
		this.singleSelectionMod = singleSelectionMod;
	}

	public void moveUp()
	{
		ContentCategory parentCategory=dao.get(currentParentId);
		if(parentCategory!=null)	
			loadItems(parentCategory.getParentCategory());
		notifyDataSetChanged();
	}
	
	public long getCurrentParentLocalId()
	{
		return currentParentId;
	}
	public ContentCategory getSelected()
	{
		if(selectedCategories.size()>0)
				return selectedCategories.get(0);
		else
			return null;
	}
	public ArrayList<ContentCategory> getAllSelected()
	{
		return selectedCategories;
	}
	


	
}
