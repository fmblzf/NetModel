package com.example.fmblzf.netmodel.origin.httpclient;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmblzf on 2017/5/2.
 */
public class HttpClientWraper {

    /**
     * 当前类的标记
     */
    private static final String TAG = "HttpClientWraper";


    /**
     *
     *
     *  Apache的HttpClient,android6.0已经移除了内置模块，使用时需要在build.gradle中添加对应的userLibrary 'org.apache.http.legacy'
     *  在Android2.2版本之前使用率很高，因为当时Android内置网络模块HttpUrlConnection存在好多bug
     *
     *
     *
     */



    private static HttpClientWraper instance;

    /**
     *  单例模式，私有化构造器
     */
    private HttpClientWraper(){

    }

    /**
     * 获取当前的实体对象
     * @return
     */
    public static HttpClientWraper getInstance(){
        if (instance == null){
            synchronized (HttpClientWraper.class){
                if (instance == null){
                    instance = new HttpClientWraper();
                }
                return instance;
            }
        }
        return instance;
    }

    /**
     * 创建HttpClient对象
     * @return
     */
    public HttpClient createHttpClient(){
        HttpParams mDefaultHttpParams = new BasicHttpParams();
        //设置连接超时
        HttpConnectionParams.setConnectionTimeout(mDefaultHttpParams,1500);
        //设置请求超时
        HttpConnectionParams.setSoTimeout(mDefaultHttpParams,1500);
        //设置非延迟操作
        HttpConnectionParams.setTcpNoDelay(mDefaultHttpParams,true);
        //设置http协议版本
        HttpProtocolParams.setVersion(mDefaultHttpParams, HttpVersion.HTTP_1_1);
        //设置内容字符类型
        HttpProtocolParams.setContentCharset(mDefaultHttpParams, HTTP.UTF_8);
        //设置持续握手
        HttpProtocolParams.setUseExpectContinue(mDefaultHttpParams,true);
        //创建HttpClient
        HttpClient httpClient = new DefaultHttpClient(mDefaultHttpParams);
        return httpClient;
    }

    /**
     * 使用HttpGet类，实现网络请求
     * @param url
     */
    public void httpClientGet(String url){
        HttpGet mHttpGet = new HttpGet(url);
        mHttpGet.addHeader("Connection","Keep-Alive");
        HttpClient httpClient = createHttpClient();
        try {
            HttpResponse response = httpClient.execute(mHttpGet);
            int code = response.getStatusLine().getStatusCode();
            HttpEntity mHttpEntity = response.getEntity();
            if (null != mHttpEntity){
                InputStream inputStream = mHttpEntity.getContent();
                String resposeStr = convertStreamToString(inputStream);
                Log.e(TAG,"请求状态码："+code+"\n请求结果\n"+resposeStr);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     *
     * GET方法url拼接而成，但是url在HTTP1.1之后才没有限制长度，之前的都限制在2048字符之内，所以一般都用POST代替GET方法
     *
     */


    /**
     * 使用HttpPost类，实现网络请求
     * @param url
     */
    public void httpClientPost(String url){
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Connection","Keep-Alive");
        HttpClient httpClient = createHttpClient();
        List<NameValuePair> postParams = new ArrayList<>();
        //要传递的参数
        postParams.add(new BasicNameValuePair("username","admin"));
        postParams.add(new BasicNameValuePair("password","123456"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParams));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int code = httpResponse.getStatusLine().getStatusCode();
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null){
                InputStream inputStream = entity.getContent();
                String responeStr = convertStreamToString(inputStream);
                Log.e(TAG,"请求状态码："+code+"\n请求结果\n"+responeStr);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
