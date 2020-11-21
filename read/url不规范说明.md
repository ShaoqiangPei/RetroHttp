## url不规范说明

#### 一.描述
一般我们在用Retrofit2.x做数据请求框架的时候。都是先在Base_url中写一段url链接，类似如下：
```
private static final String BASE_RELEASE_URL = "https://xxxx.xx.xx:xxxx/xxx/";//url以"/"结尾
```
然后再在请求数据的ApiService接口中写另一半含参的url，类似如下：
```
  @GET("users/{user}/repos")
  Call<List<Repo>> listRepos(@Path("user") String user);
```
这是因为整个链接正好是前面拼接后面成完整链接，如下：
```
https://xxxx.xx.xx:xxxx/xxx/users/{user}/repos
```
但是我今天遇到一个奇葩链接，整个链接如下：
```
https://xxxx.xx.xx:xxxx/id=xxx
```
在写`url`请求的时候就犯难了。接着往下看

#### 二.处理办法
在`Retrofit2.x`中，`Post`和`Get`注解不能传递空值。如果没有数据的话写`.`或者`/`,如`@GET("/")`或`@GET(".")`。需要注意的是:
`@GET`请求参数对用`@QueryMap` ，`POST`请求参数对应 `@FieldMap`  
则对于以下格式的`url`：
```
https://xxxx.xx.xx:xxxx/id=xxx
```
我们可以在`ApiService`中像下面这样定义`Api`:
```
    @GET("/")   //没有数据就填 . 或者 /   需要注意的是 @GET请求需要用@QueryMap
    Observable<User> getRealTimeData(@QueryMap Map<String,String>map);//@GET请求参数对用 @QueryMap ，POST请求参数对应 @FieldMap
```
然后在发送请求的参数`map`中将`id`作为`map`的`key`即可。

