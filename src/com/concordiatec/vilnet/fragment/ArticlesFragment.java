package com.concordiatec.vilnet.fragment;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.ArticleDetailActivity;
import com.concordiatec.vilnet.ArticleWriteActivity;
import com.concordiatec.vilnet.adapter.ArticlesAdapter;
import com.concordiatec.vilnet.base.BaseSherlockFragment;
import com.concordiatec.vilnet.constant.BroadKeys;
import com.concordiatec.vilnet.constant.Constant;
import com.concordiatec.vilnet.helper.BroadHelper;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.listener.VicSimpleAnimationListener;
import com.concordiatec.vilnet.model.Article;
import com.concordiatec.vilnet.model.Comment;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.service.ArticleListService;
import com.concordiatec.vilnet.tools.Route;
import com.concordiatec.vilnet.util.AniUtil;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.ProgressUtil;
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
	private boolean isLoadingNow = false;
	private boolean isFirst = true;
	public boolean isNoMoreData;
	private BroadcastReceiver receiver;
	
	@Override
	public void onDestroy() {
		newsListView = null;
		listData = null;
		ptrLayout = null;
		listHeaderPaddingView = null;
		sortContentLayout = null;
		sortContentLayout = null;
		sortCurrentSelect = null;
		adapter = null;
		aService = null;
		writeButton = null;
		clickedArticle = null;
		BroadHelper.destoryReceiver(getActivity(), receiver);
		receiver = null;
		
		super.onDestroy();
	}
	/**
	 * 데이타 로드 시작
	 */
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        	aService = new ArticleListService(getActivity());
        	if( isFirst ){
        		getArticles();
        		isFirst = false;
        	}
        	
        }
    }
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.frag_articles, container, false);
		
		// broadcast receiver register
		receiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) { 
				if( intent.hasExtra(BroadKeys.ARTICLE_CHANGE) ){ //글 수정
					Article article = (Article) intent.getSerializableExtra(BroadKeys.ARTICLE_CHANGE);
					adapter.articleStateChange(article);
				}else if( intent.hasExtra(BroadKeys.ARTICLE_DELETE) ){ //글 삭제
					adapter.deleteData(clickedPosition);
				}else if( intent.hasExtra(BroadKeys.COMMENT_CHANGE) ){ //댓글 수정
					Comment c = (Comment) intent.getSerializableExtra(BroadKeys.COMMENT_CHANGE);
					adapter.commentStateChanged( c );
				}else if( intent.hasExtra(BroadKeys.COMMENT_DELETE) ){ //댓글 삭제
					Comment c = (Comment) intent.getSerializableExtra(BroadKeys.COMMENT_DELETE);
					adapter.commentStateChanged( c , true );					
				}else if( intent.hasExtra(BroadKeys.COMMENT_ADD) ){//댓글 추가
					Comment c = (Comment) intent.getSerializableExtra(BroadKeys.COMMENT_ADD);
					adapter.commentAdd( c );					
				}else{
					refreshList();
				}
			}
		};
		
		BroadHelper.initBroadReceiver(getActivity(), receiver);
		
		this.initViews();
		return rootView;
	}
	

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
					super.onFailure(httpResponseCode, responseBody);
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
					super.onFailure(httpResponseCode, responseBody);
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
		return ( sortContentLayout != null && sortContentLayout.getVisibility() == View.VISIBLE );
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
			animation.setAnimationListener(new VicSimpleAnimationListener() {
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
			startActivity(intent);
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
	 * 글쓰기버튼
	 * @author Nick.Z
	 *
	 */
	private final class WriteButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			LocalUser loginUser = getLoginUser();
			if (loginUser == null) {
				Route.goLogin(getActivity());
			} else {
				Route.moveTo(getActivity(), ArticleWriteActivity.class);
			}
		}
	}
}
