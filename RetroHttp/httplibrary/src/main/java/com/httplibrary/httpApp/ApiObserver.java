//package com.httplibrary.httpApp;
//
//import com.httplibrary.http.error.ServerException;
//import com.httplibrary.http.rx.RxObserver;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * ############ 此类为模板代码,勿删 ############
// *
// * Description:通讯返回结果集的统一处理
// *
// * Author:pei
// * Date: 2019/3/22
// */
//public abstract class ApiObserver<T> extends RxObserver<T> {
//
//    //自定义统一code处理及提示语
//    public static final int CONNECT_TIME_OUT_CODE=-4; //服务器连接超时
//
//
//    /**自定义统一code处理及提示语**/
//    private Map<Integer, String> getResultMap(){
//        Map<Integer, String>map=new HashMap<>();
//
//        //自定义异常code及提示语
//        map.put(ApiObserver.CONNECT_TIME_OUT_CODE,"服务器连接超时");
//
//        return map;
//    }
//
//
//    /**返回失败的统一处理**/
//    @Override
//    public void unifiedError(ServerException serverException) {
////        LogUtil.i("=======ApiObserver返回错误统一处理========");
////        LogUtil.i("=======原始返回数据错误===errorCode="+serverException.getCode()+",   errorMessage="+serverException.getMessage());
////
////        //处理"connect timed out"提示语
////        if("connect timed out".equals(serverException.getMessage())){
////            serverException.setCode(ApiObserver.CONNECT_TIME_OUT_CODE);
////            serverException.setMessage(ApiObserver.CONNECT_TIME_OUT_MESSAGE);
////            doError(serverException);
////        }
////        LogUtil.i("=======返回数据错误===errorCode="+serverException.getCode()+",   errorMessage="+serverException.getMessage());
//    }
//
//    /**返回成功的统一处理**/
//    @Override
//    public void unifiedSuccess(Object obj) {
////        LogUtil.i("=======ApiObserver返回成功统一处理========");
//
//    }
//
//
//
//}