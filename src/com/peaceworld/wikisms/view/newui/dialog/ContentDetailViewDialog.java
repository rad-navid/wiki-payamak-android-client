package com.peaceworld.wikisms.view.newui.dialog;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.Layout.Alignment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.peaceworld.wikisms.R;
import com.peaceworld.wikisms.model.dao.ContentFullDao;
import com.peaceworld.wikisms.model.entity.Content;
import com.peaceworld.wikisms.model.entity.ContentDirectChange;
import com.peaceworld.wikisms.model.entity.UserTable;
import com.peaceworld.wikisms.model.entity.dao.ContentDirectChangeDao;
import com.peaceworld.wikisms.model.entity.dao.UserTableDao;
import com.peaceworld.wikisms.view.activity.DialogConfirmationListener;
import com.peaceworld.wikisms.view.newui.ContentViewListFragment;
import com.peaceworld.wikisms.view.newui.MenuAnimManager;
import com.peaceworld.wikisms.view.newui.OnSwipListener;
import com.peaceworld.wikisms.view.newui.TouchSwipeDetector;
import com.peaceworld.wikisms.view.newui.dialog.SimpleOneInputDialog.TYPE;

public class ContentDetailViewDialog extends DialogFragment implements OnSwipListener, DialogConfirmationListener {
	
	private ArrayList<Content> ContentList;
	private long categoryLocalId;
	private int pointer;
	private TextView contentViewer;
	private TouchSwipeDetector swipDetector;
	private int[]imageViewIds={R.id.DeleteImageView,R.id.ExtendCategoryImageView
			,R.id.ChangeCategoryImageView,R.id.EditImageView
			,R.id.ShareImageView,R.id.SmsComposerImageView,
			R.id.leftArrowImageView,R.id.rightArrowImageView};
	private ImageView favoritImageView,likeImageView,viewImageView,shareImageView,SmsComposerImageView;
	private TextView likeTextView,viewTextView;
	private ContentFullDao contentFullDao;
	private ContentDirectChangeDao contentDirectChangeDao;
	private UserTableDao userTableDao;
	private ContentViewListFragment contentListFragment;
	private int TextColor=0, TextSize=0;
	private Typeface typeface;
	
	
	public ContentDetailViewDialog(ContentViewListFragment listFragment, ArrayList<Content> ContentList,long categoryLocalId, int pointer) {
	
		setRetainInstance(true);
		this.categoryLocalId=categoryLocalId;
		this.pointer=pointer;
		this.ContentList=ContentList;
		this.contentListFragment=listFragment;
		swipDetector=new TouchSwipeDetector(getActivity(), this);
		contentFullDao=new ContentFullDao(getActivity());
		contentDirectChangeDao = new ContentDirectChangeDao(getActivity());
		userTableDao=new UserTableDao(getActivity());
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view=inflater.inflate(R.layout.dialog_detail_content_view, null);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		contentViewer=(TextView)view.findViewById(R.id.ContentViewer);
		
		favoritImageView=(ImageView)view.findViewById(R.id.FavoritImageView);
		favoritImageView.setOnClickListener(onClickListener);
		likeImageView=(ImageView)view.findViewById(R.id.LikeImageView);
		likeImageView.setOnClickListener(onClickListener);
		likeTextView =(TextView)view.findViewById(R.id.LikeTextView);
		viewImageView=(ImageView)view.findViewById(R.id.ViewIconImageView);
		viewImageView.setOnClickListener(onClickListener);
		viewTextView =(TextView)view.findViewById(R.id.ViewTextView);
		
		shareImageView=(ImageView)view.findViewById(R.id.ShareImageView);
		SmsComposerImageView=(ImageView)view.findViewById(R.id.SmsComposerImageView);
		
		for(int i=0;i<imageViewIds.length;i++)
		{
			ImageView imageView=(ImageView)view.findViewById(imageViewIds[i]);
			imageView.setOnClickListener(onClickListener);
		}
		return view;
	}
	

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String textFont = shpref.getString("DETAILTEXTFONT", "BZar.ttf");
		TextColor = shpref.getInt("DETAILTEXTCOLOR",getResources().getColor(R.color.default__detail_text_color));
		TextSize = Integer.parseInt(shpref.getString("DETAILTEXTSIZE", "25"));
		typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/" + textFont);
		contentViewer.setTypeface(typeface);
		contentViewer.setTextSize(TextSize);
		contentViewer.setTextColor(TextColor);
		contentViewer.setOnTouchListener(swipDetector);
		
		loadContentPreview(pointer);
	}
	
	private void loadContentPreview(int pointer)
	{
		
		if(pointer<0 )
		{
			if(ContentList.size()>1)
				pointer=0;
		}
		
		//ContentList.size()-2 is the dummy object came from previous list activity
		if(pointer> ContentList.size()-2)
			contentListFragment.loadMoreToContentList();
		
		if(pointer> ContentList.size()-2)
		{
			if(ContentList.size()>1)
				pointer=ContentList.size()-2;
		}
		
		if(ContentList.size()<2){
			this.dismiss();
			return;
		}
		
		Content content=ContentList.get(pointer);
		addContentToView(pointer,content);
		
		
		likeTextView.setText(content.getLikedCounter()+"");
		viewTextView.setText(content.getViewedCounter()+"");
		
		this.pointer=pointer;
		
		if(content.isFavorite())
			favoritImageView.setImageDrawable(getResources().getDrawable(R.drawable.favorite));
		else
			favoritImageView.setImageDrawable(getResources().getDrawable(R.drawable.unfavorite));
		
		if(content.isLiked())
			likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.unlike));
		else
			likeImageView.setImageDrawable(getResources().getDrawable(R.drawable.like));
		
		if(content.isViewed())
			viewImageView.setImageResource(R.drawable.view_gray);
		else
			viewImageView.setImageResource(R.drawable.view_blue);
		
		contentViewed();
	}

	private void refreshContentInList()
	{
		Content changedContent=ContentList.remove(pointer);
		Content newContent=contentFullDao.get(changedContent.getId());
		if(newContent!=null &&	newContent.getContentTag().contains(";"+categoryLocalId+";") )
			ContentList.add(pointer, newContent);
		
		loadContentPreview(pointer);
		contentListFragment.notifyDataSetChanged(pointer);
	}

	private void addContentToView(int pointer, Content content) {
		
		SpannableStringBuilder builder = new SpannableStringBuilder();
		
		SpannableString  title = new SpannableString( (pointer+1)+"\r\n\r\n" );
		title.setSpan(new StyleSpan( Typeface.ITALIC ), 0, title.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		title.setSpan(new AlignmentSpan.Standard(Alignment.ALIGN_CENTER), 0, title.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		builder.append(title);
		
		SpannableString  contentString = new SpannableString( content.getPlainText());
		builder.append(contentString);
		
		 String creatorString="";
		    UserTable creatorUser=userTableDao.get(content.getCreatorUser());
		    if(creatorUser!=null && !creatorUser.getUsername().equalsIgnoreCase(""))
		    {
		    	creatorString=creatorUser.getUsername();
				creatorString="\r\n\r\n\r\nاز طرف "+creatorString;
				SpannableString  username = new SpannableString( creatorString );
				username.setSpan(new AlignmentSpan.Standard(Alignment.ALIGN_OPPOSITE), 0, username.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				builder.append(username);
		    }

		    
	    contentViewer.setText( builder, BufferType.SPANNABLE);
	}

	

	@Override
	public void swipeLeft() {
		handelAnim(true);
	}

	@Override
	public void swipeRight() {
		handelAnim(false);
	}
	
	private void handelAnim(final boolean SweepLeft)
	{
		AnimationSet outAnim=null;
		AnimationSet tmpInAnim=null;
		
		if(SweepLeft)
		{
			tmpInAnim=MenuAnimManager.getContentIncome(false, true);
			outAnim=MenuAnimManager.getContentIncome(false, false);
		}
		else
		{
			tmpInAnim=MenuAnimManager.getContentIncome(true, true);
			outAnim=MenuAnimManager.getContentIncome(true, false);
		}

		final AnimationSet inAnim=tmpInAnim;
	
		outAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				if(SweepLeft)
				{
					if(pointer+1 < ContentList.size())
						loadContentPreview(pointer+1);
				}
				else
				{
					if(pointer-1 > 0)
						loadContentPreview(pointer-1);
				}
				contentViewer.startAnimation(inAnim);
			}
			
		});
		contentViewer.startAnimation(outAnim);
	}

	OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			
			case R.id.EditImageView:
				editContentCliked();
				break;
			case R.id.DeleteImageView:
				deleteCliked();
				break;
			case R.id.ExtendCategoryImageView:
				addCategoryCliked();
				break;
			case R.id.ChangeCategoryImageView:
				changeCateogryCliked();
				break;
			case R.id.LikeImageView:
				likeCliked();
				break;
			case R.id.ViewIconImageView:
				ViewedCliked();
				break;
			case R.id.ShareImageView:
				shareCliked();
				break;
			case R.id.SmsComposerImageView:
				SmsComposerCliked();
				break;
			case R.id.FavoritImageView:
				favoriteCliked();
				break;
			case R.id.leftArrowImageView:
				swipeRight();
				break;
			case R.id.rightArrowImageView:
				swipeLeft();
				break;

			default:
				break;
			}
			
		}

		private void ViewedCliked() {

			Content content=ContentList.get(pointer);
			content.setViewed(!content.isViewed());
			content.setPlainText("");
			contentFullDao.update(content);
			if(content.isViewed())
			{
				animateImageButton(viewImageView, R.drawable.view_gray);
			}
			else
			{
				animateImageButton(viewImageView, R.drawable.view_blue);
			}
			
			//viewTextView.setText(content.getViewedCounter()+"");
		}
	};
	
	private void contentViewed()
	{
		Content content=ContentList.get(pointer);
		ContentDirectChange directChanege=contentDirectChangeDao.get(content.getId());
		if(directChanege==null)
		{
			directChanege=new ContentDirectChange();
			directChanege.setId(content.getId());
			directChanege.setLiked(0);
			directChanege.setViewd(1);
			contentDirectChangeDao.insert(directChanege);
		}
		
	}
	
	private void likeCliked()
	{
		Content content=ContentList.get(pointer);
		
		if(content.isLiked())
		{
			content.setLiked(false);
			animateImageButton(likeImageView, R.drawable.like);
			content.setLikedCounter(content.getLikedCounter()-1);
		}
		else
		{
			contentFullDao.contentLiked(content.getId());
			content.setLiked(true);
			animateImageButton(likeImageView, R.drawable.unlike);
			content.setLikedCounter(content.getLikedCounter()+1);
		}
		content.setPlainText("");
		contentFullDao.update(content);
		likeTextView.setText(content.getLikedCounter()+"");
		

	}
	private void favoriteCliked()
	{
		
		Content content=ContentList.get(pointer);
		if(content.isFavorite())
		{
			contentFullDao.contentMarkedAsUnFavorite(content.getId());
			content.setFavorite(false);
			animateImageButton(favoritImageView, R.drawable.unfavorite);
		}
		else
		{
			contentFullDao.contentMarkedAsFavorite(content.getId());
			content.setFavorite(true);
			animateImageButton(favoritImageView, R.drawable.favorite);
			
			contentFullDao.updateContentState(content.getId());
		}  

	}
	private void deleteCliked()
	{
		long contentId=ContentList.get(pointer).getId();
		SimpleOneInputDialog deletedialog=new SimpleOneInputDialog(contentId,SimpleOneInputDialog.TYPE.DELETE_CONTENT,this);
		deletedialog.show(getFragmentManager(), "delete dialog");
	}
	private void editContentCliked()
	{
		EditContentDialog editContntDialog=new EditContentDialog(ContentList.get(pointer),this);
		editContntDialog.show(getFragmentManager(), "subject");
	}
	private void changeCateogryCliked()
	{
		long contentId=ContentList.get(pointer).getId();
		 CategorySelectionDialog categorySelectionDialog= new CategorySelectionDialog(contentId,categoryLocalId,this);
		 categorySelectionDialog.show(getFragmentManager(), "change dialog");
	}
	private void addCategoryCliked()
	{
		long contentId=ContentList.get(pointer).getId();
		 CategorySelectionDialog categorySelectionDialog=
				 new CategorySelectionDialog(contentId);
		 categorySelectionDialog.show(getFragmentManager(), "add dialog");
		
	}
	private void shareCliked()
	{
		contentFullDao.updateContentState(ContentList.get(pointer).getId());
		
		animateImageButton(shareImageView, R.drawable.share);
		String text=ContentList.get(pointer).getPlainText();
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
	private void SmsComposerCliked()
	{
		contentFullDao.updateContentState(ContentList.get(pointer).getId());
		
		animateImageButton(SmsComposerImageView, R.drawable.compress);
		String text=ContentList.get(pointer).getPlainText();
		Intent sendIntent = new Intent();
		sendIntent.setAction("com.peaceworld.intent.WIKIPAYAMAK");
		sendIntent.putExtra("PREMIUMVERSION", true);
		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.setType("text/plain");
		try{
			startActivity(sendIntent);
		}catch(Exception ex)
		{
			try{
				Intent smsComposerInstallationIntent =new Intent(Intent.ACTION_VIEW,Uri.parse("http://cafebazaar.ir/app/?id=com.peaceworld.demo.smscomposer"));
				startActivity(smsComposerInstallationIntent);
			}catch(Exception ex2)
			{
				ex2.printStackTrace();
				//Toast.makeText(getActivity(),getResources().getString(R.string.smscomposer_installation_fail) , Toast.LENGTH_LONG).show();
			}
			
			
		}
	}
	
	private void animateImageButton(final ImageView imageView,
			final int finalStateImageId) {
		AnimationSet scaleOut= MenuAnimManager.getScaleOutAnimation();
		final AnimationSet scaleIn= MenuAnimManager.getScaleInAnimation();
		scaleOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				
				imageView.setImageDrawable(getResources().getDrawable(finalStateImageId));
				imageView.startAnimation(scaleIn);

			}
		});

		imageView.startAnimation(scaleOut);
	}

	@Override
	public void actionPerformed(boolean confirmed, TYPE actionType) {
		if(confirmed)
		{
			refreshContentInList();
		}
		
	}

	
}