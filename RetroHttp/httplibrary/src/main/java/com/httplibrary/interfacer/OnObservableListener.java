package com.httplibrary.interfacer;

import io.reactivex.Observable;

/**
 * Title: Observable 同时发起多个请求的监听(并发请求)
 *
 * description:
 * autor:pei
 * created on 2021/1/20
 */
public interface OnObservableListener {

    Observable getObservable(int index);
}
