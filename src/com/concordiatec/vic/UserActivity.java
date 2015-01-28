package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import com.concordiatec.vic.adapter.ArticlesAdapter;
import com.concordiatec.vic.base.SubPageSherlockFragmentActivity;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.service.ArticleListService;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.google.gson.internal.LinkedTreeMap;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class UserActivity extends SubPageSherlockFragmentActivity implements OnRefreshListener {

	private View headerView;
	private ListView listView;
	private ArticleListService articleListService;
	private PullToRefreshLayout ptrLayout;
	private List<Article> listData;
	private int[] tabLines = { R.id.user_tab_line_1, R.id.user_tab_line_2, R.id.user_tab_line_3, R.id.user_tab_line_4 };
	private int[] tabs = { R.id.user_tab_label_1 , R.id.user_tab_label_2 , R.id.user_tab_label_3 , R.id.user_tab_label_4 };
	
	//article variables
	private boolean isArticleRefresh;
	private ArticlesAdapter articlesAdapter;
	private boolean isArticlesLoadingNow;
	public int clickedArticlePosition;
	public Article clickedArticle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		articleListService = new ArticleListService(this);
		
		listView = (ListView) findViewById(R.id.user_article_list);
		headerView = getLayoutInflater().inflate(R.layout.user_header_wallpaper, null);
		
		listView.addHeaderView(headerView);
		listView.setOnScrollListener(new ListViewScrollListener());
		listView.setOnItemClickListener(new ListViewItemClickListener());
		
		initPtrLayout();
		getArticles();
		
		for (int i = 0; i < tabs.length; i++) {
			final int index = i;
			findViewById(tabs[i]).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					activeTabLine(index);
				}
			});
			
		}
		
		activeTabLine(0);
		
	}
	
	
	private void activeTabLine( int position ){
		for( int i = 0 ; i < tabLines.length; i++ ){
			if( i == position ){
				findViewById(tabLines[i]).setVisibility(View.VISIBLE);
			}else{
				findViewById(tabLines[i]).setVisibility(View.GONE);
			}
			
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		headerView = null;
		listView = null;
		articleListService = null;
		ptrLayout = null;
		listData = null;
		articlesAdapter = null;
		clickedArticle = null;
		tabLines = null;
		tabs = null;
	}
	

	/**
	 * initialize ActionBar pull to refresh widget
	 */
	private void initPtrLayout(){
		ptrLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(ptrLayout);
	}
	
	/**
	 * 소식가져오기
	 */
	private void getArticles(){
		ProgressUtil.show(this);
		articleListService.getArticles(new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				if( data == null || data.getData() == null ) {
					NotifyUtil.toast(UserActivity.this, getString(R.string.failed_to_request_data));
				}else{
					setArticlesAdapterData(data.getData());
				}
				ProgressUtil.dismiss();
			}
			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if( Constant.DEBUG ){
					LogUtil.show("Status : "+ httpResponseCode);
					LogUtil.show("Response Body : "+ responseBody);
				}
				NotifyUtil.toast(UserActivity.this, getString(R.string.failed_to_request_data));
				ProgressUtil.dismiss();
				ptrLayout.setRefreshComplete();
			}
		});
	}
	
	/**
	 * listview data setup
	 * @param data
	 */
	private void setArticlesAdapterData(Object data){
		if( data == null ) return;
		listData = articleListService.mapListToModelList( (ArrayList<LinkedTreeMap<String,Object>>)data );
		if( !isArticleRefresh || articlesAdapter == null ){
			articlesAdapter = new ArticlesAdapter(this , listData);
			listView.setAdapter(articlesAdapter);
		}else{
			articlesAdapter.setData(listData);
		}
		if( ptrLayout.isRefreshing() ){
			ptrLayout.setRefreshComplete();
		}
	}
	
	/**
	 * get more article data
	 */
	private void getMoreArticles(){
		isArticlesLoadingNow = true;
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", articlesAdapter.getLastRecordId()+"");
		
		articleListService.getArticles(paramMap , new SimpleVicResponseListener(){
			@Override
			public void onSuccess(ResData data) {
				List<Article> tmpData = articleListService.mapListToModelList( (ArrayList<LinkedTreeMap<String,Object>>)data.getData() );
				articlesAdapter.addData(tmpData);
				isArticlesLoadingNow = false;
			}
			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				NotifyUtil.toast(UserActivity.this, getString(R.string.no_more_data));
				isArticlesLoadingNow = false;
			}
			@Override
			public void onEmptyResponse() {
				if( Constant.DEBUG ){
					super.onEmptyResponse();
				}
				NotifyUtil.toast(UserActivity.this, getString(R.string.no_more_data));
				isArticlesLoadingNow = false;
				if( ProgressUtil.isShowing() ){
					ProgressUtil.dismiss();
				}
				if( ptrLayout.isRefreshing() ){
					ptrLayout.setRefreshComplete();
				}
			}
		});
	}
	
	/**
	 * 소식 리스트 더보기 
	 * @author Nick.Z
	 *
	 */
	private final class ListViewScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (isArticlesLoadingNow  == false && view.getLastVisiblePosition() >= (articlesAdapter.getCount()/2)) {
					getMoreArticles();
				}
				break;
			}
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}
	}
	

	/**
	 * 소식 리스트 아이템 클릭시 액션
	 * @author Nick.Z
	 *
	 */
	
	private final class ListViewItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			clickedArticlePosition = position;
			clickedArticle = articlesAdapter.getItem(id);
			Intent intent = new Intent(UserActivity.this , ArticleDetailActivity.class);
			intent.putExtra("article_id", clickedArticle.getId());
			startActivityForResult(intent , Constant.DETAIL_ACTIVITY_REQUEST);
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		refreshList();
	}
	
	private void refreshList(){
		if( articlesAdapter != null ){
			articlesAdapter.clear();
		}
		this.isArticleRefresh = true;
		getArticles();
	}
}
