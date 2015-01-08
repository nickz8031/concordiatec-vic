package com.concordiatec.vic.util;
import com.concordiatec.vic.widget.CustomProgressDialog;
import android.app.Activity;

public class ProgressUtil {
	public static CustomProgressDialog dialog;
	
	public static void show(Activity context , String msg){
		if( dialog == null ){
			dialog = new CustomProgressDialog(context, msg);
		}
		dialog.show();
	}
	
	public static void show( Activity context ){
		show(context, "");
	}
	
	public static void dismiss(){
		dialog.dismiss();
	}
}
