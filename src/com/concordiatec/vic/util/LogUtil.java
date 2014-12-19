package com.concordiatec.vic.util;

import android.util.Log;

public class LogUtil {
	private static final String TAG = "*----NICK.Z-----*";
	public static void show( String msg ){
		Log.e(TAG , "*------[ " + msg + " ]------*");
	}
	
	public static void show( int msg ){
		LogUtil.show( msg + "");
	}
}
