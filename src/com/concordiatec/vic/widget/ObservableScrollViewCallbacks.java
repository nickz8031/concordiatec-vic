package com.concordiatec.vic.widget;

public interface ObservableScrollViewCallbacks {
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging);

    public void onDownMotionEvent();

    public void onUpOrCancelMotionEvent(ObScrollState scrollState);
}
