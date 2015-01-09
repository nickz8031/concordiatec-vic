package com.concordiatec.vic.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.concordiatec.vic.R;
import com.concordiatec.vic.util.NotifyUtil;

public class SubPageSherlockActivity extends SherlockActivity {
	protected ImageView backButton;
	private TextView customTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(R.layout.actionbar_subpage);
		actionBar.setDisplayShowCustomEnabled(true);
		
		backButton = (ImageView) actionBar.getCustomView().findViewById(R.id.actionbar_back);
		customTitle = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_custom_title);
		backButton.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		} );
	}
	
	@Override
	public void setTitle(CharSequence title) {
		if( title.length() > 0 ){
			customTitle.setText(title);
			customTitle.setVisibility(View.VISIBLE);
		}
	}
	
	protected void noDataToast(Activity activity){
		NotifyUtil.toast(activity, getString(R.string.no_data));
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
