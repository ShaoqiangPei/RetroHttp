package com.httplibrary.app;

import android.app.Application;

/**
 * Title:retrohttp初始化类
 * description:
 * autor:pei
 * created on 2019/12/17
 */
public class RetroConfig {

    private Application mApplication;

    private RetroConfig(){}

    private static class Holder {
        private static RetroConfig instance = new RetroConfig();
    }

    public static RetroConfig getInstance() {
        return Holder.instance;
    }

    /**初始化赋值**/
    public void init(Application application){
        this.mApplication=application;
    }

    /**获取项目上下文**/
    public Application getApplication() {
        if(mApplication==null){
            throw new NullPointerException("====RetroHttp需要初始化：RetroConfig.getInstance.init(Application application)===");
        }
        return mApplication;
    }

}
