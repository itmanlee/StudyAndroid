package com.wangkeke.mvpdemo;

import com.wangkeke.mvpdemo.base.BasePresenter;
import com.wangkeke.mvpdemo.base.BaseView;

public interface LoginContract {

    interface Model<T> {
        void postNetLogin(T t, String userName, String password);
    }

    /**
     * 用于activity等ui页面效果回调展示处理
     */
    interface View extends BaseView<Presenter> {

        @Override
        void showLoading();

        @Override
        void dismissLoading();

        @Override
        void onError(String errorMsg);

        void onSuccess(User user);
    }


    /**
     * 用于协调model（数据提供者）与view直接的数据传递和逻辑控制
     */
    interface Presenter extends BasePresenter<View> {

        void postNetLogin(String userName, String password);

    }
}
