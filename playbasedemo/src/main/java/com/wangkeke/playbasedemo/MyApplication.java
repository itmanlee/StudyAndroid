package com.wangkeke.playbasedemo;

import android.app.Application;

import com.kk.taurus.exoplayer.ExoMediaPlayer;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;

public class MyApplication extends Application {

    public static final int PLAN_ID_EXO = 2;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化库
        PlayerLibrary.init(this);

        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_EXO, ExoMediaPlayer.class.getName(), "ExoPlayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_EXO);

        ExoMediaPlayer.init(this);
    }
}
