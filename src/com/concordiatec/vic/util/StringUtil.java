package com.concordiatec.vic.util;

import com.concordiatec.vic.R;
import com.concordiatec.vic.inf.IVicClickableSpan;
import com.concordiatec.vic.tools.Tools;
import com.concordiatec.vic.widget.TagView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.View;

public class StringUtil {
	public static boolean isEmpty( Object str ){
		return (str == null || "".equals(str.toString()));		
	}
	
	public static Drawable getCommentNameTagDrawable( Context context , String text ){
		TagView tagView = new TagView(context);
		TagView.Tag tag = new TagView.Tag(text, context.getResources().getColor(R.color.theme_color) );
		tagView.setTextColor(Color.WHITE);
		tagView.setTextSize(14);
		tagView.setTagCornerRadius(5);
		tagView.setTagPaddingHor(20);
		tagView.setTagPaddingVert(10);
		tagView.setSingleTag(tag);
		Bitmap bm = Tools.convertViewToBitmap(tagView);
		
		Drawable drawable = new BitmapDrawable(context.getResources(), bm);
		drawable.setBounds(0 , 10 , drawable.getIntrinsicWidth()+10, drawable.getIntrinsicHeight()+10);
		return drawable;
	}
	
	/**
	 * set image span
	 * @param target
	 * @param drawable
	 * @param start
	 * @param end
	 */
	public static void setImageSpan( SpannableString target , Drawable drawable , int start , int end ){
		target.setSpan(new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE), start , end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	/**
	 * set bold
	 * @param target SpannableString
	 * @param start start position
	 * @param end end position
	 */
	public static void setBoldText(SpannableString target , int start , int end){
		target.setSpan(
				new StyleSpan(android.graphics.Typeface.BOLD), 
				start, 
				end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	
	/**
	 * set clickable text
	 * @param target
	 * @param start
	 * @param end
	 * @param lis
	 */
	public static void setClickableText(SpannableString target , int start , int end , IVicClickableSpan lis ){
		final IVicClickableSpan listener = lis;
		target.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				listener.onClick();
			}
			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setUnderlineText(false);
				ds.setColor(Color.BLUE);
			}
		}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	}
	/**
	 * set apartment text color
	 * @param target
	 * @param color
	 * @param start
	 * @param end
	 */
	public static void setTextColor(SpannableString target , int color , int start , int end){
		target.setSpan( 
				new ForegroundColorSpan(color) , 
				start, 
				end , 
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
	}
}
