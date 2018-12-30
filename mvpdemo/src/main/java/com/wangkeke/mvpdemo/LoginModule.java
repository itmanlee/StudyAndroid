package com.wangkeke.mvpdemo;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    private LoginContract.View mView;

    public LoginModule(LoginContract.View mView) {
        this.mView = mView;
    }

    @Provides
    LoginContract.View providerContractView(){
        return this.mView;
    }
}
