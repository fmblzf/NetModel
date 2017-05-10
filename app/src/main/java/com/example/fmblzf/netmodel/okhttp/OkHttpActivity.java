package com.example.fmblzf.netmodel.okhttp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fmblzf.netmodel.R;

/**
 * Created by Administrator on 2017/5/6.
 */
public class OkHttpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OkHttpActivity";

    private TextView mAysnTextView;
    private TextView mSyncTextView;

    private TextView mPostTextView;

    private OkHttpWrapper mOkHttpWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okhttp_layout);
        //初始化标题栏
        initActionBar();
        //查找设置控件
        findView();
        //初始化对象
        initObject();
    }

    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("OkHttp3.X");
    }

    /**
     * 初始化对象
     */
    private void initObject() {
        mOkHttpWrapper = OkHttpWrapper.getInstance(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int what = msg.what;
                String body = (String) msg.obj;
                if (what == 1000){
                    Log.i(TAG+"-AsynHttp",body);
                    Toast.makeText(OkHttpActivity.this,"AsynHttp",Toast.LENGTH_SHORT).show();
                }else if (what == 1001){
                    Log.i(TAG+"-SyncHttp",body);
                    Toast.makeText(OkHttpActivity.this,"SyncHttp",Toast.LENGTH_SHORT).show();
                }else if(what == 1002){
                    Log.i(TAG+"-PostHttp",body);
                    Toast.makeText(OkHttpActivity.this,"PostHttp",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    /**
     * 查找设置控件
     */
    private void findView() {
        mAysnTextView = (TextView) this.findViewById(R.id.okhttp_aysn);
        mAysnTextView.setOnClickListener(this);
        mSyncTextView = (TextView) this.findViewById(R.id.okhttp_sync);
        mSyncTextView.setOnClickListener(this);
        mPostTextView = (TextView) this.findViewById(R.id.okhttp_post);
        mPostTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.okhttp_aysn:
                mOkHttpWrapper.getAsynHttp();
                break;
            case R.id.okhttp_sync:
                mOkHttpWrapper.getSyncHttp();
                break;
            case R.id.okhttp_post:
                mOkHttpWrapper.postHttp();
                break;
        }
    }
}
