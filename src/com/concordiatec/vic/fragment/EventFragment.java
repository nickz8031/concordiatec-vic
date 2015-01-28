package com.concordiatec.vic.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.concordiatec.vic.adapter.EventsAdapter;
import com.concordiatec.vic.base.BaseSherlockFragment;
import com.concordiatec.vic.model.Coupon;
import com.concordiatec.vic.util.AniUtil;
import com.concordiatec.vic.R;

public class EventFragment extends BaseSherlockFragment implements OnRefreshListener {

	private View rootView;
	private ListView eventListView;
	private List<Coupon> listData;
	private TextView listHeaderPaddingView;
	private PullToRefreshLayout ptrLayout;
	private LinearLayout sortCurrentLayout;
	private TextView sortCurrentSelect;
	private RelativeLayout sortContentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.frag_coupons, container , false);
		this.initWidgets();
		return rootView;
	}
	
	/**
	 * init widgets in main news fragment
	 */
	private void initWidgets(){
		this.initListView();
		this.initPtrLayout();
		this.initSortBar();
		

		
		
	}
	
	/**
	 * add header padding item into ListView
	 */
	private void initListView() {
		eventListView = (ListView) rootView.findViewById(R.id.news_list);
		listData = new ArrayList<Coupon>();
		Coupon coupon = new Coupon();
		for (int i = 0; i < 15; i++) {
			listData.add(coupon);
		}
		
		listHeaderPaddingView = new TextView(getActivity());
		int height = (int) getResources().getDimension(R.dimen.sort_display_height);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		listHeaderPaddingView.setLayoutParams(params);
		eventListView.addHeaderView(listHeaderPaddingView);
		
		eventListView.setAdapter(new EventsAdapter(getActivity(), listData));
		
		eventListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			}}
		);
		
		
	}
	
	/**
	 * initialize ActionBar pull to refresh widget
	 */
	private void initPtrLayout(){
		ptrLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().listener(this).setup(ptrLayout);
	}
	
	@Override
	public void onRefreshStarted(View view) {
		// demo
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ptrLayout.setRefreshComplete();
					}
				});
			}
		};
		timer.schedule(task, 3000);
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
	
	protected void setSortbarDrawableRight(int resId){
		sortCurrentSelect.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(resId), null);
	}
}
