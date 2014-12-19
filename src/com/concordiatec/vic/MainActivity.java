package com.concordiatec.vic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import com.actionbarsherlock.view.Menu;
import com.concordiatec.vic.adapter.MainVpAdapter;
import com.concordiatec.vic.base.BaseSherlockFragmentActivity;
import com.concordiatec.vic.fragment.MainEventFragment;
import com.concordiatec.vic.fragment.MainInfoFragment;
import com.concordiatec.vic.fragment.MainNewsFragment;
import com.concordiatech.vic.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;

public class MainActivity extends BaseSherlockFragmentActivity {
	// private final String DEBUG_TAG = this.getClass().getSimpleName();
	private ViewPager mainVp;
	private List<Fragment> viewPagerViews;
	private int initFragmentPosition = 0;
	// tab underline
	private int[] tabLineIds = { R.id.tab_active_line_1, R.id.tab_active_line_2, R.id.tab_active_line_3 };
	// tab
	private int[] tabIds = { R.id.main_tab_select_1, R.id.main_tab_select_2, R.id.main_tab_select_3 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		runInit();
	}
	
	private void runInit() {
		getOverflowMenu();
		initPages();
	}
	
	/**
	 * overflow menu button not display problem
	 */
	private void getOverflowMenu() {
        try {
           ViewConfiguration config = ViewConfiguration.get(this);
           Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
           if(menuKeyField != null) {
               menuKeyField.setAccessible(true);
               menuKeyField.setBoolean(config, false);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	

	private void initPages() {
		viewPagerViews = new ArrayList<Fragment>();
		viewPagerViews.add(new MainNewsFragment());
		viewPagerViews.add(new MainEventFragment());
		viewPagerViews.add(new MainInfoFragment());
		mainVp = (ViewPager) findViewById(R.id.main_vp);
		mainVp.setAdapter(new MainVpAdapter(getSupportFragmentManager(), viewPagerViews));
		mainVp.setOffscreenPageLimit(2);
		this.setFragmentActive(initFragmentPosition);
		mainVp.setOnPageChangeListener(new MainVPChangeListener());
		for (int i = 0; i < tabIds.length; i++) {
			final int pos = i;
			findViewById(tabIds[i]).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setFragmentActive(pos);
				}
			});
		}
	}

	private void setFragmentActive(int pos) {
		for (int j = 0; j < viewPagerViews.size(); j++) {
			if (pos == j) {
				mainVp.setCurrentItem(j);
				findViewById(tabLineIds[j]).setVisibility(View.VISIBLE);
			} else {
				findViewById(tabLineIds[j]).setVisibility(View.GONE);
			}
		}
	}

	private final class MainVPChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			setFragmentActive(arg0);
		}
	}
}
