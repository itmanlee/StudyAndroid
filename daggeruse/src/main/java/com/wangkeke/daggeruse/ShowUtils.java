package com.wangkeke.daggeruse;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import javax.inject.Inject;

public class ShowUtils {

    private Context context;

    @Inject
    public ShowUtils(Context context) {
        this.context = context;
        if(context instanceof Activity){
            Log.e("daggerLog","我是Activity的context");
        }
    }
}
