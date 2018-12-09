package com.wangkeke.goomvpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    private MainContract.Presenter mainPresenter;

    private TextView tvShow;

    private ProgressBar progressBar;

    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvShow = findViewById(R.id.tv_show);
        tvShow.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvShow.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSend.setVisibility(View.GONE);
                mainPresenter.sendRequest(1,5);
            }
        });

        mainPresenter = new MainPresenter(this);
    }

    @Override
    public void showLoadingDialoig() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingDialog() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showData(String data) {
        tvShow.setVisibility(View.VISIBLE);
        tvShow.setText(data);
    }

    @Override
    public void failed(String errorMsg) {
        //请求失败后的处理
        Toast.makeText(this, ""+errorMsg, Toast.LENGTH_SHORT).show();
    }

}
