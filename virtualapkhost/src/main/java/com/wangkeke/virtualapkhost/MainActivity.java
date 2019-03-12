package com.wangkeke.virtualapkhost;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.didi.virtualapk.PluginManager;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpPluActivity();
            }
        });

        findViewById(R.id.btn_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void jumpPluActivity() {

        if (PluginManager.getInstance(this).getLoadedPlugin("com.wangkeke.virtualplugin") == null) {
            Toast.makeText(getApplicationContext(),
                    "插件未加载,请尝试重启APP", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.setClassName("com.wangkeke.virtualplugin", "com.wangkeke.virtualplugin.PluginTestActivity");
        startActivity(intent);
    }


}
