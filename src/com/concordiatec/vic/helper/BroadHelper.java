package com.concordiatec.vic.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.concordiatec.vic.constant.Constant;

public class BroadHelper {
	
	public static BroadcastReceiver initOnlineBroadReciever( Context context , BroadcastReceiver receiver ) {
		IntentFilter filter = new IntentFilter(Constant.ONLINE_BROAD_ACTION);
		context.registerReceiver(receiver, filter);
		return receiver;
	}
	
	public static void sendOnlineBroad(Context context , boolean isOnline ){
		Intent intent = new Intent(Constant.ONLINE_BROAD_ACTION);
		intent.putExtra(Constant.ONLINE_BROAD_INTENT_KEY, isOnline);
		context.sendBroadcast(intent);
	}
	
	public static void destoryReceiver( Context context , BroadcastReceiver receiver ){
		context.unregisterReceiver(receiver);
	}
	
}
