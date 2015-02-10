package com.concordiatec.vilnet.adapter;

import java.util.List;
import com.bumptech.glide.Glide;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.constant.Constant;
import com.concordiatec.vilnet.constant.ResponseStatus;
import com.concordiatec.vilnet.listener.SimpleVicResponseListener;
import com.concordiatec.vilnet.listener.VicSimpleAnimationListener;
import com.concordiatec.vilnet.model.Coupon;
import com.concordiatec.vilnet.model.ResData;
import com.concordiatec.vilnet.service.CouponService;
import com.concordiatec.vilnet.service.UserService;
import com.concordiatec.vilnet.tools.Route;
import com.concordiatec.vilnet.util.NotifyUtil;
import com.concordiatec.vilnet.util.ProgressUtil;
import com.concordiatec.vilnet.widget.CircleImageView;
import com.concordiatec.vilnet.widget.TagView;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class CouponsAdapter extends VicBaseAdapter {
	private List<Coupon> data;
	private Context context;
	private LayoutInflater inflater;
	private Resources res;
	private CouponService couponService;
	
	public CouponsAdapter(Context context , List<Coupon> data) {
		this.data = data;
		this.context = context;
		this.res = context.getResources();
		this.couponService = new CouponService(context);
		this.inflater = LayoutInflater.from(context);
	}

	public void couponStateChange( Coupon coupon ){
		for (int i = 0; i < data.size(); i++) {
			if( coupon.getId() == getItem(i).getId() ){
				this.data.set(i, coupon);
				notifyDataSetChanged();
				break;
			}
		}
	}
	
	public void clear(){
		this.data.clear();
		notifyDataSetChanged();
	}
	
	public void deleteData( int position ){
		this.data.remove(position-1);
		notifyDataSetChanged();
	}
	
	public void setData( List<Coupon> data ){
		this.data = data;
		notifyDataSetChanged();
	}
	
	public void updateData( Coupon article , int position ){
		this.data.set(position-1, article);
		notifyDataSetChanged();
	}
	
	public void addData( Coupon data ){
		if(data != null){
			this.data.add(data);
		}
	}
	
	public void addData( List<Coupon> data ){
		if(data != null && data.size()>0 ){
			for (int i = 0; i < data.size(); i++) {
				addData( data.get(i) );
			}
			notifyDataSetChanged();
		}
	}
	
	public int getLastRecordId(){
		if( this.data.size()>0 ){
			return this.data.get(this.data.size()-1).getId();
		}else{
			return 0;
		}
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Coupon getItem(int position) {
		return data.get(position);
	}
	public Coupon getItem(long position) {
		return data.get((int)position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CouponHolder holder;
		Coupon apData = getItem(position);
		if (convertView == null) {
			holder = new CouponHolder();
			convertView = inflater.inflate(R.layout.li_frag_coupons, parent, false);
			holder.couponImage = (CircleImageView) convertView.findViewById(R.id.main_coupon_img);
			holder.couponTag = (TagView) convertView.findViewById(R.id.coupon_tag);
			holder.shopName = (TextView) convertView.findViewById(R.id.shop_name);
			holder.couponName = (TextView) convertView.findViewById(R.id.coupon_title);
			holder.likeButton = (ImageButton) convertView.findViewById(R.id.coupon_like_btn);
			holder.price = (TextView) convertView.findViewById(R.id.coupon_price);
			holder.listPrice = (TextView) convertView.findViewById(R.id.coupon_list_price);
			holder.distance = (TextView) convertView.findViewById(R.id.shop_distance);
			holder.getCouponBtn = (TextView) convertView.findViewById(R.id.coupon_ctrl_btn);
			
			holder.shareButton = (ImageButton) convertView.findViewById(R.id.coupon_share_btn);
			
			convertView.setTag(holder);
		} else {
			 holder = (CouponHolder) convertView.getTag();
		}
		
		Glide.with(context).load(apData.getImage()).crossFade().into(holder.couponImage);
		
		TagView.Tag tag = new TagView.Tag(apData.getKindName(), CouponService.getTagColor( context, apData.getKind() ) );
		holder.couponTag.setSingleTag(tag);
				
		holder.price.setText( apData.getPrice() + context.getString(R.string.unit_won) );
		holder.listPrice.setText( apData.getListPrice() + context.getString(R.string.unit_won) );
		
		holder.listPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				
		holder.shopName.setText( apData.getShop().getShopUserName() );
		holder.couponName.setText( apData.getName() );
		
		if( apData.isLike() ){
			setLike(holder.likeButton);
			holder.likeButton.setTag(true);
		}else {
			setDislike(holder.likeButton);
			holder.likeButton.setTag(null);
		}
		
		if( apData.isKeep() ){
			downloadCoupon(holder.getCouponBtn);
			holder.getCouponBtn.setOnClickListener( null );
		}else{
			restoreCoupon(holder.getCouponBtn);
			holder.getCouponBtn.setOnClickListener( new ClickListener(apData.getId()) );
		}
		
		holder.likeButton.setOnClickListener( new ClickListener(apData.getId()) );
		
		
		
		//need edit************
		holder.distance.setText( "50" + context.getString(R.string.meter) );
		
		
		return convertView;
	}
	
	private final class ClickListener implements OnClickListener{
		private int id;
		public ClickListener( int id ) {
			this.id = id;
		}
		@Override
		public void onClick(View v) {
			if (new UserService(context).getLoginUser() == null) {
				Route.goLogin(context);
				return;
			}
			final View t = v;
			switch (v.getId()) {
			case R.id.coupon_like_btn:
				if(v.getTag() == null){
					activeLikeAnimation(v);
				}
				likeCoupon(v , id);
				break;
			case R.id.coupon_ctrl_btn:
				ProgressUtil.show(context);
				couponService.downCoupon(id , new SimpleVicResponseListener(){
					@Override
					public void onSuccess(ResData data) {
						downloadCoupon(t);
						NotifyUtil.toast(context, R.string.coupon_download_succed);
						ProgressUtil.dismiss();
					}
					@Override
					public void onFailure(int httpResponseCode, String responseBody) {
						if( Constant.DEBUG ) super.onFailure(httpResponseCode, responseBody);
						switch (httpResponseCode) {
						//판매기간 종료
						case ResponseStatus.COUPON_SELL_TIME_END:
							NotifyUtil.toast(context, R.string.coupon_sell_timeout);
							break;
						default:
							break;
						}
						ProgressUtil.dismiss();
					}
				});
				break;
			default:
				break;
			}
		}
		
	}
	
	private void downloadCoupon(View v){
		TextView t = (TextView) v;
		Drawable drawable = res.getDrawable(R.drawable.ic_use_check);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setTextColor( res.getColor( R.color.theme_color ) );
		t.setText(res.getString(R.string.use_coupon));
		t.setCompoundDrawables(drawable, null, null, null);
	}
	
	private void restoreCoupon(View v){
		TextView t = (TextView) v;
		Drawable drawable = res.getDrawable(R.drawable.ic_coupon_down_24);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		t.setTextColor( res.getColor( R.color.coupon_normal_ctrl_text ) );
		t.setText(res.getString(R.string.get_coupon));
		t.setCompoundDrawables(drawable, null, null, null);
	}
	
	/**
	 * like action
	 * 
	 * @param v
	 */
	private void likeCoupon(View v, int id) {
		
		final View view = v;
		final boolean isLike = (v.getTag() == null);
		if (isLike) {
			setLike(view);
			view.setTag(true);
		} else {
			setDislike(view);
			view.setTag(null);
		}
		couponService.likeCoupon(id , new SimpleVicResponseListener());
	}
	
	/**
	 * start animation with like action
	 * 
	 * @param v
	 */
	private void activeLikeAnimation(View v) {
		final View target = v;
		final Animation toBig = AnimationUtils.loadAnimation(context, R.anim.big_scale);
		final Animation toNormal = AnimationUtils.loadAnimation(context, R.anim.small_scale);
		target.setAnimation(toBig);
		toBig.start();
		toBig.setAnimationListener(new VicSimpleAnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				target.clearAnimation();
				target.setAnimation(toNormal);
				toNormal.start();
			}
		});
	}

	private void setLike(View v) {
		((ImageButton) v).setImageResource(R.drawable.ic_action_favorite);
	}

	private void setDislike(View v) {
		((ImageButton) v).setImageResource(R.drawable.ic_action_favorite_outline);
	}
	

	
	static class CouponHolder {
		TextView shopName;
		CircleImageView couponImage;
		TextView couponName;
		TagView couponTag;
		TextView distance;
		TextView listPrice;
		TextView price;
		TextView getCouponBtn;
		ImageButton shareButton;
		ImageButton likeButton;
	}
}
