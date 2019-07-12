package com.wangkeke.camerautilsdemo;

import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    private Button btnDelXwalk,btnDelDalvik,btnTombstones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnDelXwalk = findViewById(R.id.btn_del_xwalk_cache);
        btnDelDalvik = findViewById(R.id.btn_del_dalvik);
        btnTombstones = findViewById(R.id.btn_del_tombstones);

        btnDelXwalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("wangkeke","del --------------------- data");
                RootCmd.execRootCmd("rm -r data/data/com.xinshiyun.cloudcourtjudge/app_xwalkcore/Default");
            }
        });


        btnDelDalvik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RootCmd.execRootCmd("rm -r data/dalvik-cache");
            }
        });


        btnTombstones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RootCmd.execRootCmd("rm -r data/tombstones");
            }
        });

        long available = getAvailableInternalMemorySize();
        long total = getTotalInternalMemorySize();

        Log.e("wangkeke","available = "+available+"       total = "+total);
    }



    /**
     * 获取手机内部剩余存储空间
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }
    /**
     * 获取手机内部总的存储空间
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }
}
