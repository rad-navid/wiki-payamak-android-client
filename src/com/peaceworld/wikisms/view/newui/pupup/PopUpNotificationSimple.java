package com.peaceworld.wikisms.view.newui.pupup;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.controller.rs.manager.CategoryNotificationClickedHandler;
import com.peaceworld.wikisms.model.dao.CategoryNotificationFullDao;
import com.peaceworld.wikisms.model.dao.ContentNotificationFullDao;
import com.peaceworld.wikisms.model.entity.CategoryNotification;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentCategory;
import com.peaceworld.wikisms.model.entity.ContentNotification;
import com.peaceworld.wikisms.model.entity.dao.ContentCategoryDao;
import com.peaceworld.wikisms.model.entity.dao.ContentDao;
import com.peaceworld.wikisms.view.newui.adapter.NotificationListAdapter;

public class PopUpNotificationSimple extends PopUpDialog {
	
	private TextView showInfo1,showInfo2,showInfo3;
	private TextView showTitle1,showTitle2,showTitle3;
	private Button ok,cancel;
	private ContentNotification contentNotification;
	private CategoryNotification categoryNotification;
	private ContentDao contentDao;
	private ContentCategoryDao categoryDao;
	private CategoryNotificationFullDao CCNDao;
	private ContentNotificationFullDao CNDao;
	private Context context;
	private NotificationListAdapter notificationListAdapter;
	
	private PopUpNotificationSimple(Context context) {
		super(context, R.layout.pop_up_notification_simple_layout);
		init();
		this.context=context;
		contentDao=new ContentDao(context);
		categoryDao=new ContentCategoryDao(context);
		CCNDao=new CategoryNotificationFullDao(context);
		CNDao=new ContentNotificationFullDao(context);
	}
	
	public PopUpNotificationSimple(Context context,ContentNotification notification,NotificationListAdapter notificationListAdapter) {
		this(context);
		contentNotification=notification;
		constructDialog();
		this.notificationListAdapter=notificationListAdapter;
	}
	public PopUpNotificationSimple(Context context,CategoryNotification notification,NotificationListAdapter notificationListAdapter) {
		this(context);
		categoryNotification=notification;
		constructDialog();
		this.notificationListAdapter=notificationListAdapter;
	}

	private void init() {
		showInfo1=(TextView)mView.findViewById(R.id.showInfo1);
		showInfo2=(TextView)mView.findViewById(R.id.showInfo2);
		showInfo3=(TextView)mView.findViewById(R.id.showInfo3);
		showTitle1=(TextView)mView.findViewById(R.id.showTitle1);
		showTitle2=(TextView)mView.findViewById(R.id.showTitle2);
		showTitle3=(TextView)mView.findViewById(R.id.showTitle3);
		ok=(Button)mView.findViewById(R.id.OkButton);
		cancel=(Button)mView.findViewById(R.id.CancelButton);
		ok.setOnClickListener(onClickListener_ok);
		cancel.setOnClickListener(onClickListener_cancel);
	}
	
	
	
	private void constructDialog() {
		if(contentNotification!=null)
		{
			ContentNotification.ACTION action=
					ContentNotification.ACTION.valueOf(contentNotification.getAction());
			switch(action)
			{
			case CONTENT_DELETE:
				constructContentNotification_action_CONTENT_DELETE();
				break;
			case CONTENT_EDIT:
				constructContentNotification_action_CONTENT_EDIT();
				break;
			case CONTENT_TAG_ADDED:
				constructContentNotification_action_CONTENT_TAG_ADDED();
				break;
			case CONTENT_TAG_CHANGED:
				constructContentNotification_action_CONTENT_TAG_CHANGED();
				break;
			case CONTENT_CREATE:
				notification_action_CONTENT_CREATE();
				break;
				
			default:
				notification_action_UNKONWN();
				break;
			
			}
		}
		else if(categoryNotification!=null)
		{
			CategoryNotification.ACTION action=CategoryNotification.ACTION.valueOf(categoryNotification.getAction());
			switch(action)
			{
			case CATEGORY_CHANGED:
				notification_action_CATEGORY_CHANGED();
				break;
			case CATEGORY_CREATE:
				notification_action_CATEGORY_CREATE();
				break;
			case CATEGORY_DELETE:
				notification_action_CATEGORY_DELETE();
				break;
			case CATEGORY_MERGE:
				notification_action_CATEGORY_MERGE();
				break;
			case CATEGORY_MOVE:
				notification_action_CATEGORY_MOVE();
				break;
				
			default:
				notification_action_UNKONWN();
				break;
			}
			
		}
		else
		{
			// unknown notifiction
			notification_action_UNKONWN();
		}
		
	}

	private void notification_action_CONTENT_CREATE() {

		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_content_create));
		showInfo1.setText(contentNotification.getMetaInfo());
	}

	private void notification_action_UNKONWN() {

		showInfo1.setText(context.getResources().getString(R.string.notification_dialog_unkown));
		
	}

	private void notification_action_CATEGORY_MOVE() {

		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_category_move));
		ContentCategory c1= categoryDao.get(categoryNotification.getOperand1());
		ContentCategory c2= categoryDao.get(categoryNotification.getOperand2());
		showInfo1.setText(" گروه "+c1.getName()+" به زیر گروه های "+c2.getName()+" منتقل شده");
		
	}

	private void notification_action_CATEGORY_MERGE() {

		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_category_merge));
		ContentCategory c1= categoryDao.get(categoryNotification.getOperand1());
		ContentCategory c2= categoryDao.get(categoryNotification.getOperand2());
		showInfo1.setText(" گروه "+c1.getName()+" و گروه "+c2.getName()+" ادغام شده "+"\r\n با نام جدید :"+categoryNotification.getMetadata());
		
	}

	private void notification_action_CATEGORY_DELETE() {
		
		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_category_delete));
		ContentCategory cc= categoryDao.get(categoryNotification.getOperand1());
		showInfo1.setText("نام گروه :"+cc.getName());
		showInfo3.setText(categoryNotification.getComment());
		
		showTitle1.setVisibility(View.VISIBLE);
		showInfo1.setVisibility(View.VISIBLE);
		showTitle3.setVisibility(View.VISIBLE);
		showInfo3.setVisibility(View.VISIBLE);
	}

	private void notification_action_CATEGORY_CREATE() {
		
		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_category_create));
		String parentName="";
		if(categoryNotification.getOperand2()==0)
			parentName="  اصلی ";
		else{
		ContentCategory pc= categoryDao.get(categoryNotification.getOperand2());
		parentName=" "+ pc.getName()+" ";
		}
		showInfo1.setText(" گروه "+categoryNotification.getMetadata()+" به عنوان یک زیرگروه "+parentName+" ایجاد شود");
		showTitle1.setVisibility(View.VISIBLE);
		showInfo2.setVisibility(View.VISIBLE);
	}

	private void notification_action_CATEGORY_CHANGED() {
		
		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_category_edit));
		ContentCategory c1= categoryDao.get(categoryNotification.getOperand1());
		showInfo2.setText(c1.getName()+" تغییر نام یافته به "+categoryNotification.getMetadata());
		showTitle1.setVisibility(View.VISIBLE);
		showInfo2.setVisibility(View.VISIBLE);
		
	}

	private void constructContentNotification_action_CONTENT_TAG_CHANGED() {
		
		Content content=contentDao.get(contentNotification.getContentId());
		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_content_changed_category));
		showTitle1.setVisibility(View.VISIBLE);
		showInfo1.setText(content.getPlainText());
		
		ContentCategory newCategory= categoryDao.get(contentNotification.getOperand2());
		ContentCategory oldCategory=categoryDao.get(contentNotification.getOperand1());
		showTitle2.setText(""+oldCategory.getName()+"  به  "+newCategory.getName()+" تغییر میکند");
		showTitle2.setVisibility(View.VISIBLE);
		
	}

	private void constructContentNotification_action_CONTENT_TAG_ADDED() {
		Content content=contentDao.get(contentNotification.getContentId());
		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_content_add_category));
		showTitle1.setVisibility(View.VISIBLE);
		showInfo1.setText(content.getPlainText());
		
		ContentCategory newCategory= categoryDao.get(contentNotification.getOperand1());
		showTitle2.setText(" به گروه "+newCategory.getName()+"نیز افزوده شود .");
		showTitle2.setVisibility(View.VISIBLE);
		
	}

	private void constructContentNotification_action_CONTENT_EDIT() {
		
		Content content=contentDao.get(contentNotification.getContentId());
		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_content_edited));
		showTitle1.setVisibility(View.VISIBLE);
		showInfo1.setText(content.getPlainText());
		
		showTitle2.setText(context.getResources().getString(R.string.notification_dialog_content_edited_new_value));
		showTitle2.setVisibility(View.VISIBLE);
		showInfo2.setText(contentNotification.getMetaInfo());
		showInfo2.setVisibility(View.VISIBLE);
		
		if(contentNotification.getComment() !=null &&contentNotification.getComment().length()>3)
		{
			showTitle3.setText(context.getResources().getString(R.string.notification_dialog_comment));
			showTitle3.setVisibility(View.VISIBLE);
			showInfo3.setText(contentNotification.getComment());
			showInfo3.setVisibility(View.VISIBLE);
		}
	
	}

	private void constructContentNotification_action_CONTENT_DELETE() {
		
		Content content=contentDao.get(contentNotification.getContentId());
		showTitle1.setText(context.getResources().getString(R.string.notification_dialog_content_delete));
		showTitle1.setVisibility(View.VISIBLE);
		showInfo1.setText(content.getPlainText());
		if(contentNotification.getComment() !=null &&contentNotification.getComment().length()>3)
		{
			showTitle2.setText(context.getResources().getString(R.string.notification_dialog_comment));
			showTitle2.setVisibility(View.VISIBLE);
			showInfo2.setText(contentNotification.getComment());
			showInfo2.setVisibility(View.VISIBLE);
		}
		
	}
	
	OnClickListener onClickListener_ok=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(categoryNotification!=null)
			{
				categoryNotification.setAccept(true);
				CCNDao.update(categoryNotification);
				CategoryNotificationClickedHandler handler=new CategoryNotificationClickedHandler(context);
				handler.classifyNotifications(categoryNotification, true);
			}
			else if(contentNotification!=null)
			{
				contentNotification.setAccept(true);
				CNDao.update(contentNotification);
			}
			
			notificationListAdapter.removeClickedNotification();
			dismiss();
		}
	};
	
	OnClickListener onClickListener_cancel=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(categoryNotification!=null)
			{
				categoryNotification.setDeny(true);
				CCNDao.update(categoryNotification);
				CategoryNotificationClickedHandler handler=new CategoryNotificationClickedHandler(context);
				handler.classifyNotifications(categoryNotification, false);
			}
			else if(contentNotification!=null)
			{
				contentNotification.setDeny(true);
				CNDao.update(contentNotification);
			}
			notificationListAdapter.removeClickedNotification();
			dismiss();
		}
	};
}
