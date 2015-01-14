package com.concordiatec.vic.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.concordiatec.vic.R;
import com.concordiatec.vic.inf.ChooseImageCallback;
import com.concordiatec.vic.util.LocalImageUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoosePicFolderListAdapter extends BaseAdapter {
	private Context context;
	private String filecount = "filecount";
	private String filename = "filename";
	private String imgpath = "imgpath";
	private List<HashMap<String, String>> listdata;
	private LocalImageUtil util;
	private Bitmap[] bitmaps;
	private int index = -1;
	private List<View> holderlist;

	public ChoosePicFolderListAdapter(Context context, List<HashMap<String, String>> listdata) {
		this.context = context;
		this.listdata = listdata;
		bitmaps = new Bitmap[listdata.size()];
		util = new LocalImageUtil(context);
		holderlist = new ArrayList<View>();
	}

	@Override
	public int getCount() {
		return listdata.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listdata.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		Holder holder;
		if (arg0 != index && arg0 > index) {
			holder = new Holder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.li_choose_folder, null);
			holder.photo_imgview = (ImageView) arg1.findViewById(R.id.filephoto_imgview);
			holder.filecount_textview = (TextView) arg1.findViewById(R.id.filecount_textview);
			holder.filename_textView = (TextView) arg1.findViewById(R.id.filename_textview);
			arg1.setTag(holder);
			holderlist.add(arg1);
		} else {
			holder = (Holder) holderlist.get(arg0).getTag();
			arg1 = holderlist.get(arg0);
		}
		holder.filename_textView.setText(listdata.get(arg0).get(filename));
		holder.filecount_textview.setText(listdata.get(arg0).get(filecount));
		if (bitmaps[arg0] == null) {
			util.imgExcute(holder.photo_imgview, new ChooseImageCallback() {
				@Override
				public void resultImgCall(ImageView imageView, Bitmap bitmap) {
					bitmaps[arg0] = bitmap;
					imageView.setImageBitmap(bitmap);
				}
			}, listdata.get(arg0).get(imgpath));
		} else {
			holder.photo_imgview.setImageBitmap(bitmaps[arg0]);
		}
		return arg1;
	}

	class Holder {
		public ImageView photo_imgview;
		public TextView filecount_textview;
		public TextView filename_textView;
	}
}
