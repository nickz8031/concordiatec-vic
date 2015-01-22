package com.concordiatec.vic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.concordiatec.vic.adapter.MainVpAdapter;
import com.concordiatec.vic.base.BaseSherlockFragmentActivity;
import com.concordiatec.vic.fragment.MainEventFragment;
import com.concordiatec.vic.fragment.MainInfoFragment;
import com.concordiatec.vic.fragment.MainNewsFragment;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;

public class MainActivity extends BaseSherlockFragmentActivity {
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
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (new UserService(this).getLoginUser() == null) {
			menu.findItem(R.id.logout).setVisible(false);
		}
		return true;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout:
			logout();
			break;
		default:
			break;
		}
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
	private static int backFlagChangeSec = 0;
	private static int backFlagMaxSec = 2;
	private Timer backPressTimer;
	private TimerTask backPressTask;

	@Override
	public void onBackPressed() {
		if (backFlagChangeSec > 0 && backFlagChangeSec <= backFlagMaxSec) {
			closeApplication();
		} else {
			if (backPressTask != null && backPressTimer != null) {
				backPressTask.cancel();
				backPressTimer.cancel();
			}
			backFlagChangeSec = 0;
			backPressTimer = new Timer();
			backPressTask = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() { // UI thread
						@Override
						public void run() {
							backFlagChangeSec++;
						}
					});
				}
			};
			backPressTimer.schedule(backPressTask, 0, 1000);
			NotifyUtil.toast(this, getString(R.string.double_back_press));
		}
	}

	public void closeApplication() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
