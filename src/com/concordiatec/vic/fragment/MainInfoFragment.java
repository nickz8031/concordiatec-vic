package com.concordiatec.vic.fragment;


import java.util.ArrayList;
import java.util.List;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockFragment;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.service.ArticleService;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatech.vic.R;
import com.google.gson.internal.LinkedTreeMap;

public class MainInfoFragment extends SherlockFragment {
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main_info, container, false);
		return rootView;
	}

}
