package com.concordiatec.vilnet;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.concordiatec.vilnet.adapter.CouponsAdapter;
import com.concordiatec.vilnet.base.SubPageSherlockActivity;
import com.concordiatec.vilnet.constant.BroadKeys;
import com.concordiatec.vilnet.constant.Constant;
import com.concordiatec.vilnet.helper.BroadHelper;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.model.Coupon;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.service.CouponService;
import com.concordiatec.vilnet.util.LogUtil;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.ProgressUtil;
import com.google.gson.internal.LinkedTreeMap;

public class MyCouponActivity extends SubPageSherlockActivity implements OnRefreshListener {
	private BroadcastReceiver receiver;
	private ListView couponList;
	private CouponsAdapter adapter;
	private int clickedPosition;
	private Coupon clickedCoupon;
	private CouponService couponService;
	private List<Coupon> listData;
	private boolean isRefresh;
	private PullToRefreshLayout ptrLayout;
	private View noDataView;
	private boolean isNoMoreData;
	private boolean isLoadingNow;
	
	@Override
	protected void onDestroy() {
		BroadHelper.destoryReceiver(this, receiver);
		couponList = null;
		adapter = null;
		couponService = null;
		clickedCoupon = null;
		super.onDestroy();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycoupon);
		initService();
		initViews();
		getMyCoupons();
	}
	
	private void initService(){
		couponService = new CouponService(this);
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if( intent.hasExtra(BroadKeys.COUPON_CHANGE) ){
					Coupon coupon = (Coupon) intent.getSerializableExtra( BroadKeys.COUPON_CHANGE );
					adapter.couponStateChange(coupon);
				}
			}
		};
		BroadHelper.initBroadReceiver(this, receiver);
	}
	
	private void initViews(){
		couponList = (ListView) findViewById(R.id.coupon_list);
		couponList.setOnScrollListener(new ListViewScrollListener());
		couponList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				clickedPosition = position;
				clickedCoupon = adapter.getItem(id);
				Intent intent = new Intent(MyCouponActivity.this, CouponDetailActivity.class);
				intent.putExtra("coupon_id", clickedCoupon.getId());
				startActivity(intent);
			}
		});
		
		ptrLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(ptrLayout);
	}
	
	private void getMyCoupons(){
		ProgressUtil.show(this);
		couponService.getMyCoupons(new SimpleVicResponseListener(){
			@Override
			public void onSuccess(ResData data) {
				if (data == null || data.getData() == null) {
					NotifyUtil.toast(MyCouponActivity.this, getString(R.string.failed_to_request_data));
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
		});
	}
	/**
	 * 더 보기
	 */
	private void getMoreCoupons() {
		if (isNoMoreData) return;
		isLoadingNow = true;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", adapter.getLastRecordId() + "");
		couponService.getCoupons(new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				List<Coupon> tmpData = couponService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data.getData());
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
	 * adapter에 데이타 삽입
	 * 
	 * @param data
	 */
	private void setAdapterData(Object data) {
		if (data == null) return;
		listData = couponService.mapListToModelList((ArrayList<LinkedTreeMap<String, Object>>) data);
		if (!isRefresh || adapter == null) {
			adapter = new CouponsAdapter(this, listData);
			couponList.setAdapter(adapter);
		} else {
			adapter.setData(listData);
		}
		if (ptrLayout.isRefreshing()) {
			ptrLayout.setRefreshComplete();
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		refreshList();
	}
	
	private void refreshList(){
		if( noDataView != null ){
			couponList.removeHeaderView( noDataView );
			noDataView = null;
		}
		if (adapter != null) {
			adapter.clear();
		}
		this.isNoMoreData = false;
		this.isRefresh = true;
		getMyCoupons();
	}
	

	private void setNullView(){
		if( noDataView == null ){
			noDataView = LayoutInflater.from(this).inflate(R.layout.li_no_data, null);
			couponList.addHeaderView(noDataView);
			couponList.setAdapter(null);
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
