package com.peaceworld.wikisms.view.newui.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.dao.CategoryFullDao;
import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.ContentNotification;
import com.peaceworld.wikisms.view.activity.CategoryManipulationainActivity;
import com.peaceworld.wikisms.view.activity.DialogConfirmationListener;
import com.peaceworld.wikisms.view.newui.adapter.BasicContentCategoryAdapter;

public class CategorySelectionDialog extends DialogFragment {

	private GridView gridView;
	private BasicContentCategoryAdapter adapter;
	private Button okBtn, cancleBtn;
	private ImageView creatImageView, moveUpImageView;
	private long contentId,contentCategoryLocalId;
	private Behavior behavior;
	private ContentNotification CN;
	private DialogConfirmationListener listener;
	private enum Behavior{ADD,CHANGE,CN}

	public CategorySelectionDialog(long contentId) {
		setRetainInstance(true);
		this.contentId=contentId;
		behavior=Behavior.ADD;
	}
	
	public CategorySelectionDialog(ContentNotification CN) {
		setRetainInstance(true);
		this.CN=CN;
		behavior=Behavior.CN;
	}
	
	public CategorySelectionDialog(long contentId, long contentCategoryLocalId, DialogConfirmationListener listener) {
		setRetainInstance(true);
		
		this.contentId=contentId;
		this.contentCategoryLocalId=contentCategoryLocalId;
		behavior=Behavior.CHANGE;
		this.listener=listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_category_selection, null);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		gridView = (GridView) view.findViewById(R.id.contentBrowserGridView);
		creatImageView = (ImageView) view.findViewById(R.id.categoryManipulationButton);
		moveUpImageView = (ImageView) view.findViewById(R.id.UpButton);
		okBtn = (Button) view.findViewById(R.id.OkButton);
		cancleBtn = (Button) view.findViewById(R.id.CancelButton);
		creatImageView.setOnClickListener(onClickListener);
		moveUpImageView.setOnClickListener(onClickListener);
		okBtn.setOnClickListener(onClickListener);
		cancleBtn.setOnClickListener(onClickListener);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new BasicContentCategoryAdapter(getActivity(), 0,true);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(adapter);

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.categoryManipulationButton:
				Intent intent=new Intent(getActivity(), CategoryManipulationainActivity.class);
				getActivity().startActivity(intent);
				break;
			case R.id.CancelButton:
				dismissAllowingStateLoss();
				break;
			case R.id.UpButton:
				adapter.moveUp();
				break;
			case R.id.OkButton:
					if(behavior==Behavior.CHANGE)
						changeContentCategory();
					else if(behavior==Behavior.ADD)
						addContentCategory();
					else if(behavior==Behavior.CN)
						editCN();
				break;

			default:
				break;
			}
		}
	};
	
	
	private void changeContentCategory()
	{
		ContentCategory newCategory=adapter.getSelected();
		if(newCategory == null)
			return;
		ContentFullDao contentFullDao=new ContentFullDao(getActivity());
		if(contentFullDao.isMemberOf(contentId, newCategory.getId()))
		{
			Toast.makeText(getActivity(), " این پیامک در حال حاضر عضو گروه "+newCategory.getName()+" است!!!", Toast.LENGTH_SHORT).show();
			return;
		}
		contentFullDao.changeContentCategoryTag(contentId, contentCategoryLocalId, newCategory.getId(),"",true);
		Toast.makeText(getActivity(), " به گروه "+newCategory.getName()+" تغییر کرد", Toast.LENGTH_SHORT).show();
		dismissAllowingStateLoss();
		if(listener!=null)
			listener.actionPerformed(true, null);
	}

	private void addContentCategory()
	{
		ContentCategory newCategory=adapter.getSelected();
		if(newCategory == null)
			return;
		ContentFullDao contentFullDao=new ContentFullDao(getActivity());
		if(contentFullDao.isMemberOf(contentId, newCategory.getId()))
		{
			Toast.makeText(getActivity(), " این پیامک در حال حاضر عضو گروه "+newCategory.getName()+" است!!!", Toast.LENGTH_SHORT).show();
			return;
		}
		contentFullDao.addContentCategoryTag(contentId, newCategory.getId(),"",true);
		Toast.makeText(getActivity(), " به گروه "+newCategory.getName()+" اضافه شد", Toast.LENGTH_SHORT).show();
		dismissAllowingStateLoss();
	}
	private void editCN()
	{
		ContentCategory selectedCategory=adapter.getSelected();
		CategoryFullDao categoryFullDao=new CategoryFullDao(getActivity());
		if(selectedCategory != null && CN !=null)
		{
			CN.setOperand1(selectedCategory.getId());
			long operand2=categoryFullDao.getFirstAcceptedParent(selectedCategory.getId());
			CN.setOperand2(operand2);
		}
		dismissAllowingStateLoss();
	}
}