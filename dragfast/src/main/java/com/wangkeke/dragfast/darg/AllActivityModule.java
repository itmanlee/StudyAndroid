package com.wangkeke.dragfast.darg;

import com.wangkeke.dragfast.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(subcomponents = {
        BaseActivityComponent.class
})
public abstract class AllActivityModule {

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributeMainActivityInjector();
}
