package com.concordiatec.vic.adapter;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PhotoShowGridAdapter extends BaseAdapter {
	private List<String> paths;
	private Context context;
	private List<View> holderlist;
	private int index = -1;
	public PhotoShowGridAdapter(Context context) {
		this.paths = new ArrayList<String>();
		this.context = context;
		holderlist = new ArrayList<View>();
	}
	@Override
	public int getCount() {
		return paths.size();
	}

	@Override
	public String getItem(int position) {
		return paths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void addData(String data){
		this.paths.add(data);
		notifyDataSetChanged();
	}
	
	public void setData( List<String> dataList ) {
		this.paths.clear();
		this.paths = dataList;
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (position != index && position > index) {
			index = position;
			convertView = LayoutInflater.from(context).inflate(R.layout.gr_photo_show, null);
			holder = new Holder();
			holder.iView = (ImageView) convertView.findViewById(R.id.grid_image_view);
			convertView.setTag(holder);
			holderlist.add(convertView);
		} else {
			holder = (Holder) holderlist.get(position).getTag();
			convertView = holderlist.get(position);
		}
		Glide.with(context).load(getItem(position)).into(holder.iView);
		return convertView;
	}
	
	class Holder{
		ImageView iView;
	}
}
