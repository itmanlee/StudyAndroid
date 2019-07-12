package com.wangkeke.databindingdemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class User extends BaseObservable{

    private String name;

    private String passWord;

    private int age;

    public User() {
    }

    public User(String name, String passWord, int age) {
        this.name = name;
        this.passWord = passWord;
        this.age = age;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        //只更新本字段
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
        //更新所有字段
        notifyChange();
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyChange();
    }
}
