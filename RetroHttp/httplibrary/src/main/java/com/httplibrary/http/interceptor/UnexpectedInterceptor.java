package com.httplibrary.http.interceptor;

import android.os.Build;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Instruction:用于解决文件下载出现的
 *             unexpected end of stream on Connection{10.144.59.130:8291 错误
 *
 * Author:pei
 * Date: 2017/8/15
 * Description:
 */
public class UnexpectedInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Connection", "close");
            request = builder.build();
        }
        return chain.proceed(request);
    }
}
