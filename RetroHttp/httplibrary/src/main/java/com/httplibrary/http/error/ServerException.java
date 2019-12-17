package com.httplibrary.http.error;

/**
 * Title:异常数据承载实体
 * Description:
 *
 * Created by pei
 * Date: 2017/10/31
 */
public class ServerException extends Exception {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
