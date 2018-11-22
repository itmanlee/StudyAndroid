package com.wangkeke.dragfast.darg;

import android.content.Context;
import android.content.SharedPreferences;

import com.wangkeke.dragfast.MainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    @Provides
    String provideName() {
        return MainActivity.class.getName();
    }

    @Provides
    SharedPreferences provideSp(MainActivity activity) {
        return activity.getSharedPreferences("def_ap", Context.MODE_PRIVATE);
    }
}
