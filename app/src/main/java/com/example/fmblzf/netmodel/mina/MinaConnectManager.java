package com.example.fmblzf.netmodel.mina;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

/**
 * Created by fmblzf on 2017/5/27.
 * 实现Mina的TCP/UDP请求连接
 *
 */

public class MinaConnectManager {

    /**
     *  超时时间
     */
    private static final int CONNECT_TIMEOUT = 10000;
    /**
     * 服务器ip
     */
    private static final String HOSTNAME = "10.0.6.82";
    /**
     * 服务器端口
     */
    private static final int PORT = 9321;

    private MinaConnectManager(){}

    public static MinaConnectManager getInstance(){
        return ManagerInstanceHolder.mInstance;
    }

    /**
     * 静态内部类实现单利模式，最推荐的方式
     */
    private static class ManagerInstanceHolder{
        private static MinaConnectManager mInstance = new MinaConnectManager();
    }

    /**
     * 创建tcp服务端的连接
     */
    public void createTcpConnect() throws InterruptedException {
        //首先创建对应的tcp连接
        IoConnector ioConnector = new NioSocketConnector();
        //设置超时时间
        ioConnector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        //添加过滤器
        ioConnector.getFilterChain().addLast("logger",new LoggingFilter());//添加日志过滤器
        ioConnector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));//设置字节处理过滤器
        //添加IoHandler
        ioConnector.setHandler(new ClientSessionHandler());

        IoSession ioSession;

        //通过ConnectFuture来连接服务器
        for (;;){
            try {
                ConnectFuture future = ioConnector.connect(new InetSocketAddress(HOSTNAME, PORT));
                future.awaitUninterruptibly();
                ioSession = future.getSession();
                break;//连接成功跳出死循环
            }catch (Exception e){
                System.err.println("Failed to connect");
                e.printStackTrace();
                Thread.sleep(5000);//如果连接失败，5秒后继续连接直到连接成功
            }
        }
        //再次等候直到操作结束
        ioSession.getCloseFuture();
        ioConnector.dispose();//然后关闭连接
    }

    /**
     * 创建udp服务端的连接
     */
    public void createUdpConnect() throws InterruptedException {
        //首先创建对应的tcp连接
        IoConnector ioConnector = new NioDatagramConnector();
        //设置超时时间
        ioConnector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        //添加过滤器
        ioConnector.getFilterChain().addLast("logger",new LoggingFilter());//添加日志过滤器
        ioConnector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));//设置字节处理过滤器
        //添加IoHandler
        ioConnector.setHandler(new ClientSessionHandler());

        IoSession ioSession;

        //通过ConnectFuture来连接服务器
        for (;;){
            try {
                ConnectFuture future = ioConnector.connect(new InetSocketAddress(HOSTNAME, PORT+1));
                future.awaitUninterruptibly();
                ioSession = future.getSession();
                break;//连接成功跳出死循环
            }catch (Exception e){
                System.err.println("Failed to connect");
                e.printStackTrace();
                Thread.sleep(5000);//如果连接失败，5秒后继续连接直到连接成功
            }
        }
        //再次等候直到操作结束
        ioSession.getCloseFuture();
        ioConnector.dispose();//然后关闭连接
    }


}
