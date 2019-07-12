package com.wangkeke.camerautilsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements CameraViewUtils.CameraMarkCallBack {

    private SurfaceView surfaceView;
    private CameraViewUtils cameraViewUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surface_view);

        cameraViewUtils = new CameraViewUtils(false, surfaceView,1, MainActivity.this);

        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public void markCallBack(String filePath) {

    }

    @Override
    public void hasHdmiData() {

    }

    @Override
    public void showDataSize(String resolution, String previewSize, String inputSize, int fps) {

    }

    @Override
    public void hdmiState(boolean state) {

    }
}
