

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
### 一. RetroHttp架构说明
RetroHttp架构说明请参考 [RetroHttp架构说明](https://github.com/ShaoqiangPei/RetroHttp/blob/master/read/RetroHttp%E6%9E%B6%E6%9E%84%E8%AF%B4%E6%98%8E.md)  
### 二. RetroHttp使用  

