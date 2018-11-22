package com.wangkeke.rxjava2use;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wrapObjectTest();
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
