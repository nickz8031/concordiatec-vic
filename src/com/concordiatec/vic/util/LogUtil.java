package com.concordiatec.vic.util;

import android.util.Log;

public class LogUtil {
	private static final String TAG = "*----NICK.Z-----*";
	public static void show( String msg ){
		Log.e(TAG , "*------[ " + msg + " ]------*");
	}
	
	public static void show( String msg , String tag ){
		Log.e(tag , "*------[ " + msg + " ]------*");
	}
	
	public static void showDebug(String msg){
		Log.d(TAG , "*------[ " + msg + " ]------*");
	}
	
	public static void showDebug(String msg , String tag){
		Log.d(tag , "*------[ " + msg + " ]------*");
	}
	
	public static void showInfo(String msg){
		Log.i(TAG , "*------[ " + msg + " ]------*");
	}
	
	public static void showInfo(String msg , String tag){
		Log.i(tag , "*------[ " + msg + " ]------*");
	}
	
}
