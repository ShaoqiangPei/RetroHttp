## ResponseCode使用说明

### 概述
ResponseCode 主要用于定义具体通讯返回的code的定义(返回结果code定义)  
点击此处，可查看[ResponseCode模板代码](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/httpApp/ResponseCode.java)

### 使用说明
#### 一. 拷贝模板
将 [ResponseCode模板代码](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/httpApp/ResponseCode.java)
拷贝到自己的项目中，然后按需修改，以助实现自己的通讯。

#### 二. 自定义通讯返回结果的code集
我们知道在 ApiObserver 中其实也存在自定义code的情况，但是 ResponseCode类与ApiObserver类中的code定义又有很大不同：
- ApiObserver类中code定义：主要对返回的结果做统一code编排处理，且与code伴生的还有一 一对应的提示语
- ResponseCode类中code定义：针对单个通讯情况进行code定义分别处理，code没有固定提示语，提示语由具体业务逻辑中用户自定义

在ResponseCode类中自定义返回结果处理的code，你可以像下面这样：
```
    /**注：以下两个code定义只做范例,具体code定义以项目实际为准**/
    
    public static final int SUCCES_CODE = 1;//成功

    public static final int FAILED_CODE = 0;//失败
```

