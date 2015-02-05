package com.concordiatec.vic;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import com.actionbarsherlock.view.Menu;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.LoginActivity;
import com.concordiatec.vic.R;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.model.LocalUser;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.model.Shop;
import com.concordiatec.vic.model.ShopImage;
import com.concordiatec.vic.service.ShopDetailService;
import com.concordiatec.vic.service.ShopService;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.Converter;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.google.gson.internal.LinkedTreeMap;

public class ShopDetailActivity extends SubPageSherlockActivity implements OnClickListener {
	private int id;
	private int owner;
	private int group = 0;
	private int userId;
	private ArrayList<String> images = new ArrayList<String>();
	private String name;
	private String phone;
	private double lng = 0;
	private double lat = 0;
	private Shop shop;
	private LocalUser user;
	private TextView shopName;
	private TextView shopAddr;
	private TextView distance;
	private TextView score;
	private TextView textFav;
	private TextView imgCount;
	private LinearLayout shopDes;
	private ImageView shopPhoto;
	private RatingBar scoreRating;
	private ImageView favImg;
	private ImageView shopWallpaper;
	private LinearLayout cameraWrap;
	private LinearLayout callPhone;
	private LinearLayout shopFav;
	private LinearLayout shopGps;
	private LinearLayout shopScore;
	private ShopDetailService detailService;
	private ShopService shopService;

	@Override
	protected void onDestroy() {
		shop = null;
		user = null;
		shopName = null;
		shopAddr = null;
		distance = null;
		score = null;
		textFav = null;
		imgCount = null;
		shopDes = null;
		shopPhoto = null;
		scoreRating = null;
		favImg = null;
		shopWallpaper = null;
		cameraWrap = null;
		callPhone = null;
		shopFav = null;
		shopGps = null;
		shopScore = null;
		detailService = null;
		shopService = null;
		setContentView(R.layout.null_layout);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.shop_detail, menu);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zero_shop_act);
		init();
		getShop();
		initClick();
	}

	private void init() {
		id = getIntent().getIntExtra("shop_id", 0);
		if (id < 1) {
			NotifyUtil.toast(this, getString(R.string.error_request));
			finish();
		}
		detailService = new ShopDetailService(this);
		shopService = new ShopService(this);
		user = new UserService(this).getLoginUser();
		if (user == null) {
			userId = 0;
		} else {
			userId = user.usrId;
		}
		shopName = (TextView) findViewById(R.id.shopName);
		shopAddr = (TextView) findViewById(R.id.shopAddr);
		distance = (TextView) findViewById(R.id.distance);
		score = (TextView) findViewById(R.id.score);
		textFav = (TextView) findViewById(R.id.textFav);
		imgCount = (TextView) findViewById(R.id.imgCount);
		shopDes = (LinearLayout) findViewById(R.id.shopDes);
		shopPhoto = (ImageView) findViewById(R.id.shopPhoto);
		favImg = (ImageView) findViewById(R.id.favImg);
		scoreRating = (RatingBar) findViewById(R.id.shop_rating);
		shopWallpaper = (ImageView) findViewById(R.id.shopWallpaper);
		cameraWrap = (LinearLayout) findViewById(R.id.cameraWrap);
		callPhone = (LinearLayout) findViewById(R.id.callPhone);
		shopFav = (LinearLayout) findViewById(R.id.shopFav);
		shopGps = (LinearLayout) findViewById(R.id.shopGps);
		shopScore = (LinearLayout) findViewById(R.id.shopScore);
	}

	private void getShop() {
		ProgressUtil.show(this);
		detailService.getDetail(id, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				shop = shopService.mapToModel((LinkedTreeMap<String, Object>) data.getData());
				for (int i = 0; i < shop.getShopIntro().size(); i++) {
					TextView text = new TextView(ShopDetailActivity.this);
					LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					text.setLayoutParams(params);
					text.setTextSize(12);
					text.setPadding(0, 0, 0, Converter.dip2px(ShopDetailActivity.this, 5));
					text.setTextColor(getResources().getColor(R.color.normal_light_font));
					text.setText(shop.getShopIntro().get(i));
					shopDes.addView(text);
				}
				// get owner
				owner = shop.getShopUserId();
				// get group
				group = shop.getGroup().getId();
				// get phone number
				phone = shop.getShopPhone();
				
				if( shop.getImages() != null && shop.getImages().size() > 0 ){
					for (int i = 0; i < shop.getImages().size(); i++) {
						images.add(shop.getImages().get(i).getName());
					}
				}
				// set title
				name = shop.getShopUserName();
				setTitle(name);
				// set photo
				Glide.with(ShopDetailActivity.this).load(shop.getShopUserPhoto()).into(shopPhoto);
				// set name
				shopName.setText(shop.getShopUserName());
				// set address
				shopAddr.setText(shop.getShopAddr2());
				// set is like
				if (shop.isLike()) {
					textFav.setText("소식끊기");
					favImg.setImageResource(R.drawable.icon_unfav_128);
				} else {
					textFav.setText("소식받기");
					favImg.setImageResource(R.drawable.icon_fav_128);
				}
				// set distance
				lng = shop.getShopLng();
				lat = shop.getShopLat();
				distance.setText(shop.getDistance() + "");
				// set score
				score.setText((float) shop.getScore().getAvg() + "");
				// set rating stars
				scoreRating.setRating((float) shop.getScore().getAvg());
				// set wallpaper
				if (shop.getCoverImage() != null) {
					// get images
					imgCount.setText(Integer.toString(shop.getImages().size()));
					cameraWrap.setVisibility(View.VISIBLE);
					ShopImage imgObj = shop.getImages().get(0);
					Glide.with(ShopDetailActivity.this).load(imgObj.getName()).into(shopWallpaper);
				} else {
					shopWallpaper.setImageResource(R.drawable.nopic);
				}
				ProgressUtil.dismiss();
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if (Constant.DEBUG) {
					super.onFailure(httpResponseCode, responseBody);
				}
				ProgressUtil.dismiss();
			}

			@Override
			public void onEmptyResponse() {
				if (Constant.DEBUG) {
					super.onEmptyResponse();
				}
				ProgressUtil.dismiss();
			}
		});
	}

	private void initClick() {
		callPhone.setOnClickListener(this);
		shopFav.setOnClickListener(this);
		shopGps.setOnClickListener(this);
		shopScore.setOnClickListener(this);
		shopWallpaper.setOnClickListener(this);
		shopPhoto.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.callPhone:
			if (phone != null) {
				Intent i = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
				startActivity(i);
			}
			break;
		case R.id.shopFav:
			if (userId == 0) {
				Intent i = new Intent(this, LoginActivity.class);
				startActivity(i);
			} else {
				doFav();
			}
			break;
		case R.id.shopGps:
			if (lng != 0 && lat != 0) {
				Intent i = new Intent(this, ShopGpsActivity.class);
				Bundle b = new Bundle();
				b.putInt("group", group);
				b.putString("name", name);
				b.putDouble("lng", lng);
				b.putDouble("lat", lat);
				i.putExtra("data", b);
				startActivity(i);
			}
			break;
		case R.id.shopScore:
			if (shop != null) {
				Intent i = new Intent(this, ShopScoreActivity.class);
				i.putExtra("data", shop);
				startActivity(i);
			}
			break;
		case R.id.shopWallpaper:
			Intent intent = new Intent(this, ShopGridActivity.class);
			intent.putExtra("shop", shop);
			startActivity(intent);
			break;
		case R.id.shopPhoto:
			break;
		}
	}

	private void doFav() {
		ProgressUtil.show(this);
		shopService.likeShop(id, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				if (Tools.getIntValue(data.getData()) > 0) {
					favImg.setImageResource(R.drawable.icon_unfav_128);
					textFav.setText(getString(R.string.disconnect_news));
				} else {
					favImg.setImageResource(R.drawable.icon_fav_128);
					textFav.setText(getString(R.string.get_news));
				}
				ProgressUtil.dismiss();
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if( Constant.DEBUG ){
					super.onFailure(httpResponseCode, responseBody);
				}
				ProgressUtil.dismiss();
			}
		});
	}
}
