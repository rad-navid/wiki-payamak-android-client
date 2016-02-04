package com.peaceworld.wikisms.view.newui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.view.activity.DialogConfirmationListener;
import com.peaceworld.wikisms.view.newui.dialog.SimpleOneInputDialog.TYPE;

public class EditContentDialog extends DialogFragment {
	
	private Button okButton,cancleButton;
	private EditText contentEditBoxEditText,commentEditText;
	private ContentFullDao contentFullDao;
	private Content content;
	private DialogConfirmationListener listener;
	
	public EditContentDialog(Content content, DialogConfirmationListener listener) {
		setRetainInstance(true);
		this.content=content;
		this.listener=listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.dialog_edit_content, null);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		okButton=(Button)view.findViewById(R.id.OkButton);
		cancleButton=(Button)view.findViewById(R.id.CancelButton);
		contentEditBoxEditText=(EditText)view.findViewById(R.id.contentEditBoxEditText);
		commentEditText=(EditText)view.findViewById(R.id.contentCommentBoxEditText);
		
		okButton.setOnClickListener(onClickListener_ok);
		cancleButton.setOnClickListener(onClickListener_cancle);
		contentEditBoxEditText.setText(content.getPlainText());
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		contentFullDao=new ContentFullDao(getActivity());

	}
	
	OnClickListener onClickListener_ok=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			String newContentText=contentEditBoxEditText.getText().toString();
			if(newContentText.trim().length()<=0)
			{
				Toast.makeText(getActivity(), "متن پیامک را نباید پاک کنید", Toast.LENGTH_SHORT).show();
				return;
			}else if(newContentText.trim().compareToIgnoreCase(content.getPlainText().trim())==0)
			{
				Toast.makeText(getActivity(), "هنوز اصلاحی صورت نگرفته", Toast.LENGTH_SHORT).show();
				return;
			}
			else if(newContentText.split("\\s+").length<7)
			{
				Toast.makeText(getActivity(), "متن پیامک خیلی کوتاه است", Toast.LENGTH_SHORT).show();
				return;
			}
			else if(commentEditText.getText().toString().trim().length()<=0 ||
					commentEditText.getText().toString().split("\\s+").length<2 )
			{
				Toast.makeText(getActivity(), "در قسمت توضیحات حداقل با 2 کلمه ذکر کنید که چه اصلاحی صورت گرفته", Toast.LENGTH_SHORT).show();
				return;
			}
			
			boolean result=contentFullDao.editContent(content.getId(), 
					newContentText, commentEditText.getText().toString(),true);
			
			if(result)
				Toast.makeText(getActivity(), "متن پیامک اصلاح شد", Toast.LENGTH_SHORT).show();
			dismissAllowingStateLoss();
			
			if(listener!=null)
				listener.actionPerformed(result, TYPE.EDIT_CONTENT);
		}
	};
	
	OnClickListener onClickListener_cancle=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dismissAllowingStateLoss();
			if(listener!=null)
				listener.actionPerformed(false, TYPE.EDIT_CONTENT);
		}
	};

}