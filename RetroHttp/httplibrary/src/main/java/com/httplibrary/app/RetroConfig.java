package com.httplibrary.app;

import android.app.Application;

import com.httplibrary.util.RetroLog;

/**
 * Title:retrohttp初始化类
 * description:
 * autor:pei
 * created on 2019/12/17
 */
public class RetroConfig {

    private Application mApplication;
    private boolean mHttpLog=false;//是否开启网络打印(默认不开启)

    private RetroConfig(){}

    private static class Holder {
        private static RetroConfig instance = new RetroConfig();
    }

    public static RetroConfig getInstance() {
        return Holder.instance;
    }

    /**初始化赋值(在项目的自定义Application中初始化)**/
    public RetroConfig init(Application application){
        this.mApplication=application;
        return RetroConfig.this;
    }

    /**
     * 是否打开网络库Log打印
     *
     * @param print true:打开调试log,  false:关闭调试log
     * @return
     */
    public void setHttpLog(boolean print){
        this.mHttpLog=print;
        //设置自定义网络打印开关
        RetroLog.setDebug(mHttpLog);
    }

    /**获取项目上下文**/
    public Application getApplication() {
        if(mApplication==null){
            throw new NullPointerException("====RetroHttp需要初始化：RetroConfig.getInstance.init(Application application)===");
        }
        return mApplication;
    }

    /**
     * 获取网络通讯log打印开关
     * @return true:打开调试log,  false:关闭调试log
     */
    public boolean isHttpLog() {
        return mHttpLog;
    }

}
