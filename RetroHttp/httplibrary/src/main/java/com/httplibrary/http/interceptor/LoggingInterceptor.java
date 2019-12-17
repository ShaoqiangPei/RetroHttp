package com.httplibrary.http.interceptor;


import com.httplibrary.util.RetroLog;

import java.io.IOException;
import java.nio.charset.Charset;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by Admin on 2017/5/13.
 * 自定义log打印拦截器
 */

public class LoggingInterceptor implements Interceptor{

    private long mRequestTime;//请求发起的时间
    private long mResponseTime;//收到响应的时间

    @Override
    public Response intercept(Chain chain) throws IOException {
        //发送请求参数
        mRequestTime=requestParams(chain);
        //接收返回结果
        Response response=getResponseParams(chain);

        return response;
    }

    /**发送请求参数**/
    private long requestParams(Chain chain) throws IOException {
        Request request = chain.request();

        long requestTime = System.currentTimeMillis();//请求发起的时间
        HttpUrl requestUrl = request.url();
        String requestMethod = request.method();
        Connection connection = chain.connection();
        Headers requestHeaders = request.headers();
        String requestParams = null;

        RequestBody requestBody = request.body();
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            requestParams = buffer.readString(charset);
        }

        RetroLog.w("=====通讯请求数据====requestUrl===" + requestUrl);
        RetroLog.w("=====通讯请求数据====connection===" + connection);
        RetroLog.w("=====通讯请求数据====requestHeaders===" + requestHeaders);
        RetroLog.w("=====通讯请求数据====requestMethod===" + requestMethod);
        RetroLog.w("=====通讯请求数据====requestParams===" + requestParams);

        return requestTime;
    }

    /**接收返回结果**/
    private Response getResponseParams(Chain chain) throws IOException {
        mResponseTime = System.currentTimeMillis();//收到响应的时间
        Response response = chain.proceed(chain.request());
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        long delayTime=mResponseTime-mRequestTime;//单位毫秒
        double delaySecondTime=((double)delayTime)/1000;//单位秒

        RetroLog.w("=====通讯返回数据====delayTime==="+delayTime+"("+delaySecondTime+"秒)");
        RetroLog.w("=====通讯返回数据====content==="+content);

        if (response.body() != null) {
            // 深坑,打印body后原ResponseBody会被清空，需要重新设置body
            ResponseBody body = ResponseBody.create(mediaType, content);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }

}
