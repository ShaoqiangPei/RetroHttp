## ApiRetrofitor使用说明

### 概述
ApiRetrofitor 是具体的某个通讯使用的通讯对像(Retrofitor)，继承自 BaseRetrofitor 类.  
点击此处，可查看[ApiRetrofitor模板代码](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/httpApp/ApiRetrofitor.java)

### 使用说明
#### 一. 拷贝模板
将 [ApiRetrofitor模板代码](https://github.com/ShaoqiangPei/RetroHttp/blob/master/RetroHttp/httplibrary/src/main/java/com/httplibrary/httpApp/ApiRetrofitor.java)拷贝到自己的项目中，然后按需修改，以助实现自己的通讯。
#### 二. 定义单个具体通讯的url
在 ApiRetrofitor 中，我们可以看到这样一段代码：
```
    private static final String RELEASE_URL = "";//正式
    private static final String TEST_URL = "";//测试
```
ApiRetrofitor 继承自 BaseRetrofitor，而 BaseRetrofitor 作为整个通讯的基类，已经有了正式及测试的baseurl。  
当 ApiRetrofitor 中：
```
    private static final String RELEASE_URL = "";//正式
    private static final String TEST_URL = "";//测试
```
时，用 ApiRetrofitor 对象通讯，会使用其基类BaseRetrofitor中对应的正式或测试url作为通讯的baseUrl。  
当用 ApiRetrofitor 对象进行的通讯需要一个全新的url时，我们可以将新的url直接赋值给ApiRetrofitor中的 RELEASE_URL和 TEST_URL，类似下面这样：
```
    private static final String RELEASE_URL = "https://xxxx.xx.xx:xxxx/xxx/";//正式
    private static final String TEST_URL = "https://xxxx.xx.xx:xxx/xxx/";//测试
```
则此时用 ApiRetrofitor 对象通讯，就会使用ApiRetrofitor中的RELEASE_URL或TEST_URL作为通讯的baseUrl。
#### 三. 遇到通讯需要使用不同url的情况
当你还有另一条通讯需要使用 BaseRetrofitor 和 ApiRetrofitor都不同的url作为通讯的baseUrl时，这时你可以以ApiRetrofitor为模板，建一个新的Retrofitor
(假设为FoodRetrofitor)继承自 BaseRetrofitor，然后你就可以在你的FoodRetrofitor中给 RELEASE_URL 和 TEST_URL 赋值新的url了。  
最后，再用 FoodRetrofitor 对象通讯，即可保证新的通讯的url不同于 BaseRetrofitor 和 ApiRetrofitor。

