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
        String url="http://xxx.xxx.cn:3366/down/baidu.apk";//为示例代码url
        //启动下载
        ProgressDialog progressDialog = DownLoadHelper.getInstance().showDownLoading(mContext);
        DownLoadHelper.getInstance().setFileName("abc.apk")//设置apk文件名
                .setIcon(R.mipmap.ic_launcher)//设置apk图标资源id
                .setAuthorityTag("com.p.atestdemo")//设置要与清单文件的 provider 中配置的authorities值一致
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

                        LogUtil.i("=========下载失败=====");
                        
                        //下载失败的逻辑
                        //......

                    }
                });
```
这样，就可以下载新的apk文件进行安装了。不过在调用这个下载方法前，我们最好先判断下手机内存是否足够。
