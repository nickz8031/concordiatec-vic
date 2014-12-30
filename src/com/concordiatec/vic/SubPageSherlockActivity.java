package com.concordiatec.vic;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class SubPageSherlockActivity extends SherlockActivity {
	protected ImageView backButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(R.layout.subpage_custom_ab);
		actionBar.setDisplayShowCustomEnabled(true);
		
		backButton = (ImageView) actionBar.getCustomView().findViewById(R.id.actionbar_back);
		
		backButton.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		} );
		
	}
}
