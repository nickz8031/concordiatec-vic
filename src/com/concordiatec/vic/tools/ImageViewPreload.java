package com.concordiatec.vic.tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ImageViewPreload {
	public static ImageViewPreload preload;
	private Context context;
	private float viewWidth;
	public ImageViewPreload( Context context ){
		this.context = context;
		this.viewWidth = 0;
	}
	
	private float getPixelWidth( float marginSize ){
		if( viewWidth == 0 ){
			WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
			Display display = windowManager.getDefaultDisplay();  
			DisplayMetrics dm = new DisplayMetrics();
			display.getMetrics(dm);
			viewWidth = dm.widthPixels - marginSize;
		}
		return viewWidth;
	}
	
	public float viewHeight( int imgW , int imgH , float viewMargin ){
		float w = getPixelWidth(viewMargin);
		float scale = w / imgW;
		return imgH * scale;
	}
	
	public float viewHeight( int imgW , int imgH ){
		return viewHeight(imgW , imgH , 0);
	}
}
