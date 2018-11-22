package com.wangkeke.mvpdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements IControlActivityVIew{

    private ControlPresenter controlPresenter;

    private ModelViewTest modelViewTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initPresenter();
    }

    private void initView() {
        modelViewTest = findViewById(R.id.model_view_test);

        modelViewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlPresenter.changeViewTextColor(Color.parseColor("#434556"));
            }
        });

        modelViewTest.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                controlPresenter.viewChangeActivityToToast("我改变了view的状态");
                return false;
            }
        });
    }

    private void initPresenter() {

        controlPresenter = new ControlPresenter(modelViewTest,this);

    }

    @Override
    public void closeActivity() {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
