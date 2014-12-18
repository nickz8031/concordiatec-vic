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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.concordiatec.vic.adapter.MainNewsAdapter;
import com.concordiatec.vic.model.Article;
import com.concordiatec.vic.widget.ObScrollState;
import com.concordiatec.vic.widget.ObservableListView;
import com.concordiatec.vic.widget.ObservableScrollViewCallbacks;
import com.concordiatech.vic.R;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainNewsFragment extends SherlockFragment implements OnRefreshListener , ObservableScrollViewCallbacks {

	private View rootView;
	private LayoutInflater inflater;
	
	private ObservableListView newsListView;
	private List<Article> listData;
	private PullToRefreshLayout ptrLayout;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		rootView = inflater.inflate(R.layout.fragment_main_news, container , false);
		
		newsListView = (ObservableListView) rootView.findViewById(R.id.news_list);
		
		listData = new ArrayList<Article>();
		Article article = new Article();
		for (int i = 0; i < 15 ; i++) {
			listData.add(article);
		}
		
		newsListView.setAdapter(new MainNewsAdapter(getActivity() , listData));
		
		ptrLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().listener(this).setup(ptrLayout);
		
		this.addHeaderViewToListView();
		
		return rootView;
	}
	/**
	 * add header padding item into listview
	 */
	private void addHeaderViewToListView() {
		TextView listHeaderTextView = new TextView(getActivity());
		int height = (int) getResources().getDimension(R.dimen.sort_display_height);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, height);
		listHeaderTextView.setLayoutParams(params);
		newsListView.addHeaderView(listHeaderTextView);
	}
	
	@Override
	public void onRefreshStarted(View view) {
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
	
	//ObservableListView

    private View mHeaderView;
    private View mToolbarView;
    private int mBaseTranslationY;
	@Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbarView.getHeight();
            if (firstScroll) {
                float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
                if (-toolbarHeight < currentHeaderTranslationY && toolbarHeight < scrollY) {
                    mBaseTranslationY = scrollY;
                }
            }
            int headerTranslationY = Math.min(0, Math.max(-toolbarHeight, -(scrollY - mBaseTranslationY)));
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }
    @Override
	public void onUpOrCancelMotionEvent(ObScrollState scrollState) {
		mBaseTranslationY = 0;

        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (scrollState == ObScrollState.UP) {
            if (toolbarHeight < newsListView.getCurrentScrollY()) {
                if (headerTranslationY != -toolbarHeight) {
                    ViewPropertyAnimator.animate(mHeaderView).cancel();
                    ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
                }
            }
        } else if (scrollState == ObScrollState.DOWN) {
            if (toolbarHeight < newsListView.getCurrentScrollY()) {
                if (headerTranslationY != 0) {
                    ViewPropertyAnimator.animate(mHeaderView).cancel();
                    ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
                }
            }
        }
	}
	//end of ObservableListView
	
}
