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

    public static final int NO_NET_WORK=-1;//网络未连接，请开启网络后使用
    public static final int NETWORK_EXCEPTION_CODE=-2;//通讯数据解析异常:通讯onNext(Object obj)返回数据为空
    public static final int SERVER_FAILED_CODE=-3;//通讯发生错误,会有自己的message,通讯进入onError()流程

    private static Map<Integer, String> getErrorMap(){
        mErrorMap.put(NO_NET_WORK,"网络未连接,请开启网络后使用!");
        mErrorMap.put(NETWORK_EXCEPTION_CODE,"通讯数据解析异常");
        return mErrorMap;
    }

    public static String getErrorMessageByCode(int errorCode){
        return getErrorMap().get(errorCode);
    }

}
