package com.peaceworld.wikisms.view.newui.pupup;

import com.peaceworld.wikisms.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopUpDialog {

	protected WindowManager mWindowManager;

	protected Context mContext;
	protected PopupWindow mWindow;

	private TextView mHelpTextView;
	private ImageView mUpImageView;
	private ImageView mDownImageView;
	protected View mView;

	protected Drawable mBackgroundDrawable = null;
	protected ShowListener showListener;

	public PopUpDialog(Context context, int viewResource) {
		mContext = context;
		mWindow = new PopupWindow(context);

		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setContentView(layoutInflater.inflate(viewResource, null));

		
		mUpImageView = (ImageView) mView.findViewById(R.id.arrow_up);
		mDownImageView = (ImageView) mView.findViewById(R.id.arrow_down);
		
	}

	public PopUpDialog(Context context) {
		this(context, R.layout.popup);
		mHelpTextView = (TextView) mView.findViewById(R.id.text);
		mHelpTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		mHelpTextView.setSelected(true);

	}

	public PopUpDialog(Context context, String text) {
		this(context);
		setText(text);
	}

	public void show(View anchor) {
		preShow();

		int[] location = new int[2];

		anchor.getLocationOnScreen(location);

		Rect anchorRect = new Rect(location[0], location[1], location[0]
				+ anchor.getWidth(), location[1] + anchor.getHeight());

		mView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		int rootHeight = mView.getMeasuredHeight();
		int rootWidth = mView.getMeasuredWidth();

		final int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		final int screenHeight = mWindowManager.getDefaultDisplay().getHeight();

		int yPos = anchorRect.top - rootHeight;

		boolean onTop = true;

		if (anchorRect.top < screenHeight / 2) {
			yPos = anchorRect.bottom;
			onTop = false;
		}

		int whichArrow, requestedX;

		whichArrow = ((onTop) ? R.id.arrow_down : R.id.arrow_up);
		requestedX = anchorRect.centerX();

		View arrow = whichArrow == R.id.arrow_up ? mUpImageView
				: mDownImageView;
		View hideArrow = whichArrow == R.id.arrow_up ? mDownImageView
				: mUpImageView;

		final int arrowWidth = arrow.getMeasuredWidth();

		arrow.setVisibility(View.VISIBLE);

		ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) arrow
				.getLayoutParams();

		hideArrow.setVisibility(View.INVISIBLE);

		int xPos = 0;

		// ETXTREME RIGHT CLIKED
		if (anchorRect.left + rootWidth > screenWidth) {
			xPos = (screenWidth - rootWidth);
		}
		// ETXTREME LEFT CLIKED
		else if (anchorRect.left - (rootWidth / 2) < 0) {
			// i have added 50 to prevent messing up
			xPos = anchorRect.left+50;
		}
		// INBETWEEN
		else {
			xPos = (anchorRect.centerX() - (rootWidth / 2));
		}

		param.leftMargin = (requestedX - xPos) - (arrowWidth / 2);

		if(mHelpTextView!=null)
		{
			if (onTop) {
				mHelpTextView.setMaxHeight(anchorRect.top - anchorRect.height());
	
			} else {
				mHelpTextView.setMaxHeight(screenHeight - yPos);
			}
		}
		mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);

		mView.setAnimation(AnimationUtils.loadAnimation(mContext,
				R.anim.float_anim));

	}

	protected void preShow() {
		if (mView == null)
			throw new IllegalStateException("view undefined");

		
		
		if (showListener != null) {
			showListener.onPreShow();
			showListener.onShow();
		}

		if (mBackgroundDrawable == null)
			mWindow.setBackgroundDrawable(new BitmapDrawable());
		else
			mWindow.setBackgroundDrawable(mBackgroundDrawable);


		setWindowsSize(mWindow);
		mWindow.setTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setOutsideTouchable(true);

		mWindow.setContentView(mView);
	}

	private void setWindowsSize(PopupWindow mWindow) {
		
		int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		int screenWidthDpi=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, screenWidth, mContext.getResources().getDisplayMetrics());
		int WidthPixels=200;
		if(screenWidthDpi<130)
			WidthPixels=160;
		else if(screenWidthDpi<200)
			WidthPixels=220;
		else if(screenWidthDpi<230)
			WidthPixels=245;
		else if(screenWidthDpi<350)
			WidthPixels=235;
		else if(screenWidthDpi<2200)
			WidthPixels=265;
		else 
			WidthPixels=325;
		
		int WidthDpi=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WidthPixels, mContext.getResources().getDisplayMetrics());
		mWindow.setWidth((int)WidthDpi);
		
		int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
		int screenHeightDpi=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, screenHeight, mContext.getResources().getDisplayMetrics());
		int HeightPixels=150;
		
		if(screenHeightDpi<200)
			HeightPixels=150;
		else if(screenHeightDpi<350)
			HeightPixels=200;
		else if(screenHeightDpi<1500)
			HeightPixels=235;
		else 
			HeightPixels=250;
		
		int HeightDpi=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HeightPixels, mContext.getResources().getDisplayMetrics());
		mWindow.setHeight((int)HeightDpi);
		
	}

	public void setBackgroundDrawable(Drawable background) {
		mBackgroundDrawable = background;
	}

	public void setContentView(View root) {
		mView = root;

		mWindow.setContentView(root);
	}

	public void setContentView(int layoutResID) {
		LayoutInflater inflator = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setContentView(inflator.inflate(layoutResID, null));
	}

	public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
		mWindow.setOnDismissListener(listener);
	}

	public void dismiss() {
		mWindow.dismiss();
		if (showListener != null) {
			showListener.onDismiss();
		}
	}

	public void setText(String text) {
		mHelpTextView.setText(text);
	}

	public static interface ShowListener {
		void onPreShow();
		void onDismiss();
		void onShow();
	}

	public void setShowListener(ShowListener showListener) {
		this.showListener = showListener;
	}
}
