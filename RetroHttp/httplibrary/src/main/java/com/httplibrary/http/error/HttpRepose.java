package com.httplibrary.http.error;

import com.httplibrary.R;
import com.httplibrary.http.rx.RxObserver;
import com.httplibrary.util.StringUtil;

/**
 * Title:统一处理提示语
 * Description:
 *
 * Created by pei
 * Date: 2017/10/31
 */
public class HttpRepose {

    public static String APPEND_MESSAGE="(原错误为:%s)";

    //当前通讯为http请求头不兼容,请更换为https请求头[对应自定义code=ErrorCode.HTTP_HPPS_CODE]
    private static final String HTTP_HPPS_MESSAGE="CLEARTEXT communication to appxd.inm.cn not permitted by network security policy";
    //通讯连接超时[对应自定义code=ErrorCode.CONNECT_TIME_OUT_CODE]
    private static final String CONNECT_TIME_OUT_MESSAGE="connect timed out";

    /**发生错误时，错误提示的统一处理**/
    public static ServerException handle(ServerException exception) {
        int code = exception.getCode();
        String message = exception.getMessage();

        switch (code) {
            case ErrorCode.NO_NET_WORK://请连接网络
            case ErrorCode.NETWORK_EXCEPTION_CODE://通讯数据解析异常
                message = ErrorCode.getErrorMessageByCode(code);
                break;
            case ErrorCode.SERVER_FAILED_CODE://通讯发生错误,会有自己的message,通讯进入onError()流程
                //针对此类错误中一些特殊情况的处理
                ServerException specialException=getSpecialException(code,message);
                if(specialException!=null){
                    code=specialException.getCode();
                    message=specialException.getMessage();
                }
                break;
            default:
                break;
        }
        exception.setCode(code);
        exception.setMessage(message);
        return exception;
    }

    /**针对ErrorCode.SERVER_FAILED_CODE这类错误中的一些特殊错误的处理**/
    private static ServerException getSpecialException(int code, String message) {
        String lastMessage = String.format(HttpRepose.APPEND_MESSAGE,message);
        if (StringUtil.isNotEmpty(message)) {
            switch (message) {
                case HTTP_HPPS_MESSAGE://当前通讯为http请求头不兼容,请更换为https请求头
                    code = ErrorCode.HTTP_HPPS_CODE;
                    message = ErrorCode.getErrorMessageByCode(code) + lastMessage;
                    break;
                case CONNECT_TIME_OUT_MESSAGE://通讯连接超时
                    code = ErrorCode.CONNECT_TIME_OUT_CODE;
                    message = ErrorCode.getErrorMessageByCode(code) + lastMessage;
                    break;
                default:
                    break;
            }
            ServerException exception = new ServerException();
            exception.setCode(code);
            exception.setMessage(message);
            return exception;
        }
        return null;
    }

}
