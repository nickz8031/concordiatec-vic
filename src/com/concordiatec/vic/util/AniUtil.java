package com.concordiatec.vic.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AniUtil {
	public static int animationDuration = 200;

	public static Animation fadeInAnimation() {
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		animation.setDuration(animationDuration);
		animation.setFillAfter(true);
		return animation;
	}

	public static Animation fadeOutAnimation() {
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		animation.setDuration(animationDuration);
		animation.setFillAfter(true);
		return animation;
	}
}
