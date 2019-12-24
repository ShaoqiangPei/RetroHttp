package com.httplibrary.download;

import com.httplibrary.util.RetroLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public class AppDownLoadHelper {

    public static final int CONNECTION_TIMEOUT = 10;

    public static final int READ_DOWN_TIMEOUT = 20;

    public String mBaseUrl;

    public String mApkName;

    public Set<AppProgressListener> mDownloadListeners;

    private AppDownLoadManager mManager;

    public AppProgressListener mListener;

    private String mUrl;

    private String mPath;

    private int mConnectionTimeout;

    private int mReadTimeout;

    public Object mTag;

    private Retrofit mRetrofit;

    private UploadService mUploadService;

    private boolean isCancel;

    private AppDownLoadHelper() {
        mDownloadListeners = new HashSet<>();
        mManager = AppDownLoadManager.getInstance();
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public void setmUrl(String url) {
        if (url != null) {
            int lastIndex = url.lastIndexOf("/");
            if (lastIndex != -1) {
                mApkName = url.substring(lastIndex + 1);
                mBaseUrl = url.substring(0, lastIndex + 1);
            }
        }
    }

    /**
     * 默认分配tag
     */
    public void setTag(Object tag) {
        if (tag != null) {
            mTag = tag;
        } else {
            mTag = UUID.randomUUID().toString();
        }
    }

    public void registerListener(AppProgressListener listener) {
        mDownloadListeners.add(listener);
    }

    public void unRegisterListener(AppProgressListener listener) {
        mDownloadListeners.remove(listener);
    }

    private OkHttpClient getDefaultOkHttp() {
        return getBuilder().build();
    }

    private OkHttpClient.Builder getBuilder() {
        AppSigningInterceptor signingInterceptor = new AppSigningInterceptor();
        signingInterceptor.setProgressListener(mListener);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(signingInterceptor);
        builder.connectTimeout(mConnectionTimeout, TimeUnit.SECONDS);
        builder.readTimeout(mReadTimeout, TimeUnit.SECONDS);
        return builder;
    }


    public static class Builder {
        private AppProgressListener mListener;
        private String mUrl;
        private String mPath;
        private int mConnectionTimeout;
        private int mReadTimeout;
        private Object mTag;

        public Builder() {
            this.mConnectionTimeout = CONNECTION_TIMEOUT;
            this.mReadTimeout = READ_DOWN_TIMEOUT;
        }

        /**
         * 设置任务标记
         */
        public Builder setTag(Object tag) {
            this.mTag = tag;
            return this;
        }

        /**
         * 设置下载文件的URL
         */
        public Builder setUrl(String url) {
            this.mUrl = url;
            return this;
        }

        /**
         * 设置HTTP请求连接超时时间，默认10s
         */
        public Builder setConnectionTimeout(int timeout) {
            this.mConnectionTimeout = timeout;
            return this;
        }

        /**
         * 设置HTTP请求数据读取超时时间，默认20s
         */
        public Builder setReadTimeout(int timeout) {
            this.mReadTimeout = timeout;
            return this;
        }

        /**
         * 设置下载文件保存地址，使用绝对路径
         */
        public Builder setPath(String path) {
            this.mPath = path;
            return this;
        }

        /**
         * 设置下载监听
         */
        public Builder setDownLoadListener(AppProgressListener listener) {
            this.mListener = listener;
            return this;
        }

        /**
         * 创建一个本地任务
         */
        public AppDownLoadHelper create() {
            final AppDownLoadHelper helper = new AppDownLoadHelper();
            helper.mConnectionTimeout = mConnectionTimeout;
            helper.mReadTimeout = mReadTimeout;
            helper.mPath = mPath;
            helper.setTag(mTag);
            helper.setmUrl(mUrl);
            if (mListener != null) {
                helper.mDownloadListeners.add(mListener);
            }
            return helper;
        }

    }

    public void execute() {
        mManager.addHelper(this);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getDefaultOkHttp())
                .build();

        mUploadService = mRetrofit.create(UploadService.class);

        final long startTime = System.currentTimeMillis();
        mUploadService.downloadFile(mApkName)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        RetroLog.i( "========next" + responseBody.contentLength());

                        OutputStream output = null;
                        InputStream input = null;
                        File ret;
                        try {
                            input = responseBody.byteStream();
                            ret = new File(mPath);
                            output = new FileOutputStream(ret);
                            byte[] buffer = new byte[8192];
                            int len = -1;
                            while ((len = input.read(buffer)) != -1) {
                                if (isCancel) {
                                    ret.delete();
                                    return;
                                }
                                output.write(buffer, 0, len);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (input != null) {
                                    input.close();
                                }
                                if (output != null) {
                                    output.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        long endTime = System.currentTimeMillis();
                        RetroLog.i( "========time" + (endTime - startTime));

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        if (mListener != null) {
                            mListener.onError(throwable.getMessage());
                        }

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if (isCancel) {
                            mListener.onError("cancel");
                        } else if (mListener != null) {
                            mListener.onCompleted();
                        }

                    }
                });
        if (mListener != null) {
            mListener.onStart();
        }
    }

    public interface UploadService<T> {
        //更新app,下载最新app版本文件
        @Streaming
        @GET
        Observable<ResponseBody> downloadFile(@Url String url);
    }

}