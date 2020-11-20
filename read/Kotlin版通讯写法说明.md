## Kotlin版通讯写法说明

### 概述
基于`java`与`Kotlin`互通，但在写法上的不同，当开发者建立的是`Kotlin`项目的时候，也可以引入本库使用。本文针对`kotlin`项目，对网络通讯的写法
做些主要写法说明，帮助大家能够快速，方便的将本库接入到`kotlin`项目中。

### 使用说明
#### 一.前置
鉴于本库的使用有一个`Java`版本的完整说明，接下来，我将以`登录功能`为例，讲解一个完整通讯下几个关键类的`kotlin`版本写法，其他的大家可以参看本库
`通讯接入流程`。
#### 二.ApiRetrofitor 写法
`ApiRetrofitor`一般以单例形式出现，其`kotlin`版本示例写法如下：
```
/**
 * Title: 基本信息Retrofitor[单例模式]
 * description:
 * autor:pei
 * created on 2020/9/23
 */
object ApiRetrofitor:BaseRetrofitor(){

    private val RELEASE_URL = ""//正式
    private val TEST_URL = ""//测试

    override fun getReleaseUrl(): String {
        return RELEASE_URL
    }

    override fun getTestUrl(): String {
        return TEST_URL
    }

}
```
#### 二.  ApiService 中通讯接口写法
一般在`ApiService`中写接口的时候，都会利用`泛型`尽量将返回参数范围缩小到最小，但是由于在`kotlin`书写的过程中，便于在`Presenter`中对返回数据的每个环节做细致处理，这里我就以最大范围接收数据了。以`登录`为例，接口示例代码如下：
```
/**
 * Title: 通讯接口
 * description:
 * autor:pei
 * created on 2020/9/23
 */
interface ApiService{

    //用户登录
    @POST("my/user/login")
    fun login(@Body user:RequestUser):Observable<ResponseData<Any>>
}
```
#### 三. Presenter 写法
在整个网络通讯流程,`Presenter`是起到至关重要的环节，还是以`登录`为例，则`LoginPresenter`代码如下：
```
/**
 * Title: 登录
 * description:
 * autor:pei
 * created on 2020/9/28
 */
class LoginPresenter : LoginContract.Presenter {

    private var mContext: Context? = null
    private var mView: LoginContract.View? = null

    constructor(context: Context, view: PreView) {
        this.mContext = context
        this.mView = view as LoginContract.View
    }

    override fun attachView() {}

    override fun detachView() {}

    override fun login(userName: String, password: String) {
        //组装参数
        var user:RequestUser= RequestUser(userName,encryptPassword)
        var apiService: ApiService =
            ApiRetrofitor.getBaseRetrofitor<BaseRetrofitor>().apiService as ApiService
        //发起请求
        var observable: Observable<ResponseData<Any>> = apiService.login(user)
        //显示加载dialog
        LoadingDialog.getInstance().showLoading(mContext)
        RxManager.connect(observable, object : ApiObserver<ResponseData<Any>>() {
            override fun doNext(responseData : ResponseData<Any>) {
                LoadingDialog.getInstance().hideLoading()

                var code:Int=responseData.code
                var message:String?=responseData.message

                when(code){
                    ResponseCode.SUCCES_CODE ->{
                        var obj=responseData.data
                        if(obj!=null){
                            var userInfo:UserInfo = GsonUtil.gsonToBean(obj.toString(),UserInfo::class.java)
                            (mView as LoginContract.View).loginSuccess(userInfo)
                        }else{
                            (mView as LoginContract.View).loginFail("数据解析错误")
                        }
                    }
                    ResponseCode.FAILED_CODE ->{
                        (mView as LoginContract.View).loginFail(message!!)
                    }
                }
            }

            override fun doError(e: ServerException?) {
                LoadingDialog.getInstance().hideLoading()
                var errorMessage: String? = e?.message ?: null
                (mView as LoginContract.View).loginFail(errorMessage)
            }
        })
    }

}
```

关于`kotlin`版本网络通讯中几个主要类的写法已经介绍完毕，当然，整个通讯的话，肯定不止设涉及到这几个类，但由于其他类在`java`转`kotlin`上没有多大困难，
这里便不详细讲解。对于以上的讲解，大家只要抓住主要的几个写法不同点即可。

