package com.example.fmblzf.netmodel.okhttp;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
     * OkHttp 的封装代码 https://github.com/pengjianbo/OkHttpFinal.git
     *
     *
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
    //文件上传时的文件类型
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    //Multipart文件时的类型
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

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
        call.enqueue(new Callback() {//如果需要同步上传文件的话，将“enqueue”改成“execute”
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
        String url = "http://10.0.6.82:8000/wrap";
        OkHttpClient okHttpClient = new OkHttpClient();
        //post传递的参数
        RequestBody formBody = new FormBody.Builder().add("text","Python服务器测试").add("width","10").build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendMessage(1002,response.body().string());
            }
        });
    }
    /**
     * 上传文件
     * @param filePath
     */
    public void uploadFile(String filePath){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://api.github.com/markdown/raw").post(RequestBody.create(MEDIA_TYPE_MARKDOWN,new File(filePath))).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,response.body().string());
            }
        });
    }

    /**
     * 下载文件
     * @param fileUrl
     */
    public void downloadFile(String fileUrl){
        if (fileUrl == null){
            fileUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495015913466&di=099481542fdfdd28adb935f5dd38a109&imgtype=0&src=http%3A%2F%2Fsm.loupan.com%2Fupfile2%2Fimage%2F20170225%2F20170225115404_5942463.jpeg";
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(fileUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                String filePath = Environment.getExternalStorageDirectory()+"/download/download.jpeg";
                File file = new File(filePath);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()){
                    parentFile.mkdirs();
                }
                if (!file.exists()){
                    file.createNewFile();
                }
                OutputStream outputStream = new FileOutputStream(file);
                int len = 0;
                byte[] bytes = new byte[1024*2];
                while ((len = inputStream.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                    outputStream.flush();
                }
                inputStream.close();
                outputStream.close();
                sendMessage(1004,filePath);
            }
        });
    }

    /**
     * 上传文件同时带有数据的类型
     * @param fileUrl
     */
    public void uploadMultipart(String fileUrl){
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title","中国地图");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = jsonObject.toString();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("data",data).addFormDataPart("file","china.jpg",
                RequestBody.create(MEDIA_TYPE_PNG,new File(fileUrl))
                ).build();
        final Request request = new Request.Builder().addHeader("Authorization", "Client-ID " + "...").url("http://10.0.6.82:8089/webapp/multipart.do").post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,response.body().string());
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
