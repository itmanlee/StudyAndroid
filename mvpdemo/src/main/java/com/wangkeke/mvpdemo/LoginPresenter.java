package com.wangkeke.mvpdemo;

import android.content.Context;

import javax.inject.Inject;

public class LoginPresenter implements LoginContract.Presenter {

    @Inject
    LoginContract.View mView;

    private LoginContract.Model<LoginContract.View> model;

    @Inject
    public LoginPresenter() {
        model = new LoginModel();
    }

    public void setmView(LoginContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void postNetLogin(String userName, String password) {
        model.postNetLogin(mView,userName,password);
    }
}
