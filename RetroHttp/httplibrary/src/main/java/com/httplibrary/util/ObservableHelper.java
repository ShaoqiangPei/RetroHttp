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

    /***
     * 接连发起多个通讯的Observable(一个接一个,但顺序不确定)
     *
     * @param observable
     * @param function  Function<T,R>: T->进入该方法的参数， R->输出结果
     * @param <T>
     * @return
     */
    public static <T>Observable getNextObservable(Observable<T>observable, Function<T, ObservableSource<T>> function){

        //        //示例(请结合getNextObservableInOrder的示例参看)
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
            RetroLog.i("=====function不为null=====");
            return observable.observeOn(Schedulers.io())  //回到IO线程去发起登录请求
                    .observeOn(AndroidSchedulers.mainThread()) //回到主线程
                    .flatMap(function);
        }else{
            RetroLog.i("======function为null=====");
        }
        return observable;
    }

    /***
     * 按顺序发起通讯的Observable(一个接一个,且按顺序发送)
     *
     * @param observable
     * @param function Function<T,R>: T->进入该方法的参数， R->输出结果
     * @param <T>
     * @return
     */
    public static <T> Observable getNextObservableInOrder(Observable<T>observable, Function<T, ObservableSource<T>> function){

        //        //示例
        //        Observable observable = apiService.check();
        //        observable = ObservableHelperF.getNextObservableInOrder(observable, new Function<ResponseData, ObservableSource<ResponseData>>() {
        //            @Override
        //            public ObservableSource<ResponseData> apply(ResponseData responseData) throws Exception{
        //                LogUtil.i("=======Function通讯成功======");
        //
        //                //解析第一个通讯的结果
        //                int code = responseData.getCode();
        //                //只有在第一此通讯顺利的时候,才发起第二次通讯
        //                if (code == ResponseCode.SUCCES_CODE) {//成功
        //                    LogUtil.i("=======准备发起第二次通讯======");
        //                    //准备发起第二次通讯
        //                    RequestUser user = new RequestUser();
        //                    user.setUsername("xxx");
        //                    user.setPassword("xxxxxx");
        //                    return apiService.login(user);
        //                }
        //                return null;
        //            }
        //        });


        if (function != null) {
            RetroLog.i("=====function不为null=====");
            return observable.observeOn(Schedulers.io())  //回到IO线程去发起登录请求
                    .observeOn(AndroidSchedulers.mainThread()) //回到主线程
                    .concatMap(function);
            //                    .doOnError(new Consumer<Throwable>() {
            //                        @Override
            //                        public void accept(Throwable throwable){
            //                            LogUtil.i("=====function异常处理====="+throwable.getMessage());
            //                        }
            //                    });
        }else{
            RetroLog.i("======function为null=====");
        }
        return observable;
    }

}
