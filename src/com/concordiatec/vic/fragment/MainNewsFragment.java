package com.concordiatec.vic.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.concordiatec.vic.adapter.MainNewsAdapter;
import com.concordiatec.vic.model.Article;
import com.concordiatech.vic.R;

public class MainNewsFragment extends SherlockFragment {

	private View rootView;
	
	private ListView newsListView;
	private List<Article> listData;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_main_news, container , false);
		
		newsListView = (ListView) rootView.findViewById(R.id.news_list);
		
		listData = new ArrayList<Article>();
		Article article = new Article();
		for (int i = 0; i < 15 ; i++) {
			listData.add(article);
		}
		
		newsListView.setAdapter(new MainNewsAdapter(getActivity() , listData));
		
		return rootView;
	}
	
}
