package com.concordiatec.vic.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class NotifyUtil {
	/*
	 * AlertDialog
	 */
	public static void alert(Context context, String message) {
		alert(context,message,"CLOSE");
	}

	private static void alert(Context context, String message, String btn) {
		new AlertDialog.Builder(context).setMessage(message).setPositiveButton(btn, null).show();	
	}
	/*
	 * Toast
	 */
	public static void toast(Context context, String m){
		Toast.makeText(context ,m, Toast.LENGTH_SHORT).show();
	}
	
	public static void toastCustom(Context context , String m , int topOffset){
		Toast toast = Toast.makeText(context ,m, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER|Gravity.TOP, 0, topOffset);
		toast.show();
	}
}