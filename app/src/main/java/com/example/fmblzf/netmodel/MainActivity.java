package com.example.fmblzf.netmodel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fmblzf.netmodel.origin.httpclient.HttpClientWraper;
import com.example.fmblzf.netmodel.origin.urlconnect.HttpUrlConnectionWraper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mHttpGetTextView;
    private TextView mHttpPostTextView;

    private TextView mHttpUrlGetTextView;
    private TextView mHttpUrlPostTextView;

    private HttpClientWraper mHttpClientWraper;

    private HttpUrlConnectionWraper mHttpUrlConnectionWraper;


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
        }
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
