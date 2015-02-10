package com.concordiatec.vilnet;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.base.SubPageAlphaSherlockActivity;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.UniversalUtil;
import com.concordiatec.vilnet.widget.ZoomableImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ArticleImagesActivity extends SubPageAlphaSherlockActivity implements OnPageChangeListener {
	private List<String> imagesList;
	private List<View> viewImages;
	private ViewPager vpager;
	private DisplayImageOptions imageOptions;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageLoader.getInstance().init( UniversalUtil.getUniversalConfig(this) );
		imageOptions = UniversalUtil.getUniversalDisplayOption();
		
		Intent intent = getIntent();
		if( intent.hasExtra("photo_paths") ){
			imagesList = intent.getExtras().getStringArrayList("photo_paths");
			if( imagesList != null && imagesList.size() > 0 ){
				hideStatusBar(true);
				//toggleActionBar();
				setContentView(R.layout.activity_article_images);
				vpager = (ViewPager) findViewById(R.id.image_vp);
				
				viewImages = new ArrayList<View>();
				
				for (int i = 0; i < imagesList.size(); i++) {
					
					View v = LayoutInflater.from(this).inflate(R.layout.article_image_activity, null);
					ZoomableImageView iv = (ZoomableImageView) v.findViewById(R.id.galleryImg);
					ImageLoader.getInstance().displayImage( imagesList.get(i) , iv , imageOptions );
					iv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							toggleActionBar();
						}
					});
					viewImages.add(v);
				}
				setTitle("1/"+imagesList.size());
				vpager.setAdapter( new ImageAdapter() );
				vpager.setOnPageChangeListener(this);
			}
		}else{
			NotifyUtil.toast(this, getString(R.string.no_data));
			finish();
		}
	}
	
	private void toggleActionBar(){
		if( getSupportActionBar().isShowing() ){
			getSupportActionBar().hide();
		}else{
			getSupportActionBar().show();
		}
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
	
	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int current) {
		setTitle((current+1)+"/"+imagesList.size());
	}
	
	private final class ImageAdapter extends PagerAdapter{		
		@Override
		public int getCount() {
			return viewImages.size();
		}
		
		@Override
		public Object instantiateItem(View container, int position) {
			View tmpView = viewImages.get(position);
			if( tmpView!= null && tmpView.getParent()!=null ){
				((ViewGroup)tmpView.getParent()).removeView(tmpView);
			}
			( (ViewPager)container ).addView( viewImages.get(position) );
			return viewImages.get(position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			( (ViewPager)container ).removeView( viewImages.get(position) );
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return ( arg0 == arg1 );
		}
		
	}


	
}
