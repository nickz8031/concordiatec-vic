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
import com.concordiatec.vic.adapter.ArticlesAdapter;
import com.concordiatec.vic.base.BaseSherlockFragment;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.User;
import com.concordiatec.vic.service.ArticleListService;
import com.concordiatec.vic.util.AniUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.ArticleDetailActivity;
import com.concordiatec.vic.ArticleWriteActivity;
import com.concordiatec.vic.LoginActivity;
import com.concordiatec.vic.R;
import com.google.gson.internal.LinkedTreeMap;

public class ArticlesFragment extends BaseSherlockFragment implements OnRefreshListener {
	private View rootView;
	private ListView newsListView;
	private List<Article> listData;
	private PullToRefreshLayout ptrLayout;
	private TextView listHeaderPaddingView;
	private LinearLayout sortCurrentLayout;
	private RelativeLayout sortContentLayout;
	private TextView sortCurrentSelect;
	private ArticlesAdapter adapter;
	private ArticleListService aService;
	private LinearLayout writeButton;
	private int clickedPosition;
	private Article clickedArticle;
	private boolean isRefresh = false;
	public boolean isLoadingNow = false;

	/**
	 * 데이타 로드 시작
	 */
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        	aService = new ArticleListService(getActivity());
        	getArticles();
        }
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.frag_articles, container, false);
		
		// sign in/out broadcast receiver register
		IntentFilter filter = new IntentFilter(Constant.ONLINE_BROAD_ACTION);
		getActivity().registerReceiver(onlineReceiver, filter);
		
		this.initViews();
		return rootView;
	}
	/**
	 * login & logout broadcast receiver
	 */
	BroadcastReceiver onlineReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			refreshList();
		}
	};
	public boolean isNoMoreData;

	/**
	 * initialize views in fragment
	 */
	private void initViews() {
		this.initListView();
		this.initSortBar();
		
		//pull to refresh
		ptrLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().listener(this).setup(ptrLayout);
		
		//write button
		writeButton = (LinearLayout) rootView.findViewById(R.id.floating_write_layout);
		writeButton.setOnClickListener(new WriteButtonClickListener());
	}
	
	/**
	 * listview 초기화
	 */
	private void initListView() {
		newsListView = (ListView) rootView.findViewById(R.id.news_list);
		// add header padding view
		listHeaderPaddingView = new TextView(getActivity());
		int height = (int) getResources().getDimension(R.dimen.sort_display_height);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		listHeaderPaddingView.setLayoutParams(params);
		newsListView.addHeaderView(listHeaderPaddingView);
		newsListView.setOnScrollListener(new ListViewScrollListener());
		newsListView.setOnItemClickListener(new ListViewItemClickListener());
	}

	/**
	 * sort bar initialize
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
	
	/**
	 * 소식리스트 가져오기
	 */
	private void getArticles() {
		ProgressUtil.show(getActivity());
		aService.getArticles(new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				if (data == null || data.getData() == null) {
					NotifyUtil.toast(getActivity(), getString(R.string.failed_to_request_data));
				} else {
					setAdapterData(data.getData());
				}
				ProgressUtil.dismiss();
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if (Constant.DEBUG) {
					LogUtil.show("Status : " + httpResponseCode);
					LogUtil.show("Response Body : " + responseBody);
				}
				NotifyUtil.toast(getActivity(), getString(R.string.failed_to_request_data));
				ProgressUtil.dismiss();
				ptrLayout.setRefreshComplete();
			}
		});
	}

	/**
	 * adapter데이타 설정
	 * @param data
	 */
	private void setAdapterData(Object data) {
		if (data == null) return;
		listData = aService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data);
		if (!isRefresh || adapter == null) {
			adapter = new ArticlesAdapter(getActivity(), listData);
			newsListView.setAdapter(adapter);
		} else {
			adapter.setData(listData);
		}
		if (ptrLayout.isRefreshing()) {
			ptrLayout.setRefreshComplete();
		}
	}

	/**
	 * pull to refresh
	 */
	@Override
	public void onRefreshStarted(View view) {
		refreshList();
	}

	/**
	 * 새로 고침
	 */
	private void refreshList() {
		if (adapter != null) {
			adapter.clear();
		}
		this.isNoMoreData = false;
		this.isRefresh = true;
		getArticles();
	}

	/**
	 * 더보기 
	 */
	private void getMore() {
		if (isNoMoreData) return;
		isLoadingNow = true;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", adapter.getLastRecordId() + "");
		aService.getArticles(paramMap, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				List<Article> tmpData = aService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data.getData());
				adapter.addData(tmpData);
				isLoadingNow = false;
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if (Constant.DEBUG) {
					LogUtil.show(responseBody);
				}
				isLoadingNow = false;
			}

			@Override
			public void onEmptyResponse() {
				if (Constant.DEBUG) {
					super.onEmptyResponse();
				}
				isNoMoreData = true;
				isLoadingNow = false;
				if (ProgressUtil.isShowing()) {
					ProgressUtil.dismiss();
				}
				if (ptrLayout.isRefreshing()) {
					ptrLayout.setRefreshComplete();
				}
			}
		});
	}

	/**
	 * 정열바 클릭시 액션
	 * @author Nick.Z
	 *
	 */
	private final class SortClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			toggleSortbar();
			
		}
	}
	
	@Override
	public void backPressed(){
		if( isSortbarShowing() ){
			toggleSortbar();
		}
	}
	
	@Override
	public boolean backPressFlag(){
		return isSortbarShowing();
	}
	
	private boolean isSortbarShowing(){
		return ( sortContentLayout.getVisibility() == View.VISIBLE );
	}
	
	private void toggleSortbar(){
		AlphaAnimation animation;
		if ( !isSortbarShowing() ) {
			sortCurrentLayout.setBackgroundResource(R.drawable.sorting_top_style_active);
			sortContentLayout.startAnimation(AniUtil.fadeInAnimation());
			sortContentLayout.setVisibility(View.VISIBLE);
			setSortbarDrawableRight(R.drawable.drop_up_24);
		} else {
			sortCurrentLayout.setBackgroundResource(R.drawable.sorting_top_style);
			setSortbarDrawableRight(R.drawable.drop_down_24);
			animation = (AlphaAnimation) AniUtil.fadeOutAnimation();
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					sortContentLayout.setVisibility(View.GONE);
				}
			});
			sortContentLayout.startAnimation(animation);
		}
	}

	/**
	 * 리스트 아이템 클릭시 액션
	 * @author Nick.Z
	 */
	private final class ListViewItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			clickedPosition = position;
			clickedArticle = adapter.getItem(id);
			Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
			intent.putExtra("article_id", clickedArticle.getId());
			startActivityForResult(intent, Constant.DETAIL_ACTIVITY_REQUEST);
		}
	}

	/**
	 * 리스트 더보기
	 * @author Nick.Z
	 *
	 */
	private final class ListViewScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (isLoadingNow == false && view.getLastVisiblePosition() >= (adapter.getCount() / 2) && isNoMoreData == false) {
					getMore();
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}
	}

	/**
	 * sort바 아래로/위로 아이콘 치환
	 * @param resId
	 */
	protected void setSortbarDrawableRight(int resId) {
		sortCurrentSelect.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(resId), null);
	}

	/**
	 * MainActivity가 onAcitivity실행시 실행되는 메소드
	 * @param resultCode
	 * @param data
	 */
	public void responseResult(int resultCode, Intent data) {
		switch (resultCode) {
		case Constant.ARTICLE_EDIT_SUCCED:
			if (data.hasExtra("edit_article")) {
				String editContent = data.getStringExtra("edit_article");
				clickedArticle.setContent(editContent);
				adapter.updateData(clickedArticle, clickedPosition);
			}
			break;
		case Constant.ARTICLE_DELETE_SUCCED:
			adapter.deleteData(clickedPosition);
			break;
		default:
			break;
		}
	}

	/**
	 * 글쓰기버튼
	 * @author Nick.Z
	 *
	 */
	private final class WriteButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			User loginUser = getLoginUser();
			if (loginUser == null) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(getActivity(), ArticleWriteActivity.class);
				startActivity(intent);
			}
		}
	}
}
