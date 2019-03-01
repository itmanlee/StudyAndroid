package com.wangkeke.rxjava2use;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    private Observable<String> ObservableOne;
    private Observable<String> ObservableTwo;
    private Observable<String> ObservableFinal;

    private ObservableEmitter<String> emitterOne;
    private ObservableEmitter<String> emitterTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ObservableOne = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                emitterOne = e;
            }
        });

        ObservableTwo = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                emitterTwo = e;
            }
        });


        ObservableFinal = Observable.merge(ObservableOne, ObservableTwo);


        DisposableObserver disposableObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String value) {
                Log.e("wangkeke", "aBoolean = " + value);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
            }
        };

        ObservableFinal.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(disposableObserver);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    emitterOne.onNext("1#1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    emitterTwo.onNext("1$1");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
