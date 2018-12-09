package com.wangkeke.goomvpdemo;

import com.wangkeke.goomvpdemo.net.ApiService;
import com.wangkeke.goomvpdemo.net.RetrofitUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainModel implements MainContract.MainModel{

    @Override
    public void getMainData(int start,int count, final Result callback) {

        ApiService apiService = RetrofitUtils.getInstance().create(ApiService.class);

        Observable<Object> observable = apiService.getTop250(start,count);

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object s) {
                        callback.successResponse("我是成功后返回的response:"+s.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.failedResponse("我发生了一些错误:"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
