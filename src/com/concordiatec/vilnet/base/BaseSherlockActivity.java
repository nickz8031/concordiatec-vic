package com.concordiatec.vilnet.base;

import java.util.Timer;
import java.util.TimerTask;
import com.actionbarsherlock.app.SherlockActivity;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.util.NotifyUtil;

public class BaseSherlockActivity extends SherlockActivity {
	private static int backFlagChangeSec = 0;
    private static int backFlagMaxSec = 2;
    private Timer backPressTimer;  
    private TimerTask backPressTask;
	@Override
    public void onBackPressed() {
		if( backFlagChangeSec > 0 && backFlagChangeSec <= backFlagMaxSec ){
			closeApplication();
		}else{
			if( backPressTask != null && backPressTimer!=null ){
				backPressTask.cancel();
	        	backPressTimer.cancel();
			}
        	backFlagChangeSec = 0;
			backPressTimer = new Timer();
			backPressTask = new TimerTask() {  
		        @Override  
		        public void run() {
		            runOnUiThread(new Runnable() {      // UI thread  
		                @Override  
		                public void run() {
		                    backFlagChangeSec++;
		                }  
		            });  
		        }  
		    };
			backPressTimer.schedule(backPressTask, 0 , 1000);
			NotifyUtil.toast( this , getString(R.string.double_back_press) );
		}
		
    }
	
	public void closeApplication() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
    }
}
