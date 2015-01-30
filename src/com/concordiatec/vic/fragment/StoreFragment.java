package com.concordiatec.vic.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.SherlockFragment;
import com.concordiatec.vic.R;
import com.concordiatec.vic.util.LogUtil;

public class StoreFragment extends SherlockFragment{
	private View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		
		rootView = inflater.inflate(R.layout.frag_info, container, false);
		
		return rootView;
	}
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        	//doing request data
        }
    }
}
