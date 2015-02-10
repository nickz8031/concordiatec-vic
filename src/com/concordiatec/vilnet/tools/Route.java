package com.concordiatec.vilnet.tools;

import com.concordiatec.vilnet.LoginActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class Route {
	public static void moveTo( Activity from , Class<?> to ){
		moveTo(from, to, true);
	}
	public static void moveTo( Activity from , Class<?> to , boolean closeThis ){
		Intent intent = new Intent(from , to);
		from.startActivity(intent);
		if( closeThis ){
			((Activity)from).finish();
		}
	}
	
	public static void goLogin(Activity from , boolean isClose){
		moveTo(from, LoginActivity.class, isClose);
	}
	
	public static void goLogin(Context from){
		Intent intent = new Intent(from , LoginActivity.class);
		from.startActivity(intent);
	}
}
