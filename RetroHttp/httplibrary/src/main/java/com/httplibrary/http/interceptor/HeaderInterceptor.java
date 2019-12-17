package com.httplibrary.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Admin on 2017/5/13.
 * header拦截器，对Header统一处理，涉及到header加密的也在此处理
 */
public class HeaderInterceptor implements Interceptor{

    private HeaderInterceptorListener mHeaderInterceptorListener;

    public HeaderInterceptor(HeaderInterceptorListener listener){
        this.mHeaderInterceptorListener=listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(mHeaderInterceptorListener!=null){
            request=mHeaderInterceptorListener.diposeRequest(request);
        }

//        Request request = chain.request();
//        Request.Builder builder = request.newBuilder();
//        //所有通讯增加版本号的请求头
//        String modelName= AppUtil.getDeviceModel().replaceAll(" ","");
//        String userAgent="Android"+modelName+"/"+ AppUtil.getVersionName();
//        builder.addHeader("User-Agent",userAgent);
//        request = builder.build();

//        String timestamp = Constants.getTimestamp();
//        Map<String, String> mapSign = new HashMap<>();
//        mapSign.put("timestamp", timestamp);
//        Set<String> names = url.queryParameterNames();
//        if (names != null) {
//            for (String name : names) {
//                mapSign.put(name, url.queryParameter(name));
//            }
//        }
//        Request.Builder builder = request.newBuilder();
//        builder.addHeader("apikey", Constants.APIKEY);
//        builder.addHeader("timestamp", timestamp);
//        builder.addHeader("signature", RequestUtils.getSignature(mapSign));
//
//        //登录激活不需要传递Token
//        String activateUrl = RestApiRetrofit.BASE_URL + "activate";
//        String loginUrl = RestApiRetrofit.BASE_URL + "login";
//        HttpUrl httpUrl = request.url();
//        String requestUrl = httpUrl.toString();
//        boolean isActivateOrLogin = activateUrl.equals(requestUrl) || loginUrl.equals(requestUrl);
//        if (!isActivateOrLogin && StringUtil.isNotEmpty(getToken())) {
//            builder.addHeader("token", getToken());
//        }
//        //获取版本号需要传ANDROID
//        String viersionUrl = RestApiRetrofit.BASE_URL + "version";
//        boolean isGetVersion = viersionUrl.equals(requestUrl);
//        if (isGetVersion) {
//            builder.addHeader("plateform", Constants.PLATEFORM);
//        }
//        request = builder.build();
        return chain.proceed(request);
    }

    public interface HeaderInterceptorListener{

        Request diposeRequest(Request request);
    }
}
