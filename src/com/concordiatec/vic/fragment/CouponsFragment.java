package com.concordiatec.vic.fragment;

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
import com.concordiatec.vic.adapter.CouponsAdapter;
import com.concordiatec.vic.base.BaseSherlockFragment;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.listener.VicSimpleAnimationListener;
import com.concordiatec.vic.model.Coupon;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.service.CouponService;
import com.concordiatec.vic.util.AniUtil;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.CouponDetailActivity;
import com.concordiatec.vic.R;
import com.google.gson.internal.LinkedTreeMap;

public class CouponsFragment extends BaseSherlockFragment implements OnRefreshListener {
	private View rootView;
	private ListView couponsListView;
	private List<Coupon> listData;
	private CouponService couponService;
	private TextView listHeaderPaddingView;
	private PullToRefreshLayout ptrLayout;
	private LinearLayout sortCurrentLayout;
	private TextView sortCurrentSelect;
	private RelativeLayout sortContentLayout;
	private CouponsAdapter adapter;
	private boolean isNoMoreData;
	private boolean isRefresh;
	public int clickedPosition;
	public Coupon clickedCoupon;
	public boolean isLoadingNow;
	
	private boolean isFirst = true;
	private View noDataView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.frag_coupons, container, false);
		this.initViews();
		return rootView;
	}

	/**
	 * view 초기화
	 */
	private void initViews() {
		initListView();
		initSortBar();
	}

	/**
	 * 데이타 로드 시작
	 */
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
		couponService = new CouponService(getActivity());
		getCoupons();
	}

	/**
	 * add header padding item into ListView
	 */
	private void initListView() {
		ptrLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().listener(this).setup(ptrLayout);
		couponsListView = (ListView) rootView.findViewById(R.id.coupons_list);
		listHeaderPaddingView = new TextView(getActivity());
		int height = (int) getResources().getDimension(R.dimen.sort_display_height);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		listHeaderPaddingView.setLayoutParams(params);
		couponsListView.addHeaderView(listHeaderPaddingView);
		couponsListView.setOnScrollListener(new ListViewScrollListener());
		couponsListView.setOnItemClickListener(new ListViewItemClickListener());
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

	/**
	 * adapter에 데이타 삽입
	 * 
	 * @param data
	 */
	private void setAdapterData(Object data) {
		if (data == null) return;
		listData = couponService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data);
		if (!isRefresh || adapter == null) {
			adapter = new CouponsAdapter(getActivity(), listData);
			couponsListView.setAdapter(adapter);
		} else {
			adapter.setData(listData);
		}
		if (ptrLayout.isRefreshing()) {
			ptrLayout.setRefreshComplete();
		}
	}

	/**
	 * 서버로부터 데이타 요청
	 */
	private void getCoupons() {
		ProgressUtil.show(getActivity());
		couponService.getCoupons(new SimpleVicResponseListener() {
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
	private void setNullView(){
		if( noDataView == null ){
			noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.li_no_data, null);
			couponsListView.addHeaderView(noDataView);
			couponsListView.setAdapter(null);
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
	 * 더 보기
	 */
	private void getMoreCoupons() {
	}

	/**
	 * 새로 고침
	 */
	private void refreshList() {
		if( noDataView != null ){
			couponsListView.removeHeaderView( noDataView );
			noDataView = null;
		}
		if (adapter != null) {
			adapter.clear();
		}
		this.isNoMoreData = false;
		this.isRefresh = true;
		getCoupons();
	}

	private final class SortClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			toggleSortbar();
		}
	}

	/**
	 * refresh list
	 */
	@Override
	public void onRefreshStarted(View view) {
		refreshList();
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
	 * sort바가 보여진 상태에 있는지 판단
	 * 
	 * @return
	 */
	private boolean isSortbarShowing() {
		return (sortContentLayout.getVisibility() == View.VISIBLE);
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

	protected void setSortbarDrawableRight(int resId) {
		sortCurrentSelect.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(resId), null);
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
			clickedCoupon = adapter.getItem(id);
			Intent intent = new Intent(getActivity(), CouponDetailActivity.class);
			intent.putExtra("coupon_id", clickedCoupon.getId());
			startActivityForResult(intent, Constant.COUPON_DETAIL_ACTIVITY_REQUEST);
		}
	}

	/**
	 * 리스트 더보기
	 * 
	 * @author Nick.Z
	 *
	 */
	private final class ListViewScrollListener implements OnScrollListener {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (adapter.getCount() > 15 && isLoadingNow == false && view.getLastVisiblePosition() >= (adapter.getCount() / 2) && isNoMoreData == false) {
					getMoreCoupons();
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		}
	}
}
