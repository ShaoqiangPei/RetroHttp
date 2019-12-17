package com.httplibrary.http.interceptor;

import com.httplibrary.util.RetroLog;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Title:调用系统log拦截器
 * Description:
 * <p>
 * Created by pei
 * Date: 2018/3/13
 */
public class SystemLogger implements HttpLoggingInterceptor.Logger{

    public HttpLoggingInterceptor getHttpLoggingInterceptor(){
        HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor(SystemLogger.this);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    @Override
    public void log(String message) {
        RetroLog.w("====打印系统级log===message=="+message);
    }


}
