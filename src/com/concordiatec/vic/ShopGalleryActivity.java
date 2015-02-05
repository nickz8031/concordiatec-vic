package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.model.Shop;
import com.concordiatec.vic.model.ShopImage;
import com.concordiatec.vic.widget.ZoomableImageView;

public class ShopGalleryActivity extends SubPageSherlockActivity  implements OnPageChangeListener{
	
	private int current = 0;
	
	private Shop shop;
	private int id;
	
	private ImageAdapter adapter;
	private List<View> views;
	private ViewPager pager;
	
	private TextView curr;
	private TextView total;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zero_shop_gallery);
		
		shop = (Shop) getIntent().getSerializableExtra("shop");
		id = getIntent().getIntExtra("img_id", 0);
		
		setTitle( shop.getShopUserName() );
		hideStatusBar(true);
		curr = (TextView) findViewById(R.id.curr);
		total = (TextView) findViewById(R.id.total);
		pager = (ViewPager) findViewById(R.id.pager);
		
		initViews();
	}
	private void hideStatusBar(boolean isHide) {
		if (isHide) {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			getWindow().setAttributes(lp);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().setAttributes(attr);
		}
	}
	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);
		
		views = new ArrayList<View>();
		
		for (int i = 0; i < shop.getImages().size(); i++) {
			ShopImage img = shop.getImages().get(i);
			if(id == img.getId()) current = i;
			
			View view = inflater.inflate(R.layout.zero_shop_gallery_item,null);
			
			ZoomableImageView imageView = (ZoomableImageView) view.findViewById(R.id.galleryImg);
			
			Glide.with(this).load(img.getName()).placeholder(R.drawable.nopic).into(imageView);
			
			views.add(view);
			
		}
		
		
		curr.setText(Integer.toString(current+1));
		total.setText(Integer.toString(views.size()));
		
		adapter = new ImageAdapter(views, this);
		
		pager.setOffscreenPageLimit(views.size()-1);
		pager.setAdapter(adapter);
		pager.setCurrentItem(current);
		
		pager.setOnPageChangeListener(this);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}


	@Override
	public void onPageSelected(int arg0) {
		curr.setText(Integer.toString(arg0+1));
	}
	
	
	/*
	 * adapter
	 */
	class ImageAdapter extends PagerAdapter{
		
		private List<View> views;
		
		public ImageAdapter(List<View> views, Activity context) {
			this.views = views;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position));
			return views.get(position);
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}
		
	}
	
}