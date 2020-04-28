package com.httplibrary.http.error;

import java.util.HashMap;
import java.util.Map;

/**
 * Title:错误提示 code-message
 * Description:
 *
 * Created by pei
 * Date: 2017/10/31
 */
public class ErrorCode {

    private static Map<Integer, String> mErrorMap=new HashMap<>();

    /***
     * 当ApiObserver类中unifiedSuccess(Object obj)方法返回此字符串时,则数据不再往下传递
     * 用于做整个通讯收到结果拦截的统一处理
     */
    public static final String INTERCEPT_RESULT="intercept_result_retro_http";

    /**通讯发生错误,会有自己的message,通讯进入onError()流程**/
    public static final int SERVER_FAILED_CODE=-1;//通讯发生错误,会有自己的message,通讯进入onError()流程

    /**此库内置code定义**/
    public static final int NO_NET_WORK=-2;//网络未连接，请开启网络后使用
    public static final int NETWORK_EXCEPTION_CODE=-3;//通讯数据解析异常:通讯onNext(Object obj)返回数据为空
    public static final int HTTP_HPPS_CODE=-4;//通讯为http请求头不兼容,请更换为https请求头
    public static final int CONNECT_TIME_OUT_CODE=-5;//通讯连接超时


    private static Map<Integer, String> getErrorMap(){
        mErrorMap.put(NO_NET_WORK,"网络未连接,请开启网络后使用!");
        mErrorMap.put(NETWORK_EXCEPTION_CODE,"通讯数据解析异常");
        mErrorMap.put(HTTP_HPPS_CODE,"通讯为http请求头不兼容,请更换为https请求头");
        return mErrorMap;
    }

    public static String getErrorMessageByCode(int errorCode){
        return getErrorMap().get(errorCode);
    }

}
