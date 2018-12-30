package com.wangkeke.mvpdemo;

import dagger.Component;

@Component(modules = {LoginModule.class})
public interface LoginActivityComponent {
    void inject(MainActivity mainActivity);
}
