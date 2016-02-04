package com.peaceworld.wikisms.view.newui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.utility.Settings;

public class FilterSettingDialog extends DialogFragment {

	private RadioGroup viewSettingRG, orderSettingRG;

	public FilterSettingDialog() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.dialog_filter_setting, null);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		viewSettingRG = (RadioGroup) view.findViewById(R.id.viewSettingRG);
		orderSettingRG = (RadioGroup) view.findViewById(R.id.orderSettingRG);
		
		sync();

		return view;
	}

	private void sync() {
		String orderFilter=Settings.getOrderFilter(getActivity());
		if(orderFilter.equalsIgnoreCase(Settings.ORDER_BY_DATE))
			orderSettingRG.check(R.id.OrderByDateRadio);
		else if(orderFilter.equalsIgnoreCase(Settings.ORDER_BY_LIKED))
			orderSettingRG.check(R.id.OrderByLikedRadio);
		else if(orderFilter.equalsIgnoreCase(Settings.ORDER_BY_VIEWED))
			orderSettingRG.check(R.id.OrderByViewedRadio);
		
		String viewFilter=Settings.getViewFilter(getActivity());
		if(viewFilter.equalsIgnoreCase(Settings.VIEW_ALL))
			viewSettingRG.check(R.id.allContentsRadio);
		else if(viewFilter.equalsIgnoreCase(Settings.VIEW_UNVIEWED))
			viewSettingRG.check(R.id.unViewedOnlyRadio);
		else if(viewFilter.equalsIgnoreCase(Settings.VIEW_VIEWED))
			viewSettingRG.check(R.id.viewedOnlyRadio);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		viewSettingRG
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						switch (checkedId) {
						case R.id.unViewedOnlyRadio:
							Settings.setViewFilter(getActivity(), Settings.VIEW_UNVIEWED);
							break;
						case R.id.viewedOnlyRadio:
							Settings.setViewFilter(getActivity(), Settings.VIEW_VIEWED);
							break;
						case R.id.allContentsRadio:
							Settings.setViewFilter(getActivity(), Settings.VIEW_ALL);
							break;
						}
					}
				});

		orderSettingRG
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						switch (checkedId) {
						case R.id.OrderByDateRadio:
							Settings.setOrderFilter(getActivity(), Settings.ORDER_BY_DATE);
							break;
						case R.id.OrderByLikedRadio:
							Settings.setOrderFilter(getActivity(), Settings.ORDER_BY_LIKED);
							break;
						case R.id.OrderByViewedRadio:
							Settings.setOrderFilter(getActivity(), Settings.ORDER_BY_VIEWED);
							break;
						}

					}
				});

	}


}