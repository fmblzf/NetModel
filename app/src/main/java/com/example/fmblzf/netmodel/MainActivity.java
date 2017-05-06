package com.example.fmblzf.netmodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.fmblzf.netmodel.okhttp.OkHttpActivity;
import com.example.fmblzf.netmodel.origin.httpclient.HttpClientWraper;
import com.example.fmblzf.netmodel.origin.urlconnect.HttpUrlConnectionWraper;
import com.example.fmblzf.netmodel.volley.VolleyActivity;
import com.example.fmblzf.netmodel.volley.VolleyWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mHttpGetTextView;
    private TextView mHttpPostTextView;

    private TextView mHttpUrlGetTextView;
    private TextView mHttpUrlPostTextView;

    private TextView mVolleyTextView;

    private TextView mOkHttpTextView;

    private HttpClientWraper mHttpClientWraper;

    private HttpUrlConnectionWraper mHttpUrlConnectionWraper;

    private VolleyWrapper mVolleyWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取当前的控件
        findView();
        //初始化当前界面的实体对象
        initObject();
    }

    /**
     *  初始化对象实体
     */
    private void initObject() {
        mHttpClientWraper = HttpClientWraper.getInstance();
        mHttpUrlConnectionWraper = HttpUrlConnectionWraper.getInstance();
        mVolleyWrapper = VolleyWrapper.getInstance();
    }

    /**
     * 查找对应的View 控件
     */
    private void findView() {
        mHttpGetTextView = (TextView) this.findViewById(R.id.http_get_click);
        mHttpGetTextView.setOnClickListener(this);
        mHttpPostTextView = (TextView) this.findViewById(R.id.http_post_click);
        mHttpPostTextView.setOnClickListener(this);
        mHttpUrlGetTextView = (TextView) this.findViewById(R.id.http_url_get_click);
        mHttpUrlGetTextView.setOnClickListener(this);
        mHttpUrlPostTextView = (TextView) this.findViewById(R.id.http_url_post_click);
        mHttpUrlPostTextView.setOnClickListener(this);
        mVolleyTextView = (TextView) this.findViewById(R.id.volley_click);
        mVolleyTextView.setOnClickListener(this);
        mOkHttpTextView = (TextView) this.findViewById(R.id.okhttp_click);
        mOkHttpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.http_get_click:
                //触发HttpClient Get事件
                clickHttpGet();
                break;
            case R.id.http_post_click:
                //触发HttpClient POST事件
                clickHttpPost();
                break;
            case R.id.http_url_get_click:
                //触发HttpURLConnection GET事件
                clickHttpUrlGet();
                break;
            case R.id.http_url_post_click:
                //触发HttpURLConnection POST事件
                clickHttpUrlPost();
                break;
            case R.id.volley_click:
                //触发Volley事件
                clickVolley();
                break;
            case R.id.okhttp_click:
                clickOkHttp();
                break;
        }
    }

    private void clickOkHttp() {
        Intent intent = new Intent(this, OkHttpActivity.class);
        startActivity(intent);
    }

    /**
     *  触发Volley事件
     */
    private void clickVolley() {
        Intent intent = new Intent(this, VolleyActivity.class);
        startActivity(intent);
    }

    /**
     * 触发HttpURLConnection POST事件
     */
    private void clickHttpUrlPost() {
        new Thread(){
            @Override
            public void run() {
                mHttpUrlConnectionWraper.httpUrlConnectionPost("http://www.baidu.com");
            }
        }.start();
    }

    /**
     * 触发HttpURLConnection GET事件
     */
    private void clickHttpUrlGet() {
        new Thread(){
            @Override
            public void run() {
                mHttpUrlConnectionWraper.httpUrlConnectionGet("http://www.baidu.com");
            }
        }.start();
    }

    /**
     * 点击触发HttpClient POST 方法
     */
    private void clickHttpPost() {
        new Thread(){
            @Override
            public void run() {
                mHttpClientWraper.httpClientPost("http://www.baidu.com");
            }
        }.start();
    }

    /**
     * 点击触发HttpClient Get 方法
     */
    private void clickHttpGet() {
        new Thread(){
            @Override
            public void run() {
                mHttpClientWraper.httpClientGet("http://www.baidu.com");
            }
        }.start();
    }
}
