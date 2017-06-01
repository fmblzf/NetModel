package com.example.fmblzf.netmodel.mina;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
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

    public enum ConnectType{
        TCP(0),UDP(1);

        private int value;

        private ConnectType(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }

    }

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
     * 开始连接
     * @param type
     */
    public void start(final ConnectType type){
        new Thread(){
            @Override
            public void run() {
                if (type == ConnectType.TCP){
                    try {
                        createTcpConnect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        createUdpConnect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private IoSession ioSession = null;
    private IoConnector tcpIoConnector = null;

    /**
     * 创建tcp服务端的连接
     */
    public void createTcpConnect() throws InterruptedException {
        if (ioSession == null) {
            //首先创建对应的tcp连接
            tcpIoConnector = new NioSocketConnector();
            //设置超时时间
            tcpIoConnector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
            //添加过滤器
            tcpIoConnector.getFilterChain().addLast("logger", new LoggingFilter());//添加日志过滤器
            tcpIoConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));//设置字节处理过滤器
            //添加IoHandler
            tcpIoConnector.setHandler(new ClientSessionHandler());

//            IoSession ioSession;

            //通过ConnectFuture来连接服务器
            for (; ; ) {
                try {
                    ConnectFuture future = tcpIoConnector.connect(new InetSocketAddress(HOSTNAME, PORT));
                    future.awaitUninterruptibly();
                    ioSession = future.getSession();
                    break;//连接成功跳出死循环
                } catch (Exception e) {
                    System.err.println("Failed to connect");
                    e.printStackTrace();
                    Thread.sleep(5000);//如果连接失败，5秒后继续连接直到连接成功
                }
            }
        }
        ioSession.write("tcp-ceshi");
        //再次等候直到操作结束
//        ioSession.getCloseFuture();
//        ioConnector.dispose();//然后关闭连接
    }

    private IoSession udpUoSession = null;
    private IoConnector udpIoConnector = null;
    /**
     * 创建udp服务端的连接
     */
    public void createUdpConnect() throws InterruptedException {
        if (udpUoSession == null) {
            //首先创建对应的tcp连接
            udpIoConnector = new NioDatagramConnector();
            //设置超时时间
            udpIoConnector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
            //添加过滤器
            udpIoConnector.getFilterChain().addLast("logger", new LoggingFilter());//添加日志过滤器
            udpIoConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));//设置字节处理过滤器
            //添加IoHandler
            udpIoConnector.setHandler(new ClientSessionHandler());

            //通过ConnectFuture来连接服务器
            for (; ; ) {
                try {
                    ConnectFuture future = udpIoConnector.connect(new InetSocketAddress(HOSTNAME, PORT + 1));
                    future.awaitUninterruptibly();
                    udpUoSession = future.getSession();
                    break;//连接成功跳出死循环
                } catch (Exception e) {
                    System.err.println("Failed to connect");
                    e.printStackTrace();
                    Thread.sleep(5000);//如果连接失败，5秒后继续连接直到连接成功
                }
            }
        }
        udpUoSession.write("udp-ceshi");
        //再次等候直到操作结束
//        ioSession.getCloseFuture();
//        ioConnector.dispose();//然后关闭连接
    }

    public void quit(){
        if (ioSession != null && ioSession.isConnected()){
            ioSession.getCloseFuture();
            ioSession.closeNow();
            tcpIoConnector.dispose();
            ioSession = null;
            tcpIoConnector = null;
        }
        if (udpUoSession != null && udpUoSession.isConnected()){
            udpUoSession.getCloseFuture();
            udpUoSession.closeNow();
            udpIoConnector.dispose();
            udpUoSession = null;
            udpIoConnector = null;
        }
    }

}
