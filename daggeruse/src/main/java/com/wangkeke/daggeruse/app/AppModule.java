package com.wangkeke.daggeruse.app;

import com.wangkeke.daggeruse.School;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Singleton
    @Provides
    public School provideSingleSchool(){
        return new School("全局单例的School","中国");
    }
}
