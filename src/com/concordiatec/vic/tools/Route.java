package com.concordiatec.vic.tools;

import android.app.Activity;
import android.content.Intent;

public class Route {
	public static void moveTo( Activity from , Class<?> to ){
		moveTo(from, to, true);
	}
	public static void moveTo( Activity from , Class<?> to , boolean closeThis ){
		Intent intent = new Intent(from , to);
		from.startActivity(intent);
		if( closeThis ){
			from.finish();
		}
	}
}
