package com.wangkeke.dragfast;

import com.wangkeke.dragfast.darg.AllActivityModule;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        AllActivityModule.class
})
public interface MyAppConponent {
    void inject(MyApplication application);
}
