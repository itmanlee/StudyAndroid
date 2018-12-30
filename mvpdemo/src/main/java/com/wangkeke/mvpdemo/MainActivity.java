package com.wangkeke.mvpdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements LoginContract.View {

    private Button btnLogin;

    private ProgressBar progressBar;

    private TextView tvShow;

    @Inject
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerLoginActivityComponent.builder().loginModule(new LoginModule(this)).build().inject(this);

        initView();
    }

    private void initView() {

        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        tvShow = findViewById(R.id.tv_show_user);

        progressBar.setVisibility(View.GONE);
        tvShow.setVisibility(View.GONE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShow.setVisibility(View.GONE);
                presenter.postNetLogin("admin", "123456");
            }
        });
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(this, "错误信息：" + errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(User user) {
        Toast.makeText(this, "用户【" + user.getUserName() + "】登录成功", Toast.LENGTH_SHORT).show();
        tvShow.setVisibility(View.VISIBLE);
        tvShow.setText("用户名：" + user.getUserName() + "\n" + "密码：" + user.getPassword());
    }
}
