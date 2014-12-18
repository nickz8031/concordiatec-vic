package com.concordiatec.vic.widget;
/**
 * Provides common API for observable and scrollable widgets.
 */
public interface ObScrollable {
    /**
     * Sets a callback listener.
     *
     * @param listener listener to set
     */
    void setScrollViewCallbacks(ObservableScrollViewCallbacks listener);

    /**
     * Scrolls vertically to the absolute Y.
     * Implemented classes are expected to scroll to the exact Y pixels from the top,
     * but it depends on the type of the widget.
     *
     * @param y vertical position to scroll to
     */
    void scrollVerticallyTo(int y);

    /**
     * Returns the current Y of the scrollable view.
     *
     * @return current Y pixel
     */
    int getCurrentScrollY();
}