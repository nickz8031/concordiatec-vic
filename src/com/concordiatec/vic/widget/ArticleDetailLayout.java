package com.concordiatec.vic.widget;

import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.Glide;
import com.concordiatec.vic.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ArticleDetailLayout extends RelativeLayout {
	
	private Context mContext;
	private List<String> imgURIs;
	
	
	
	private boolean titleVisibility;
	private boolean titleOpacity;
	private float titleFromOpacity;
	private float titleToOpacity;
	private int titleBackground;
	private float titleHeight;
	private int titlePosition;
	
	public ArticleDetailLayout(Context context) {
		this(context , null);
	}
	public ArticleDetailLayout(Context context, AttributeSet attrs) {
		this(context, attrs , 0);
	}

	public ArticleDetailLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		
		LayoutInflater.from(context).inflate(R.layout.article_detail_image_view, this, true);
		final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,R.styleable.ArticleDetailImage,defStyle,0);
		imgURIs = new ArrayList<String>();
		titleVisibility = attributes.getBoolean(R.styleable.ArticleDetailImage_title_visibility, true);
		if( titleVisibility ){
			titleOpacity = attributes.getBoolean(R.styleable.ArticleDetailImage_title_opacity, true);
			titleFromOpacity = attributes.getFloat(R.styleable.ArticleDetailImage_title_from_opa, 0);
			titleToOpacity = attributes.getFloat(R.styleable.ArticleDetailImage_title_to_opa, 0.5f);
			titleBackground = attributes.getColor(R.styleable.ArticleDetailImage_title_background, Color.BLACK);
			titleHeight = attributes.getDimension(R.styleable.ArticleDetailImage_title_height, context.getResources().getDimension(R.dimen.default_ar_d_title_height));
			titlePosition = attributes.getInteger(R.styleable.ArticleDetailImage_title_position, 0);
		}
	}
	
	public void setLayoutHeight(int height){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
		this.setLayoutParams(params);
	}
	
	public void addImage(String uri){
		imgURIs.add(uri);
	}
	
	private void recycle(){
		if( imgURIs.size() == 0 ) return;
		for( String uri : imgURIs ){
			ImageView iView = new ImageView(mContext);
			Glide.with(mContext).load(uri).into(iView);
			
		}
		
	}
}
