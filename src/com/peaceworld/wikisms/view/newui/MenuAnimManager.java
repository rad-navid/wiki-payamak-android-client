package com.peaceworld.wikisms.view.newui;

import android.graphics.Canvas;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public class MenuAnimManager {
	
	public static CanvasTransformer getCanvasTransformer()
	{
		return getSlideCanvasTransformer();
	}
	
	private static CanvasTransformer getScaleCanvasTransformer()
	{
		CanvasTransformer scaleCanvasTransformer=new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, 1, 0, 0);
			}			
		};
		return scaleCanvasTransformer;
	}
	
	private static CanvasTransformer getZoomCanvasTransformer()
	{
		CanvasTransformer zoomCanvasTransformer=new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen*0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
			}
		};
		return zoomCanvasTransformer;
	}
	
	
	private static CanvasTransformer getSlideCanvasTransformer()
	{
		CanvasTransformer slideCanvasTransformer=new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.translate(0, canvas.getHeight()*(1-interp.getInterpolation(percentOpen)));
			}			
		};
		return slideCanvasTransformer;
	}
	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}		
	};
	
	
	public static  Animation getAnimation(View view)
	{
		
		//Animation animation = new AlphaAnimation(00f, 1.00f);
		//Animation animation = new TranslateAnimation(1.00f, -1.00f, 0.00f, 0.00f);
		Animation animation=new ScaleAnimation(0, 1, 0, 1);
		animation.setDuration(500);
		return animation;
	}
	
	public static  AnimationSet getSmsSentAnimation()
	{
		AnimationSet set=new AnimationSet(true);
		Animation alfaAnim = new AlphaAnimation(1.00f, 0.00f);
		alfaAnim.setDuration(500);
		set.addAnimation(alfaAnim);
		//Animation animation = new TranslateAnimation(1.00f, -1.00f, 0.00f, 0.00f);
		Animation scalAnim=new ScaleAnimation(1, 0, 1, 0);
		scalAnim.setDuration(500);
		set.addAnimation(scalAnim);
		
		return set;
	}
	
	public static  AnimationSet getScaleOutAnimation()
	{
		AnimationSet set=new AnimationSet(true);
		Animation alfaAnim = new AlphaAnimation(1.00f, 0.00f);
		alfaAnim.setDuration(500);
		set.addAnimation(alfaAnim);
		//Animation animation = new TranslateAnimation(1.00f, -1.00f, 0.00f, 0.00f);
		Animation scalAnim=new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
		scalAnim.setDuration(500);
		set.addAnimation(scalAnim);
		
		return set;
	}
	public static  AnimationSet getScaleInAnimation()
	{
		AnimationSet set=new AnimationSet(true);
		Animation alfaAnim = new AlphaAnimation(0.00f, 1.00f);
		alfaAnim.setDuration(500);
		set.addAnimation(alfaAnim);
		//Animation animation = new TranslateAnimation(1.00f, -1.00f, 0.00f, 0.00f);
		Animation scalAnim=new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
		scalAnim.setDuration(500);
		set.addAnimation(scalAnim);
		
		return set;
	}
	
	
	public static  AnimationSet getNotificationAnimation()
	{
		AnimationSet set=new AnimationSet(false);
		Animation alfaAnim = new AlphaAnimation(1.00f, 0.00f);
		alfaAnim.setDuration(1000);
		alfaAnim.setInterpolator(new AccelerateInterpolator());
		set.addAnimation(alfaAnim);
		
		Animation transferAnimation= new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,0.00f,TranslateAnimation.RELATIVE_TO_PARENT, -1.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f);
		transferAnimation.setDuration(1000);
		transferAnimation.setInterpolator(new AnticipateInterpolator());
		set.addAnimation(transferAnimation);
		
		return set;
	}
	
	public static  AnimationSet getContentIncome(boolean fromLeft, boolean income)
	{
		
		AnimationSet set=new AnimationSet(true);
		Animation alfaAnim ;//= new AlphaAnimation(1.00f, 0.00f);
		if(income)
			alfaAnim = new AlphaAnimation(0.00f, 1.00f);
		else
			alfaAnim = new AlphaAnimation(1.00f, 0.00f);
		alfaAnim.setDuration(1000);
		set.addAnimation(alfaAnim);
		Animation transferAnimation = null ;
		
		if(fromLeft && income)
			transferAnimation= new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,-1.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f);
		else if (fromLeft && !income)
			transferAnimation= new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,0.00f,TranslateAnimation.RELATIVE_TO_PARENT, 1.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f);
		else if (!fromLeft && income)
			transferAnimation= new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,1.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f);
		else if (!fromLeft && !income)
			transferAnimation= new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,0.00f,TranslateAnimation.RELATIVE_TO_PARENT, -1.00f,TranslateAnimation.RELATIVE_TO_PARENT, 0.00f, TranslateAnimation.RELATIVE_TO_PARENT, 0.00f);
		transferAnimation.setDuration(1000);
		set.addAnimation(transferAnimation);
		
		Animation scalAnim=null;
		if(income)
			scalAnim= new ScaleAnimation(0, 1, 0, 1f, Animation.RELATIVE_TO_PARENT, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
		else
			scalAnim= new ScaleAnimation(1, 0, 1, 0.0f, Animation.RELATIVE_TO_PARENT, (float)0.5, Animation.RELATIVE_TO_SELF, (float)0.5);
		scalAnim.setDuration(1000);
		set.addAnimation(scalAnim);
		
		return set;
	}
	

}
