package com.concordiatec.vilnet.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class CustomViewFlipper extends ViewFlipper {
	private OnFlipListener onFlipListener;
	public static interface OnFlipListener {
		public void onShowPrevious(CustomViewFlipper flipper);
		public void onShowNext(CustomViewFlipper flipper);
	}
	public CustomViewFlipper(Context context) {
		super(context);
	}
	public CustomViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void showNext() {
		super.showNext();
		if( hasFlipListener() ){
			onFlipListener.onShowNext(this);
		}
	}
	
	@Override
	public void showPrevious() {
		super.showPrevious();
		if( hasFlipListener() ){
			onFlipListener.onShowPrevious(this);
		}
	}
	
	public void setOnFlipListener( OnFlipListener onFlipListener ){
		this.onFlipListener = onFlipListener;
	}
	
	private boolean hasFlipListener() {
	    return onFlipListener != null;
	}
	
}
