package com.httplibrary.interfacer;

import retrofit2.Retrofit;

/**
 * Title:Retrofit 接口
 * Description:
 *
 * Created by pei
 * Date: 2017/11/1
 */
public interface IRetrofitor {

    Retrofit getRetrofit();

    Object getApiService();

}
