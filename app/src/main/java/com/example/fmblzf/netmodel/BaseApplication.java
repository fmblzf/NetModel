package com.example.fmblzf.netmodel;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by fmblzf on 2017/5/3.
 */
public class BaseApplication extends Application {

    //全局的请求轮询队列
    private static RequestQueue mQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        //创建该队列
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    /**
     * 获取当前应用的全局网络及缓存轮询队列
     * @return
     */
    public static RequestQueue getQueue(){
        return mQueue;
    }
}
