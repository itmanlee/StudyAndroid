package com.wangkeke.recyclerviewsnap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imgOld,imgNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView2();
    }

    private void initView() {

        imgOld = findViewById(R.id.default_icon);
        imgNew = findViewById(R.id.over_img);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        imgOld.setImageBitmap(bitmap);


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[2*bitmap.getWidth()*bitmap.getHeight()];


        bitmap.getPixels(pixels,0,2*width,0,0,width,height);
        bitmap.getPixels(pixels,width,2*width,0,0,width,height);

        Bitmap newBitmap = Bitmap.createBitmap(pixels,0,2*width,2*width,height, Bitmap.Config.ARGB_8888);

        imgNew.setImageBitmap(newBitmap);

    }

    private void initView2() {

        imgOld = findViewById(R.id.default_icon);
        imgNew = findViewById(R.id.over_img);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.test);

        imgOld.setImageBitmap(bitmap);


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[bitmap.getWidth()*bitmap.getHeight()];

        bitmap.getPixels(pixels, 0, width/2, width/2, 0, width/2, height/2);

        Bitmap newBitmap = Bitmap.createBitmap(pixels,0,width,width,height, Bitmap.Config.ARGB_8888);

        imgNew.setImageBitmap(newBitmap);

    }
}
