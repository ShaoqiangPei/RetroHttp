package com.retrohttp.app;

import android.app.Application;

import com.httplibrary.app.RetroConfig;

/**
 * Title:
 * description:
 * autor:pei
 * created on 2021/4/1
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化 RetroHttp
        RetroConfig.getInstance().init(this)
                //是否开启网络log打印
                .setHttpLog(true);
    }

}
