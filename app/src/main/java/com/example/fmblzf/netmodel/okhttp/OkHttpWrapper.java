package com.example.fmblzf.netmodel.okhttp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Fmblzf on 2017/5/6.
 */
public class OkHttpWrapper {

    /**
     *
     *
     * 会从很多常用的连接问题中自动恢复，
     * 如果服务器配置了多个ip地址，当第一个IP连接失败的时候，OkHttp会自动尝试下一个IP，
     * 此外OkHttp还处理了代理服务器问题和SSL握手失败问题
     *
     *
     */


    /**
     * 当前标记
     */
    private static final String TAG = "OkHttpWrapper";

    //当前对象实例
    private static OkHttpWrapper instance;

    private HandlerThreadCallable mHandler;

    /**
     * 私有化构造器，实现单例模式
     */
    private OkHttpWrapper(Handler.Callback callback){
        mHandler = new HandlerThreadCallable(callback);
    }

    /**
     * 获取当前实例对象
     * @return
     */
    public static OkHttpWrapper getInstance(Handler.Callback callback){
        if (null == instance){
            synchronized (OkHttpWrapper.class){
                if (instance == null){
                    instance = new OkHttpWrapper(callback);
                }
                return instance;
            }
        }
        return instance;
    }

    /**
     * 发送消息
     * @param flag
     * @param response
     */
    private void sendMessage(int flag,String response){
        Message message = mHandler.obtainMessage();
        message.what = flag;
        message.obj = response;
        mHandler.sendMessage(message);
    }

    /**
     * 获取异步请求
     */
    public void getAsynHttp(){
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url("http://v.juhe.cn/toutiao/index?type=&key=dd7bba438f17877f6790d49087424418").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.i(TAG,response.body().string());
                //string()只能调用一次，完了之后就会缓存
                sendMessage(1000,response.body().string());
            }
        });
    }
    /**
     * 获取同步请求
     * 即使同步，也要放在子线程中启动操作，否则就会报错
     * 因为：耗时操作不能在主线程中
     */
    public void getSyncHttp(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://v.juhe.cn/toutiao/index?type=&key=dd7bba438f17877f6790d49087424418").build();
        final Call call = okHttpClient.newCall(request);
        new Thread(){
            @Override
            public void run() {
                Response response = null;
                try {
                    response = call.execute();
//                    Log.i(TAG,response.body().string());
                    sendMessage(1001,response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * okHttp异步处理
     */
    public void postHttp(){
        String url = "http://api.1-blog.com/biz/bizserver/article/list.do";
        OkHttpClient okHttpClient = new OkHttpClient();
        //post传递的参数
        RequestBody formBody = new FormBody.Builder().add("size","10").build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendMessage(1002,response.body().string());
            }
        });
    }

    /**
     *
     * 处理异步线程处理时的回调数据处理
     *
     */
    public static class HandlerThreadCallable extends Handler{

        private Callback callback;

        public HandlerThreadCallable(Callback callback){
            this.callback = callback;
        }

        @Override
        public void dispatchMessage(Message msg) {
            callback.handleMessage(msg);
        }
    }
}
