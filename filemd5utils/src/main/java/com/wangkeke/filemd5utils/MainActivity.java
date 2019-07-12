package com.wangkeke.filemd5utils;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Button btnGetMd5, btnSend;
    private TextView tvMd5Show, tvApkInfo;
    private EditText etMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkApkInfo();
    }

    private void checkApkInfo() {
        PackageManager pm = this.getPackageManager();

        String fileName = "faguanCcRjudge.apk";
        String fileStoreDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xsyUpdate";

        File file = new File(fileStoreDir, fileName);
        Log.e("wangkeke", "file path = " + file.getAbsolutePath());

        PackageInfo info = pm.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String packageName = appInfo.packageName;  //得到安装包名称
            String version = info.versionName;//获取安装包的版本号
            tvApkInfo.setText("packageName : " + packageName + "\n" + "version : " + version);
        }
    }

    private void initView() {

        btnGetMd5 = findViewById(R.id.btn_getmd5_file);
        btnSend = findViewById(R.id.btn_send_email);
        tvMd5Show = findViewById(R.id.tv_md5_show);
        tvApkInfo = findViewById(R.id.tv_apk_info);
        etMail = findViewById(R.id.et_mail);
        btnGetMd5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        String md5Value = getMd5Str();
                        if (TextUtils.isEmpty(md5Value)) {
                            md5Value = "获取MD5失败，请检查！";
                        }
                        e.onNext(md5Value);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String md5Value) throws Exception {
                                tvMd5Show.setText("安装包MD5值：" + md5Value);
                            }
                        });
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = etMail.getText().toString().trim();
                if (TextUtils.isEmpty(mail) || !mail.contains("@")) {
                    Toast.makeText(MainActivity.this, "邮件地址为空或者地址不合法！", Toast.LENGTH_SHORT).show();
                    return;
                }

                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        String md5Value = getMd5Str();
                        if (TextUtils.isEmpty(md5Value)) {
                            md5Value = "获取MD5失败，请检查！";
                        }
                        e.onNext(md5Value);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String md5Value) throws Exception {
                                try {
                                    setEmail(md5Value);
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, "未检测到邮件客户端，请安装邮件客户端，并绑定账号！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public String getMd5Str() {
        String fileName = "faguanCcRjudge.apk";
        String fileStoreDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "xsyUpdate";

        File file = new File(fileStoreDir, fileName);
        Log.e("wangkeke", "file path = " + file.getAbsolutePath());
        if (file.exists()) {
            String md5Str = Md5Helper.getFileMD5(file);
            Log.e("wangkeke", "file md5 value = " + md5Str);
            return md5Str;
        } else {
            Toast.makeText(this, "安装包不存在，请上传到sd卡指定目录！", Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    private void setEmail(String md5) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:1039163285@qq.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "获取到安装包MD5值，请查收！");
        data.putExtra(Intent.EXTRA_TEXT, md5);
        startActivity(data);
    }
}
