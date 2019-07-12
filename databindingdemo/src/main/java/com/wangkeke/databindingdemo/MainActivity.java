package com.wangkeke.databindingdemo;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wangkeke.databindingdemo.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding bindmain = DataBindingUtil.setContentView(this, R.layout.activity_main);

        user = new User("wangkeke","123456",20);

        bindmain.setUserInfo(user);
        bindmain.setUserHandler(new UserHandler());


        user.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(propertyId == BR.name){
                    Log.e("wangkeke","-------BR.name");
                }else if(propertyId == BR.age){
                    Log.e("wangkeke","-------BR.age");
                }else if(propertyId == BR.passWord){
                    Log.e("wangkeke","-------BR.passWord");
                }
            }
        });
    }

    public class UserHandler {

        public void changeUserName() {
            user.setName("name" + new Random().nextInt(100));
        }

        public void changeUserAge() {
            user.setAge(20 + new Random().nextInt(100));
        }

    }

}
