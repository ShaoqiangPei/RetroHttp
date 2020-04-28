package com.httplibrary.http.rx;

import com.httplibrary.http.error.ErrorCode;
import com.httplibrary.http.error.HttpRepose;
import com.httplibrary.http.error.ServerException;
import com.httplibrary.util.HttpTimeFlag;
import com.httplibrary.util.RetroLog;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Title:返回结果处理类
 * Description:
 *
 * Created by pei
 * Date: 2017/10/31
 */
public abstract class RxObserver<T> implements Observer {

    private String mTimeTag;//通讯时间戳tag
    private Disposable mDisposable;

    @Override
    public void onSubscribe(@NonNull Disposable d){
        this.mDisposable=d;
    }

    @Override
    public void onComplete() {
        //取消订阅
        cancel();
    }

    @Override
    public void onError(@NonNull Throwable e) {
        //通讯发生错误,会有系统errMessage提示
        setExceptionInfo(e, ErrorCode.SERVER_FAILED_CODE);
        //取消订阅
        cancel();
    }

    @Override
    public void onNext(Object obj){
        if(obj==null){
            //设置错误及提示语
            setExceptionInfo(null,ErrorCode.NETWORK_EXCEPTION_CODE);
        }else{
            //通讯用时打印
            HttpTimeFlag.getInstance().stopFlagByString(getTimeTag());
            //通讯成功先经过unifiedSuccess统一处理，然后下发给doNext方法
            Object object=unifiedSuccess(obj);
            if(object!=null&&ErrorCode.INTERCEPT_RESULT.equals(object.toString())){
                RetroLog.w("======统一处理后做拦截处理,不走后续流程========");
            }else{
                RetroLog.w("======统一处理后不拦截,继续走后续流程========");
                doNext((T) object);
            }
        }
    }

    private void cancel(){
        if(mDisposable!=null&&!mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }

    /**设置错误及提示语**/
    public void setExceptionInfo(Throwable e, int errorCode){
        //通讯用时打印
        HttpTimeFlag.getInstance().stopFlagByString(getTimeTag());
        //错误处理
        ServerException serverException=new ServerException();
        serverException.setCode(errorCode);
        if(e!=null){
            serverException.setMessage(e.getMessage());
        }
        //设置code,设置固定提示语
        ServerException exception= HttpRepose.handle(serverException);
        //发生错误时统一处理,然后下发到doError方法
        doError(unifiedError(exception));
    }

    /**获取时间戳Tag**/
    private String getTimeTag() {
        return mTimeTag;
    }

    /**设置时间戳Tag**/
    public void setTimeTag(String timeTag) {
        this.mTimeTag = timeTag;
    }

    /**App中自定义统一code处理及提示语**/
    public abstract Map<Integer, String> getResultMap();
    /**发生错误时的统一处理(接收返回结果)**/
    public abstract ServerException unifiedError(ServerException serverException);
    /**通讯成功的统一处理(接收返回结果)**/
    public abstract Object unifiedSuccess(Object obj);

    //单个通讯的处理
    public abstract void doNext(T t);
    public abstract void doError(ServerException e);

}
