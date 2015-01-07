package com.concordiatec.vic.widget;


import com.concordiatec.vic.R;
import com.concordiatec.vic.util.Converter;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog {
	private String progressMsg;
    public CustomProgressDialog(Context context , String msg) {  
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.progressMsg = msg;
        this.setContentView(R.layout.custom_progress);
        ((TextView)findViewById(R.id.loadingImageText)).setText(progressMsg);
    }
    
    public CustomProgressDialog(Context context , boolean isCustom) {
    	this(context, "" ,isCustom);
    }
    
    public CustomProgressDialog(Context context , String msg , boolean isCustom) {
    	this(context, msg);
    	if( isCustom ){
    		this.findViewById(R.id.progress_view_group).setPadding(0, Converter.dip2px(context, 300), 0, 0);
    	}
    }
    
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {  
            dismiss();  
        }
    }

}
