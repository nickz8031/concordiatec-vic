package com.concordiatec.vilnet.widget;


import com.concordiatec.vilnet.R;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog {
	private TextView msgContent;
    public CustomProgressDialog(Context context , String msg) {  
        super(context , android.R.style.Theme_Translucent_NoTitleBar);
        this.setContentView(R.layout.custom_progress);
        msgContent = (TextView)findViewById(R.id.loadingImageText);
        setText(msg);
    }   
    
    public void setText(CharSequence msg){
    	msgContent.setText(msg);
    }
    
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {  
            dismiss();  
        }
    }

}
