## Apk文件下载与安装

### 概述
RetroHttp 库具备文件的下载与安装功能，其主要在 download 文件夹下，外部使用的话，只涉及到 DownLoadHelper 类。

### 使用说明
#### 一. 安装和文件读写权限
在manifast.xml配置文件中，添加以下权限：
```
    <!--安装未知来源应用的权限 -->
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    <!--联网权限 -->
    <uses-permission android:name="android.permission.INTERNET"/>
    <!--文件读写权限 -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
其中，联网权限和文件读写权限要在 项目中动态申请权限。
#### 二. provider文件权限
在 mainfast 的配置文件中加入FileProvider注册(注意：provider 和 activity 标签同级，都是 application 标签的子类)：
```
<application>
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/retrohttp_files" />
        </provider>
</application>
```
authorities="${applicationId}"值为你项目的applicationId，一般在app——model的buidle.gradle中或配置文件config.gradle中，参数值位置大如下所示：
```
    compileSdkVersion 28
    defaultConfig {
        applicationId "com.p.atestdemo"
        minSdkVersion 17
        targetSdkVersion 28
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
```
位置类似上面的 applicationId 后面跟的值。

按一般流程来说，需要在你项目的 res文件夹下建 xml文件 updat_files.xml(文件名自己定义）。由于RetroHttp库里面已经新建了retrohttp_files.xml文件，所以，如果你项目中要用的话，只需要如上所述在provider中加“meta-data”标签，类似如下：
```
    <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/retrohttp_files" />
        </provider>
```
#### 三. 下载安装方法的使用
前面权限都已经弄好后，当涉及到apk文件下载和安装的时候，你可以类似这样：
```
    //是否增量更新的标志
    private boolean mUpdate=false;

    private void updateApk(){
        String url=null;
        
        //此处mUpdate你要先根据业务逻辑赋值为true或false
        if(mUpdate){
            //增量更新的url
            url="https://xxxx/xxx/test.patch";//示例代码
        }else{
            //全量更新的url
            url="https://xxxx/xxx/test_2.0.0.apk";//示例代码
        }
        LogUtil.i("=========mUpdate======"+mUpdate);
        LogUtil.i("=========url======"+url);

        String fileName=DownLoadHelper.getInstance().getDefaultFileName(url);
        LogUtil.i("=========fileName======"+fileName);
        //启动下载
        DownLoadHelper.getInstance().setFileName(fileName)//设置下载文件名
                .setFileLength(88490)//设置下载文件大小,不设置时，将采用系统获取的文件大小,默认采用系统获取的文件大小
                .setIcon(R.mipmap.ic_launcher)//设置apk图标资源id
                .setAuthorityTag("com.p.atestdemo")//设置要与清单文件的 provider 中配置的authorities值一致
                .setIncrementUpdate(mUpdate)//是否开启增量更新(true:开启,false:关闭,此行代码不设置会默认全量更新)
                .setUseDefaultDownDialog(true)//开启默认下载进度dialog,默认为false，即不使用默认进度加载的dialog
                .downLoadFile(url, MainActivity.this, new DownLoadHelper.DownloadListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void update(int progress, boolean done) {
                        
                    }

                    @Override
                    public void onCompleted() {
                       
                    }

                    @Override
                    public void onError(String err) {
                        LogUtil.i("=========下载失败====="+err);
                        if(DownLoadHelper.getInstance().isDeltaFileFailed(err)){
                            LogUtil.i("=========增量更新下载失败====="+err);
                            //增量更新下载失败的逻辑(一般发rx通知出来，然后改全量更新)
                            //......
                        }else{
                            LogUtil.i("=========全量更新下载失败====="+err);
                            //全量更新下载失败的逻辑(一般提示退出app)
                            //......
                        }
                    }
                });
    }
```
这里需要注意的是:
- setFileLength(10000)：是设置下载文件总大小，当不设置此属性的时候，系统会默认采用ContenLength来计算文件总大小
- setIncrementUpdate(false)：表示是否采用增量更新的方式下载，默认是不采用
- setUseDefaultDownDialog(true)：表示是否使用默认的下载进度框，true 表示使用，false 表示不使用，默认情况下为false，即不使用

当我们需要自定义下载进度加载的`dialog`时,我们需要设置`setUseDefaultDownDialog(false)`,然后添加自己定义的进度条，类似如下:
```
  /**更新apk**/
    private void updateApk(String url){
        String fileName= DownLoadHelper.getInstance().getDefaultFileName(url);
        RetroLog.i("=========fileName===kk==="+fileName);
        //自定义加载进度框
        MyProgressDialog dialog= new MyProgressDialog();
        dialog.show();
        //启动下载
        DownLoadHelper.getInstance().setFileName(fileName)//设置下载文件名
                //其他代码省略
                //......
                .setUseDefaultDownDialog(false)//禁用默认下载进度dialog
                .downLoadFile(url, MainActivity.this, new DownLoadHelper.DownloadListener(){
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void update(int progress, boolean done) {
                         //更新进度条
                         dialog.setProgress(progress)；
                    }

                    @Override
                    public void onCompleted() {
                         //进度条隐藏
                         dialog.dismiss();
                    }

                    @Override
                    public void onError(String err) {
                         //进度条隐藏
                         dialog.dismiss();

                        RetroLog.i("=========下载失败=====" + err);
                        if (DownLoadHelper.getInstance().isDeltaFileFailed(err)) {
                            RetroLog.i("=========增量更新下载失败=====" + err);
                            //增量更新下载失败的逻辑(一般发rx通知出来，然后改全量更新)
                            //......
                            //由于使用非增量更新，故此处不做处理
                        } else {
                            RetroLog.i("=========全量更新下载失败=====" + err);
                            //全量更新下载失败的逻辑(一般提示退出app)

                        }
                    }
                });
    }
```
这样，就可以下载新的apk文件进行安装了。不过在调用这个下载方法前，我们最好先判断下手机内存是否足够。
#### 四. 关于增量更新
##### 4.1 生成差分文件和合成新文件的工具
本库`increment_update`文件夹下是增量更新相关工具文件，包括以下几个文件夹
```
bsdiff-4.3  差分工具
bzip2-1.0.6  差分工具依赖库
bspatch(so+jar)  实现增量更新的so+jar包
bsdiff-win-master  windows版差量工具
```
这里前三个文件夹供大家学习之用，了解就好。主要看`bsdiff-win-master`文件夹
`bsdiff-win-master`文件夹下主要看`bsdiff-v4.3-win-x64.zip`压缩包
将`bsdiff-v4.3-win-x64.zip`解压出来，里面有两个exe可执行文件，可在windows 64位系统上运行：
```
bsdiff.exe  生成差分文件的工具
bspatch.exe  合成新文件的工具
```
以下贴出利用`bsdiff.exe`和`bspatch.exe`工具，结合cmd命令生成差分文件的命令：
```
//生成增量文件
bsdiff app_1.0.apk app_2.0.apk old-to-new.patch

//合成新文件
bspatch app_1.0.apk new2.apk old-to-new.patch

//校验app_2.0.apk与新合成的new2.apk的md5是否一致(一致则说明合成apk正确)
Linux环境下md5校验命令
md5 old.apk       //old为要校验的apk文件名

windows环境下md5校验命令
certutil -hashfile old.apk MD5      //old为要校验的apk文件名  
```
##### 4.2 生成差分文件和合成新文件的依赖库
除了可以用本库的差分工具生成差分文件和合成新文件，你也可以用增量更新库实现文件的差分和合成。  
具体可参考[增量更新库](https://github.com/ShaoqiangPei/IncrementUpdate)

##### 4.3 增量更新的几个疑问解答
###### 增量更新库的实现方式
实现增量更新功能时，最好是建立`JNI`项目，利用 `native+cmake`的方式实现功能，然后发布成库供引用(类似[增量更新库](https://github.com/ShaoqiangPei/IncrementUpdate)的操作)，而不要用`so+jar`的方式供别人引用，因为`so+jar`容易出现ABI架构兼容的问题，似乎`so+jar`在后续也步怎么支持了。
###### 生成差分文件和合成文件的方式
生成差分文件和合成文件的方式有两种：利用上面提供的`exe`文件进行差分和合成，利用[增量更新库](https://github.com/ShaoqiangPei/IncrementUpdate)
代码进行差分和合成。这里有个疑问:如果我用[增量更新库](https://github.com/ShaoqiangPei/IncrementUpdate)实现的增量更新功能，但我生成差分文件文件却是用的`exe`工具，那么会不会导致代码合成新文件时出错？
答案是不会，虽然一个是工具，一个是代码，但算法一样。
###### 获取旧包路径问题
网上参看相关文章，大家在展示增量更新功能的时候，都是新建一个demo，然后将打包好的旧包(test_1.0.apk),新包(test_2.0.apk)和差分文件(test.patch)一起拷贝到sdcard的一个具体路径下，这样，他们测试的时候，就可以直接获取`test_1.0.apk`,`test_2.0.apk`和`test.patch`的绝对路径进行测试了。但是在实际开发过程俄中，都是在代码中直接获取当前安装包运行路径(路径类似`/data/app/包名+字符/base.apk`)。那么疑问是：生成差分文件是用打包好的`test_1.0.apk`,而用于合成文件时却是用的旧包的运行路径，会不会导致更新时合成新文件失败？
答案是不会，不用担心，经测试可以完美合成。
###### 增量更新测试问题
测试的时候，一般是打旧包版本(test_1.0.apk),然后打新包版本(test_2.0.apk),用这两个包生成差分文件(test.patch)，最后将`test.patch`上传到服务器。
重点来了，测试的时候，不是直接在`androidstudio`中运行`test_1.0`版本的代码来测试增量更新，而应该是用已经打包好的旧包版本(test_1.0.apk)来测试这项功能。
###### 增量更新失败排查问题
若差分文件下载成功,合成文件失败，手机界面显示在安装软件界面并由弹出框提示：解析包安装错误。请检查代码中合成新文件时的`旧包路径`，`新包路径`及`差分文件路径`是否错误。

