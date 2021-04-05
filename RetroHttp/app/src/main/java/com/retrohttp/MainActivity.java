package com.retrohttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.httplibrary.download.DownLoadHelper;
import com.httplibrary.util.RetroLog;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private Button mBtnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnTest=findViewById(R.id.mBtnTest);
        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请授权
                MainActivityPermissionsDispatcher.requestWithPermissionCheck(MainActivity.this);
            }
        });
    }

    /**申请权限**/
    @NeedsPermission({Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void request() {
        RetroLog.i("=====允许授权=====");
        //获取设备号
        //......

        RetroLog.i("=====开始下载=====");
        String url="https://naibayd.inm.cc/index.php/fm/file/app/metering_test_1.0.1.apk";
        updateApk(url);

    }

    /**弹授权框(第一次拒绝后,第二次打开弹出的授权框)**/
    @OnShowRationale({Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDialog(final PermissionRequest request){
        new AlertDialog.Builder(this)
                .setMessage("是否授权手机权限?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    /**拒绝授权**/
    @OnPermissionDenied({Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void deniedPermission(){
        RetroLog.i("=====拒绝授权的处理=====");
        //弹框提示：需要授权
        //......
    }

    /**不再询问**/
    @OnNeverAskAgain({Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void neverAskPermission(){
        RetroLog.i("=====不再询问的处理=====");

        //弹框提示：需要授权
        //......
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**更新apk**/
    private void updateApk(String url){
//        int firstIndex=url.lastIndexOf("/") + 1;
//        int lastIndex = url.length();
//        String fileName = url.substring(firstIndex, lastIndex);

        String fileName= DownLoadHelper.getInstance().getDefaultFileName(url);
        RetroLog.i("=========fileName===kk==="+fileName);
        //启动下载
//        ProgressDialog progressDialog = DownLoadHelper.getInstance().showDownLoading(MainActivity.this);
        DownLoadHelper.getInstance().setFileName(fileName)//设置下载文件名
                .setFileLength(0)//设置下载文件大小
                .setIcon(R.mipmap.ic_launcher)//设置apk图标资源id
                .setAuthorityTag("com.retrohttp")//设置要与清单文件的 provider 中配置的authorities值一致
                .setIncrementUpdate(false)//是否开启增量更新(true:开启,false:关闭,此行代码不设置会默认全量更新)
                .setUseDefaultDownDialog(true)//开启默认下载进度dialog
                .downLoadFile(url, MainActivity.this, new DownLoadHelper.DownloadListener(){
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void update(int progress, boolean done) {
//                        progressDialog.setProgress((int)(progress * 1f));
                    }

                    @Override
                    public void onCompleted() {
//                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(String err) {
//                        progressDialog.dismiss();
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


}
