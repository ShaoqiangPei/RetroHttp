package com.httplibrary.http.error;

import com.httplibrary.http.rx.RxObserver;

/**
 * Title:统一处理提示语
 * Description:
 *
 * Created by pei
 * Date: 2017/10/31
 */
public class HttpRepose {

    /**发生错误时，错误提示的统一处理**/
    public static ServerException handle(ServerException exception, RxObserver rxObserver){
        int code = exception.getCode();
        String message = exception.getMessage();

        switch (code) {
            case ErrorCode.NO_NET_WORK://请连接网络
            case ErrorCode.NETWORK_EXCEPTION_CODE://通讯数据解析异常
                message=ErrorCode.getErrorMessageByCode(code);
                break;
            default:
                break;
        }
        exception.setMessage(message);
        rxObserver.unifiedError(exception);
      return exception;
    }

}
