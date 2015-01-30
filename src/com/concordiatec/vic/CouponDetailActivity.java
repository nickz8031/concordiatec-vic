package com.concordiatec.vic;

import android.os.Bundle;
import com.concordiatec.vic.base.SubPageSherlockActivity;
import com.concordiatec.vic.widget.TagView;

public class CouponDetailActivity extends SubPageSherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle( getString(R.string.coupon_detail_title) );
		setContentView(R.layout.activity_coupon_detail);
		TagView couponTag = (TagView) findViewById(R.id.coupon_tag);
		TagView.Tag tag = new TagView.Tag("실시간상품", getResources().getColor(R.color.theme_color) );
		couponTag.setSingleTag(tag);
	}
}
