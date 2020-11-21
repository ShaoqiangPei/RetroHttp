package com.httplibrary.util;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Title:Observable帮助类
 * description:
 * autor:pei
 * created on 2020/11/18
 */
public class ObservableHelper {

    /**多个通讯依次执行的开头Observable**/
    public static Observable getFirstObservable(Observable observable){
        if(observable!=null){
            return observable.subscribeOn(Schedulers.io());//在IO线程进行网络请求
        }
        return null;
    }

    /**一个通讯中推结果的处理**/
    public static <T>Observable sendUI(Observable<T>observable, Consumer<T> consumer){

//        //示例
//        observable.doOnNext(new Consumer<T>() {
//            @Override
//            public void accept(Object o) throws Exception {
//                ToastUtil.shortShow("====给界面的提示====");
//            }
//        });

        if(observable!=null){
            return observable.observeOn(AndroidSchedulers.mainThread()) //回到主线程去处理请求注册结果
                    .doOnNext(consumer);
        }
        return null;
    }

    /***
     * 接连发起多个通讯的Observable(一个接一个,但顺序不确定)
     *
     * @param observable
     * @param function  Function<T,R>: T->进入该方法的参数， R->输出结果
     * @param <T>
     * @return
     */
    public static <T>Observable getNextObservable(Observable<T>observable, Function<T, ObservableSource<T>> function){

//        //示例
//        observable.observeOn(Schedulers.io()) //在IO线程进行网络请求
//                //Function<T,R>: T->进入改方法的参数， R->输出结果
//                .flatMap(new Function<ResponseData<Object>,ObservableSource<ResponseData<Object>>>(){
//                    @Override
//                    public ObservableSource<ResponseData<Object>> apply(ResponseData<Object> objectResponseData) throws Exception {
//                        LogUtil.i("=2222===code="+objectResponseData.getCode()+"  message="+objectResponseData.getMessage());
//                        LogUtil.i("=233===code="+objectResponseData.getData().toString());
//                        LogUtil.i("=====获取信息======");
//                        //组装参数
//                        RequestPrintRecipeInfo info=new RequestPrintRecipeInfo(
//                                1234,
//                                333,
//                                12,
//                                2
//                        );
//                        ApiService testService= (ApiService) ApiRetrofitor.INSTANCE.getApiService();
//                        return testService.printSingleRecipeInfo(info);
//                    }
//                });

        if(observable!=null){
            return observable.observeOn(Schedulers.io())  //回到IO线程去发起登录请求
                    .flatMap(function);
        }
        return null;
    }

    /**按顺序发起通讯的Observable(一个接一个,且按顺序发送)**/
    public static <T>Observable getNextObservableInOrder(Observable<T>observable, Function<T, ObservableSource<T>> function){

//        //示例
//        observable.observeOn(Schedulers.io()) //在IO线程进行网络请求
//                //Function<T,R>: T->进入改方法的参数， R->输出结果
//                .concatMap(new Function<ResponseData<Object>,ObservableSource<ResponseData<Object>>>(){
//                    @Override
//                    public ObservableSource<ResponseData<Object>> apply(ResponseData<Object> objectResponseData) throws Exception {
//                        LogUtil.i("=2222===code="+objectResponseData.getCode()+"  message="+objectResponseData.getMessage());
//                        LogUtil.i("=233===code="+objectResponseData.getData().toString());
//                        LogUtil.i("=====获取信息======");
//                        //组装参数
//                        RequestPrintRecipeInfo info=new RequestPrintRecipeInfo(
//                                1234,
//                                333,
//                                12,
//                                2
//                        );
//                        ApiService testService= (ApiService) ApiRetrofitor.INSTANCE.getApiService();
//                        return testService.printSingleRecipeInfo(info);
//                    }
//                });

        if(observable!=null){
            return observable.observeOn(Schedulers.io())  //回到IO线程去发起登录请求
                    .concatMap(function);
        }
        return null;
    }

    /**通讯最后的接收处理**/
    public static <T>void runLast(Observable<T>observable,Consumer<T>resultConsumer,Consumer<Throwable>throwableConsumer){

//        //示例
//        observable.observeOn(AndroidSchedulers.mainThread()) //回到主线程去处理请求结果
//                .subscribe(new Consumer<ResponseData<T>>(){
//
//                    @Override
//                    public void accept(ResponseData<T> tResponseData) throws Exception {
//                        LogUtil.i("=====app==="+tResponseData.getCode());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        LogUtil.i("=====error==="+throwable.toString());
//                    }
//                });

        if(observable!=null){
            observable.observeOn(AndroidSchedulers.mainThread())  //回到主线程去处理请求登录的结果
                    .subscribe(resultConsumer, throwableConsumer);
        }
    }

}
