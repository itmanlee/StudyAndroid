package com.wangkeke.wlmusicdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.ywl5320.wlmedia.WlMedia;
import com.ywl5320.wlmedia.enums.WlCodecType;
import com.ywl5320.wlmedia.enums.WlMute;
import com.ywl5320.wlmedia.enums.WlPlayModel;
import com.ywl5320.wlmedia.enums.WlScaleType;
import com.ywl5320.wlmedia.listener.WlOnCompleteListener;
import com.ywl5320.wlmedia.listener.WlOnErrorListener;
import com.ywl5320.wlmedia.listener.WlOnPreparedListener;
import com.ywl5320.wlmedia.listener.WlOnTimeInfoListener;
import com.ywl5320.wlmedia.listener.WlOnVideoViewListener;
import com.ywl5320.wlmedia.util.WlTimeUtil;
import com.ywl5320.wlmedia.widget.WlSurfaceView;

public class MainActivity extends AppCompatActivity {

//    private WlMusic wlMusic;
    private WlMedia wlMedia,videoMedia;

    private WlSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surface);

        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startPlay();


                startPlayTwo();
            }
        });


        findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                wlMusic.setPlaySpeed(2.0f);
            }
        });

    }

    private void startPlayTwo() {

        videoMedia = new WlMedia();// 可支持多实例播放（主要对于音频，视频实际验证效果不佳）
        videoMedia.setPlayModel(WlPlayModel.PLAYMODEL_AUDIO_VIDEO);//声音视频都播放
        videoMedia.setCodecType(WlCodecType.CODEC_MEDIACODEC);//优先使用硬解码
        videoMedia.setMute(WlMute.MUTE_CENTER);//立体声
        videoMedia.setVolume(80);//80%音量
        videoMedia.setPlayPitch(1.0f);//正常速度
        videoMedia.setPlaySpeed(1.0f);//正常音调
        videoMedia.setTimeOut(30);//网络流超时时间
        videoMedia.setShowPcmData(true);//回调返回音频pcm数据
        surfaceView.setWlMedia(videoMedia);//给视频surface设置播放器

//异步准备完成后开始播放
        videoMedia.setOnPreparedListener(new WlOnPreparedListener() {
            @Override
            public void onPrepared() {
                Log.e("wangkeke","---------------- onPrepared");
                videoMedia.setVideoScale(WlScaleType.SCALE_16_9);//设置16:9的视频比例
                videoMedia.start();//开始播放
                double duration = videoMedia.getDuration();//获取时长
            }
        });

        surfaceView.setOnVideoViewListener(new WlOnVideoViewListener() {
            @Override
            public void initSuccess() {
                videoMedia.setSource("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
                videoMedia.prepared();
            }

            @Override
            public void moveSlide(double value) {
//                tvTime.setText(WlTimeUtil.secdsToDateFormat((int)value) + "/" + WlTimeUtil.secdsToDateFormat((int)wlMedia.getDuration()));
            }

            @Override
            public void movdFinish(double value) {
                videoMedia.seek((int) value);
            }
        });

        videoMedia.setOnErrorListener(new WlOnErrorListener() {
            @Override
            public void onError(int code, String msg) {
                Log.e("wangkeke","code = "+code + "   msg = "+msg);
            }
        });

//设置url源
//        videoMedia.setSource("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
//        videoMedia.prepared();//异步准备


    }

    private void startPlay(){
        // 可支持多实例播放（主要对于音频，视频实际验证效果不佳）
        wlMedia = new WlMedia();
        wlMedia.setPlayModel(WlPlayModel.PLAYMODEL_ONLY_AUDIO);//声音视频都播放
        wlMedia.setPlaySpeed(1.0f);//正常音调

        //异步准备完成后开始播放
        wlMedia.setOnPreparedListener(new WlOnPreparedListener() {
            @Override
            public void onPrepared() {
                // wlMedia.setVideoScale(WlScaleType.SCALE_16_9);//设置16:9的视频比例
//                wlMedia.start();//开始播放
                double duration = wlMedia.getDuration();//获取时长
                Log.e("tag","prepare duration = "+duration);
            }
        });

        wlMedia.setOnTimeInfoListener(new WlOnTimeInfoListener() {
            @Override
            public void onTimeInfo(double currentTime) {
                Log.e("tag","currentTime = "+currentTime);
            }
        });

        wlMedia.setOnCompleteListener(new WlOnCompleteListener() {
            @Override
            public void onComplete() {
                Log.e("tag","播放完成");
            }
        });

        //设置url源
        wlMedia.setSource("http://ccr.sifayun.com/home/record/mp4/000/a6f5e2ff-4374-4230-b71c-cb36759b517d.aac");
        wlMedia.prepared();//异步准备

















        /*wlMusic = WlMusic.getInstance();
        wlMusic.setSource("http://ccr.sifayun.com/home/record/mp4/000/a6f5e2ff-4374-4230-b71c-cb36759b517d.aac"); //设置音频源
//        wlMusic.setSource("http://sc.sycdn.kuwo.cn/resource/n2/82/88/1267104818.mp3"); //设置音频源
//        wlMusic.setCallBackPcmData(true);//是否返回音频PCM数据
//        wlMusic.setShowPCMDB(true);//是否返回音频分贝大小
//        wlMusic.setPlayCircle(true); //设置不间断循环播放音频
//        wlMusic.setVolume(65); //设置音量 65%
        wlMusic.setPlaySpeed(2.0f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
//        wlMusic.setPlayPitch(1.5f); //设置播放速度 (1.0正常) 范围：0.25---4.0f
//        wlMusic.setMute(MuteEnum.MUTE_CENTER); //设置立体声（左声道、右声道和立体声）
//        wlMusic.setConvertSampleRate(SampleRateEnum.RATE_44100);//设定恒定采样率（null为取消）
        wlMusic.prePared();//准备开始

        wlMusic.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                wlMusic.start(); //准备完成开始播放
                Log.e("wangkeke","prepare duration = "+wlMusic.getDuration());
            }
        });

        wlMusic.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(boolean load) {

                Log.e("wangkeke","load duration = "+wlMusic.getDuration());
            }
        });

        wlMusic.setOnInfoListener(new OnInfoListener() {
            @Override
            public void onInfo(TimeBean timeBean) {
                Log.e("progress",timeBean.getCurrSecs()+"  ---  "+timeBean.getTotalSecs());
            }
        });*/


    }
}
