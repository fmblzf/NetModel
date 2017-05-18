package com.example.fmblzf.netmodel.utils;

import android.os.Environment;

import com.example.fmblzf.netmodel.okhttp.OkHttpWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fmblzf on 2017/5/18.
 * 日志管理功能
 *
 */
public class LoggerUtils {

    private static final String TAG = "LoggerUtils";

    //日志所在文件夹
    private static final String path = Environment.getExternalStorageDirectory()+"/log";


    /**
     * 添加辅助信息
     * @param info
     * @return
     */
    private static String fixLogInfo(String info){
        StringBuilder sb = new StringBuilder();
        sb.append(getCurTime()+"\r\n");
        sb.append("-------------------------------start----------------------------------"+"\r\n");
        sb.append(info+"\r\n");
        sb.append("-------------------------------end----------------------------------"+"\r\n");
        sb.append("\r\n");
        sb.append("\r\n");
        return sb.toString();
    }

    /**
     * 写入日志
     * @param info
     */
    public static void log(String info){
        if(info == null || info.length() == 0){
            return;
        }
        info = fixLogInfo(info);
        File file = new File(path+"/"+getFileName()+".txt");
        File parentParent = file.getParentFile();
        if (!parentParent.exists()){
            parentParent.mkdirs();
        }
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] bytes = info.getBytes();
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file,true);
            outputStream.write(bytes);
            outputStream.flush();
            uploadLog();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 上传日志文件到服务器
     */
    private static void uploadLog(){
        OkHttpWrapper okHttpWrapper = OkHttpWrapper.getInstance(null);
        okHttpWrapper.uploadFiles(new String[]{path+"/"+getFileName()+".txt"},new String[]{getFileName()+".txt"});
    }

    /**
     * 获取当前文件名称，以当前的日期为名称
     * @return
     */
    private static String getFileName(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        return "log_"+df.format(new Date());
    }

    /**
     * 获取当前系统的时间
     * @return
     */
    private static String getCurTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

}
