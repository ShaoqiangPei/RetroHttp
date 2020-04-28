## ApiObserver使用说明

### 概述
ApiObserver 继承自RxObserver，主要用于 通讯返回结果集的统一处理。  
点击此处，可查看[ApiObserver模板代码](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/httpApp/ApiObserver.java)

### 使用说明
#### 一. 拷贝模板
将 [ApiObserver模板代码](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/httpApp/ApiObserver.java)拷贝到自己的项目中，然后按需修改，以助实现自己的通讯。
#### 二. 给返回结果定义各种情况的code，并作统一处理
你可以在 ApiObserver中给所有通讯返回结果定义统一的code定义，类似下面这样：
```
    //自定义统一code处理及提示语
    public static final int CONNECT_TIME_OUT_CODE=-4; //服务器连接超时
    
    public static final int LOGIN_TIME_OUT_CODE=-5; //登录过期
    
```
#### 三. 给定义的code，设置相对应的提示语
在 ApiObserver 中的 getResultMap方法中，给自己定义的code设置相对应的提示语，类似下面这样：
```
    /**自定义统一code处理及提示语**/
    private Map<Integer, String> getResultMap() {
        Map<Integer, String> map = new HashMap<>();

        //自定义异常code及提示语的统一处理
        map.put(ApiObserver.CONNECT_TIME_OUT_CODE, "服务器连接超时");

        return map;
    }
```
#### 四. 更改 "无网络" 和 “通讯onNext(Object obj)返回数据为空” 的提示语
 ApiObserver 继承自 RxObserver，追溯 RxObserver代码，我们会发现在RxObserver中已经使用了两个既定常量的处理：
 ```
 ErrorCode.NO_NET_WORK=-2  无网络，提示语为：网络未连接,请开启网络后使用!
 ErrorCode.NETWORK_EXCEPTION_CODE=-3  通讯onNext(Object obj)返回数据为空,提示语为：通讯数据解析异常
 ```
 若我们想更改这两种情况的提示语，那么我们可以在 ApiObserver 的  getResultMap方法中作如下处理：
 ```
     /**自定义统一code处理及提示语**/
    private Map<Integer, String> getResultMap() {
        Map<Integer, String> map = new HashMap<>();

        //自定义异常code及提示语的统一处理
        map.put(ErrorCode.NO_NET_WORK, "没网了");
        map.put(ErrorCode.NETWORK_EXCEPTION_CODE, "解析出问题了");

        return map;
    }
 ```
 #### 五. 返回结果异常的统一处理
 第 二，三，四步主要是定义统一的code及提示语。定义完后，当我们需要对所有返回的异常结果作统一处理时，我们可以在unifiedError(ServerException serverException)方法中做如下处理：
 ```
     /**返回失败的统一处理**/
    @Override
    public void unifiedError(ServerException serverException) {
        LogUtil.i("=======ApiObserver返回错误统一处理========");
        LogUtil.i("=======原始返回数据错误===errorCode=" + serverException.getCode() + ",   errorMessage=" + serverException.getMessage());
        
        //处理"connect timed out"提示语
        if ("connect timed out".equals(serverException.getMessage())) {
            serverException.setCode(ApiObserver.CONNECT_TIME_OUT_CODE);
            serverException.setMessage(ApiObserver.CONNECT_TIME_OUT_MESSAGE);
            doError(serverException);
        }
        LogUtil.i("=======返回数据错误===errorCode=" + serverException.getCode() + ",   errorMessage=" + serverException.getMessage());
    }
 ```
 #### 六. 返回结果成功的统一处理
 返回结果成功的统一处理，我们可以在unifiedSuccess(Object obj)中进行处理：
 ```
     /**返回成功的统一处理**/
    @Override
    public void unifiedSuccess(Object obj) {
        LogUtil.i("=======ApiObserver返回成功统一处理========");
        
                //对返回成功结果做统一处理的逻辑
        //若返回结果定义为 ErrorCode.INTERCEPT_RESULT,则表示结果经拦截不再传到界面去
        //若返回结果为object(不定义为ErrorCode.INTERCEPT_RESULT)则表示返回数据经统一处理后，还会到界面做进一步处理
        //......


        ResponseData responseData= (ResponseData) obj;
        int code=responseData.getCode();
        //以统一拦截code==0的数据并使之不传到界面为例
        if(code==0){
            //关闭网络加载框
            LoadingDialog.getInstance().hideLoading();

            //统一处理(拦截结果不传到界面)的逻辑
            //...

            LogUtil.i("=====到底怎么回事啊啊啊啊===");

            //此处做拦截统一处理，返回 ErrorCode.INTERCEPT_RESULT,表示结果不再传到界面去
            return ErrorCode.INTERCEPT_RESULT;
        }



        LogUtil.i("=====返回数据成功(统一处理后):obj="+obj);
        return obj;

    }
 ```


