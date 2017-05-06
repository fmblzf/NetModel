package com.example.fmblzf.netmodel.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Fmblzf on 2017/5/5.
 */
public class MemoryCache {


    /**
     *
     *
     *  内存缓存管理类
     *  实现根据设备以及应用的自动分配的内存大小来设置当前图片的内存缓存空间大小
     *  根据系统提供的Lru算法实现内存的自动化管理自动化触发GC机制；
     *
     *  之前的内存管理使用的强应用HashMap<String,Bitmap>不能很好的被系统销毁回收，很容易造成OOM;
     *  Android2.3之前的解决方式是：设置弱应用HashMap<String,SoftReference<Bitmap>>来解决当前内存不够时，GC回优先销毁收回弱应用的对象从而释放内存；
     *  但是Android2.3之后，系统会优先考虑会回收弱应用，所以使用系统提供的LruCache<> 该缓存管理遵循最近少使用的算法实现内存释放
     *
     *
     *
     */


    /**
     * 当前标记
     */
    private static final String TAG = "MemoryCache";

    //静态的实体对象
    private static MemoryCache instance;

    /**
     * 内存缓存块，具有最近最少使用的算法内存管理工具
     */
    private LruCache<String,Bitmap> lruCache;

    /**
     * 私有化构造器，实现单例模式
     */
    private MemoryCache(){
        long maxMemory = Runtime.getRuntime().maxMemory()/8;
        lruCache = new LruCache<String,Bitmap>((int)maxMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 获取当前对象的单利实例
     * @return
     */
    public static MemoryCache getInstance(){
        if (instance == null){
            synchronized (MemoryCache.class){
                if (instance == null){
                    instance = new MemoryCache();
                }
                return instance;
            }
        }
        return instance;
    }

    /**
     * 根据key获取当前的Bitmap
     * @param url 图片的url远程路径
     * @return
     */
    public Bitmap getBitmap(String url){
        return lruCache.get(url);
    }

    /**
     * 设置当前的key-value
     * @param url 图片的url远程路径
     * @param bitmap
     */
    public void setBitmap(String url,Bitmap bitmap){
        lruCache.put(url,bitmap);
    }

}
