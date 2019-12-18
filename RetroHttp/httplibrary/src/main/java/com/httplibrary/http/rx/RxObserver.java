package com.httplibrary.http.rx;

import com.httplibrary.http.error.ErrorCode;
import com.httplibrary.http.error.HttpRepose;
import com.httplibrary.http.error.ServerException;
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
            unifiedSuccess(obj);
            doNext((T) obj);
        }
    }

    private void cancel(){
        if(mDisposable!=null&&!mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }

    /**设置错误及提示语**/
    public void setExceptionInfo(Throwable e, int errorCode){
        ServerException serverException=new ServerException();
        serverException.setCode(errorCode);
        if(e!=null){
            serverException.setMessage(e.getMessage());
        }
        //设置code,设置固定提示语
        ServerException exception= HttpRepose.handle(serverException,this);
        doError(exception);
    }

    /**发生错误时的统一处理**/
    public abstract void unifiedError(ServerException serverException);
    /**通讯成功的统一处理**/
    public abstract void unifiedSuccess(Object obj);

    //单个通讯的处理
    public abstract void doNext(T t);
    public abstract void doError(ServerException e);

}
