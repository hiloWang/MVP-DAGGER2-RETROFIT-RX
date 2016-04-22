package com.github.hilo.view;

/**
 * Description：MvpView
 * <p>
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an Activity or Fragment.
 * <p>
 * Created by：Hilo
 * Time：2016-01-04 11:31
 */
public interface MvpView {
    /**
     * 发生错误
     *
     * @param e e
     */
    void onFailure(Throwable e);
}
