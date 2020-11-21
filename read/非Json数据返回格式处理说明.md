## 非Json数据返回格式处理说明

#### 一.描述
一般我们在`Retrofit`通讯中接收的数据都为标准的`Json`格式，则我们可以用`Gson`或其他解析`Json`的库将返回的数据进行解析，但是有些
通讯返回的数据并非标准的`Json`数据，例如下面:
```
  a=1,b=2,c=3,d=4
```
这时，再用`Gson`解析的时候会报错，那么如何在`RetroHttp`框架下，解决此类非`Json`数据格式的解析呢？

#### 二.解决办法
当遇到不是标准`json`串的数据时，我们可以用`ResponseBody`接收原始返回数据，则在`ApiService`接口中写方法如下：
```
//简单的get请求(URL中带有参数)
@GET("trades/{userId}")
Observable<ResponseBody> getItem(@Path("userId") String userId);
```
然后在接收到`ResponseBody`时，类似下面这样获取接收数据：
```
            @Override
            public void doNext(ResponseBody body) {
                try {
                    String content = body.string();
                    LogUtil.i("======接收数据=====content="+content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
```
这样，当我们收到原始数据之后，就可以自己定义解析方式了。
