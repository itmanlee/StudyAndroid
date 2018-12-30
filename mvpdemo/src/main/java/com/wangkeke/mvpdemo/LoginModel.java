package com.wangkeke.mvpdemo;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginModel implements LoginContract.Model<LoginContract.View> {



    @Override
    public void postNetLogin(final LoginContract.View view, final String userName, final String password) {

        view.showLoading();

        Observable.timer(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        view.dismissLoading();
                        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
                            view.onError("用户名或密码不能为空！");
                            return;
                        }

                        if(userName.equals("admin") && password.equals("123456")){
                            User user  = new User(userName,password);
                            view.onSuccess(user);
                        }else {
                            view.onError("用户名或密码错误，请重试！");
                        }
                    }
                });
    }
}
