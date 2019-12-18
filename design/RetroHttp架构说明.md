## RetroHttp架构说明

### 概述
为了实现一个app中可以访问多个不同的url，RetroHttp采用分层架设，封装基本通讯参数。  
了解RetroHttp的架构，有助于我们更好的使用。

### 说明
#### 一.包结构总览
RetroHttp 包含以下几个package：
```
   ------- app
   ------- util
   ------- interfacer
   ------- http
   ------- httpApp
```
#### 二.各包详解
##### 2.1 app 包
app 包 主要用来给 RetroHttp的使用做全局初始化，其包结构如下：
```
   ------- app
       ------ RetroConfig : retrohttp初始化类    
```
其中，当我们在添加完 RetroHttp 库引用的时候，要利用 RetroConfig类进行全局初始化，在你项目的 Application 中调用如下方法：
```
    //在你项目的 Application 中调用 
    RetroConfig.getInstance().init(Application application);
```
##### 2.2 util 包
主要用于库内部使用的一些工具类及打印网络通讯log的工具类

##### 2.3 interfacer 包
此包下存放 http 通讯总接口

##### 2.4 http 包
http通讯核心包，其包结构如下：
```
   ------- http
       ------ error 包 : 通讯错误处理的包 
       ------ intercepter 包 : 通讯拦截器包 
       ------ retrofit 包 : 通讯核心包
       ------ rx 包 : 发起通讯及返回结果处理包
       ------ ssl 包 : 通讯证书包
```
这里主要讲解两个比较重要的包：intercepter 包 和 retrofit 包。  
- intercepter(通讯拦截器包)  
主要处理通讯中关于拦截器的内容，包括设置header，缓存，打印通讯log等。包结构如下：  
   ------- intercepter  
   &emsp;&emsp;------ [CacherInterceptor](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/http/interceptor/CacherInterceptor.java) : 缓存拦截器  
   &emsp;&emsp;------ [HeaderInterceptor](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/http/interceptor/HeaderInterceptor.java) : header拦截器(对Header统一处理)  
   &emsp;&emsp;------ [LoggingInterceptor](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/http/interceptor/LoggingInterceptor.java) : 自定义log打印拦截器   
   &emsp;&emsp;------ [SystemLogger](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/http/interceptor/SystemLogger.java) : 调用系统log拦截器   
   &emsp;&emsp;------ [UnexpectedInterceptor](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/http/interceptor/UnexpectedInterceptor.java) : 用于解决文件下载出现的Stream相关错误     
- retrofit(通讯核心包)    
主要用于处理http通讯，其包结构如下：   
   ------- retrofit   
   &emsp;&emsp;------ [SuperRetrofitor](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/http/retrofit/SuperRetrofitor.java) : 网络通讯超类   
   &emsp;&emsp;------ [HttpConfig](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/http/retrofit/HttpConfig.java) : 网络通讯相关配置     
##### 2.5 httpApp 包
**注意：RetroHttp库中 app包，util包,interfacer包和http包皆为不可更改包，即你只需要引用就行。但是 httpApp包 不同，
你需要将 httpApp包(整个包，包括里面的所有类) 拷贝到自己的项目中，然后根据实际情况将每个类做修改，以符合使用(库中httpApp包下所有类已被注释，仅作模板参考)**  

httpApp包为项目对接包，需要开发者将此包及包中所有类均拷贝到自己的项目中，然后根据模板作对应修改，以满足实际开发需求。  
httpApp包结构如下:  
   ------- httpApp 
   &emsp;&emsp;------ [ApiService]():通讯接口   
   &emsp;&emsp;------ [BaseRetrofitor]():网络通讯基类  
   &emsp;&emsp;------ [ApiRetrofitor]():具体的某个通讯使用的Retrofitor   
   &emsp;&emsp;------ [ApiObserver]():通讯返回结果集的统一处理(继承自RxObserver)    
   &emsp;&emsp;------ [ResponseCode]():返回结果code定义(此类用于定义具体通讯返回的code的定义)   













