package com.concordiatec.vic.base;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragment;
import com.concordiatec.vic.model.LocalUser;
import com.concordiatec.vic.service.UserService;

public class BaseSherlockFragment extends SherlockFragment {
	protected BroadcastReceiver onlineReceiver;
	
	protected LocalUser getLoginUser(){
		return new UserService(getActivity()).getLoginUser();
	}
	/**
	 * call this method when back button pressed in parent activity
	 */
	public void backPressed(){}
	
	/**
	 * call this method before doing backPressed method
	 */
	public boolean backPressFlag(){
		return true;
	}
}

