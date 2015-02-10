package com.concordiatec.vilnet;

import java.util.List;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.base.SubPageSherlockActivity;
import com.concordiatec.vilnet.constant.BroadKeys;
import com.concordiatec.vilnet.constant.Constant;
import com.concordiatec.vilnet.constant.ResponseStatus;
import com.concordiatec.vilnet.helper.BroadHelper;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.listener.VicSimpleAnimationListener;
import com.concordiatec.vilnet.model.Coupon;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.service.CouponService;
import com.concordiatec.vilnet.service.UserService;
import com.concordiatec.vilnet.tools.Route;
import com.concordiatec.vilnet.tools.Tools;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.ProgressUtil;
import com.concordiatec.vilnet.widget.CircleImageView;
import com.concordiatec.vilnet.widget.Rotate3dAnimation;
import com.concordiatec.vilnet.widget.TagView;
import com.google.gson.internal.LinkedTreeMap;

public class CouponDetailActivity extends SubPageSherlockActivity implements OnClickListener {
	private int couponId;
	private CouponService couponService;
	private Coupon detailData;
	// 가게명
	private TextView shopNameView;
	// 쿠폰명
	private TextView couponNameView;
	// 원가
	private TextView listPriceView;
	// 가격
	private TextView priceView;
	// 마감시간
	private TextView couponEndView;
	// 사용시간
	private TextView usePeriodView;
	// 쿠폰주의사항
	private LinearLayout couponNoticeLayout;
	// 아래측 가게 레이아웃
	private RelativeLayout storeInfoLayout;
	// 아래측 가게이름
	private TextView storeNameView;
	// 아래측 가게 주소
	private TextView storeAddrView;
	// 가게 소개
	private LinearLayout shopIntroLayout;
	// 쿠폰 이미지
	private CircleImageView couponImage;
	private TextView couponCtrlBtn;
	private ImageButton couponLikeBtn;
	private TagView couponTag;
	// 앞면
	private RelativeLayout couponTopFrontLayout;
	// 뒷면
	private LinearLayout couponTopBackLayout;
	private TextView couponReturn;
	private TextView couponCancelUse;
	private TextView couponConfirmUse;

	private View mStartAnimView = null;
	private View mContainer = null;
	private int mDuration = 200;
	private float mCenterX = 0.0f;
	private float mCenterY = 0.0f;
	private float mDepthZ = 0.0f;
	private int mIndex = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		couponId = getIntent().getIntExtra("coupon_id", 0);
		if (couponId == 0) {
			notifyErrorRequest();
		}
		setTitle(getString(R.string.coupon_detail_title));
		setContentView(R.layout.activity_coupon_detail);
		couponService = new CouponService(this);
		initViews();
		initClickListener();
		getDetail();
	}

	private void initViews() {
		couponImage = (CircleImageView) findViewById(R.id.coupon_img);
		shopNameView = (TextView) findViewById(R.id.shop_name);
		couponNameView = (TextView) findViewById(R.id.coupon_title);
		listPriceView = (TextView) findViewById(R.id.coupon_list_price);
		priceView = (TextView) findViewById(R.id.coupon_price);
		couponEndView = (TextView) findViewById(R.id.coupon_end_time);
		usePeriodView = (TextView) findViewById(R.id.use_period);
		couponNoticeLayout = (LinearLayout) findViewById(R.id.coupon_notice_layout);
		storeInfoLayout = (RelativeLayout) findViewById(R.id.store_info_layout);
		storeNameView = (TextView) findViewById(R.id.store_name);
		storeAddrView = (TextView) findViewById(R.id.store_addr);
		shopIntroLayout = (LinearLayout) findViewById(R.id.shop_intro_layout);
		couponCtrlBtn = (TextView) findViewById(R.id.coupon_ctrl_btn);
		couponLikeBtn = (ImageButton) findViewById(R.id.coupon_like_btn);
		couponTag = (TagView) findViewById(R.id.coupon_tag);
		couponTopFrontLayout = (RelativeLayout) findViewById(R.id.coupon_top_front);
		couponTopBackLayout = (LinearLayout) findViewById(R.id.coupon_top_back);
		couponReturn = (TextView) findViewById(R.id.return_coupon);
		couponCancelUse = (TextView) findViewById(R.id.coupon_cancel_use);
		mContainer = findViewById(R.id.coupon_top_info_frame);
		couponConfirmUse = (TextView) findViewById(R.id.coupon_confirm_use);
		mStartAnimView = couponTopFrontLayout;
	}
	@Override
	public void onDestroy() {
		couponService = null;
		detailData = null;
		shopNameView = null;
		couponNameView = null;
		listPriceView = null;
		priceView = null;
		couponEndView = null;
		usePeriodView = null;
		couponNoticeLayout = null;
		storeInfoLayout = null;
		storeNameView = null;
		storeAddrView = null;
		shopIntroLayout = null;
		couponCtrlBtn = null;
		couponTopFrontLayout = null;
		couponTopBackLayout = null;
		mContainer = null;
		mStartAnimView = null;
		couponReturn = null;
		couponCancelUse = null;
		couponConfirmUse = null;
		super.onDestroy();
	}
	private void initClickListener() {
		storeInfoLayout.setOnClickListener(this);
		couponCtrlBtn.setOnClickListener(this);
		couponLikeBtn.setOnClickListener(this);
		couponReturn.setOnClickListener(this);
		couponCancelUse.setOnClickListener(this);
		couponConfirmUse.setOnClickListener(this);
	}

	private void getDetail() {
		ProgressUtil.show(this);
		couponService.getCouponDetail(couponId, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				detailData = couponService.mapToModel((LinkedTreeMap<String, Object>) data.getData());
				if (detailData == null) {
					notifyErrorRequest();
					return;
				}
				setDataToViews();
				ProgressUtil.dismiss();
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if (Constant.DEBUG) {
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

	private void setDataToViews() {
		TagView.Tag tag = Tools.getTagLabel(detailData.getKindName(), CouponService.getTagColor(CouponDetailActivity.this, detailData.getKind()));
		couponTag.setSingleTag(tag);
		if (detailData.isKeep()) {
			useCoupon(couponCtrlBtn);
		}
		if (detailData.isLike()) {
			setLike(couponLikeBtn);
			couponLikeBtn.setTag(true);
		}
		shopNameView.setText(detailData.getShop().getShopUserName());
		Glide.with(CouponDetailActivity.this).load(detailData.getImage()).crossFade().into(couponImage);
		storeNameView.setText(detailData.getShop().getShopUserName());
		storeAddrView.setText(detailData.getShop().getShopAddr1().length() > 0 ? detailData.getShop().getShopAddr1() : detailData.getShop().getShopAddr2());
		couponNameView.setText(detailData.getName());
		listPriceView.setText(detailData.getListPrice() + getString(R.string.unit_won));
		priceView.setText(detailData.getPrice() + getString(R.string.unit_won));
		couponEndView.setText(detailData.getEndTime() + " " + getString(R.string.event_end_label));
		usePeriodView.setText(detailData.getUseStart() + getString(R.string.period_link_string) + detailData.getUseEnd());
		List<String> notice = detailData.getNotice();
		for (int i = 0; i < notice.size(); i++) {
			couponNoticeLayout.addView(getCouponNoticeText(notice.get(i)));
		}
		List<String> shopIntro = detailData.getShop().getShopIntro();
		for (int i = 0; i < shopIntro.size(); i++) {
			shopIntroLayout.addView(getCouponNoticeText(shopIntro.get(i)));
		}
	}

	@Override
	public void onClick(View view) {
		final View v = view;
		switch (v.getId()) {
		case R.id.store_info_layout:
			Intent intent = new Intent(CouponDetailActivity.this, ShopDetailActivity.class);
			intent.putExtra("shop_id", detailData.getShopId());
			startActivity(intent);
			break;
		case R.id.coupon_like_btn:
			if (new UserService(this).getLoginUser() == null) {
				Route.goLogin(this);
				return;
			}
			likeCoupon(v, couponId);
			break;
		case R.id.coupon_ctrl_btn: // download
			if (new UserService(this).getLoginUser() == null) {
				Route.goLogin(this);
				return;
			}
			ProgressUtil.show(this);
			couponService.downCoupon(couponId, new SimpleVicResponseListener() {
				@Override
				public void onSuccess(ResData data) {
					useCoupon(v);
					detailData.setKeep(true);
					BroadHelper.sendSerializableBroad(CouponDetailActivity.this, BroadKeys.COUPON_CHANGE, detailData);
					NotifyUtil.toast(CouponDetailActivity.this, R.string.coupon_download_succed);
					ProgressUtil.dismiss();
				}

				@Override
				public void onFailure(int httpResponseCode, String responseBody) {
					if (Constant.DEBUG) super.onFailure(httpResponseCode, responseBody);
					switch (httpResponseCode) {
					// 판매기간 종료
					case ResponseStatus.COUPON_SELL_TIME_END:
						NotifyUtil.toast(CouponDetailActivity.this, R.string.coupon_sell_timeout);
						break;
					default:
						break;
					}
					ProgressUtil.dismiss();
				}
			});
			break;
		case R.id.coupon_use_confirm:
			rotateTopPanel();
			break;
		case R.id.return_coupon:
			if (new UserService(this).getLoginUser() == null) {
				Route.goLogin(this);
				return;
			}
			ProgressUtil.show(this);
			couponService.returnCoupon(couponId, new SimpleVicResponseListener(){
				@Override
				public void onSuccess(ResData data) {
					downloadCoupon(v);
					detailData.setKeep(false);
					BroadHelper.sendSerializableBroad(CouponDetailActivity.this, BroadKeys.COUPON_CHANGE, detailData);
					NotifyUtil.toast(CouponDetailActivity.this, R.string.coupon_return_succed);
					ProgressUtil.dismiss();
				}
				@Override
				public void onFailure(int httpResponseCode, String responseBody) {
					// TODO Auto-generated method stub
					super.onFailure(httpResponseCode, responseBody);
				}
			});
			break;
		case R.id.coupon_cancel_use:
			rotateTopPanel();
			break;
		case R.id.coupon_confirm_use:
			if (new UserService(this).getLoginUser() == null) {
				Route.goLogin(this);
				return;
			}
			ProgressUtil.show(this);
			couponService.useCoupon(couponId, new SimpleVicResponseListener(){
				@Override
				public void onSuccess(ResData data) {
					ProgressUtil.dismiss();
				}
				@Override
				public void onFailure(int httpResponseCode, String responseBody) {
					// TODO Auto-generated method stub
					super.onFailure(httpResponseCode, responseBody);
				}
			});
			break;
		default:
			break;
		}
	}
	
	private void rotateTopPanel(){
		mCenterX = mContainer.getWidth() / 2;
		mCenterY = mContainer.getHeight() / 2;
		mDepthZ = Math.min(mDepthZ, 100.0f);
		applyRotation(mStartAnimView, 0, 90);
	}
	/**
	 * card rotate animation
	 * @author Nick.z
	 */
	private final class SwapViews implements Runnable {
		@Override
		public void run() {
			couponTopFrontLayout.setVisibility(View.GONE);
			couponTopBackLayout.setVisibility(View.GONE);
			mIndex++;
			if (0 == mIndex % 2) {
				mStartAnimView = couponTopFrontLayout;
			} else {
				mStartAnimView = couponTopBackLayout;
			}
			mStartAnimView.setVisibility(View.VISIBLE);
			mStartAnimView.requestFocus();
			Rotate3dAnimation rotation = new Rotate3dAnimation(-90, 0, mCenterX, mCenterY, mDepthZ, false);
			rotation.setDuration(mDuration);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());
			mStartAnimView.startAnimation(rotation);
		}
	}
	private void applyRotation(View animView, float startAngle, float toAngle) {
		float centerX = mCenterX;
		float centerY = mCenterY;
		Rotate3dAnimation rotation = new Rotate3dAnimation(startAngle, toAngle, centerX, centerY, mDepthZ, true);
		rotation.setDuration(mDuration);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new VicSimpleAnimationListener(){
			@Override
			public void onAnimationEnd(Animation animation) {
				mContainer.post(new SwapViews());
			}
		});
		animView.startAnimation(rotation);
	}

	/**
	 * like action
	 * @param v
	 */
	private void likeCoupon(View v, int id) {
		final int couponId = id;
		final View view = v;
		final boolean isLike = (v.getTag() == null);
		couponService.likeCoupon(couponId, new SimpleVicResponseListener() {
			@Override
			public void onSuccess(ResData data) {
				if (isLike) {
					Tools.bigToNormalAnimation( CouponDetailActivity.this , view);
					setLike(view);
					view.setTag(true);
				} else {
					setDislike(view);
					view.setTag(null);
				}
				detailData.setLike(isLike);
				BroadHelper.sendSerializableBroad(CouponDetailActivity.this, BroadKeys.COUPON_CHANGE, detailData);
			}

			@Override
			public void onFailure(int httpResponseCode, String responseBody) {
				if (Constant.DEBUG) {
					super.onFailure(httpResponseCode, responseBody);
				}
			}
		});
	}
	private void setLike(View v) {
		((ImageButton) v).setImageResource(R.drawable.ic_action_favorite);
	}

	private void setDislike(View v) {
		((ImageButton) v).setImageResource(R.drawable.ic_action_favorite_outline);
	}

	private void useCoupon(View v) {
		TextView t = (TextView) v;
		Drawable drawable = getResources().getDrawable(R.drawable.ic_use_check);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setTextColor(getResources().getColor(R.color.theme_color));
		t.setText(getResources().getString(R.string.use_coupon));
		t.setCompoundDrawables(drawable, null, null, null);
		v.setId( R.id.coupon_use_confirm );
		v.setOnClickListener(this);
	}
	
	private void downloadCoupon(View v) {
		TextView t = (TextView) v;
		Drawable drawable = getResources().getDrawable(R.drawable.ic_coupon_down_24);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setTextColor(getResources().getColor(R.color.coupon_normal_ctrl_text));
		t.setText(getResources().getString(R.string.get_coupon));
		t.setCompoundDrawables(drawable, null, null, null);
		v.setId( R.id.coupon_ctrl_btn );
		v.setOnClickListener(this);
	}

	private TextView getCouponNoticeText(String words) {
		TextView tView = new TextView(this);
		tView.setText(getString(R.string.center_dot) + " " + words);
		tView.setTextColor(getResources().getColor(R.color.normal_light_font));
		return tView;
	}

	private void notifyErrorRequest() {
		NotifyUtil.toast(CouponDetailActivity.this, getString(R.string.error_request));
		finish();
	}

	@Override
	public void onBackPressed() {
		if (ProgressUtil.isShowing()) { return; }
		super.onBackPressed();
	}
}
