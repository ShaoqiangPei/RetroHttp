//package com.httplibrary.httpApp;
//
//import com.httplibrary.http.interceptor.HeaderInterceptor;
//import com.httplibrary.http.retrofit.SuperRetrofitor;
//import com.httplibrary.util.RetroLog;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import okhttp3.Request;
//
///**
// * ############ 此类为模板代码,勿删 ############
// *
// * Description:网络通讯基类
// *
// * Author:pei
// * Date: 2019/3/21
// */
//public abstract class BaseRetrofitor extends SuperRetrofitor {
//
//    /**正试版base_release_url(注:此处url为范本,到时填实际url)**/
//    private static final String BASE_RELEASE_URL = "https://xxxx.xx.xx:xxxx/xxx/";//url以"/"结尾
//    /**测试版base_test_url(注:此处url为范本,到时填实际url)**/
//    private static final String BASE_TEST_URL = "https://xxx.xx.xx.xx:xxxx/xxxxxx/";//url以"/"结尾
//
//    @Override
//    public Class<?> getApiServiceClass() {
//        return ApiService.class;
//    }
//
//    @Override
//    public String getBaseReleaseUrl() {
//        return BASE_RELEASE_URL;
//    }
//
//    @Override
//    public String getBaseTestUrl() {
//        return BASE_TEST_URL;
//    }
//
//    /**设置统一的Header**/
//    @Override
//    public Map<String, String> getHeaderMap() {
//        Map<String, String> map = new HashMap<>();
//
////        //所有通讯增加自定义请求头,格式类似：
////        map.put(key, value);
//
//        return map;
//    }
//
//    @Override
//    public boolean isTest() {
//        return AppConfig.getInstance().isTest();
//    }
//
//    /**设置网络连接配置**/
//    public <T extends BaseRetrofitor>T getBaseRetrofitor(){
//        return (T) BaseRetrofitor.this
//                //设置是否关闭自定义log拦截器,一般与网络log打印控制一致(默认为false,即关闭log打印和拦截器)
//                .setCustomerLog(RetroConfig.getInstance().isHttpLog());
////                .setSystemLog(false)//设置是否使用系统拦截器打印log,默认false,不使用
////                .setDefaultSSL(true)//设置通讯是否使用SSL加密,默认true,使用
////                .setConnectTimeOut(30) //设置网络连接超时，默认30秒
////                .setReadTimeOut(30) //设置网络读取超时，默认30秒
////                .setWriteTimeOut(30) //设置网络写入超时，默认30秒
////                .setRetryConnect(true);//设置网络连接错误重连，默认重连
//    }
//
//}
