package com.concordiatec.vic.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.concordiatec.vic.LoginActivity;
import com.concordiatec.vic.R;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;

public class SubPageSherlockFragmentActivity extends SherlockFragmentActivity {
	protected ImageView backButton;
	private TextView customTitle;
	
	protected final static int REQUEST_TAKE_PHOTO = 30001;
	protected final static int REQUEST_CHOOSE_PHOTO = 30002;
	protected final static int REQUEST_TAKE_SURE = 30003;
	
	
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
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void setTitle(CharSequence title) {
		if (title.length() > 0) {
			customTitle.setText(title);
			customTitle.setVisibility(View.VISIBLE);
		}
	}

	protected void noDataToast(Activity activity) {
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

	protected User checkLogin(Activity activity) {
		User loginUser = new UserService(activity).getLoginUser();
		if (loginUser == null) {
			Intent intent = new Intent(activity, LoginActivity.class);
			startActivityForResult(intent, 0);
			return null;
		} else {
			return loginUser;
		}
	}
}
