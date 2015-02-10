package com.concordiatec.vilnet.util;
import com.concordiatec.vilnet.widget.CustomProgressDialog;
import android.app.Activity;
import android.content.Context;

public class ProgressUtil {
	public static CustomProgressDialog dialog;

	
	public static void show( Activity context ){
		show(context, "");
	}
	public static void show( Context context ){
		show(context, "");
	}
	public static void show(Activity context , String msg){
		dialog = new CustomProgressDialog(context, msg);
		dialog.show();
	}
	public static void show(Context context , String msg){
		dialog = new CustomProgressDialog(context, msg);
		dialog.show();
	}
	
	public static boolean isShowing(){
		if( dialog != null ){
			return dialog.isShowing();
		}else{
			return false;
		}
		
	}
	
	public static void setText(CharSequence text){
		if( dialog != null ){
			dialog.setText(text);
		}
		
	}
	
	public static void dismiss(){
		if( dialog!=null ){
			dialog.dismiss();
			dialog = null;
		}
	}
}
