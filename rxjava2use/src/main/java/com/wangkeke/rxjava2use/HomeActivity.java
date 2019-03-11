package com.wangkeke.rxjava2use;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    private Observable<String> ObservableOne;
    private Observable<String> ObservableTwo;
    private Observable<String> ObservableFinal;

    private ObservableEmitter<String> emitterOne;
    private ObservableEmitter<String> emitterTwo;
    private Observable<Boolean> ObservableThree;

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


        ObservableThree = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                Log.e("wangkeke","---------- one");
                Thread.sleep(1000);
                e.onNext(true);
            }
        }).zipWith(Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                Log.e("wangkeke","---------- two");
                Thread.sleep(3000);
                e.onNext(true);
            }
        }), new BiFunction<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                if (aBoolean && aBoolean2) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        Disposable threeDisopose = ObservableThree.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Toast.makeText(HomeActivity.this, "value = "+aBoolean.booleanValue(), Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(HomeActivity.this, "error = "+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e("wangkeke","three ---- onComplete");
                    }
                });


    }
}
