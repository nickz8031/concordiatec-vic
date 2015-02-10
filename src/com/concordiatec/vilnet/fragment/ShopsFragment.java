package com.concordiatec.vilnet.fragment;

import java.util.ArrayList;
import java.util.List;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.ShopDetailActivity;
import com.concordiatec.vilnet.adapter.ShopsAdapter;
import com.concordiatec.vilnet.base.BaseSherlockFragment;
import com.concordiatec.vilnet.constant.Constant;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.listener.VicSimpleAnimationListener;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.model.Shop;
import com.concordiatec.vilnet.service.ShopListService;
import com.concordiatec.vilnet.service.ShopService;
import com.concordiatec.vilnet.util.AniUtil;
import com.concordiatec.vilnet.util.LogUtil;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.ProgressUtil;
import com.google.gson.internal.LinkedTreeMap;

public class ShopsFragment extends BaseSherlockFragment implements OnRefreshListener{
	private View rootView;
	private PullToRefreshLayout ptrLayout;
	private ListView shopsListView;
	private TextView listHeaderPaddingView;
	private ShopsAdapter adapter;
	public int clickedPosition;
	public Shop clickedShop;
	public boolean isLoadingNow;
	public boolean isNoMoreData;
	private LinearLayout sortCurrentLayout;
	private TextView sortCurrentSelect;
	private RelativeLayout sortContentLayout;
	private ShopListService shopListService;
	private ShopService shopService;
	private boolean isRefresh;
	private List<Shop> listData;
	private boolean isFirst = true;
	private View noDataView;
	
	@Override
	public void onDestroy() {
		rootView = null;
		ptrLayout = null;
		shopsListView = null;
		listHeaderPaddingView = null;
		adapter = null;
		adapter = null;
		clickedShop = null;
		sortContentLayout = null;
		sortCurrentSelect = null;
		sortCurrentLayout = null;
		shopListService = null;
		shopService = null;
		listData = null;
		super.onDestroy();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.frag_stores, container, false);
		shopListService = new ShopListService(getActivity());
		shopService = new ShopService(getActivity());
		initViews();
		return rootView;
	}
	
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        	if( isFirst ) {
				initData();
				isFirst = false;
			}
        }
    }
	
	/**
	 * 페이지에 필요한 데이타 최기화, 서보통신 부분
	 */
	private void initData() {
		shopListService = new ShopListService(getActivity());
		getShops();
	}
	
	/**
	 * view 초기화
	 */
	private void initViews() {
		initListView();
		initSortBar();
	}
	
	/**
	 * add header padding item into ListView
	 */
	private void initListView() {
		ptrLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().listener(this).setup(ptrLayout);
		shopsListView = (ListView) rootView.findViewById(R.id.stores_list);
		listHeaderPaddingView = new TextView(getActivity());
		int height = (int) getResources().getDimension(R.dimen.sort_display_height);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		listHeaderPaddingView.setLayoutParams(params);
		shopsListView.addHeaderView(listHeaderPaddingView);
		shopsListView.setOnScrollListener(new ListViewScrollListener());
		shopsListView.setOnItemClickListener(new ListViewItemClickListener());
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
	
	public void getShops(){
		ProgressUtil.show(getActivity());
		shopListService.getShops(new SimpleVicResponseListener() {
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
			public void onEmptyResponse() {
				if( Constant.DEBUG ){
					super.onEmptyResponse();
				}
				setNullView();
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if (Constant.DEBUG) {
					LogUtil.show("Status : " + httpResponseCode);
					LogUtil.show("Response Body : " + responseBody);
				}
				NotifyUtil.toast(getActivity(), getString(R.string.failed_to_request_data));
				setNullView();
			}
		});
	}
	
	public void getMoreShops(){
		
	}
	
	private void setNullView(){
		if( noDataView == null ){
			noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.li_no_data, null);
			shopsListView.addHeaderView(noDataView);
			shopsListView.setAdapter(null);
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
	 * 새로 고침
	 */
	private void refreshList() {
		if( noDataView != null ){
			shopsListView.removeHeaderView( noDataView );
			noDataView = null;
		}
		if (adapter != null) {
			adapter.clear();
		}
		this.isNoMoreData = false;
		this.isRefresh = true;
		getShops();
	}
	/**
	 * adapter에 데이타 삽입
	 * 
	 * @param data
	 */
	private void setAdapterData(Object data) {
		if (data == null) return;
		listData = shopService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data);
		if (!isRefresh || adapter == null) {
			adapter = new ShopsAdapter(getActivity(), listData);
			shopsListView.setAdapter(adapter);
		} else {
			adapter.setData(listData);
		}
		if (ptrLayout.isRefreshing()) {
			ptrLayout.setRefreshComplete();
		}
	}
	
	/**
	 * back button pressed
	 */
	@Override
	public void backPressed() {
		if (isSortbarShowing()) {
			toggleSortbar();
		}
	}

	/**
	 * 뒤로 버튼 클릭시 선행 액션이 진행여부를 판단한다
	 * 
	 * @author nick.z
	 */
	@Override
	public boolean backPressFlag() {
		return isSortbarShowing();
	}
	
	/**
	 * 리스트 아이템 클릭시 액션
	 * 
	 * @author Nick.Z
	 */
	private final class ListViewItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			clickedPosition = position;
			clickedShop = adapter.getItem(id);
			Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
			intent.putExtra("shop_id", clickedShop.getId());
			startActivityForResult(intent, Constant.SHOP_DETAIL_ACTIVITY_REQUEST);
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
				if (adapter.getCount() > 15 && isLoadingNow == false && view.getLastVisiblePosition() >= (adapter.getCount() / 2) && isNoMoreData == false) {
					getMoreShops();
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}
	}
	
	private final class SortClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			toggleSortbar();
		}
	}
	
	/**
	 * sort바 숨김/보임
	 */
	private void toggleSortbar() {
		AlphaAnimation animation;
		if (!isSortbarShowing()) {
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
	 * sort바가 보여진 상태에 있는지 판단
	 * 
	 * @return
	 */
	private boolean isSortbarShowing() {
		return (sortContentLayout.getVisibility() == View.VISIBLE);
	}
	protected void setSortbarDrawableRight(int resId) {
		sortCurrentSelect.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(resId), null);
	}
}
