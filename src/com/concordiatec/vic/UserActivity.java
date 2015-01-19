package com.concordiatec.vic;

import android.os.Bundle;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.util.LogUtil;

public class UserActivity extends SubPageSherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogUtil.show("Hello world!");
	}
}
