package com.wangkeke.dragfast;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private TextView tvShow;

    @Inject
    String className;

    @Inject
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        tvShow = findViewById(R.id.tv_show);

        tvShow.setText("className = "+className+"\n"+"sp = "+sp);
    }
}
