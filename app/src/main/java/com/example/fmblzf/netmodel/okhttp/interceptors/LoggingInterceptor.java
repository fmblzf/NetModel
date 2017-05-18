package com.example.fmblzf.netmodel.okhttp.interceptors;

import com.example.fmblzf.netmodel.utils.LoggerUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fmblzf on 2017/5/18.
 * 创建日志拦截器
 * 创建拦截器，只需要实现接口Interceptor即可
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        LoggerUtils.log(String.format("Sending request %s on %s%n%s",request.url(),chain.connection(),request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        LoggerUtils.log(String.format("Received response for %s in %.1f ms %s",response.request().url(),(t2-t1)/1e6d,response.headers()));

        return response;
    }
}


























