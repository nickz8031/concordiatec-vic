package com.concordiatec.vic.util;

import android.content.Context;

public class Converter {

	public static int px2dip(Context context, float pxValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (pxValue / scale + 0.5f); 
    } 

    public static int dip2px(Context context, float dipValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (dipValue * scale + 0.5f); 
    } 

    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 

    public static float sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale); 
    } 
    
    public static float px2dipFloat(Context context, float pxValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (pxValue / scale ); 
    } 

    public static float dip2pxFloat(Context context, float dipValue) { 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (dipValue * scale); 
    } 

    public static float px2spFloat(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (pxValue / fontScale); 
    } 

    public static float sp2pxFloat(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (spValue * fontScale); 
    } 
}
