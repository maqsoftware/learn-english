package com.maqautocognita.listener;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public interface IScrollPaneListener<T> {

    void onPaneChanged(T object, int paneIndex);
}
