package com.peaceworld.wikisms.view.newui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.entity.CategoryNotification;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.ContentNotification;
import com.peaceworld.wikisms.model.entity.dao.CategoryNotificationDao;
import com.peaceworld.wikisms.model.entity.dao.CategoryNotificationTable;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryDao;
import com.peaceworld.wikisms.model.entity.dao.ContentDao;
import com.peaceworld.wikisms.model.entity.dao.ContentNotificationDao;
import com.peaceworld.wikisms.model.entity.dao.ContentNotificationTable;
import com.peaceworld.wikisms.view.newui.MenuAnimManager;
import com.peaceworld.wikisms.view.newui.adapter.NotificationUtility.NotificationVerificationType;
import com.peaceworld.wikisms.view.newui.pupup.PopUpNotificationSimple;

public class NotificationListAdapter extends BaseAdapter implements OnItemClickListener {
	
	public enum NotificationType{Content,Category}
	
	
	private Context context;
	private ArrayList<NotificationHelper> notificationsList;
	private CategoryNotificationDao categoryNotificationDao;
	private ContentNotificationDao contentNotificationDao;
	private ContentDao contentDao;
	private ContentCategoryDao categoryDao;
	private HashMap<Integer, View>ViewMap;
	private int clickedViewIndex;
	private NotificationUtility notificationUtility;
	private static NotificationListAdapter instance;
	
	
	
	Handler handler_refresh = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  refresh();
		     }
		 };

	public static NotificationListAdapter getInstance()
	{
		return instance;
	}
	
	
	public NotificationListAdapter(Context context)
	{
		this.context=context;
		categoryNotificationDao=new CategoryNotificationDao(context);
		contentNotificationDao=new ContentNotificationDao(context);
		contentDao=new ContentDao(context);
		categoryDao=new ContentCategoryDao(context);
		ViewMap= new HashMap<Integer, View>();
		notificationUtility=new NotificationUtility(context);
		refresh();
		instance=this;
		
		
	}
	
	private void removeCheckNotif()
	{
		View view=ViewMap.get(clickedViewIndex);
		if(view !=null)
		{
			
			AnimationSet animset=MenuAnimManager.getNotificationAnimation();
			animset.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					refresh();
				}
			});
			view.startAnimation(animset);
		}
		else
		{
			refresh();
		}
	}
	
	private void refresh()
	{	
		try{
			loadNotifications();
			purifyNotificationsList();
			this.notifyDataSetChanged();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void removeClickedNotification()
	{
		try{
			removeCheckNotif();
			//handler_remove_notification.sendEmptyMessage(0);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void refreshNotifications()
	{
		handler_refresh.sendEmptyMessage(0);
	}

	public void loadNotifications()
	{
		if(notificationsList==null)
			notificationsList=new ArrayList<NotificationHelper>();
		notificationsList.clear();
		
		
		Cursor cur=categoryNotificationDao.query(
				CategoryNotificationTable.Columns.ACCEPT+" = 0 AND "+
				CategoryNotificationTable.Columns.DENY+" = 0 AND "+
				CategoryNotificationTable.Columns.SENTFROMSERVER+" = 1 AND "+
				CategoryNotificationTable.Columns.SENTTOSERVER+" = 0  ",null);
		
		List<CategoryNotification> tmpCCNL= categoryNotificationDao.asList(cur);
		for(CategoryNotification cn:tmpCCNL)
		{
			notificationsList.add(new NotificationHelper(NotificationType.Category, cn));
		}	
		
		
		 cur=contentNotificationDao.query(
				ContentNotificationTable.Columns.ACCEPT+" = 0 AND "+
				ContentNotificationTable.Columns.DENY+" = 0 AND "+
				ContentNotificationTable.Columns.SENTFROMSERVER+" = 1 AND "+
				ContentNotificationTable.Columns.SENTTOSERVER+" = 0  ",null);
		
		List<ContentNotification> tmpCNL= contentNotificationDao.asList(cur);
		for(ContentNotification cn:tmpCNL)
		{
			notificationsList.add(new NotificationHelper(NotificationType.Content, cn));
		}
		
	}
	

	@Override
	public int getCount() {
		if(notificationsList!=null)
			return notificationsList.size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if(notificationsList!=null)
			return notificationsList.get(arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int index, View view, ViewGroup arg2) {
		
		view=View.inflate(context, R.layout.notification_list_item,null);
		ImageView image=(ImageView)view.findViewById(R.id.notificationImageView);
		TextView title=(TextView)view.findViewById(R.id.notificationTitleTextView);
		TextView data=(TextView)view.findViewById(R.id.notificationDataTextView);
		classifyNotifications(index,image,title,data);
		ViewMap.put(index, view);
		if(index!=0)
		{
		    view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.notification_deactive_selector));
		}
		else
		{
			view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.notification_active_selector));
		}
		return view ;
	}

	
	private void purifyNotificationsList() {
		
		while(notificationsList.size()>0 )
		{
			NotificationVerificationType verificationType=notificationUtility.VerifyNotification(notificationsList.get(0));
			if(verificationType==NotificationVerificationType.valid)
				break;
			else
			{
				NotificationHelper nh=notificationsList.remove(0);
				handelNotValidNotification(nh,verificationType);
			}
		}
	}

	private void handelNotValidNotification(NotificationHelper nh, NotificationVerificationType verificationType) {
		
		if(nh.type==NotificationType.Content)
		{
			ContentNotification cn=(ContentNotification)nh.notification;
			if(verificationType==NotificationVerificationType.invalid)
				contentNotificationDao.delete(cn.getId());
			else if(verificationType==NotificationVerificationType.validButPreviouslyDenied)
			{
				cn.setDeny(true);
				contentNotificationDao.update(cn);
			}
		}
		else if(nh.type==NotificationType.Category)
		{
			CategoryNotification ccn=(CategoryNotification)nh.notification;
			if(verificationType==NotificationVerificationType.invalid)
			{
				categoryNotificationDao.delete(ccn.getId());
			}
			else if(verificationType==NotificationVerificationType.validButPreviouslyDenied)
			{
				ccn.setDeny(true);
				categoryNotificationDao.update(ccn);
			}
		}
	}

	private void classifyNotifications(int index, ImageView image, TextView title, TextView data)
	{
		NotificationHelper notificationHelper=notificationsList.get(index);
		if(notificationHelper.type==NotificationType.Content)
		{
			ContentNotification cnotif=(ContentNotification)notificationHelper.notification;
			ContentNotification.ACTION action=ContentNotification.ACTION.valueOf(cnotif.getAction());
			switch(action)
			{
			case CONTENT_DELETE:
				notification_action_CONTENT_DELETE(cnotif,image,title,data);
				break;
			case CONTENT_EDIT:
				notification_action_CONTENT_EDIT(cnotif,image,title,data);
				break;
			case CONTENT_TAG_ADDED:
				notification_action_CONTENT_TAG_ADDED(cnotif,image,title,data);
				break;
			case CONTENT_TAG_CHANGED:
				notification_action_CONTENT_TAG_CHANGED(cnotif,image,title,data);
				break;
			case CONTENT_CREATE:
				notification_action_CONTENT_CREATE(cnotif,image,title,data);
				break;
				
			default:
				notification_action_UNKONWN(notificationHelper,image,title,data);
				break;
			
			}
		}
		else if(notificationHelper.type==NotificationType.Category)
		{
			CategoryNotification ccnotif=(CategoryNotification)notificationHelper.notification;
			CategoryNotification.ACTION action=CategoryNotification.ACTION.valueOf(ccnotif.getAction());
			switch(action)
			{
			case CATEGORY_CHANGED:
				notification_action_CATEGORY_CHANGED(ccnotif,image,title,data);
				break;
			case CATEGORY_CREATE:
				notification_action_CATEGORY_CREATE(ccnotif,image,title,data);
				break;
			case CATEGORY_DELETE:
				notification_action_CATEGORY_DELETE(ccnotif,image,title,data);
				break;
			case CATEGORY_MERGE:
				notification_action_CATEGORY_MERGE(ccnotif,image,title,data);
				break;
			case CATEGORY_MOVE:
				notification_action_CATEGORY_MOVE(ccnotif,image,title,data);
				break;
				
			default:
				notification_action_UNKONWN(notificationHelper,image,title,data);
				break;
			}
			
		}
		else
		{
			notification_action_UNKONWN(notificationHelper,image,title,data);
		}
	}
	
	
	private void notification_action_CATEGORY_MOVE(
			CategoryNotification ccnotif, ImageView image, TextView title,
			TextView data) {
		
		image.setImageResource(R.drawable.ic_action_cut);
		title.setText(context.getResources().getString(R.string.notification_category_move));
		ContentCategory c1= categoryDao.get(ccnotif.getOperand1());
		ContentCategory c2= categoryDao.get(ccnotif.getOperand2());
		String info="";
		if(c1==null)
			return;
		if(c2 == null)
			info= c1.getName()+ " منتقل شده ";
		else
			info = c1.getName()+" منتقل شده به "+c2.getName();
		
		data.setText(info);
		   
	}

	private void notification_action_CATEGORY_MERGE(
			CategoryNotification ccnotif, ImageView image, TextView title,
			TextView data) {
		
		image.setImageResource(R.drawable.ic_action_merge);
		title.setText(context.getResources().getString(R.string.notification_category_merge));
		ContentCategory c1= categoryDao.get(ccnotif.getOperand1());
		ContentCategory c2= categoryDao.get(ccnotif.getOperand2());
		if(c1==null || c2 == null)
			return;		
		data.setText(c1.getName()+" و "+c2.getName()+" ادغام شده");
		
	}

	private void notification_action_CATEGORY_DELETE(
			CategoryNotification ccnotif, ImageView image, TextView title,
			TextView data) {
		image.setImageResource(R.drawable.ic_action_remove);
		title.setText(context.getResources().getString(R.string.notification_category_delete));
		ContentCategory cc= categoryDao.get(ccnotif.getOperand1());
		if(cc==null)
			return;
		data.setText(cc.getName());
		
	}

	private void notification_action_CATEGORY_CREATE(
			CategoryNotification ccnotif, ImageView image, TextView title,
			TextView data) {
		image.setImageResource(R.drawable.ic_action_new);
		title.setText(context.getResources().getString(R.string.notification_category_add));
		String info="";
		if(ccnotif.getOperand2()==0)
		{
			info= ccnotif.getMetadata()+" زیر گروه گروه اصلی ";
		}
		else
		{
			ContentCategory pc= categoryDao.get(ccnotif.getOperand2());
			if(pc == null)
				info= ccnotif.getMetadata()+" ایجاد شده ";
			else       
				info= ccnotif.getMetadata()+" زیر گروه "+ pc.getName();
		}
		data.setText(info);
		
	}

	private void notification_action_CATEGORY_CHANGED(
			CategoryNotification ccnotif, ImageView image, TextView title,
			TextView data) {
		image.setImageResource(R.drawable.ic_action_edit);
		title.setText(context.getResources().getString(R.string.notification_category_rename));
		ContentCategory c1= categoryDao.get(ccnotif.getOperand1());
		if(c1==null)
			return;
		data.setText(c1.getName()+" تغییر نام یافته به "+ccnotif.getMetadata());
		
	}

	private void notification_action_CONTENT_CREATE(ContentNotification cnotif,
			ImageView image, TextView title, TextView data) {
		
		image.setImageResource(R.drawable.ic_action_new_email);
		title.setText(context.getResources().getString(R.string.notification_content_add));
		data.setText(cnotif.getMetaInfo());
		
	}

	private void notification_action_CONTENT_TAG_CHANGED(
			ContentNotification cnotif, ImageView image, TextView title,
			TextView data) {
		image.setImageResource(R.drawable.change);
		title.setText(context.getResources().getString(R.string.notification_content_changed_category));
		ContentCategory newCategory= categoryDao.get(cnotif.getOperand2());
		ContentCategory oldCategory=categoryDao.get(cnotif.getOperand1());
		if(newCategory==null || oldCategory==null)
			return;
		data.setText(oldCategory.getName()+" --> "+newCategory.getName());
		
	}

	private void notification_action_CONTENT_TAG_ADDED(
			ContentNotification cnotif, ImageView image, TextView title,
			TextView data) {
		image.setImageResource(R.drawable.extension);
		title.setText(context.getResources().getString(R.string.notification_content_add_category));
		ContentCategory newCategory= categoryDao.get(cnotif.getOperand1());
		if(newCategory == null)
			return;
		data.setText(" + "+newCategory.getName());
		
	}

	private void notification_action_UNKONWN(NotificationHelper notificationHelper,
			ImageView image, TextView title, TextView data) {

		image.setImageResource(R.drawable.pressed);
		title.setText("");
		data.setText("");
	}

	private void notification_action_CONTENT_EDIT(ContentNotification cnotif,
			ImageView image, TextView title, TextView data) {
		image.setImageResource(R.drawable.edit);
		title.setText(context.getResources().getString(R.string.notification_content_edit));
		Content content=contentDao.get(cnotif.getContentId());
		if(content==null)
			return;
		data.setText(content.getPlainText());
	}

	private void notification_action_CONTENT_DELETE(ContentNotification cnotif,
			ImageView image, TextView title, TextView data) {
		image.setImageResource(R.drawable.delete);
		title.setText(context.getResources().getString(R.string.notification_content_delete));
		Content content=contentDao.get(cnotif.getContentId());
		if(content == null)
			return;
		data.setText(content.getPlainText());
		
	}


	class NotificationHelper{
	
		NotificationType type;
		Object notification;
		
		public NotificationHelper(NotificationType type, Object notification) {
			super();
			this.type = type;
			this.notification = notification;
		}

		public NotificationType getType() {
			return type;
		}

		public void setType(NotificationType type) {
			this.type = type;
		}

		public Object getNotification() {
			return notification;
		}

		public void setNotification(Object notification) {
			this.notification = notification;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
		
		if(index>0)
			Toast.makeText(context, "به ترتیب از اولین ایتم چک کنید", Toast.LENGTH_SHORT).show();
		else
		{
			showNotificationDialog(index,view);
			clickedViewIndex=index;
		}
	}
	
	private void showNotificationDialog(int index,View view)
	{

		NotificationHelper notificationHelper=notificationsList.get(index);
		PopUpNotificationSimple simplePopupDialog = null ;
		if(notificationHelper.type==NotificationType.Content)
		{
			ContentNotification cnotif=(ContentNotification)notificationHelper.notification;
			simplePopupDialog = new PopUpNotificationSimple(context, cnotif,this);
		}
		else if(notificationHelper.type==NotificationType.Category)
		{
			CategoryNotification cnotif=(CategoryNotification)notificationHelper.notification;
			simplePopupDialog = new PopUpNotificationSimple(context, cnotif,this);
		}
		simplePopupDialog.show(view);
		
	}

}
