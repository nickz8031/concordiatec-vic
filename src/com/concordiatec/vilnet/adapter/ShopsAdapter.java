package com.concordiatec.vilnet.adapter;

import java.util.List;
import com.bumptech.glide.Glide;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.model.Shop;
import com.concordiatec.vilnet.util.NotifyUtil;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ShopsAdapter extends VicBaseAdapter {
	private List<Shop> data;
	private Context context;
	private LayoutInflater inflater;
	
	public ShopsAdapter(Context context , List<Shop> data) {
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
	
	public void setData( List<Shop> data ){
		this.data = data;
		notifyDataSetChanged();
	}
	
	public void updateData( Shop article , int position ){
		this.data.set(position-1, article);
		notifyDataSetChanged();
	}
	
	public void addData( Shop data ){
		if(data != null){
			this.data.add(data);
		}
	}
	
	public void addData( List<Shop> data ){
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
	public Shop getItem(int position) {
		return data.get(position);
	}
	public Shop getItem(long position) {
		return data.get((int)position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ShopHolder holder;
		Shop apData = getItem(position);
		if (convertView == null) {
			holder = new ShopHolder();
			convertView = inflater.inflate(R.layout.li_frag_stores, parent, false);
			holder.coverImage = (ImageView) convertView.findViewById(R.id.shop_cover_image);
			holder.shopName = (TextView) convertView.findViewById(R.id.shop_name);
			holder.shopAddress = (TextView) convertView.findViewById(R.id.shop_addr);
			holder.shopDistance = (TextView) convertView.findViewById(R.id.shop_distance);
			holder.shopType = (TextView) convertView.findViewById(R.id.shop_type);
			holder.shopRating = (RatingBar) convertView.findViewById(R.id.shop_rating);
			holder.ratingText = (TextView) convertView.findViewById(R.id.rating_text);
			
			convertView.setTag(holder);
		} else {
			 holder = (ShopHolder) convertView.getTag();
		}
		if( apData.getCoverImage() != null ){
			Glide.with(context).load( apData.getCoverImage().getName() ).thumbnail(0.1f).crossFade().centerCrop().into(holder.coverImage);
		}else{
			Glide.with(context).load( apData.getShopUserPhoto() ).thumbnail(0.1f).crossFade().centerCrop().into(holder.coverImage);
		}
		
		holder.shopName.setText( apData.getShopUserName() );
		holder.shopAddress.setText( apData.getShopAddr1() );
		holder.shopDistance.setText( apData.getDistance() + "" );
		holder.shopType.setText( apData.getGroup().getName() );
		holder.shopRating.setRating( (float)apData.getScore().getAvg() );
		holder.ratingText.setText( (float)apData.getScore().getAvg() + "" );
		
		return convertView;
	}
	

	
	static class ShopHolder {
		TextView shopName;
		ImageView coverImage;
		TextView shopAddress;
		RatingBar shopRating;
		TextView ratingText;
		TextView shopType;
		TextView shopDistance;
	}
}
