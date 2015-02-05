package com.concordiatec.vic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity {
	private static final int DELAY = 2000;
    private Handler mMainHandler = new Handler() {  
	    @Override  
	    public void handleMessage(Message msg) {  
	        Intent intent = new Intent(Intent.ACTION_MAIN); 
	        intent.setClass(getApplication(), MainActivity.class);  
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	        startActivity(intent);
	    }  
    };  
      
    @Override  
    public void onCreate(Bundle icicle) {  
	    super.onCreate(icicle);  
	    getWindow().setBackgroundDrawableResource(R.drawable.splash);  
	    mMainHandler.sendEmptyMessageDelayed(0, DELAY);
    } 
} 
