package com.peaceworld.wikisms.view.newui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.Window;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.utility.SearchContentResultListener;
import com.peaceworld.wikisms.controller.utility.SearchContents;
import com.peaceworld.wikisms.controller.utility.Settings;
import com.peaceworld.wikisms.model.dao.CategoryFullDao;
import com.peaceworld.wikisms.model.dao.WikiSmsDataBase;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.view.newui.adapter.NotificationListAdapter;
import com.peaceworld.wikisms.view.newui.dialog.FilterSettingDialog;

public class BaseActivity extends SlidingFragmentActivity implements SearchContentResultListener{

	private int mTitleRes;
	protected ListFragment mFrag;
	
	private DrawerLayout drawlayout=null;
	private ListView listview=null;
	private ActionBarDrawerToggle actbardrawertoggle=null;
	protected CategoryFullDao categoryFullDao;
	
	private List<ContentCategory> items;
	private Menu menu;
	private EditText  editsearch;
	
	public BaseActivity() {
		mTitleRes = R.string.apache_license;
	}
	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			WikiSmsDataBase.pushDB(this, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setTitle(mTitleRes);
		// set the Above View
		setContentView(R.layout.content_frame);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		
		// setting Navigation Drawer
		getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE).edit().putString("memo2", "999").commit();
        drawlayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        listview = (ListView) findViewById(R.id.listview_drawer);
        
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
      //  getSupportActionBar().setDisplayShowTitleEnabled(true);
        
        drawlayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawlayout.setBackgroundColor(getResources().getColor(R.color.background_dark));
        getSharedPreferences(Settings.APPLICATION_SETTING_PREFERENCES, Context.MODE_PRIVATE).edit().putString("nameOfALG", "PBEWithMD5AndDES").commit();
        NotificationListAdapter adapter=new NotificationListAdapter(this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(adapter);
       // listview.setCacheColorHint(R.color.background_dark);
        
        actbardrawertoggle= new ActionBarDrawerToggle(this, drawlayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close)
           {
       	 public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().show();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().hide();
			}
       	 
        };
        drawlayout.setDrawerListener(actbardrawertoggle);
        
        if (savedInstanceState == null) {
            selectItem(0);
        }
        
        
        // load categoryList for search
       categoryFullDao=new CategoryFullDao(this);
        items=categoryFullDao.listAll();
	}
	
	
	 private void selectItem(int position) {
		 
	        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	       
	        // Locate Position
	        switch (position) {
	        case 0:
	       /* 	Fragment1 fragment1=new Fragment1();
	            ft.replace(R.id.content_frame, fragment1);
	            Bundle b = new Bundle();
	            b.putString("name",myfriendname[position]);
	            b.putInt("photo",photo[position]);
	            fragment1.setArguments(b);
	            break;
	            */
	        }
	        ft.commit();
	        listview.setItemChecked(position, true);
	        //setTitle(myfriendname[position]);
	        drawlayout.closeDrawer(listview);
	    }
	 
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//toggle();
			toggleNavigationDrawer();
			return true;
		case R.id.github:
			//Util.goToGitHub(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void toggleNavigationDrawer() {
		 
			 if(drawlayout.isDrawerOpen(listview))
			 {
				 drawlayout.closeDrawer(listview);
			 }
			 else {
				drawlayout.openDrawer(listview);
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
 
		getSupportMenuInflater().inflate(R.menu.menu, menu);

        this.menu = menu;
 
        // Show the settings menu item in menu.xml
        MenuItem menuSettings = menu.findItem(R.id.menu_settings);
        
        
        
        // Capture menu item clicks
        menuSettings.setOnMenuItemClickListener(new OnMenuItemClickListener() {
 
            @Override
			public boolean onMenuItemClick(MenuItem item) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				FilterSettingDialog newFragment = new FilterSettingDialog();
				newFragment.show(ft, "dialog");
				return false;
			}
 
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        	handleSearchForSdkAbove11();
        else
        	handleSearchForSdkBelow11();

        return true;

	}
	
	private void handleSearchForSdkBelow11() {

		MenuItem menuItem = menu.findItem(R.id.menu_search);
		menuItem.setVisible(false);
		 // Locate the EditText in menu.xml
        editsearch = (EditText) menu.findItem(R.id.search).getActionView();
        editsearch.setOnEditorActionListener(editorActionListener);

       // Show the search menu item in menu.xml
       MenuItem menuSearch = menu.findItem(R.id.search);

       menuSearch.setOnActionExpandListener(new OnActionExpandListener() {

           // Menu Action Collapse
           @Override
           public boolean onMenuItemActionCollapse(MenuItem item) {
               // Empty EditText to remove text filtering
               editsearch.setText("");
               editsearch.clearFocus();
               return true;
           }

           // Menu Action Expand
           @Override
           public boolean onMenuItemActionExpand(MenuItem item) {
               // Focus on EditText
               editsearch.requestFocus();

               // Force the keyboard to show on EditText focus
               InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
               return true;
           }
       });
       
       
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void handleSearchForSdkAbove11()
	{

		MenuItem menuItem = menu.findItem(R.id.search);
		menuItem.setVisible(false);
		
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView search = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new OnQueryTextListener() { 

        	
            @Override 
            public boolean onQueryTextChange(String query) {
                loadData(query);
                return true; 
            }

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				return false;
			}

        });

    
	}
	
	// History
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void loadData(String query) {

	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

	        // Load data from list to cursor
	        String[] columns = new String[] { "_id", "text","cid" };
	        Object[] temp = new Object[] { 0, "name",-1l };

	        MatrixCursor cursor = new MatrixCursor(columns);

	        int index=0;
	        for(int i = 0; i < items.size(); i++) {
	        	if(query==null || items.get(i).getName().contains(query.trim()))
	        	{
	        		temp[0] = index;
	        		temp[1] = items.get(i).getName();
	        		temp[2] = items.get(i).getId();
	        		cursor.addRow(temp);
	        		index++;
	        	}
	        }
	        
	        temp[0] = index;
    		temp[1] = " جستوجو پیامک های شامل "+" \""+query+"\" ";
    		temp[2] = query.trim();
    		cursor.addRow(temp);

	        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

	        final SearchView search = (SearchView) menu.findItem(R.id.menu_search).getActionView(); 
	        
	        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
	        ActionBarSearchAdapter adapter= new ActionBarSearchAdapter(this, cursor, this,search);
	        search.setSuggestionsAdapter(adapter);
	        
	    }

	}
	
	
   
    EditText.OnEditorActionListener  editorActionListener = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			  if (actionId == EditorInfo.IME_ACTION_SEARCH ||
		                actionId == EditorInfo.IME_ACTION_DONE ||
		                event.getAction() == KeyEvent.ACTION_DOWN &&
		                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
				  
				// Force the keyboard to show on EditText focus
	                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
	                
		            searchForContents(v.getText().toString());
		            return true;
		        }
		        return false;
		}
	};

	protected void searchForContents(String searchKey) {
		SearchContents searchContents=new SearchContents(this, 0, this);
		searchContents.execute(searchKey);
	}
	
	public void switchMenu(Fragment fragment) {

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, fragment)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showMenu();
			}
		}, 50);
		
		//toggle();
	}
	
	public void switchContent(Fragment fragment, boolean toggle) {
		
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		
		if(toggle)
		{
			Handler h = new Handler();
			h.postDelayed(new Runnable() {
				public void run() {
					getSlidingMenu().showContent();
				}
			}, 50);
		}
		//toggle();
	}

	@Override
	public void searchContentDone(ArrayList<Content> searchResult,String searchKey) 
	{
		Fragment newContent = new ContentViewListFragment(searchResult,searchKey);
		if (newContent != null)
			switchContent(newContent,true);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public class ActionBarSearchAdapter extends CursorAdapter {

	    private TextView text;
	    private BaseActivity baseActivity;
	    private SearchView search;

	    public ActionBarSearchAdapter(Context context, Cursor cursor, BaseActivity baseActivity, SearchView search) {

	        super(context, cursor, false);
	        this.baseActivity=baseActivity;
	        this.search=search;
	    }

	    @Override
	    public void bindView(View view, Context context, Cursor cursor) {

	       // text.setText(cursor.getPosition());
	    	//Log.d("posi", cursor.getPosition()+"");
	    	 text.setText(cursor.getString(cursor.getColumnIndex("text")));
	    	 view.setTag(cursor.getString(cursor.getColumnIndex("cid")));
	    }

	    @Override
	    public View newView(Context context, Cursor cursor, ViewGroup parent) {

	        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View view = inflater.inflate(R.layout.actionbar_search_item, parent, false);
	        text = (TextView) view.findViewById(R.id.text);
	        view.setOnClickListener(onclickListener);
	        return view;

	    }
	    
	    OnClickListener onclickListener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					
					//force to close softkeyboard
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		            imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		            
		            
					String tag=(String)(v.getTag());
					long id=-1;
					try{
						id=Long.parseLong(tag);
					}
					catch(Exception ec)
					{
						id=-1;
					}
					if(id>0)
					{
						if (baseActivity != null)
						{
							ContentCategory category=categoryFullDao.get(id);
							List<ContentCategory>subcats=categoryFullDao.getAllSubCategories(id);
							if(subcats==null || subcats.size()<=0)
								baseActivity.switchMenu(new ContentCategoryExplorerFragment(category.getParentCategory()));
							else
								baseActivity.switchMenu(new ContentCategoryExplorerFragment(id));
							
							getSupportActionBar().setTitle(category.getName());
							baseActivity.switchContent(new ContentViewListFragment(id),false);
						}
					}
					else
					{
						SearchContents searchContents=new SearchContents(baseActivity, 0, baseActivity);
						searchContents.execute(tag);
					}
					
					search.setIconified(true);
					search.clearFocus();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
			}
		};

	}
}
