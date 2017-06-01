package com.example.fmblzf.netmodel.mina;

import android.os.Looper;
import android.os.Process;
import android.util.Log;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * Created by Administrator on 2017/5/27.
 */

class ClientSessionHandler extends IoHandlerAdapter {
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        session.write("测试");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String result = message.toString();
        Log.i("ClientSessionHandler",result);
        if (Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId()){
            //
        }
        Log.i("ClientSessionHandler", "myPid = "+Process.myPid()+";myTid = "+Process.myTid()+";myUid = "+Process.myUid());
        //session.closeNow();
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
    }
}
