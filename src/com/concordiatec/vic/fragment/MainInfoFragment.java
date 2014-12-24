package com.concordiatec.vic.fragment;


import java.util.List;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.actionbarsherlock.app.SherlockFragment;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.service.ArticleService;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatech.vic.R;

public class MainInfoFragment extends SherlockFragment {
	private View rootView;
	private Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main_info, container, false);
		btn = (Button) rootView.findViewById(R.id.click_post_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArticleService.single().getArticles(new VicResponseListener() {
					@Override
					public void onResponse(Object data) {
						LogUtil.show( data.toString() );
					}
				});
			}
		});
		return rootView;
	}

}
