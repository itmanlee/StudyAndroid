package com.wangkeke.mvpdemo.base;

public interface BaseView<T> {

    void showLoading();

    void dismissLoading();

    /*
      * 数据获取失败
    * */
    void onError(String errorMsg);
}
