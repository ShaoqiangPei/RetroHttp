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

        int firstIndex=url.lastIndexOf("/")+1;
        int lastIndex=url.length();
        String fileName=url.substring(firstIndex,lastIndex);
        LogUtil.i("=========fileName======"+fileName);
        //启动下载
        ProgressDialog progressDialog = DownLoadHelper.getInstance().showDownLoading(mContext);
        DownLoadHelper.getInstance().setFileName(fileName)//设置下载文件名
                .setIcon(R.mipmap.ic_launcher)//设置apk图标资源id
                .setAuthorityTag("com.p.atestdemo")//设置要与清单文件的 provider 中配置的authorities值一致
                .setIncrementUpdate(mUpdate)//是否开启增量更新(true:开启,false:关闭,此行代码不设置会默认全量更新)
                .downLoadFile(url, MainActivity.this, new DownLoadHelper.DownloadListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void update(int progress, boolean done) {
                        progressDialog.setProgress((int) (progress * 1f));
                    }

                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(String err) {
                        progressDialog.dismiss();
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
具体可参考


