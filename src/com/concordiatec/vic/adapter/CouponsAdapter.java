package com.concordiatec.vic.adapter;

import java.util.List;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.model.Coupon;
import com.concordiatec.vic.service.CouponService;
import com.concordiatec.vic.util.NotifyUtil;
import com.concordiatec.vic.widget.CircleImageView;
import com.concordiatec.vic.widget.TagView;
import com.concordiatec.vic.R;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CouponsAdapter extends VicBaseAdapter {
	private List<Coupon> data;
	private Context context;
	private LayoutInflater inflater;
	
	public CouponsAdapter(Context context , List<Coupon> data) {
		this.data = data;
		this.context = context;
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
			holder.endTime = (TextView) convertView.findViewById(R.id.coupon_end_time);
			holder.distance = (TextView) convertView.findViewById(R.id.shop_distance);
			holder.getCouponBtn = (TextView) convertView.findViewById(R.id.coupon_ctrl_btn);
			holder.likeButton = (TextView) convertView.findViewById(R.id.coupon_like_btn);
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
		
		holder.endTime.setText( apData.getEndTime() + " " + context.getString(R.string.event_end_label) );
		
		holder.shopName.setText( apData.getShop().getShopUserName() );
		holder.couponName.setText( apData.getName() );
		holder.likeButton.setText( apData.getLikeCount()+"" );
		
		if( apData.isLike() ){
			holder.likeButton.setCompoundDrawablesWithIntrinsicBounds( context.getResources().getDrawable(R.drawable.ic_action_favorite) , null, null, null);
		}
		holder.likeButton.setOnClickListener( new LikeButtonClickListener() );
		
		//need edit************
		holder.distance.setText( "50" + context.getString(R.string.meter) );
		
		
		return convertView;
	}
	
	private final class LikeButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			NotifyUtil.toast(context, "aaaaaaaaaaaaaaaaaa");
		}
	}
	
	

	
	static class CouponHolder {
		TextView shopName;
		CircleImageView couponImage;
		TextView couponName;
		TagView couponTag;
		TextView distance;
		TextView listPrice;
		TextView price;
		TextView endTime;
		TextView getCouponBtn;
		ImageButton shareButton;
		TextView likeButton;
	}
}
