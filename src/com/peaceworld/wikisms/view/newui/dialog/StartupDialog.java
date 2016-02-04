package com.peaceworld.wikisms.view.newui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.utility.Settings;

public class StartupDialog extends DialogFragment {
	
	private Button okButton;
	private EditText message;
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.dialog_startup, null);
		//getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setTitle("www.wiki-sms.com");
		
		okButton=(Button)view.findViewById(R.id.OkButton);
		message=(EditText)view.findViewById(R.id.message);
		message.setText(getResources().getString(R.string.startup_dialog));
		CheckBox checkBox=(CheckBox)view.findViewById(R.id.checkBox);
		checkBox.setOnCheckedChangeListener(oncheckChangeListener);
		okButton.setOnClickListener(onClickListener_ok);
		return view;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	OnClickListener onClickListener_ok=new OnClickListener() {
		
		
		@Override
		public void onClick(View v) {
			dismissAllowingStateLoss();
		}
	};
	
	OnCheckedChangeListener oncheckChangeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked)
				Settings.setStartupDialogState(getActivity().getApplicationContext(), isChecked);
		}
	}; 

}