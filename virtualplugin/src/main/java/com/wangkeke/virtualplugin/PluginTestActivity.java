package com.wangkeke.virtualplugin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PluginTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_test);

        findViewById(R.id.iv_jump_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.wangkeke.virtualapkhost", "com.wangkeke.virtualapkhost.MainActivity");
                startActivity(intent);
            }
        });
    }
}
