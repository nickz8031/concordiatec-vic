package com.concordiatec.vilnet.helper;

import java.io.Serializable;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.concordiatec.vilnet.constant.BroadKeys;

public class BroadHelper {

	public static void sendBoolBroad(Context context, String key, boolean bool){
		Intent intent = new Intent(BroadKeys.APP_BROAD_TAG);
		intent.putExtra(key, bool);
		context.sendBroadcast(intent);
	}

	public static void sendIntBroad(Context context, String key, int value){
		Intent intent = new Intent(BroadKeys.APP_BROAD_TAG);
		intent.putExtra(key, value);
		context.sendBroadcast(intent);
	}
	
	public static void sendSerializableBroad( Context context , String key , Serializable obj){
		Intent intent = new Intent(BroadKeys.APP_BROAD_TAG);
		intent.putExtra(key, obj);
		context.sendBroadcast(intent);
	}
	
	public static void destoryReceiver( Context context , BroadcastReceiver receiver ){
		context.unregisterReceiver(receiver);
	}
	
	public static void initBroadReceiver( Context context , BroadcastReceiver receiver ){
		IntentFilter filter = new IntentFilter(BroadKeys.APP_BROAD_TAG);
		context.registerReceiver(receiver, filter);
	}
	
	//로그인/로그아웃시 보내지는 broadcast 정보
	public static void sendOnlineBroad(Context context , boolean isOnline ){
		sendBoolBroad(context , BroadKeys.ONLINE_BROAD_INTENT_KEY_NAME, isOnline);
	}
	
}
