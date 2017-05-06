package com.example.fmblzf.netmodel.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.ndktools.javamd5.core.MD5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/5/5.
 */
public class LocalCache {


    /**
     *
     *
     * 本地图片缓存，以文件的形式存储在本地内存中，
     *
     * 还可以在本地缓存中添加文件的过期时间限制或者加上算法管理本地内存，防止图片量过大时或者占用过大本地内存时，可以主动去清理无用的文件
     * 可以参考LRU算法。后续添加
     *
     * 还得考虑本地缓存的图片的大小，添加限制，
     *
     *
     *
     *
     */


    /**
     * 本地缓存的路径
     */
    private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/local_cache";

    /**
     * 当前的标记
     */
    private static final String TAG = "LocalCache";

    //当前的对象
    private static LocalCache instance;

    /**
     * 私有化构造器，实现单例模式
     */
    private LocalCache(){}

    /**
     * 获取当前对象的单例模式对象
     * @return
     */
    public static LocalCache getInstance(){
        if (instance == null){
            synchronized (LocalCache.class){
                if (instance == null){
                    instance = new LocalCache();
                }
                return instance;
            }
        }
        return instance;
    }

    /**
     * 设置本地缓存图片
     * @param url 图片的url远程路径
     * @param bitmap
     */
    public void setBitmap(String url, Bitmap bitmap){
        MD5 md5 = new MD5();
        String fileName =md5.getMD5ofStr(url);
        File file = new File(CACHE_PATH,fileName);
        File parentFile = file.getParentFile();
        //判断父文件（文件夹）是否存在，如果不存在就创建对应的文件夹
        if (!parentFile.exists()){
            parentFile.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //将bitmap保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地缓存的图片
     * @param url 图片的url远程路径
     * @return
     */
    public Bitmap getBitmap(String url){
        MD5 md5 = new MD5();
        String fileName =md5.getMD5ofStr(url);
        File file = new File(CACHE_PATH,fileName);
        if (!file.exists()){
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
