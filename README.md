

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
        RetroConfig.getInstance().init(this);

    }
}
```
#### 2.3 拷贝 httpApp 包到自己的项目中
将 RetroHttp库 中的 httpApp包 整个拷贝到自己的项目中(httpApp包下为模板类)，然后按需修改每个类，以符合实际开发通讯需求。具体处理，请参考 [RetroHttp架构说明](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/RetroHttp%E6%9E%B6%E6%9E%84%E8%AF%B4%E6%98%8E.md) 中的 “httpApp包” 讲解部分。  
**这里需要注意的是：将httpApp拷贝到你项目之后，你需要将AppConfig移到你项目结构的最外层作为整个app的配置管理类。**
### 三. 通讯接入流程 
具体参考[通讯接入流程说明](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/%E9%80%9A%E8%AE%AF%E6%8E%A5%E5%85%A5%E6%B5%81%E7%A8%8B%E8%AF%B4%E6%98%8E.md)
### 四. 查看通讯log
在BaseRetrofitor类中通过配置类 AppConfig 中的变量：
```
        //是否打印httpLog
        boolean isPrintHttpLog = AppConfig.getInstance().isHttpLog();
```
来控制是否显示通讯log，过滤 log 的 tag=http,log等级为 w
具体可参考



