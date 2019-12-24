package com.httplibrary.download;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;
import com.httplibrary.R;
import com.httplibrary.app.RetroConfig;
import com.httplibrary.util.RetroLog;
import com.httplibrary.util.StringUtil;

import java.io.File;

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
    public static final String DEST_FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File
            .separator + "appFile";

    //目标文件存储的文件名,如:"check.apk"
    private String mApkName;
    //AUTHORITY_TAG 即是在清单文件中配置的authorities
    private String mAuthorityTag;
    //appIcon,即app图标id，如：R.mipmap.ic_launcher
    private int mAppIconId;

    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
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
     * @param apkName:apk文件名，如：check.apk
     * @return
     */
    public DownLoadHelper setFileName(String apkName){
        this.mApkName=apkName;
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

    /**
     * 下载
     *
     * @param url：url全路径
     * @param context 上下文
     * @param downloadListener 监听接口
     */
    public void downLoadFile(String url,Context context, final DownloadListener downloadListener){
        //设置通知icon
        if(mAppIconId==0){
            throw new SecurityException("===请设置下载通知的图标icon资源id(一般设置app的icon)===");
        }
        //设置文件名
        if(StringUtil.isEmpty(mApkName)){
            throw new NullPointerException("===请设置下载的文件名,如:jianshu.apk===");
        }
        //设置清单文件的 provider 中配置的authorities
        if(StringUtil.isEmpty(mAuthorityTag)){
            throw new NullPointerException("===请设置清单文件的 provider 中配置的authorities值===");
        }
        RetroLog.i("=======下载url====="+url);
        this.mContext=context;
        //初始化通知
        initNotification(mAppIconId);
        //开始下载
        AppDownLoadHelper helper = AppDownLoadManager.getInstance().getHelperByTag("xx");
        if (helper != null) {
            AppDownLoadManager.getInstance().cancelHelperByTag("xx");
        }

        final String filePath=DEST_FILE_DIR+mApkName;

        new AppDownLoadHelper.Builder()
                .setPath(filePath)
                .setTag("xx")
                .setUrl(url)
                .setDownLoadListener(new AppProgressListener() {
                    @Override
                    public void onStart() {
                        RetroLog.e("========开始下载=====");
                        downloadListener.onStart();
                    }

                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        int read = (int) (bytesRead * 100f / contentLength);
                        RetroLog.i("=======下载文件总:"+contentLength+"    当前size:"+bytesRead);
                        //更新进度条
                        downloadListener.update(read,done);
                        //更新通知
                        updateNotification(read);
                    }

                    @Override
                    public void onCompleted() {
                        RetroLog.i("========下载完成=====");

                        File file= new File(filePath);
                        // 安装软件
                        downloadListener.onCompleted();
                        cancelNotification();
                        installApk(file);

                    }

                    @Override
                    public void onError(String err) {
                        downloadListener.onError(err);
                        cancelNotification();
                        RetroLog.e("========下载失败===" + err);
                    }
                })
                .create()
                .execute();
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
                .setContentTitle("inm盘代码更新")// 标题
                .setProgress(100, 0, false);// 设置进度条
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);// 获取系统通知管理器
        notificationManager.notify(NOTIFY_ID, mBuilder.build());// 发送通知
    }

    /**
     * 更新通知
     */
    public void updateNotification(int currProgress) {
        mBuilder.setContentText(currProgress + "%");
        mBuilder.setProgress(100, currProgress, false);
        notificationManager.notify(NOTIFY_ID, mBuilder.build());

    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFY_ID);
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

}
