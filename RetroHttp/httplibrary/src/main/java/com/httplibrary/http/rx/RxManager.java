package com.httplibrary.http.rx;

import com.httplibrary.http.error.ErrorCode;
import com.httplibrary.util.HttpTimeFlag;
import com.httplibrary.util.NetUtil;
import com.httplibrary.util.RetroLog;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Title:发起网络通讯及返回结果处理类
 * Description:
 *
 * Created by pei
 * Date: 2017/10/31
 */
public class RxManager {

    public static void connect(Observable observable, RxObserver rxObserver){
        if(NetUtil.isNetworkConnected()){//有网络
            RetroLog.w("============有网络===========");
            //设置通讯发起时时间戳
            setTimeTag(rxObserver);
            //对通讯线程做控制，并发起网络请求
            observable.subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                    .observeOn(Schedulers.io())//请求完成后在io线程中执行
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(rxObserver);
        }else{//无网络
            RetroLog.w("============无网络===========");
            //设置通讯发起时时间戳
            setTimeTag(rxObserver);
            //设置错误及提示语
            rxObserver.setExceptionInfo(null, ErrorCode.NO_NET_WORK);
        }
    }

    /**设置通讯发起时时间戳**/
    private static void setTimeTag(RxObserver rxObserver){
        //设置通讯发起时时间戳
        String timeTag=HttpTimeFlag.getInstance().startFlag();
        rxObserver.setTimeTag(timeTag);
    }
}
