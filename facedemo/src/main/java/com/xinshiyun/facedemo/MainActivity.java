package com.xinshiyun.facedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.facedetector.Rectangle;
import io.fotoapparat.facedetector.processor.FaceDetectorProcessor;
import io.fotoapparat.facedetector.view.RectanglesView;
import io.fotoapparat.view.CameraView;

public class MainActivity extends AppCompatActivity {

    private RectanglesView rectanglesView;
    private CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rectanglesView = findViewById(R.id.rectanglesView);
        cameraView = findViewById(R.id.cameraView);

        FaceDetectorProcessor processor = FaceDetectorProcessor.with(this)
                .listener(new FaceDetectorProcessor.OnFacesDetectedListener() {
                    @Override
                    public void onFacesDetected(List<Rectangle> faces) {
                        rectanglesView.setRectangles(faces);
                    }
                })
                .build();


        Fotoapparat.with(this)
                .into(cameraView)
                // the rest of configuration
                .frameProcessor(processor)
                .build();
    }
}
