package com.concordiatec.vic;

import java.util.List;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.constant.Constant;
import com.concordiatec.vic.listener.SimpleVicResponseListener;
import com.concordiatec.vic.model.Coupon;
import com.concordiatec.vic.model.ResData;
import com.concordiatec.vic.service.CouponService;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.util.LogUtil;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.util.ProgressUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.concordiatec.vic.widget.TagView;
import com.google.gson.internal.LinkedTreeMap;

public class ShopDetailActivity extends SubPageSherlockActivity {
	private int couponId;
	private CouponService couponService;
	private Coupon detailData;
	//가게명
	protected TextView shopNameView;
	//쿠폰명
	protected TextView couponNameView;
	//원가
	protected TextView listPriceView;
	//가격
	protected TextView priceView;
	//마감시간
	protected TextView couponEndView;
	//사용시간
	protected TextView usePeriodView;
	//쿠폰주의사항
	protected LinearLayout couponNoticeLayout;
	//아래측 가게이름
	protected TextView storeNameView;
	//아래측 가게 주소
	protected TextView storeAddrView;
	//가게 소개
	protected LinearLayout shopIntroLayout;
	protected CircleImageView couponImage;
	@Override
	protected void onDestroy() {
		couponService = null;
		detailData = null;
		shopNameView = null;
		couponNameView = null;
		listPriceView = null;
		priceView = null;
		couponEndView = null;
		usePeriodView = null;
		couponNoticeLayout = null;
		storeNameView = null;
		storeAddrView = null;
		shopIntroLayout = null;
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		couponId = getIntent().getIntExtra("coupon_id", 0);
		if( couponId == 0 ){
			notifyErrorRequest();
		}
		setTitle( getString(R.string.coupon_detail_title) );
		setContentView(R.layout.activity_coupon_detail);
		
		couponService = new CouponService(this);
		
		getDetail();
		
		
//		TagView couponTag = (TagView) findViewById(R.id.coupon_tag);
//		TagView.Tag tag = Tools.getTagLabel( "실시간상품" , getResources().getColor(R.color.theme_color));
//		
//		couponTag.setSingleTag(tag);
	}
	
	private void getDetail(){
		ProgressUtil.show(this);
		couponService.getCouponDetail( couponId , new SimpleVicResponseListener(){
			@Override
			public void onSuccess(ResData data) {
				detailData = couponService.mapToModel((LinkedTreeMap<String, Object>) data.getData());
				if( detailData == null ){
					notifyErrorRequest();
					return;
				}
				TagView couponTag = (TagView) findViewById(R.id.coupon_tag);
				TagView.Tag tag = Tools.getTagLabel( detailData.getKindName() , CouponService.getTagColor( ShopDetailActivity.this, detailData.getKind() ));
				couponTag.setSingleTag(tag);
				couponImage = (CircleImageView) findViewById(R.id.coupon_img);
				shopNameView = (TextView) findViewById(R.id.shop_name);
				couponNameView = (TextView) findViewById(R.id.coupon_title);
				listPriceView = (TextView) findViewById(R.id.coupon_list_price);
				priceView = (TextView) findViewById(R.id.coupon_price);
				couponEndView = (TextView) findViewById(R.id.coupon_end_time);
				usePeriodView = (TextView) findViewById(R.id.use_period);
				couponNoticeLayout = (LinearLayout) findViewById(R.id.coupon_notice_layout);
				storeNameView = (TextView) findViewById(R.id.store_name);
				storeAddrView = (TextView) findViewById(R.id.store_addr);
				shopIntroLayout = (LinearLayout) findViewById(R.id.shop_intro_layout);
				
				shopNameView.setText(detailData.getShop().getShopUserName());
				Glide.with(ShopDetailActivity.this).load(detailData.getImage()).crossFade().into(couponImage);
				storeNameView.setText(detailData.getShop().getShopUserName());
				storeAddrView.setText(detailData.getShop().getShopAddr1().length() > 0 ? detailData.getShop().getShopAddr1() : detailData.getShop().getShopAddr2());
				couponNameView.setText(detailData.getName());
				listPriceView.setText( detailData.getListPrice() + getString(R.string.unit_won) );
				priceView.setText( detailData.getPrice() + getString(R.string.unit_won) );
				couponEndView.setText(detailData.getEndTime() + " " + getString(R.string.event_end_label));
				usePeriodView.setText( detailData.getUseStart() + getString(R.string.period_link_string) + detailData.getUseEnd() );
				
				List<String> notice = detailData.getNotice();
				for (int i = 0; i < notice.size(); i++) {
					couponNoticeLayout.addView(getListTextView(notice.get(i)));
				}
				List<String> shopIntro = detailData.getShop().getShopIntro();
				for (int i = 0; i < shopIntro.size(); i++) {
					shopIntroLayout.addView( getListTextView(shopIntro.get(i)) );
				}
				ProgressUtil.dismiss();
			}
			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if( Constant.DEBUG ){
					super.onFailure(httpResponseCode, responseBody);
				}
				notifyErrorRequest();
			}
			@Override
			public void onEmptyResponse() {
				notifyErrorRequest();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		if(ProgressUtil.isShowing() ){
			return;
		}
		super.onBackPressed();
	}
	
	private TextView getListTextView( String words ){
		TextView tView = new TextView(this);
		tView.setText( getString(R.string.center_dot) + " " + words );
		tView.setTextColor(getResources().getColor(R.color.normal_light_font));
		return tView;
	}
	
	private void notifyErrorRequest(){
		NotifyUtil.toast(ShopDetailActivity.this, getString(R.string.error_request));
		finish();
	}
}
