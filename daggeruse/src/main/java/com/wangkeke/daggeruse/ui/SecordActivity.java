package com.wangkeke.daggeruse.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wangkeke.daggeruse.R;
import com.wangkeke.daggeruse.School;

public class SecordActivity extends AppCompatActivity {

//    @Inject
//    School school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secord);

//        DaggerMainComponent.create().inject(this);

//        ((MyApplicaiton)getApplication()).getAppComponent().inject(this);

//        Log.e("daggerLog","secord school add >【"+school+"】");
    }
}
