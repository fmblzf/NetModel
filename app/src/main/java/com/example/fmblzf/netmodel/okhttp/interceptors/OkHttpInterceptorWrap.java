package com.example.fmblzf.netmodel.okhttp.interceptors;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/18.
 */
public class OkHttpInterceptorWrap {

    private static final String TAG = "OkHttpInterceptorWrap";

    /**
     *
     * OkHttp3.x的拦截器实现
     * 拦截器分为：应用拦截器和网络拦截器
     * 示意图：http://upload-images.jianshu.io/upload_images/1504154-8daf5fd9540545d3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240
     *
     *
     */

    private static OkHttpInterceptorWrap instance;

    private OkHttpInterceptorWrap(){}

    /**
     * 创建全局的单利实体
     * @return
     */
    public static OkHttpInterceptorWrap getInstance(){
        if (instance == null){
            synchronized (OkHttpInterceptorWrap.class){
                if (instance == null){
                    instance = new OkHttpInterceptorWrap();
                }
            }
        }
        return instance;
    }

    /**
     * 日志拦截器方法
     */
    public void logInterceptor(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();
        Request request = new Request.Builder().url("http://www.publicobject.com/helloworld.txt").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


    /**
     * 设置OkHttpClient的参数，并且返回对应的实例
     * @return
     */
    private OkHttpClient getOkHttpClient(){
        String directoryPath = Environment.getExternalStorageDirectory()+"/okhttp_httpclient_cache";
        File file = new File(directoryPath);
        if (!file.exists()){
            file.mkdirs();
        }
        int maxSize = 1024 * 1024 * 10;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .cache(new Cache(file.getAbsoluteFile(),maxSize));
        return builder.build();
    }



}
