

# RetroHttp简介  
[![](https://jitpack.io/v/ShaoqiangPei/RetroHttp.svg)](https://jitpack.io/#ShaoqiangPei/RetroHttp)

## 概述  
RetroHttp 是一个基于 RetrofitHttp 进行封装使用的http库，目的是使通讯变得更加简洁。

## 依赖
在你 project 对应的 build.gradle 中添加以下代码：
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
在你需要引用的 module对应的build.gradle中(此处以app_module的build.gradle中引用 0.0.1版本为例)添加版本依赖：
```
	dependencies {
	        implementation 'com.github.ShaoqiangPei:RetroHttp:0.0.1'
	}
```
## 使用说明
### 一. RetroHttp架构
RetroHttp架构说明请参考 [RetroHttp架构说明](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/RetroHttp%E6%9E%B6%E6%9E%84%E8%AF%B4%E6%98%8E.md)  
### 二. RetroHttp使用  
#### 2.1 依赖
如上，在你的项目中添加 RetroHttp 库依赖
#### 2.2 RetroHttp 库初始化
你需要在你项目的自定义的Application中对RetroHttp 库做初始化操作，类似下面这样：
```
/**
 * Description:自定义Application
 * 
 * Author:pei
 * Date: 2019/8/28
 */
public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化 RetroHttp
        RetroConfig.getInstance().init(this)
                                 //是否开启网络log打印
                                 .setHttpLog(AppConfig.getInstance().isHttpLog());

    }
}
```
**注**：AppConfig为整个项目设置的一个配置类，具体介绍可参考[BaseRetrofitor使用说明](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/BaseRetrofitor%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.md)中的第七项：整个app的配置类 —— AppConfig
#### 2.3 拷贝 httpApp 包到自己的项目中
将 RetroHttp库 中的 httpApp包 整个拷贝到自己的项目中(httpApp包下为模板类)，然后按需修改每个类，以符合实际开发通讯需求。具体处理，请参考 [RetroHttp架构说明](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/RetroHttp%E6%9E%B6%E6%9E%84%E8%AF%B4%E6%98%8E.md) 中的 “httpApp包” 讲解部分。  
**这里需要注意的是：将httpApp拷贝到你项目之后，你需要将AppConfig移到你项目结构的最外层作为整个app的配置管理类。**
### 三. 通讯接入流程 
具体参考[通讯接入流程说明](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/%E9%80%9A%E8%AE%AF%E6%8E%A5%E5%85%A5%E6%B5%81%E7%A8%8B%E8%AF%B4%E6%98%8E.md)
### 四. Apk下载与安装(更新应用)  
apk的下载与更新，主要涉及到 download文件夹，外部调用的话，只涉及到其中的 DownLoadHelper 类。  
下载更新apk的方法，可参考[Apk文件下载与安装](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/Apk%E6%96%87%E4%BB%B6%E4%B8%8B%E8%BD%BD%E4%B8%8E%E5%AE%89%E8%A3%85.md)  
### 五. Http/Https 通讯的兼容
Android9.0以后通讯不支持 http 方式，建议使用 https 方式通讯。RetroHttp支持 http通讯兼容，如果你项目中要使用http通讯的话，
你可以在你项目的清单文件 manifast.xml中添加 retrohttp_config.xml文件的声明(注：retrohttp_config.xml文件包含在retrohttp库中)  
在你 manifast.xml 的 application 标签下，添加如下声明：
```
<application
        //其他代码省略
	//......
        android:networkSecurityConfig="@xml/retrohttp_config"
        
	>
	
	//其他代码省略
	//......

</application>
	
```
### 六. 查看通讯log
在RetroHttp初始化时通过配置类 AppConfig 中的变量：
```
        //是否打印httpLog
         RetroConfig.getInstance().setHttpLog(AppConfig.getInstance().isHttpLog());
```
来控制是否显示通讯log，过滤 log 的 tag=http,log等级为 w   
具体可参考[BaseRetrofitor使用说明](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/BaseRetrofitor%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E.md)中关于Log打印的介绍。

### 七. 其他问题
1. [Url不规范](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/url%E4%B8%8D%E8%A7%84%E8%8C%83%E8%AF%B4%E6%98%8E.md)  
2. [通讯返回结果为非`json`格式的解析](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/%E9%9D%9EJson%E6%95%B0%E6%8D%AE%E8%BF%94%E5%9B%9E%E6%A0%BC%E5%BC%8F%E5%A4%84%E7%90%86%E8%AF%B4%E6%98%8E.md)  
3. [Kotlin版网络通讯如何写?](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/Kotlin%E7%89%88%E9%80%9A%E8%AE%AF%E5%86%99%E6%B3%95%E8%AF%B4%E6%98%8E.md)  
4. [如何发起串联请求?](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/%E4%B8%B2%E8%81%94%E8%AF%B7%E6%B1%82%E8%AF%B4%E6%98%8E.md)  
5. [如何实现并发请求](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/%E5%B9%B6%E5%8F%91%E8%AF%B7%E6%B1%82%E8%AF%B4%E6%98%8E.md)

