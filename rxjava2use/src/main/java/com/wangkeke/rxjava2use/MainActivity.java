package com.wangkeke.rxjava2use;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_hello).setOnClickListener(v -> ToastHello());

        findViewById(R.id.btn_do).setOnClickListener(v -> doSomething());
    }

    /**
     * doXXXX操作符
     */
    private void doSomething() {
        Disposable dispose = Observable.just("hello")
                .doOnNext(s -> Log.e("wangkeke", "doOnNext :" + s))
                .doAfterNext(s -> Log.e("wangkeke", "doAfterNext :" + s))
                .doOnComplete(() -> Log.e("wangkeke", "doOnComplete"))
                .doOnSubscribe(s -> Log.e("wangkeke", "doOnSubscribe :" + s))
                //doAfterTerminate 当observable调用onComplete或onError时触发
                .doAfterTerminate(() -> Log.e("wangkeke", "doAfterTerminate"))
                .doFinally(() -> Log.e("wangkeke", "doFinally"))
                .doOnEach(s -> Log.e("wangkeke", "doOnEach :" + (s.isOnNext() ? "onNext()" : s.isOnComplete() ? "OnComplete" : "onError")))
                .subscribe(s -> Log.e("wangkeke", "收到消息：s = " + s));
    }

    private void ToastHello() {

        Observable.create((ObservableOnSubscribe<String>) e -> e.onNext("Hello,Wrold!"))
                .subscribe(s -> Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show());


        Observable.just("Hello,World!")
                .subscribe(s -> Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show());

        Observable.just("1").subscribe(System.out::println);

    }


    private void wrapObjectTest() {

        RxTextView.getInstance(this).subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Log.e("o","o = "+o);
            }
        });

    }
}
