package com.wangkeke.daggeruse;

import android.content.Context;

import com.wangkeke.daggeruse.ui.SecordActivity;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Singleton 局部单例，和生命周期绑定，注入activity单例范围就在activity，注入application就是全局单例
 */

@Singleton
@Component(modules = MainModules.class)
public interface MainComponent {
    void inject(MainActivity activity);
}
