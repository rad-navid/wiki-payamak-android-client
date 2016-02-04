package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.services.BackgroundSyncService;
import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.view.newui.dialog.StartupDialog;

public class ContentPreviewFragmentActivity extends BaseActivity  {
	
	private Fragment mContent,mMenu;
	private CanvasTransformer mTransformer;
	
	public ContentPreviewFragmentActivity() {
		super(R.string.app_name);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		
		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		String sharedSms=null;
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				if (sharedText != null) {
					sharedSms=sharedText;
				}
			}
		} 

		if (savedInstanceState != null)
		{
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
			mMenu = getSupportFragmentManager().getFragment(savedInstanceState, "mMenu");
		}
		
		if(sharedSms!=null)
		{
			ArrayList<String>sharedList=new ArrayList<String>(1);
			sharedList.add(sharedSms);
			mContent = new SmsViewListFragment(sharedList);
		}
		
		if(mContent==null)
		{
			ArrayList<String>sharedList=new ArrayList<String>(0);
			mContent = new SmsViewListFragment(sharedList);
			// set the Above View
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();
		 
		
		if(mMenu==null)
		{
			mMenu=new MainMenuFragment();
			// set the Behind View
			setBehindContentView(R.layout.menu_frame);
			getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, mMenu).commit();
		}
		
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		getSlidingMenu().setMode(SlidingMenu.RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setSlidingActionBarEnabled(true);
		sm.setBehindScrollScale(0.0f);
		if(mTransformer==null)
			mTransformer=MenuAnimManager.getCanvasTransformer();
		sm.setBehindCanvasTransformer(mTransformer);
		
		//Start service
		Intent BgSyncIntent=new Intent(this,BackgroundSyncService.class);
		startService(BgSyncIntent);
		
		boolean dontShow=Settings.isNotStartupDialogActive(getApplicationContext());
		if(!dontShow)
		{
			StartupDialog startup_dialog=new StartupDialog();
			startup_dialog.show(getSupportFragmentManager(), "startup_dialog");
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
			getSupportFragmentManager().putFragment(outState, "mContent", mContent);
			getSupportFragmentManager().putFragment(outState, "mMenu", mMenu);
	}
	
	public void switchContent(Fragment fragment, boolean toggle) {
		super.switchContent(fragment, toggle);
		mContent = fragment;
	}
	
	
	public void switchMenu(Fragment fragment) {
		super.switchMenu(fragment);
		mMenu = fragment;
		
	}
	
}
