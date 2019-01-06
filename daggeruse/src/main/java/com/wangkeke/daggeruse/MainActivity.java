package com.wangkeke.daggeruse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wangkeke.daggeruse.ui.SecordActivity;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

@ForActivityContext
public class MainActivity extends AppCompatActivity {

    @SchoolType
    @Inject
    School school;

    @SchoolType(type = 1)
    @Inject
    School school2;

    @Inject
    Student student;

    private Button btnJump;

    @Inject
    ShowUtils showUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainComponent.builder().mainModules(new MainModules(this)).build()
                .inject(this);

//        ((MyApplicaiton)getApplication()).getAppComponent().inject(this);


        Log.e("daggerLog","school name:【"+school.getName()+"】 address：【"+school.getAddress()+"】");
//        Log.e("daggerLog","student info:【"+student.getDefaultInfo()+"】");

        Log.e("daggerLog","school add >【"+school+"】");
        Log.e("daggerLog","school2 add >【"+school2+"】");
        Log.e("daggerLog","school2 name:【"+school2.getName()+"】 address：【"+school2.getAddress()+"】");

        Log.e("daggerLog","showUtils add >【"+showUtils+"】");


        btnJump = findViewById(R.id.btn_jump);
        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecordActivity.class));
            }
        });
    }
}
