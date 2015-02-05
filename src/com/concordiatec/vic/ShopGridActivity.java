package com.concordiatec.vic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.model.Shop;
import com.concordiatec.vic.model.ShopImage;

public class ShopGridActivity extends SubPageSherlockActivity {
	
	private ShopGridAdapter adapter;
	private GridView gridView;
	private Shop shop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zero_shop_grid);
		shop = (Shop) getIntent().getSerializableExtra("shop");
		init();
		if (shop.getImages() == null || shop.getImages().size() == 0) {
			Toast.makeText(this, "No images", Toast.LENGTH_SHORT).show();
		}
		
		gridView = (GridView) findViewById(R.id.gridView);
		adapter = new ShopGridAdapter(this, shop.getImages());
		gridView.setAdapter(adapter);
	}

	private void init() {
		setTitle(shop.getShopUserName());
	}

	/*
	 * adapter class
	 */
	class ShopGridAdapter extends BaseAdapter {
		private Activity context;
		private List<ShopImage> data;
		private LayoutInflater inflater;

		public ShopGridAdapter(Activity context, List<ShopImage> data) {
			this.context = context;
			this.data = data;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.zero_shop_grid_item, null);
			final ShopImage img = data.get(position);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
			Glide.with(context).load(img.getName()).placeholder(R.drawable.grid_default).centerCrop().into(imageView);
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(context, ShopGalleryActivity.class);
					i.putExtra("img_id", img.getId());
					i.putExtra("shop", shop);
					context.startActivity(i);
				}
			});
			return convertView;
		}
	}
}
