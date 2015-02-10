package com.concordiatec.vilnet.util;

import com.concordiatec.vilnet.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.widget.Toast;

public class NotifyUtil {
	/*
	 * AlertDialog one button
	 */
	public static void alert(Context context, String message) {
		alert(context,message,"CLOSE");
	}

	private static void alert(Context context, String message, String btn) {
		new AlertDialog.Builder(context).setMessage(message).setPositiveButton(btn, null).show();	
	}
	
	/**
	 * alert dialog confirm
	 * @param context
	 * @param m
	 */
	public static Builder dialog( Context context , String title){
		return new AlertDialog.Builder(context).setTitle(title);
	}
	
	public static void showDialogWithPositive( Context context , String title , OnClickListener positiveListener ){
		dialog(context , title)
		.setPositiveButton(context.getString(R.string.yes), positiveListener)
		.setNegativeButton(context.getString(R.string.no), null)
		.show();
	}

	/*
	 * Toast
	 */
	public static void toast(Context context, String m){
		Toast.makeText(context ,m, Toast.LENGTH_SHORT).show();
	}
	/*
	 * Toast resource
	 */
	public static void toast(Context context, int resId){
		Toast.makeText(context , context.getString( resId ) , Toast.LENGTH_SHORT).show();
	}
	
	public static void toastCustom(Context context , String m , int topOffset){
		Toast toast = Toast.makeText(context ,m, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER|Gravity.TOP, 0, topOffset);
		toast.show();
	}
}