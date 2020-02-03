package com.httplibrary.download;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import com.httplibrary.util.NetUtil;
import com.httplibrary.util.RetroLog;
import com.httplibrary.util.StringUtil;
import java.io.File;
import java.io.IOException;
import ha.excited.BigNews;

/**
 * Title:下载apk文件帮转类，供外部使用
 * Description:
 *
 * Created by pei
 * Date: 2018/4/24
 */
public class DownLoadHelper {

    /**
     * 目标文件存储的文件夹路径
     */
    //DEST_FILE_DIR===/storage/emulated/0/check_app
    private static final String DEST_FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "appFile";

    private static final String DELTA_FAILED_MESSAGE="增量更新失败";

    //目标文件存储的文件名,如:"check.apk,delta.patch"
    private String mFileName;
    //AUTHORITY_TAG 即是在清单文件中配置的authorities
    private String mAuthorityTag;
    //appIcon,即app图标id，如：R.mipmap.ic_launcher
    private int mAppIconId;
    //是否开启增量更新
    private boolean mIncrementUpdate=false;//默认false，不开启

    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private Context mContext;

    private DownLoadHelper() {}

    private static class Holder {
        private static DownLoadHelper instance = new DownLoadHelper();
    }

    public static DownLoadHelper getInstance() {
        return DownLoadHelper.Holder.instance;
    }

    /**
     * 设置通知icon id，如：R.mipmap.ic_launcher
     * @param appIconId
     * @return
     */
    public DownLoadHelper setIcon(int appIconId){
        this.mAppIconId=appIconId;
        return DownLoadHelper.this;
    }

    /**
     * 设置通知icon id，如：R.mipmap.ic_launcher
     * @param filName:apk文件名或增量文件名，如：check.apk,delta.patch
     * @return
     */
    public DownLoadHelper setFileName(String filName){
        this.mFileName=filName;
        return DownLoadHelper.this;
    }

    /***
     * 设置文件下载的 mAuthorityTag,即是在清单文件的 provider 中配置的authorities
     * @param authorityTag
     * @return
     */
    public DownLoadHelper setAuthorityTag(String authorityTag){
        this.mAuthorityTag=authorityTag;
        return DownLoadHelper.this;
    }

    /***
     * 是否开启增量更新,默认为false，即不开启。
     * @return
     */
    public DownLoadHelper setIncrementUpdate(boolean incrementUpdate){
        this.mIncrementUpdate=incrementUpdate;
        return DownLoadHelper.this;
    }

    /**
     * 下载
     *
     * @param url：url全路径
     * @param context 上下文
     * @param downloadListener 监听接口
     */
    public void downLoadFile(String url,Context context, final DownloadListener downloadListener){
        //检测网络
        if(!NetUtil.isNetworkConnected()){
            downloadListener.onError("当前无网络,请检测网络链接!");
            RetroLog.e("=========下载失败:当前无网络,请检测网络链接!");
            return;
        }
        if(StringUtil.isEmpty(url)){
            throw new NullPointerException("===请设置下载的url地址===");
        }
        //设置通知icon
        if(mAppIconId==0){
            throw new SecurityException("===请设置下载通知的图标icon资源id(一般设置app的icon)===");
        }
        //设置文件名
        if(StringUtil.isEmpty(mFileName)){
            throw new NullPointerException("===请设置下载的文件名,如:jianshu.apk,delta.patch===");
        }
        //当为增量更新时,mFileName中需要以“.patch”结尾
        if(mIncrementUpdate&&!mFileName.contains(".patch")){
            throw new SecurityException("===为增量更新,setFileName(String filName)中的filName需要以.patch尾缀结尾===");
        }
        //设置清单文件的 provider 中配置的authorities
        if(StringUtil.isEmpty(mAuthorityTag)){
            throw new NullPointerException("===请设置清单文件的 provider 中配置的authorities值===");
        }
        this.mContext=context;
        //打印下载信息
        printDownLoadLog(url);
        //初始化通知
        initNotification(mAppIconId);
        //开始下载
        AppDownLoadHelper helper = AppDownLoadManager.getInstance().getHelperByTag("xx");
        if (helper != null) {
            AppDownLoadManager.getInstance().cancelHelperByTag("xx");
        }

        final String filePath=DEST_FILE_DIR+mFileName;

        new AppDownLoadHelper.Builder()
                .setPath(filePath)
                .setTag("xx")
                .setUrl(url)
                .setDownLoadListener(new AppProgressListener() {
                    @Override
                    public void onStart() {
                        RetroLog.w("========开始下载=====");
                        downloadListener.onStart();
                    }

                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        int read = (int) (bytesRead * 100f / contentLength);
                        RetroLog.w("=======下载文件总:"+contentLength+"    当前size:"+bytesRead);
                        //更新进度条
                        downloadListener.update(read,done);
                        //更新通知
                        updateNotification(read);
                    }

                    @Override
                    public void onCompleted() {
                        RetroLog.w("========下载完成=====");

                        if(mIncrementUpdate){
                            RetroLog.w("========增量更新下载完成==============");
                            //".patch"文件和本地".apk"文件合成新apk文件的方法
                            bsPatchFile(context, filePath, new OnDeltaFileListener() {
                                @Override
                                public void deltaFileSuccess(File file) {

                                    downloadListener.onCompleted();
                                    cancelNotification();
                                    //文件合成成功后安装软件
                                    installApk(file);
                                }

                                @Override
                                public void deltaFileFailed() {
                                    //文件合成失败,设置增量更新标志 mIncrementUpdate=false,开始执行全量更新
                                    mIncrementUpdate=false;
                                    //增量更新运行失败
                                    downloadListener.onError(DELTA_FAILED_MESSAGE);
                                    cancelNotification();
                                }
                            });
                        }else{
                            RetroLog.w("========全量更新下载完成==============");
                            downloadListener.onCompleted();
                            cancelNotification();

                            File file= new File(filePath);
                            //安装软件
                            installApk(file);
                        }
                    }

                    @Override
                    public void onError(String err) {
                        downloadListener.onError(err);
                        cancelNotification();
                        RetroLog.e("=========下载失败:" + err);
                    }
                })
                .create()
                .execute();
    }

    /**是否为增量更新失败返回的errormessage**/
    public boolean isDeltaFileFailed(String errorMessage){
        if(DownLoadHelper.DELTA_FAILED_MESSAGE.equals(errorMessage)){
            return true;
        }
        return false;
    }

    /**打印下载信息**/
    private void printDownLoadLog(String url){
        RetroLog.w("===下载链接====url="+url);
        RetroLog.w("===下载app图标====mAppIconId="+mAppIconId);
        RetroLog.w("===下载文件名====mFileName="+mFileName);
        RetroLog.w("===下载authorityTag====mAuthorityTag="+mAuthorityTag);
        RetroLog.w("===下载是否开启增量更新(false=不开启,true=开启)====mIncrementUpdate="+mIncrementUpdate);
    }

    /***
     * 增量更新合成文件的方法
     */
    private void bsPatchFile(Context context,String patchPath,OnDeltaFileListener listener){
        new AsyncTask<Void, Void, File>() {
            @Override
            protected File doInBackground(Void... voids) {
                if(StringUtil.isEmpty(patchPath)){
                    RetroLog.w("=====增量文件路径为空=====patchPath="+patchPath);
                    return null;
                }
                //获取旧版本路径（正在运行的apk路径）
                // 示例：/data/app/com.testq-FiRlNIBqo9oKf4dXh6ChSQ==/base.apk
                String oldPath=context.getApplicationInfo().sourceDir;
                File oldFile=new File(oldPath);
                if(!oldFile.exists()){
                    RetroLog.w("=====旧版本安装文件(正在运行的apk文件)不存在======");
                    return null;
                }
                RetroLog.w("====旧版本apk文件路径==oldPath="+oldPath);
                //差分包路径，服务器下载到本地路径
                //  示例：/data/data/com.testq/old-to-new.patch
                File patchFile=new File(patchPath);
                if(!patchFile.exists()){
                    RetroLog.w("=====差分文件patchFile不存在===========");
                    return null;
                }
                //合成的新的apk保存路径
                String outputPath = createNewDownLoadFile().getAbsolutePath();
                //开始合成，是一个耗时任务
                BigNews.make(oldPath, patchPath, outputPath);
                //合成成功，重新安装apk
                File outputFile=new File(outputPath);
                if(!outputFile.exists()){
                    RetroLog.w("=====合成文件不存在======");
                    return null;
                }
                RetroLog.w("=====合成文件路径=====outputPath="+outputPath);
                return outputFile;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                if (file != null&&file.exists()) {
                    //合成文件成功,安装新版本apk
                    if(listener!=null){
                        listener.deltaFileSuccess(file);
                    }
                }else{
                    //合成文件失败
                    if(listener!=null){
                        listener.deltaFileFailed();
                    }
                }
            }
        }.execute();
    }

    /**
     * 创建合成后的新版本apk文件
     * 若截取文件名失败则默认返回文件名为：delta_pei.apk
     *
     * @return
     */
    private File createNewDownLoadFile() {
        String path=DEST_FILE_DIR+mFileName;
        File newFile = new File(path);
        if(newFile.exists()){
            newFile.delete();
        }
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    /**dialog进度条**/
    public ProgressDialog showDownLoading(Context context){
        // 创建对话框构建器
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        return progressDialog;
    }

    /**
     * 初始化Notification通知
     *
     * @param drawableId:通知图标，eg：R.mipmap.ic_launcher
     */
    public void initNotification(int drawableId){
        mBuilder=new NotificationCompat.Builder(mContext, "default");
        mBuilder.setSmallIcon(drawableId)// 设置通知的图标
                .setContentText("0%")// 进度Text
                .setContentTitle("app更新")// 标题
                .setProgress(100, 0, false);// 设置进度条
        sendNotification();
    }

    /**
     * 更新通知
     */
    public void updateNotification(int currProgress) {
        mBuilder.setContentText(currProgress + "%");
        mBuilder.setProgress(100, currProgress, false);
        sendNotification();
    }

    private void sendNotification(){
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);// 获取系统通知管理器
        //高版本需要渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//android8.0
            //只在Android O之上需要渠道
            String channelId = "default";
            String channelName = "默认通知";
            NotificationChannel notificationChannel=new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        //发送通知
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NOTIFY_ID);
        }
    }

    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
        if (!file.exists()) {
            return;
        }
        Uri uri = null;
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // UpdateConfig.FILE_PROVIDER_AUTH 即是在清单文件中配置的authorities
            uri = FileProvider.getUriForFile(mContext, mAuthorityTag, file);
            // 给目标应用一个临时授权
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(install);
    }

    public interface DownloadListener{
        void onStart();
        void update(int progress, boolean done);
        void onCompleted();
        void onError(String err);
    }

    public interface OnDeltaFileListener{
        void deltaFileSuccess(File file);
        void deltaFileFailed();
    }

}
