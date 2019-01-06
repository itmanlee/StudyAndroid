package com.wangkeke.daggeruse;

import android.app.Application;

import com.wangkeke.daggeruse.app.AppComponent;
import com.wangkeke.daggeruse.app.DaggerAppComponent;

public class MyApplicaiton extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
