package com.example.fmblzf.netmodel.origin.urlconnect;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmblzf on 2017/5/2.
 */
public class HttpUrlConnectionWraper {


    enum Method{
        /**
         * 定义枚举类类别常量
         */
        GET("GET"),POST("POST");

        //当前value值
        private String mValue;

        /**
         * 构造器，枚举类的构造器必须私有化
         * @param value
         */
        private Method(String value){
            mValue = value;
        }

        /**
         * 获取当前的value值
         * @return
         */
        public String getValue(){
            return mValue;
        }
    }

    /**
     * 当前标记
     */
    private static final String TAG = "HttpUrlConnectionWraper";

    /**
     *
     *
     * HttpURLConnection Android内置网络类，
     * 但是在Android 2.2版本之前，HttpURLConnection存在bug,所以使用的是Apache的HttpClient包；
     * 在Android 2.2 之后google已经优化和修复了HttpURLConnection的问题，所以之后建议使用HttpURLConnection
     * Android 2.2 之前的问题描述：比如可读的InputStream调用close()方法时，就有可能导致连接池失效了，通常解决方法就是直接禁用掉连接池的功能
     * 优势：API简单，体积小
     *
     *
     *
     */

    //实体对象
    private static HttpUrlConnectionWraper instance;

    /**
     * 私有化构造器，实现单例模式
     */
    private HttpUrlConnectionWraper(){

    }

    /**
     * 单例模式获取当前的实体对象
     * @return
     */
    public static HttpUrlConnectionWraper getInstance(){
        if (null == instance){
            synchronized (HttpUrlConnectionWraper.class){
                if (null == instance){
                    instance = new HttpUrlConnectionWraper();
                }
                return instance;
            }
        }
        return instance;
    }

    /**
     * 解决Android 2.2 之前的bug方式，禁用掉连接池
     */
    private void disableConnectionReuselfNecessary(){
        if (Integer.parseInt(Build.VERSION.SDK)<Build.VERSION_CODES.FROYO){
            System.setProperty("http.keepAlive","false");
        }
    }

    /**
     * 创建HttpURLConnection对象实例
     * @param url
     * @param method
     * @return
     */
    public HttpURLConnection createHttpURLConnection(String url,Method method){
        HttpURLConnection httpURLConnection  =null;
        try {
            URL mUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) mUrl.openConnection();
            //设置连接超时时间
            httpURLConnection.setConnectTimeout(1500);
            //设置读取超时时间
            httpURLConnection.setReadTimeout(1500);
            //设置请求方法
            httpURLConnection.setRequestMethod(method.getValue());
            //添加Header
            httpURLConnection.setRequestProperty("Connection","Keep-Alive");
            //接收输入流
            httpURLConnection.setDoInput(true);
            //传递参数时，需要开启
            httpURLConnection.setDoOutput(true);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return httpURLConnection;
    }

    /**
     * post请求时，传递参数的方法
     * @param outputStream 输出流
     * @param pairList 参数集合
     */
    public void postParams(OutputStream outputStream, List<NameValuePair> pairList) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (NameValuePair pair:pairList){
            if (!TextUtils.isEmpty(stringBuilder)){
                stringBuilder.append("&");
            }
            stringBuilder.append(URLEncoder.encode(pair.getName(),"UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(pair.getValue(),"UTF-8"));
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        writer.write(stringBuilder.toString());
        writer.flush();
        writer.close();
    }

    /**
     * 将当前的流转化成对应的字符串
     * @param inputStream 输入流
     * @return
     */
    private String convertStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine())!=null){
            sb.append(line+"\n");
        }
        return sb.toString();
    }

    /**
     * HttpURLConnection GET
     * @param url
     */
    public void httpUrlConnectionGet(String url){
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = createHttpURLConnection(url,Method.GET);
        try {
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            int code = httpURLConnection.getResponseCode();
            String resposeStr = convertStreamToString(inputStream);
            Log.e(TAG,"请求状态码："+code+"\n请求结果：\n"+resposeStr);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * HttpURLConnection POST
     * @param url
     */
    public void httpUrlConnectionPost(String url){
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = createHttpURLConnection(url,Method.POST);
        List<NameValuePair> pairList = new ArrayList<>();
        //添加要传递的参数
        pairList.add(new BasicNameValuePair("username","admin"));
        pairList.add(new BasicNameValuePair("password","123456"));
        try {
            postParams(httpURLConnection.getOutputStream(),pairList);
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            inputStream = httpURLConnection.getInputStream();
            String resposeStr = convertStreamToString(inputStream);
            Log.e(TAG,"请求状态码："+code+"\n请求结果：\n"+resposeStr);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
