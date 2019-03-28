package com.wangkeke.playbasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kk.taurus.exoplayer.ExoMediaPlayer;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.entity.DecoderPlan;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.OnReceiverEventListener;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.widget.BaseVideoView;

public class MainActivity extends AppCompatActivity implements OnPlayerEventListener,OnReceiverEventListener{

    private BaseVideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mVideoView = findViewById(R.id.videoView);
        mVideoView.setOnPlayerEventListener(this);
        mVideoView.setOnReceiverEventListener(this);

        findViewById(R.id.btn_change_speed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setSpeed(1.0f);
            }
        });


        /*ReceiverGroup receiverGroup = new ReceiverGroup();
        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
        receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context));
        receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
        receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context));
        mVideoView.setReceiverGroup(receiverGroup);*/

        //设置一个事件处理器
        mVideoView.setEventHandler(new OnVideoViewEventHandler());

        mVideoView.setSpeed(2.0f);

//        mVideoView.switchDecoder(MyApplication.PLAN_ID_EXO);
        //设置DataSource
        mVideoView.setDataSource(new DataSource("http://ccr.sifayun.com/home/record/mp4/000/03897963-0a43-4973-a902-4d9ab5eb6b8d.mp4"));
//        mVideoView.setDataSource(new DataSource("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"));
        mVideoView.start();



    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }
}
