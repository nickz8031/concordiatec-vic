package com.concordiatec.vic.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import com.concordiatec.vic.adapter.MainNewsAdapter;
import com.concordiatec.vic.base.BaseSherlockFragment;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.ArticleListService;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.util.AniUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.ArticleDetailActivity;
import com.concordiatec.vic.R;
import com.google.gson.internal.LinkedTreeMap;

public class MainNewsFragment extends BaseSherlockFragment implements OnRefreshListener {
	private View rootView;
	private LayoutInflater inflater;
	private ListView newsListView;
	private List<Article> listData;
	private PullToRefreshLayout ptrLayout;
	private TextView listHeaderPaddingView;
	private LinearLayout sortCurrentLayout;
	private RelativeLayout sortContentLayout;
	private TextView sortCurrentSelect;
	private MainNewsAdapter adapter;
	private ArticleListService aService;
	
	private boolean isRefresh = false;
	public boolean isLoadingNow = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		rootView = inflater.inflate(R.layout.fragment_main_news, container, false);
		this.initWidgets();
		
		//sign in/out broadcast receiver register
		IntentFilter filter = new IntentFilter(Constant.ONLINE_BROAD_ACTION); 
		getActivity().registerReceiver(onlineReceiver, filter);
		
		return rootView;
	}
	
	/**
	 * login & logout broadcast receiver
	 */
	BroadcastReceiver onlineReceiver = new BroadcastReceiver() { 
		@Override
		public void onReceive(Context context, Intent intent) {
			//1. refresh list
			refreshList();
		}
    };
    
	/**
	 * init widgets in main news fragment
	 */
	private void initWidgets(){
		aService = ArticleListService.single(getActivity());
		
		this.initListView();
		this.initPtrLayout();
		this.initSortBar();
		this.getArticles();
	}

	/**
	 * add header padding item into ListView
	 */
	private void initListView() {
		newsListView = (ListView) rootView.findViewById(R.id.news_list);
		//add header padding view
		listHeaderPaddingView = new TextView(getActivity());
		int height = (int) getResources().getDimension(R.dimen.sort_display_height);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		listHeaderPaddingView.setLayoutParams(params);
		newsListView.addHeaderView(listHeaderPaddingView);
		newsListView.setOnScrollListener(new ListViewScrollListener());
		newsListView.setOnItemClickListener(new ListViewItemClickListener());
	}

	private void getArticles(){
		Map<String, String> map = new HashMap<String, String>();

		User loginUser = new UserService(getActivity()).getLoginUser();
		if( loginUser != null ){
			map.put("user_id", loginUser.usrId+"");
		}
		ProgressUtil.show(getActivity());
		aService.getArticles(new VicResponseListener() {
			@Override
			public void onSuccess(Object data) {
				setAdapterData(data);
				ProgressUtil.dismiss();
			}

			@Override
			public void onFailure(String reason) {
				LogUtil.show(reason);
			}

			@Override
			public void onError(ResData error) {
			}
		} , map);
	}
	
	private void setAdapterData(Object data){
		listData = aService.mapListToModelList( (ArrayList<LinkedTreeMap<String,Object>>)data );
		if( !isRefresh ){
			adapter = new MainNewsAdapter(getActivity(), listData);
			newsListView.setAdapter(adapter);
		}else{
			adapter.setData(listData);
			ptrLayout.setRefreshComplete();
		}
	}
	
	/**
	 * initialize ActionBar pull to refresh widget
	 */
	private void initPtrLayout(){
		ptrLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().listener(this).setup(ptrLayout);
	}

	/**
	 * init sort bar
	 */
	private void initSortBar() {
		sortCurrentLayout = (LinearLayout) rootView.findViewById(R.id.sort_current_select_layout);
		sortCurrentSelect = (TextView) rootView.findViewById(R.id.sort_current_select);
		sortContentLayout = (RelativeLayout) rootView.findViewById(R.id.sort_content);
		sortCurrentLayout.setOnClickListener(new SortClickListener());
		sortContentLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	@Override
	public void onRefreshStarted(View view) {
		refreshList();
	}
	
	private void refreshList(){
		if( adapter != null ){
			adapter.clear();
			this.isRefresh = true;
			getArticles();
		}
	}
	
	private void getMore(){
		isLoadingNow = true;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("article_id", adapter.getLastRecordId()+"");
		aService.getArticles(new VicResponseListener() {
			@Override
			public void onSuccess(Object data) {
				List<Article> tmpData = aService.mapListToModelList( (ArrayList<LinkedTreeMap<String,Object>>)data );
				adapter.addData(tmpData);
				isLoadingNow = false;
			}
			@Override
			public void onError(ResData error) {isLoadingNow = false;}
			@Override
			public void onFailure(String reason) {isLoadingNow = false;}
		} , paramMap);
	}

	private final class SortClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			AlphaAnimation animation;
			if (sortContentLayout.getVisibility() == View.GONE) {
				v.setBackgroundResource(R.drawable.sorting_top_style_active);
				sortContentLayout.startAnimation(AniUtil.fadeInAnimation());
				sortContentLayout.setVisibility(View.VISIBLE);
				
				setSortbarDrawableRight( R.drawable.drop_up_24 );
			} else {
				v.setBackgroundResource(R.drawable.sorting_top_style);
				setSortbarDrawableRight( R.drawable.drop_down_24 );
				animation = (AlphaAnimation) AniUtil.fadeOutAnimation();
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {}
					@Override
					public void onAnimationRepeat(Animation animation) {}
					@Override
					public void onAnimationEnd(Animation animation) {
						sortContentLayout.setVisibility(View.GONE);
					}
				});
				sortContentLayout.startAnimation(animation);
			}
		}
	}
	
	private final class ListViewItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(getActivity() , ArticleDetailActivity.class);
			intent.putExtra("article_id", adapter.getItem(id).getId());
			startActivity(intent);
		}
		
	}
	private final class ListViewScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (isLoadingNow  == false && view.getLastVisiblePosition() >= (adapter.getCount()/2)) {
					getMore();
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}
	}
	
	protected void setSortbarDrawableRight(int resId){
		sortCurrentSelect.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(resId), null);
	}
}
