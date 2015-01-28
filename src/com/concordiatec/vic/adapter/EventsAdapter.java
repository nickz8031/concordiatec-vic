package com.concordiatec.vic.adapter;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.concordiatec.vic.model.Coupon;
import com.concordiatec.vic.widget.TagView;
import com.concordiatec.vic.R;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventsAdapter extends VicBaseAdapter {
	private List<Coupon> data;
	private Context context;
	private Map<Integer, View> viewMap;
	private LayoutInflater inflater;
	
	public EventsAdapter(Context context , List<Coupon> data) {
		this.data = data;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.viewMap = new HashMap<Integer, View>();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Coupon getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		CouponHolder holder;
		if (viewMap.get(position) == null) {
//			Coupon apData = getItem(position);
			convertView = inflater.inflate(R.layout.li_frag_coupons, parent, false);
			
			TagView tv = (TagView) convertView.findViewById(R.id.coupon_tag);
			TagView.Tag tag = new TagView.Tag("실시간 상품", context.getResources().getColor(R.color.theme_color) );
			tv.setSingleTag(tag);
			tv.setVisibility(View.VISIBLE);
			
			TextView price = (TextView) convertView.findViewById(R.id.coupon_price);
			TextView listPrice = (TextView) convertView.findViewById(R.id.coupon_list_price);
			TextView endTime = (TextView) convertView.findViewById(R.id.coupon_end_time);
			TextView distance = (TextView) convertView.findViewById(R.id.shop_distance);
			//가격 설정
			long tempNumber = Math.round(Math.random()*19999);
			price.setText( formatString( R.string.format_price_string , NumberFormat.getInstance().format(tempNumber+5000)) );
			
			listPrice.setText( formatString( R.string.format_price_string , NumberFormat.getInstance().format(tempNumber)) );
			listPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			
			endTime.setText( formatString( R.string.format_end_time , "2015.03.31 21:00:00") );
			
			distance.setText( formatString( R.string.format_location_range_meter , "530") );
			
			viewMap.put(position, convertView);
			convertView.startAnimation(animation);
		} else {
			convertView = viewMap.get(position);
			// holder = (MainNewsHolder) convertView.getTag();
		}
		return convertView;
	}

	private String formatString(int formatResId , String toString){
		return String.format(context.getResources().getString(formatResId), toString );
	}
	
	@SuppressWarnings("unused")
	private static class CouponHolder {
	}
}
