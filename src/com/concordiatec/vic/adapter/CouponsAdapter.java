package com.concordiatec.vic.adapter;

import java.util.List;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.model.Coupon;
import com.concordiatec.vic.service.CouponService;
import com.concordiatec.vic.service.UserService;
import com.concordiatec.vic.tools.Route;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.concordiatec.vic.widget.TagView;
import com.concordiatec.vic.LoginActivity;
import com.concordiatec.vic.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
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
			holder.price = (TextView) convertView.findViewById(R.id.coupon_price);
			holder.listPrice = (TextView) convertView.findViewById(R.id.coupon_list_price);
			holder.distance = (TextView) convertView.findViewById(R.id.shop_distance);
			holder.getCouponBtn = (TextView) convertView.findViewById(R.id.coupon_ctrl_btn);
			holder.likeButton = (ImageButton) convertView.findViewById(R.id.coupon_like_btn);
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
			holder.likeButton.setImageResource(R.drawable.ic_action_favorite);
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
			switch (v.getId()) {
			case R.id.coupon_like_btn:
				if (new UserService(context).getLoginUser() == null) {
					Route.goLogin(context);
					return;
				}
				if(v.getTag() == null){
					activeLikeAnimation(v);
				}
				likeCoupon(v , id);
				break;
			default:
				break;
			}
		}
		
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
		couponService.likeCoupon(id);
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
		toBig.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

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
