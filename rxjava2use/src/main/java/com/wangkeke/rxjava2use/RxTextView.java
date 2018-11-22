package com.wangkeke.rxjava2use;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class RxTextView extends android.support.v7.widget.AppCompatTextView {

    public RxTextView(Context context) {
        super(context);
    }

    public RxTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RxTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static Observable getInstance(final Context context){

        Observable<RxTextView> observable = Observable.create(new ObservableOnSubscribe<RxTextView>() {
            @Override
            public void subscribe(ObservableEmitter<RxTextView> e) throws Exception {
                e.onNext(new RxTextView(context));
            }
        });

        return observable;
    }




}
