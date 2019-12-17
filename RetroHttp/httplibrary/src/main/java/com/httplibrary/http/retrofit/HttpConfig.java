package com.httplibrary.http.retrofit;

import com.httplibrary.app.RetroConfig;
import java.io.File;

/**
 * Created by Admin on 2017/5/12.
 * 网络通讯相关配置
 */
public class HttpConfig {

    public static final int DEFAULT_CACHE_SIZE = 150 * 1024 * 1024; // 50 MiB
    /*超时时间-默认10秒*/
    public static final int DEFAULT_CONNECT_TIMEOUT = 30;
    public static final int DEFAULT_READ_TIMEOUT = 30;
    public static final int DEFAULT_WRITE_TIMEOUT = 30;
    public static final boolean DEFAULT_RETRY_CONNECT=true;//默认错误重连
    public static final boolean DEFAULT_SYSTEM_LOG=false;//默认不使用使用系统log拦截器
    public static final boolean DEFAULT_CUSTOMER_LOG=false;//默认不使用使用自定义log拦截器
    public static final boolean DEFAULT_SSL=true;//默认通讯使用SSL加密

    /*有网情况下的本地缓存时间默认60秒*/
    public static final int DEFAULT_COOKIE_NETWORK_TIME = 0;
    /*无网络的情况下本地缓存时间默认30天*/
    public static final int DEFAULT_COOKIE_NO_NETWORK_TIME = 24 * 60 * 60 * 30;

    public static final String PATH_DATA = RetroConfig.getInstance().getApplication().getCacheDir().getAbsolutePath() + File.separator + "data";
    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

}
