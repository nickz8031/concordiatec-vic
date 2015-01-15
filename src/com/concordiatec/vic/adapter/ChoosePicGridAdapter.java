package com.concordiatec.vic.adapter;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.R;
import com.concordiatec.vic.util.LocalImageUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

public class ChoosePicGridAdapter extends BaseAdapter {
	Context context;
	List<String> data;
	public Bitmap bitmaps[];
	LocalImageUtil util;
	OnItemClickClass onItemClickClass;
	private int index = -1;
	List<View> holderlist;

	public ChoosePicGridAdapter(Context context, List<String> data, OnItemClickClass onItemClickClass) {
		this.context = context;
		this.data = data;
		this.onItemClickClass = onItemClickClass;
		bitmaps = new Bitmap[data.size()];
		util = new LocalImageUtil(context);
		holderlist = new ArrayList<View>();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Holder holder;
		if (arg0 != index && arg0 > index) {
			index = arg0;
			arg1 = LayoutInflater.from(context).inflate(R.layout.gr_choose_pic_item, null);
			holder = new Holder();
			holder.imageView = (ImageView) arg1.findViewById(R.id.imageView1);
			holder.checkBox = (CheckBox) arg1.findViewById(R.id.checkBox1);
			arg1.setTag(holder);
			holderlist.add(arg1);
		} else {
			holder = (Holder) holderlist.get(arg0).getTag();
			arg1 = holderlist.get(arg0);
		}
		if (bitmaps[arg0] == null) {
			Glide.with(context).load(data.get(arg0)).into(holder.imageView);
		} else {
			holder.imageView.setImageBitmap(bitmaps[arg0]);
		}
		arg1.setOnClickListener(new OnPhotoClick(arg0, holder.checkBox));
		return arg1;
	}

	class Holder {
		ImageView imageView;
		CheckBox checkBox;
	}


	public interface OnItemClickClass {
		public void OnItemClick(View v, int Position, CheckBox checkBox);
	}

	class OnPhotoClick implements OnClickListener {
		int position;
		CheckBox checkBox;
		public OnPhotoClick(int position, CheckBox checkBox) {
			this.position = position;
			this.checkBox = checkBox;
		}
		@Override
		public void onClick(View v) {
			if (data != null && onItemClickClass != null) {
				onItemClickClass.OnItemClick(v, position, checkBox);
			}
		}
	}
}