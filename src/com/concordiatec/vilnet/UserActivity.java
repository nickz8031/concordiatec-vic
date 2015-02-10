package com.concordiatec.vilnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bumptech.glide.Glide;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.adapter.ArticlesAdapter;
import com.concordiatec.vilnet.base.SubPageSherlockFragmentActivity;
import com.concordiatec.vilnet.constant.BroadKeys;
import com.concordiatec.vilnet.constant.Constant;
import com.concordiatec.vilnet.helper.BroadHelper;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.model.Article;
import com.concordiatec.vilnet.model.LocalUser;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.service.ArticleListService;
import com.concordiatec.vilnet.service.UserService;
import com.concordiatec.vilnet.tools.Route;
import com.concordiatec.vilnet.util.LogUtil;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.ProgressUtil;
import com.concordiatec.vilnet.util.StringUtil;
import com.concordiatec.vilnet.widget.CircleImageView;
import com.google.gson.internal.LinkedTreeMap;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class UserActivity extends SubPageSherlockFragmentActivity implements OnRefreshListener {
	private View headerView;
	private ListView listView;
	private UserService userService;
	private ArticleListService articleListService;
	private PullToRefreshLayout ptrLayout;
	private List<Article> listData;
	private int[] tabLines = { R.id.user_tab_line_1, R.id.user_tab_line_2, R.id.user_tab_line_3, R.id.user_tab_line_4 };
	private int[] tabs = { R.id.user_tab_label_1, R.id.user_tab_label_2, R.id.user_tab_label_3, R.id.user_tab_label_4 };
	private CircleImageView profilePhoto;
	private TextView userIntro;
	private TextView userArticleCount;
	private TextView userAttentionCount;
	private View noDataView;
	// article variables
	private boolean isArticleRefresh;
	private ArticlesAdapter articlesAdapter;
	private boolean isArticlesLoadingNow;
	private boolean isNoMoreData = false;
	private int clickedArticlePosition;
	private Article clickedArticle;
	private int targetUserId;
	private HashMap<String, String> articleParams;
	protected LocalUser userData;
	private Button followBtn;
	private LocalUser loginUser;
	private BroadcastReceiver receiver;
	


	@Override
	protected void onDestroy() {
		headerView = null;
		listView = null;
		articleListService = null;
		ptrLayout = null;
		listData = null;
		articlesAdapter = null;
		clickedArticle = null;
		tabLines = null;
		tabs = null;
		profilePhoto = null;
		userIntro = null;
		followBtn = null;
		userArticleCount = null;
		userAttentionCount = null;
		setContentView(R.layout.null_layout);
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		// initialize services
		initServices();
		// initialize views
		initViews();
		// initialize data
		initData();
		// initialize extra options
		initExtra();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.user, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		LocalUser user = new UserService(this).getLoginUser();
		if( user== null || user.usrId!=targetUserId ) menu.findItem(R.id.menu_my_info).setVisible(false);
		if( user== null ) menu.findItem(R.id.logout).setVisible(false);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 새로 고침
		case R.id.menu_refresh:
			recreate();
			break;
		case R.id.menu_my_info:
			Route.moveTo(this, AccountActivity.class , false);
			break;
		case R.id.menu_help:
			break;
		case R.id.logout:
			userService.logout();
			break;
		default:
			break;
		}
		return true;
	}
	

	private void initServices() {
		articleListService = new ArticleListService(this);
		userService = new UserService(this);
		loginUser = userService.getLoginUser();
		// sign in/out broadcast receiver register

		receiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				if( intent.hasExtra(BroadKeys.ARTICLE_CHANGE) ){
					Article article = (Article) intent.getSerializableExtra(BroadKeys.ARTICLE_CHANGE);
					articlesAdapter.articleStateChange(article);
				}else if( intent.hasExtra(BroadKeys.ARTICLE_DELETE) ){
					articlesAdapter.deleteData(clickedArticlePosition);
				}else{
					if( userData!=null ){
						recreate();
					}
				}
			}
		};
		
		BroadHelper.initBroadReceiver(getApplicationContext(), receiver);
	}

	private void initViews() {
		listView = (ListView) findViewById(R.id.user_article_list);
		ptrLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
		headerView = getLayoutInflater().inflate(R.layout.user_header_wallpaper, null);
		
		profilePhoto = (CircleImageView) headerView.findViewById(R.id.user_profile_photo);
		userIntro = (TextView) headerView.findViewById(R.id.user_intro);
		userArticleCount = (TextView) headerView.findViewById(R.id.user_article_count);
		userAttentionCount = (TextView) headerView.findViewById(R.id.user_attention_store_count);
		
		followBtn = (Button) headerView.findViewById(R.id.follow_btn);
		
		listView.addHeaderView(headerView);
		listView.setOnScrollListener(new ListViewScrollListener());
		listView.setOnItemClickListener(new ListViewItemClickListener());
		
		ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(ptrLayout);
	}

	private void initData() {
		Intent intent = getIntent();
		targetUserId = intent.getIntExtra("user_id", 0);
		if (targetUserId == 0) {
			NotifyUtil.toast(this, getString(R.string.error_request));
			finish();
		}
		if( loginUser.usrId == targetUserId ){
			followBtn.setVisibility(View.GONE);
		}
		
		ProgressUtil.show(this);
		userService.getUserInfo(targetUserId, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				userData = userService.mapToModel((LinkedTreeMap<String, Object>) data.getData());
				if( StringUtil.isEmpty(userData.photo) ){
					Glide.with(UserActivity.this).load(R.drawable.ic_default_avatar).into(profilePhoto);
				}else{
					Glide.with(UserActivity.this).load(userData.photo).into(profilePhoto);
				}
				setTitle(userData.name);
				userIntro.setText( String.format(getString(R.string.default_user_introduce), userData.name) );
				ProgressUtil.dismiss();
//				userArticleCount.setText("0");
//				userAttentionCount.setText("0");
				
			}
		});
	}
	
	/**
	 * 소식 리스트 뽑기
	 */
	private void initArticleData(){
		articleParams = new HashMap<String, String>();
		articleParams.put("t_user", targetUserId + "");
		getArticles(articleParams);
	}

	private void initExtra() {
		for (int i = 0; i < tabs.length; i++) {
			final int index = i;
			findViewById(tabs[i]).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (index) {
					case 0:
						initArticleData();
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					default:
						break;
					}
					activeTabLine(index);
				}
			});
		}
		// set default active tab
		activeTabLine(0);
	}

	private void activeTabLine(int position) {
		for (int i = 0; i < tabLines.length; i++) {
			if (i == position) {
				findViewById(tabLines[i]).setVisibility(View.VISIBLE);
			} else {
				findViewById(tabLines[i]).setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 소식가져오기
	 */
	private void getArticles(Map<String, String> params) {
		ProgressUtil.show(this);
		articleListService.getArticles(params, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				if (data == null || data.getData() == null) {
					NotifyUtil.toast(UserActivity.this, getString(R.string.failed_to_request_data));
				} else {
					setArticlesAdapterData(data.getData());
				}
				ProgressUtil.dismiss();
			}
			
			@Override
			public void onEmptyResponse() {
				setNullView();
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if (Constant.DEBUG) {
					LogUtil.show("Status : " + httpResponseCode);
					LogUtil.show("Response Body : " + responseBody);
				}
				NotifyUtil.toast(UserActivity.this, getString(R.string.failed_to_request_data));
				ProgressUtil.dismiss();
				ptrLayout.setRefreshComplete();
			}
		});
	}
	
	private void setNullView(){
		if( noDataView == null ){
			noDataView = getLayoutInflater().inflate(R.layout.li_no_data, null);
			listView.addHeaderView(noDataView);
			listView.setAdapter(null);
			View retryBtn = noDataView.findViewById(R.id.no_data_retry_btn);
			retryBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					refreshList();
				}
			});
		}
		isNoMoreData = true;
		if( ptrLayout.isRefreshing() ){
			ptrLayout.setRefreshComplete();
		}
		
		if( ProgressUtil.isShowing() ){
			ProgressUtil.dismiss();
		}
	}
	
	/**
	 * listview data setup
	 * 
	 * @param data
	 */
	private void setArticlesAdapterData(Object data) {
		if (data == null) return;
		listData = articleListService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data);
		if (!isArticleRefresh || articlesAdapter == null) {
			articlesAdapter = new ArticlesAdapter(this, listData);
			listView.setAdapter(articlesAdapter);
		} else {
			articlesAdapter.setData(listData);
		}
		if (ptrLayout.isRefreshing()) {
			ptrLayout.setRefreshComplete();
		}
	}

	/**
	 * get more article data
	 */
	private void getMoreArticles() {
		if (isNoMoreData) return;
		isArticlesLoadingNow = true;
		articleParams.put("id", articlesAdapter.getLastRecordId() + "");
		articleListService.getArticles(articleParams, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				List<Article> tmpData = articleListService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data.getData());
				articlesAdapter.addData(tmpData);
				isArticlesLoadingNow = false;
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if (Constant.DEBUG) {
					LogUtil.show(responseBody);
				}
				isArticlesLoadingNow = false;
			}

			@Override
			public void onEmptyResponse() {
				if (Constant.DEBUG) {
					super.onEmptyResponse();
				}
				isNoMoreData = true;
				isArticlesLoadingNow = false;
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
	 * 소식 리스트 더보기
	 * 
	 * @author Nick.Z
	 *
	 */
	private final class ListViewScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if( scrollState == OnScrollListener.SCROLL_STATE_IDLE 
				&& isArticlesLoadingNow == false 
				&& !isNoMoreData 
				&& view.getLastVisiblePosition() >= (articlesAdapter.getCount() / 2) ){
				
				getMoreArticles();
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}
	}

	/**
	 * Article list item click listener
	 */
	private final class ListViewItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			clickedArticlePosition = position;
			clickedArticle = articlesAdapter.getItem(id);
			Intent intent = new Intent(UserActivity.this, ArticleDetailActivity.class);
			intent.putExtra("article_id", clickedArticle.getId());
			startActivityForResult(intent, Constant.DETAIL_ACTIVITY_REQUEST);
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		refreshList();
	}

	private void refreshList() {
		if (articlesAdapter != null) {
			articlesAdapter.clear();
		}
		this.isNoMoreData = false;
		this.isArticleRefresh = true;
		articleParams.remove("id");
		getArticles(articleParams);
	}
}
