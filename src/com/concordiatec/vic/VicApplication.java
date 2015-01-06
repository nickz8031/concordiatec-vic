package com.concordiatec.vic;

import com.activeandroid.ActiveAndroid;
import android.app.Application;

public class VicApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		ActiveAndroid.initialize(this);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}
}
