package com.concordiatec.vilnet.adapter;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.concordiatec.vilnet.R;
import com.concordiatec.vilnet.constant.Constant;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PhotoShowGridAdapter extends BaseAdapter {
	private ArrayList<String> paths;
	private Context context;
	private List<View> holderlist;
	private int index = -1;
	private OnDataChangedListener changedListener;
	public PhotoShowGridAdapter(Context context , OnDataChangedListener listener) {
		this.paths = new ArrayList<String>();
		this.context = context;
		this.changedListener = listener;
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
		if( Constant.SURPLUS_UPLOAD_COUNTS > 0 ) Constant.SURPLUS_UPLOAD_COUNTS--;
		notifyDataSetChanged();
		changedListener.onDataChange();
	}
	
	public ArrayList<String> getAllData(){
		return paths;
	}
	
	public void setData( ArrayList<String> dataList ) {
		this.paths.clear();
		this.paths = dataList;
		notifyDataSetChanged();
		changedListener.onDataChange();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (position != index && position > index) {
			index = position;
			convertView = LayoutInflater.from(context).inflate(R.layout.gr_photo_show, null);
			holder = new Holder();
			holder.iView = (ImageView) convertView.findViewById(R.id.grid_image_view);
			holder.removeButton = (LinearLayout) convertView.findViewById(R.id.grid_image_remove);
			convertView.setTag(holder);
			holderlist.add(convertView);
		} else {
			holder = (Holder) holderlist.get(position).getTag();
			convertView = holderlist.get(position);
		}
		final int idx = position;
		Glide.with(context).load(getItem(position)).into(holder.iView);
		holder.removeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				paths.remove(idx);
				if( Constant.SURPLUS_UPLOAD_COUNTS < Constant.MAX_UPLOAD_FILE_COUNTS ) Constant.SURPLUS_UPLOAD_COUNTS++;
				notifyDataSetChanged();
				changedListener.onDataChange();
			}
		});
		
		return convertView;
	}
	
	public interface OnDataChangedListener{
		public void onDataChange();
	}
	
	class Holder{
		ImageView iView;
		LinearLayout removeButton;
	}
}
