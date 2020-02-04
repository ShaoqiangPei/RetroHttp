## BaseRetrofitor使用说明

### 概述
BaseRetrofitor作为 http 通讯基类。开发过程中，整个app的通讯基准 url 将在此类中定义。  
点击此处，可查看[BaseRetrofitor模板代码](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/httpApp/BaseRetrofitor.java)

### 使用说明
#### 一. 拷贝模板
将整个[BaseRetrofitor模板代码](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/httpApp/BaseRetrofitor.java)拷贝到自己的项目中，然后进行修改。
#### 二. 定义整个app的基准通讯url
BaseRetrofitor中，你需要定义整个app的baseurl，包括测试版和正式版，你可以类似下面这样定义：
```
            /**正试版base_release_url(注:此处url为范本,到时填实际url)**/
            private static final String BASE_RELEASE_URL = "https://xxxx.xx.xx:xxxx/xxx/";//url以"/"结尾
            /**测试版base_test_url(注:此处url为范本,到时填实际url)**/
            private static final String BASE_TEST_URL = "https://xxx.xx.xx.xx:xxxx/xxxxxx/";//url以"/"结尾
```
将你的正式版和测试版的基准通讯url复制到上面，结尾以"/"切割，作为BaseUrl即可。
#### 三.返回接口定义类
在BaseRetrofitor中有以下方法：
```
    @Override
    public Class<?> getApiServiceClass() {
        return ApiService.class;
    }
```
这里需要返回接口定义类，以httpApp模板为例，接口定义类为ApiService，则此处返回的是 ApiService.class，若你接口定义类命名为 AService，则此处返回的是 AService.class
#### 四.统一header的设置
在BaseRetrofitor中有统一设置header的方法： 
```
    /**设置统一的Header**/
    @Override
    public Map<String, String> getHeaderMap() {
        Map<String, String> map = new HashMap<>();

//        //所有通讯增加自定义请求头,格式类似：
//        map.put("key", "value");

        return map;
    }
```
若你有统一设置通讯header的需求，则可以在上述diposeRequest(Request request)方法中以
```
        //所有通讯增加自定义请求头,格式类似：
        map.put("key", "value");
```
格式给所以通讯定义统一请求Header。
#### 五.正式，测试系统切换
在BaseRetrofitor中有一个切换正式，测试系统的方法
```
    @Override
    public boolean isTest() {
        return AppConfig.getInstance().isTest();
    }
```
这里，你需要在自己的项目中新建一个AppConfig类，作为整个app的配置类，关于AppConfig后面会讲到。
#### 六.通讯基本参数设置及调试log打印
BaseRetrofitor有一个设置通讯基本参数的方法：
```
    /**设置网络连接配置**/
    public <T extends BaseRetrofitor> T getBaseRetrofitor() {
        return (T) BaseRetrofitor.this
                //设置是否关闭自定义log拦截器,一般与网络log打印控制一致(默认为false,即关闭log打印和拦截器)
                .setCustomerLog(RetroConfig.getInstance().isHttpLog());
        //                .setSystemLog(false)//设置是否使用系统拦截器打印log,默认false,不使用
        //                .setDefaultSSL(true)//设置通讯是否使用SSL加密,默认true,使用
        //                .setConnectTimeOut(30) //设置网络连接超时，默认30秒
        //                .setReadTimeOut(30) //设置网络读取超时，默认30秒
        //                .setWriteTimeOut(30) //设置网络写入超时，默认30秒
        //                .setRetryConnect(true);//设置网络连接错误重连，默认重连
    }
```
此方法中可设置很多通讯参数，不过基本都是用的默认值，如有特殊需要，可自行设置。  
下面就来讲讲AppConfig。
#### 七.整个app的配置类 —— AppConfig
作为一个项目，你需要在自己新建的项目最外层设置一个配置类AppConfig，作为对整个app的配置参数统一管理。  
点击这里，可查看[AppConfig类模板代码](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/httpApp/AppConfig.java)

