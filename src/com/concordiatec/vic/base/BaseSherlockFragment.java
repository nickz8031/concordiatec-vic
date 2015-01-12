package com.concordiatec.vic.base;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragment;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.UserService;

public class BaseSherlockFragment extends SherlockFragment {
	protected BroadcastReceiver onlineReceiver;
	protected User getLoginUser(){
		return new UserService(getActivity()).getLoginUser();
	}
}

