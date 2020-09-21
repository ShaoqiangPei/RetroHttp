package com.httplibrary.http.retrofit;

import com.httplibrary.http.interceptor.CacherInterceptor;
import com.httplibrary.http.interceptor.HeaderInterceptor;
import com.httplibrary.http.interceptor.LoggingInterceptor;
import com.httplibrary.http.interceptor.SystemLogger;
import com.httplibrary.http.ssl.TrustAllCerts;
import com.httplibrary.interfacer.IRetrofitor;
import com.httplibrary.util.RetroLog;
import com.httplibrary.util.StringUtil;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:网络通讯超类
 *
 * Author:pei
 * Date: 2019/3/21
 */
public abstract class SuperRetrofitor implements IRetrofitor {

    private int mConnectTimeOut=0;//设置连接超时时间,单位秒
    private int mReadTimeOut=0;//设置读取超时时间,单位秒
    private int mWriteTimeOut=0;//设置写的超时时间,单位秒
    private boolean mRetryConnect=HttpConfig.DEFAULT_RETRY_CONNECT;//错误重连,默认重连
    private boolean mSystemLog=HttpConfig.DEFAULT_SYSTEM_LOG;//默认不使用使用系统log拦截器
    private boolean mCustomerLog=HttpConfig.DEFAULT_CUSTOMER_LOG;//默认不使用使用自定义log拦截器
    private boolean mDefaultSSL=HttpConfig.DEFAULT_SSL;//默认进行ssl加密

    @Override
    public Retrofit getRetrofit() {
        return getBuilder(getUrl()).build();
    }

    @Override
    public Object getApiService() {
        return getRetrofit().create(getApiServiceClass());
    }

    private Retrofit.Builder getBuilder(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClientBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    private OkHttpClient.Builder getOkHttpClientBuilder() {
        Cache cache = new Cache(new File(HttpConfig.PATH_CACHE), HttpConfig.DEFAULT_CACHE_SIZE);

        mConnectTimeOut=mConnectTimeOut>0?mConnectTimeOut:HttpConfig.DEFAULT_CONNECT_TIMEOUT;
        mReadTimeOut=mReadTimeOut>0?mReadTimeOut:HttpConfig.DEFAULT_READ_TIMEOUT;
        mWriteTimeOut=mWriteTimeOut>0?mWriteTimeOut:HttpConfig.DEFAULT_WRITE_TIMEOUT;
        mRetryConnect=mRetryConnect==false?mRetryConnect:HttpConfig.DEFAULT_RETRY_CONNECT;

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(mConnectTimeOut, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(mReadTimeOut, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(mWriteTimeOut, TimeUnit.SECONDS)//设置写的超时时间
                .retryOnConnectionFailure(mRetryConnect)//错误重连
                .addInterceptor(new HeaderInterceptor(getHeaderInterceptorListener()))//设置Header
                .addNetworkInterceptor(new CacherInterceptor())//设置缓存
                .cache(cache);
        //默认不使用系统log拦截器
        if(isSystemLog()){
            //打印通讯配置参数
            printHttpConfigs();
            //调用系统log拦截器
            builder.addInterceptor((new SystemLogger()).getHttpLoggingInterceptor());
        }
        //默认不使用自定义log拦截器
        if(isCustomerLog()) {
            //打印通讯配置参数
            printHttpConfigs();
            //自定义Log打印
            builder.addInterceptor(new LoggingInterceptor());
        }
        //ssl加密(默认为true,即使用ssl加密)
        if(isDefaultSSL()){
            builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory(), new TrustAllCerts());
        }
        return builder;
    }

    /**打印通讯配置参数**/
    private void printHttpConfigs(){
        RetroLog.w("==通讯请求===设置连接超时:mConnectTimeOut="+mConnectTimeOut);
        RetroLog.w("==通讯请求===设置读取超时:mReadTimeOut="+mReadTimeOut);
        RetroLog.w("==通讯请求===设置写的超时:mWriteTimeOut="+mWriteTimeOut);
        RetroLog.w("==通讯请求===是否发起错误重连:mRetryConnect="+mRetryConnect);
        RetroLog.w("==通讯请求===是否使用系统log拦截器:mSystemLog="+mSystemLog);
        RetroLog.w("==通讯请求===是否使用自定义log拦截器:mCustomerLog="+mCustomerLog);
        RetroLog.w("==通讯请求===是否使用默认SSL加密:mDefaultSSL="+mDefaultSSL);
    }

    /**设置url**/
    protected String getUrl() {
        if (isTest()) {
            return StringUtil.isNotEmpty(getTestUrl()) ? getTestUrl() : getBaseTestUrl();
        }
        return StringUtil.isNotEmpty(getReleaseUrl()) ? getReleaseUrl() : getBaseReleaseUrl();
    }

    /**是否使用系统log拦截器**/
    private boolean isSystemLog() {
        return mSystemLog;
    }

    /**是否使用自定义log拦截器**/
    public boolean isCustomerLog() {
        return mCustomerLog;
    }

//    /**
//     * 是否打开自定义Log调试(仅供SuperRetrofitor子类使用)
//     *
//     * @param print true:打开调试log,  false:关闭调试log
//     */
//    protected void printHttpLog(boolean print){
//        RetroLog.setDebug(print);
//    }

    /**获取通讯是否使用默认ssl加密的标志**/
    private boolean isDefaultSSL() {
        return mDefaultSSL;
    }

    /**设置链接时间connectTime(单位秒)**/
    public SuperRetrofitor setConnectTimeOut(int seconds){
        this.mConnectTimeOut=seconds;
        return this;
    }

    /**设置读取超时时间(单位秒)**/
    public SuperRetrofitor setReadTimeOut(int seconds){
        this.mReadTimeOut=seconds;
        return this;
    }

    /**设置写的超时时间(单位秒)**/
    public SuperRetrofitor setWriteTimeOut(int seconds){
        this.mWriteTimeOut=seconds;
        return this;
    }

    /**设置链接错误是否重连**/
    public SuperRetrofitor setRetryConnect(boolean retryConnect){
        this.mRetryConnect=retryConnect;
        return this;
    }

    /**设置是否使用系统log拦截器**/
    public SuperRetrofitor setSystemLog(boolean systemLog) {
        this.mSystemLog = systemLog;
        return this;
    }

    /**设置是否使用自定义log拦截器**/
    public SuperRetrofitor setCustomerLog(boolean customerLog) {
        this.mCustomerLog = customerLog;
        return this;
    }

    /**设置通讯是否使用默认ssl加密**/
    public SuperRetrofitor setDefaultSSL(boolean defaultSSL) {
        this.mDefaultSSL = defaultSSL;
        return this;
    }

    /**设置header**/
    private HeaderInterceptor.HeaderInterceptorListener getHeaderInterceptorListener(){
        return new HeaderInterceptor.HeaderInterceptorListener() {
            @Override
            public Request diposeRequest(Request request) {
                Request.Builder builder = request.newBuilder();

                Map<String,String>map=getHeaderMap();
                if(map!=null&& !map.isEmpty()){
                    //遍历map
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        //获取map的key
                        String key = null;
                        Object objKey = entry.getKey();
                        if (objKey != null) {
                            key = objKey.toString();
                        }
                        //获取map中key对应的value
                        String value = null;
                        Object objValue = entry.getValue();
                        if (objValue != null) {
                            value = objValue.toString();
                        }
                        if (StringUtil.isNotEmpty(key)) {
                            builder.addHeader(key, value);
                        }
                    }
                }
                request = builder.build();
                return request;
            }
        };
    }

    public abstract Class<?> getApiServiceClass();//获取ApiService类

    public abstract Map<String,String> getHeaderMap();//设置header的map

    public abstract String getBaseReleaseUrl();//基准正式版url
    public abstract String getBaseTestUrl();//基准测试版url

    public abstract String getReleaseUrl();//正式版本url地址
    public abstract String getTestUrl();//测试版本地址

    public abstract boolean isTest();//是否为测试系统

}