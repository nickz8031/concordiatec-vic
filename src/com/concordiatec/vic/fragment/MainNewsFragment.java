package com.concordiatec.vic.fragment;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.concordiatec.vic.adapter.MainNewsAdapter;
import com.concordiatec.vic.base.BaseSherlockFragment;
import com.concordiatec.vic.listener.VicResponseListener;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.service.ArticleService;
import com.concordiatec.vic.util.AniUtil;
import com.concordiatech.vic.R;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		rootView = inflater.inflate(R.layout.fragment_main_news, container, false);
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
		newsListView = (ListView) rootView.findViewById(R.id.news_list);
		//add header padding view
		listHeaderPaddingView = new TextView(getActivity());
		int height = (int) getResources().getDimension(R.dimen.sort_display_height);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		listHeaderPaddingView.setLayoutParams(params);
		newsListView.addHeaderView(listHeaderPaddingView);
	}
	
	private void getArticles(){
		ArticleService.single().getArticles(new VicResponseListener() {
			@Override
			public void onResponse(Object data) {
				
			}
		});
	}
	
	private void initAdapter(){
		newsListView.setAdapter(new MainNewsAdapter(getActivity(), listData));
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
