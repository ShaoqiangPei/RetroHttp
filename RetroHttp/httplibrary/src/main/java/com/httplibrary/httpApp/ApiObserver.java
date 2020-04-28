//package com.httplibrary.httpApp;
//
//import com.httplibrary.http.error.ErrorCode;
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
//    /***注：以下code及提示语定义仅作范例参考,实际定义以你项目通讯需求为准***/
//
//    //自定义统一code处理及提示语[自定义code不要和ErrorCode类中的重复]
//    public static final int XXXX_EXCEPTION_CODE=-101; //服务器xxx错误
//
//    /**自定义统一code处理及提示语**/
//    @Override
//    public Map<Integer, String> getResultMap() {
//        Map<Integer, String> map = new HashMap<>();
//
//        //自定义异常code及提示语的统一处理
//        map.put(ApiObserver.XXXX_EXCEPTION_CODE, "服务器xxx错误");
//
//
//        return map;
//    }
//
//    /**返回失败的统一处理**/
//    @Override
//    public ServerException unifiedError(ServerException serverException) {
//        LogUtil.i("=====ApiObserver返回失败统一处理===原错误code="+serverException.getCode());
//
//        //对返回错误做统一处理的逻辑,如:
////        //处理"xxxxxxxxxx"提示语
////        if ("xxxxxxxxxx".equals(serverException.getMessage())) {
////            serverException.setCode(ApiObserver.XXXX_EXCEPTION_CODE);
////            String errorMsg=getResultMap().get(ApiObserver.XXXX_EXCEPTION_CODE);
////            serverException.setMessage(String.format(HttpRepose.APPEND_MESSAGE,errorMsg));
////        }
//
//
//
//        LogUtil.e("=====返回数据错误(统一处理后):errorCode="+serverException.getCode()+", errorMessage="+serverException.getMessage());
//        return serverException;
//    }
//
//    /**返回成功的统一处理**/
//    @Override
//    public Object unifiedSuccess(Object obj) {
//        String successResult=obj==null?"null":obj.toString();
//        LogUtil.i("=====ApiObserver返回成功统一处理===原数据obj="+successResult);
//
//        //对返回成功结果做统一处理的逻辑
//        //若返回结果定义为 ErrorCode.INTERCEPT_RESULT,则表示结果经拦截不再传到界面去
//        //若返回结果为object(不定义为ErrorCode.INTERCEPT_RESULT)则表示返回数据经统一处理后，还会到界面做进一步处理
//        //......
//
//
//        ResponseData responseData= (ResponseData) obj;
//        int code=responseData.getCode();
//        if(code==0){
//            //关闭网络加载框
//            LoadingDialog.getInstance().hideLoading();
//
//            //统一处理(拦截结果不传到界面)的逻辑
//            //...
//
//            LogUtil.i("=====到底怎么回事啊啊啊啊===");
//
//            //此处做拦截统一处理，返回 ErrorCode.INTERCEPT_RESULT,表示结果不再传到界面去
//            return ErrorCode.INTERCEPT_RESULT;
//        }
//
//
//
//        LogUtil.i("=====返回数据成功(统一处理后):obj="+obj);
//        return obj;
//    }
//
//}