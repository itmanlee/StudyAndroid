package com.wangkeke.daggeruse;

import android.app.Activity;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModules {

    private Context context;

    public MainModules(Context context) {
        this.context = context;
    }

    @SchoolType
    @Singleton
    @Provides
    public School provideSchool(){
        return new School("南京高级中学","江苏南京");
    }

    @SchoolType(type = 1)
    @Singleton
    @Provides
    public School provideSchoolOther(){
        return new School("国外学校","外国");
    }

    @Singleton
    @Provides
    public ShowUtils provideShowUtils(){
        return new ShowUtils(context);
    }

    @ForActivityContext
    @Provides
    public Context provideContext(){
        return context;
    }
}
