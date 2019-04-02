package com.wangkeke.repluginone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.loader.a.PluginActivity;

public class PluginOneActivity extends PluginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(null != getIntent().getBundleExtra("bundle")){
            String hostStr = getIntent().getBundleExtra("bundle").getString("host");
            Log.e("wangkeke","hostStr = "+hostStr);
        }
    }
}
