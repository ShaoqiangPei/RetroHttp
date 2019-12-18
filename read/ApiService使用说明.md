## ApiService使用说明

### 概述
ApiService是RetroHttp库 网络通讯的接口类，要进行网络通讯的api接口，均在此类中定义。  
点击此处，可查看ApiService模板代码

### 使用说明
#### 一.使用
将ApiService模板代码复制到自己项目中，然后按需修改 api 接口定义，以助实现网络通讯。

#### 二.ApiService接口注解解释
一般我们的 “请求参数注解” 都是在自建的 java 接口内进行，类似如下:
```
public interface GitHubService {
  @GET("users/{user}/repos")
  Call<List<Repo>> listRepos(@Path("user") String user);
}
```
我们自己新建了网络接口通讯类GitHubService 后，就可以再这里面写通讯相关接口了，这时，就会涉及到 “请求参数注解”的问题。  

在一般的开发过程中，我们经常用到的请求方法就两个：POST，GET。
你可以像下面这样发起一个简单的 GET 请求：
```
//简单的get请求(没有参数)
 @GET("trades")
 Call<TradesBean> getItem();
```
要发起一个 post请求，你可以类似这样写：
```
@POST("users/new")
Call<User> createUser(@Body User user);
```

##### 2.1 简单的get请求
```
url ：http://192.168.43.173/api/trades
String baseUrl="http://192.168.43.173/api/"

 @GET("trades")
 Call<TradesBean> getItem();
```
##### 2.2 动态url请求
```
url ：http://192.168.43.173/api/trades

//传参时，将整个url作为参数传进去
@GET
Call<TradesBean> getItem(@url String url);
```
##### 2.3 URL中带有参数的get请求  
**url路径中间含参，用 @Path 注解**  
含一个参数：
```
url: http://192.168.43.173/api/trades/{userId}
String baseUrl="http://192.168.43.173/api/"

//简单的get请求(URL中带有参数)
@GET("trades/{userId}")
Call<TradesBean> getItem(@Path("userId") String userId);
```
含多个参数(以两个为例)：
```
url: http://192.168.43.173/api/trades/{userId}/pro/{name}
String baseUrl="http://192.168.43.173/api/"

//简单的get请求(URL中带有参数)
@GET("trades/{userId}/pro/{name}")
Call<TradesBean> getItem(@Path("userId") String userId,@Path("name") String name);
```
##### 2.4 参数在url问号之后的请求  
**url问号之后含参，用 @Query 注解**  
含一个参数：
```
url: http://192.168.43.173/api/trades?userId={用户id}
String baseUrl="http://192.168.43.173/api/"

 @GET("trades")
 Call<TradesBean> getItem(@Query("userId") String userId);
```
含多个参数(以两个为例)：
```
url: http://192.168.43.173/api/trades?userId={用户id}&type={类型}
String baseUrl="http://192.168.43.173/api/"

 @GET("trades")
 Call<TradesBean> getItem(@QueryMap Map<String, String> map);
```
##### 2.5 post请求一个固定的url(以表单方式传一个参数)  
**表单方式传一个参数(非文件传输)，用@FormUrlEncoded**  
```
url ：http://192.168.43.173/api/trades
String baseUrl="http://192.168.43.173/api/"

 @POST("trades")
 @FormUrlEncoded
 Call<TradesBean> postItem(@Field("reason") String reason);
```
##### 2.6 post请求一个固定的url(以表单方式传多个参数)  
**表单方式传一个参数(非文件传输)，用@FormUrlEncoded**  
以传两个参数例  
```
url ：http://192.168.43.173/api/trades
String baseUrl="http://192.168.43.173/api/"

 @POST("trades")
 @FormUrlEncoded
 Call<TradesBean> postItem(@Field("reason") String reason,@Field("name") String name);

 或者

 @POST("trades")
 @FormUrlEncoded
 Call<TradesBean> postItem(@FieldMap Map<String, String> params);
```
##### 2.7 post请求url路径中间含参，url问号之后含参(以表单方式传一个参数)  
**表单方式传一个参数(非文件传输)，用@FormUrlEncoded**  
此情况和之前讲过的get请求的处理方式一致：  
- url路径中间含参 用 @Path 注解  
- url问号之后含参 用 @Query 注解  

以"需要补全URL,问号后需要加token,post的数据只有一条reason"为例:
```
url ：http://192.168.43.173/api/trades/{userId}?token={token}
String baseUrl="http://192.168.43.173/api/"

 @FormUrlEncoded
 @POST("trades/{userId}")
 Call<TradesBean> postResult(@Path("userId") String userId,@Query("token") String token,@Field("reason") String reason);
```
##### 2.8 post请求传一个对象  
**post请求传对象，用 @Body 注解，此时不需要 @FormUrlEncoded**  
```
url ：http://192.168.43.173/api/trades
String baseUrl="http://192.168.43.173/api/"

@POST("trades")
Call<User> createUser(@Body User user);
```
##### 2.9 上传文件
**上传文件用 post请求,需要用到 @Part，@PartMap作为传参注解，并结合@Multipart注解使用**  
```
/**
  * {@link Part} 后面支持三种类型，{@link RequestBody}、{@link okhttp3.MultipartBody.Part} 、任意类型
  * 除 {@link okhttp3.MultipartBody.Part} 以外，
 * 其它类型都必须带上表单字段({@link okhttp3.MultipartBody.Part} 中已经包含了表单字段的信息)，
  */
@POST("/form")
@Multipart
Call<ResponseBody> testFileUpload1(@Part("name") RequestBody name, 
                                   @Part("age") RequestBody age, 
                                   @Part MultipartBody.Part file);

/**
  * PartMap 注解支持一个Map作为参数，支持 {@link RequestBody } 类型，
  * 如果有其它的类型，会被{@link retrofit2.Converter}转换，
  * 如后面会介绍的 使用{@link com.google.gson.Gson} 的 {@link retrofit2.converter.gson.GsonRequestBodyConverter}
  * 所以{@link MultipartBody.Part} 就不适用了,所以文件只能用<b> @Part MultipartBody.Part </b>
         */
@POST("/form")
@Multipart
Call<ResponseBody> testFileUpload2(@PartMap Map<String, RequestBody> args, @Part MultipartBody.Part file);

@POST("/form")
@Multipart
Call<ResponseBody> testFileUpload3(@PartMap Map<String, RequestBody> args);

        // 具体使用
        MediaType textType = MediaType.parse("text/plain");
        RequestBody name = RequestBody.create(textType, "Carson");
        RequestBody age = RequestBody.create(textType, "24");
        RequestBody file = RequestBody.create(MediaType.parse("application/octet-stream"), "这里是模拟文件的内容");

        // @Part
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", "test.txt", file);
        Call<ResponseBody> call3 = service.testFileUpload1(name, age, filePart);
        ResponseBodyPrinter.printResponseBody(call3);

        // @PartMap
        // 实现和上面同样的效果
        Map<String, RequestBody> fileUpload2Args = new HashMap<>();
        fileUpload2Args.put("name", name);
        fileUpload2Args.put("age", age);
        //这里并不会被当成文件，因为没有文件名(包含在Content-Disposition请求头中)，但上面的 filePart 有
        //fileUpload2Args.put("file", file);
        Call<ResponseBody> call4 = service.testFileUpload2(fileUpload2Args, filePart); //单独处理文件
        ResponseBodyPrinter.printResponseBody(call4);
```
##### 2.10 设置固定的请求头
**使用 @Headers 注解设置固定的请求头，所有请求头不会相互覆盖，即使名字相同。**
```
//请求头设置一个参数
@Headers("Cache-Control: max-age=640000")
@GET("widget/list")
Call<List<Widget>> widgetList();

//请求头设置多个参数
@Headers({ "Accept: application/vnd.github.v3.full+json","User-Agent: Retrofit-Sample-App"})
@GET("users/{username}")Call<User> getUser(@Path("username") String username);
```
##### 2.11 设置动态的请求头
**使用 @Header 注解动态更新请求头**  
使用 @Header 注解动态更新请求头，匹配的参数必须提供给 @Header ，若参数值为 null ，这个头会被省略，否则，会使用参数值的 toString 方法的返回值。
```
//动态设置一个请求头
@GET("user")
Call<User> getUser(@Header("Authorization") String authorization);

//动态设置多个请求头(以两个为例)
@GET("user")
Call<User> getUser(@Header("Authorization1") String authorization1，
                   @Header("Authorization2") String authorization2);
```
#### 三.总结
- 当@GET或@POST注解的url为全路径时（可能和baseUrl不是一个域），会直接使用注解的url的域  
- @Path是网址中的参数,例如:trades/{userId}  
- @Query是问号后面的参数,例如:trades/{userId}?token={token}  
- @QueryMap 相当于多个@Query  
- @Field用于Post请求,提交单个数据,然后要加@FormUrlEncoded  
- @FieldMap 相当于多个@Field  
- 以对象的方式提交用@Body，不需要@FormUrlEncoded注解  
- 如果请求为post实现，那么最好传递参数时使用@Field、@FieldMap和@FormUrlEncoded。因为@Query和或@QueryMap都是将参数拼接在url后面的，而@Field或@FieldMap传递的参数时放在请求体的  
- @Streaming:用于下载大文件  
- @Header,@Headers、加请求头  


