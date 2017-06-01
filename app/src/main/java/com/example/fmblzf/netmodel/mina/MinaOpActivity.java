package com.example.fmblzf.netmodel.mina;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.fmblzf.netmodel.R;

/**
 * Created by Administrator on 2017/5/31.
 */

public class MinaOpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mMinaTcpTextView;

    private TextView mMinaUdpTextView;

    private MinaConnectManager mMinaConnectManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mina_layout);
        //初始化控件
        initCompanet();
        //初始化对象
        initObject();
    }

    /**
     *  初始化对象
     */
    private void initObject() {
        mMinaConnectManager = MinaConnectManager.getInstance();
    }

    /**
     *  初始化控件
     */
    private void initCompanet() {
        mMinaTcpTextView = (TextView) this.findViewById(R.id.mina_tcp);
        mMinaTcpTextView.setOnClickListener(this);
        mMinaUdpTextView = (TextView) this.findViewById(R.id.mina_udp);
        mMinaUdpTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.mina_tcp:
                mMinaConnectManager.start(MinaConnectManager.ConnectType.TCP);
                break;
            case R.id.mina_udp:
                mMinaConnectManager.start(MinaConnectManager.ConnectType.UDP);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        mMinaConnectManager.quit();
        super.onDestroy();
    }
}
