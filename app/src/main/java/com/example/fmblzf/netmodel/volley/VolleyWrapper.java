package com.example.fmblzf.netmodel.volley;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.fmblzf.netmodel.R;
import com.example.fmblzf.netmodel.cache.LocalCache;
import com.example.fmblzf.netmodel.cache.MemoryCache;

import org.json.JSONObject;

/**
 * Created by Fmblzf on 2017/5/2.
 */
public class VolleyWrapper {

    private static final String TAG = "VolleyWrapper";

    /**
     *
     *
     * 使用Volley框架，
     * 简介：2013年Google I/O大会上推出的一个新的网络通信框架Volley
     * Volley既可以访问网络取得数据，也可以加载图片，并且在性能方面也进行了大幅度的调整，
     * 它的设计目标就是非常适合去进行数据量不大，但通信频繁的网络操作，而对于大数据量的网络操作，
     * 比如下载文件，Volley的表现就会非常糟糕。
     *
     * Volley请求网络都是基于请求队列的，只需要把对应的请求添加到队列中即可，请求队列就会依次进行请求，
     * 一般情况下，一个应用程序如果网络请求没有特别频繁则完全可以只用一个请求队列（对应Application）,
     * 如果非常多或者其他情况，则可以是一个Activity对应一个网络请求队列。
     *
     *
     *
     */


    //实体对象
    private static VolleyWrapper instance;

    /**
     * 私有构造器
     * 实现单例模式
     */
    private VolleyWrapper(){}


    /**
     * 获取当前的对象实体
     * @return
     */
    public static VolleyWrapper getInstance(){
        if (instance == null){
            synchronized (VolleyWrapper.class){
                if (instance == null){
                    instance = new VolleyWrapper();
                }
                return instance;
            }
        }
        return instance;
    }


    /**
     * 创建Volley String请求方法
     * @param method
     * @param url
     * @return
     */
    public StringRequest createStringRequest(int method,String url){
        StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage());
            }
        });
        return stringRequest;
    }

    /**
     * 创建Volley JSON方法
     * @param method
     * @param url
     * @return
     */
    public JsonObjectRequest createJsonRequest(int method,String url,JSONObject params){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG,response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage());
            }
        });
        return jsonObjectRequest;
    }

    /**
     * 给ImageView设置图片
     * @param url
     * @param imageView
     * @return
     */
    public ImageRequest createImageRequest(String url, final ImageView imageView){
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.getMessage());
            }
        });
        return imageRequest;
    }

    /**
     * 创建对应的ImageLoader加载对应的远程图片
     * @param queue Volley队列
     * @param imageView
     * @param url
     */
    public void createImageLoader(RequestQueue queue,ImageView imageView,String url){
        ImageLoader imageLoader = new ImageLoader(queue,new NativeImageCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher,R.mipmap.ic_launcher);
        imageLoader.get(url,listener);
    }

    /**
     *
     * 实现Volley中的图片本地的缓存类
     *
     */
    public static class NativeImageCache implements ImageLoader.ImageCache{

        //内存缓存
        private MemoryCache mMemoryCache;
        //本地文件缓存
        private LocalCache mLocalCache;

        /**
         * 构造方法
         */
        public NativeImageCache(){
            mMemoryCache = MemoryCache.getInstance();
            mLocalCache = LocalCache.getInstance();
        }

        @Override
        public Bitmap getBitmap(String url) {
            Bitmap bitmap = mMemoryCache.getBitmap(url);
            if (null != bitmap){
                return bitmap;
            }
            bitmap = mLocalCache.getBitmap(url);
            if (null != bitmap){
                return bitmap;
            }
            return null;
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            //将图片保存到内存中去
            mMemoryCache.setBitmap(url,bitmap);
            //将图片保存在本地内存，以文件形式存在
            mLocalCache.setBitmap(url,bitmap);
        }
    }


    



}
