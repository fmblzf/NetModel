package com.example.fmblzf.netmodel.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Fmblzf on 2017/5/5.
 */
public class NetworkCache {

    /**
     *
     * 通过AsyncTask来实现网络图片加载；
     * AsyncTask通过封装Hanlder和ThreadPoolExecutor实现网络请求图片回调到主线程
     *
     *
     *
     */

    /**
     * 当前标记
     */
    private static final String TAG = "NetworkCache";

    //当前对象的实例
    private static NetworkCache instance;

    //本地缓存对象
    private LocalCache mLocalCache;
    //内存缓存对象
    private MemoryCache mMemoryCache;

    /**
     * 私有化构造器，实现单例模式
     */
    private NetworkCache(){
        mLocalCache = LocalCache.getInstance();
        mMemoryCache = MemoryCache.getInstance();
    }

    /**
     * 获取当前的对象的实例
     * @return
     */
    public static NetworkCache getInstance(){
        if (instance == null){
            synchronized (NetworkCache.class){
                if (instance == null){
                    instance = new NetworkCache();
                }
                return instance;
            }
        }
        return instance;
    }

    /**
     * 根据远程路径获取对应的图片
     * @param imageView 需要设置远程图片的控件
     * @param url 图片的远程路径
     */
    public void setImageViewPic(ImageView imageView, String url){
        //先从内存中获取对应的Bitmap
        Bitmap memoryBitmap = mMemoryCache.getBitmap(url);
        if (null != memoryBitmap){
            imageView.setImageBitmap(memoryBitmap);
            Log.i(TAG,"从内存中已经获取到了对应的图片.....");
            return ;
        }
        //再从本地缓存中获取
        Bitmap localBitmap = mLocalCache.getBitmap(url);
        if (null != localBitmap){
            imageView.setImageBitmap(localBitmap);
            Log.i(TAG,"从本地缓存中已经获取到了对应的图片.....");
            return ;
        }
        //直接从网络下载
        new BitMapWorkAsyncTask().execute(imageView,url);
    }

    /**
     *
     * AsyncTask实现类-异步任务
     *
     */
    public class BitMapWorkAsyncTask extends AsyncTask<Object,Void,Bitmap>{

        //当前需要设置图片的控件
        private ImageView mImageView;
        //当前远程图片的路径
        private String url;

        /**
         * 运行在子线程中
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 运行在子线程中
         * new BitMapWorkAsyncTask().execute(Object... params);
         * 运行在子线程中，可以执行耗时操作，比如网络请求操作
         * @param params 开始任务时，传递进来的参数在该方法的此处可以取到
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            this.mImageView = (ImageView) params[0];
            this.url = (String) params[1];
            Bitmap bitmap = downBitmap(url);
            return bitmap;
        }

        /**
         * 请求数据成功后的回调，运行在主线程中
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.i(TAG,"从网络下载图片成功.....");
            //在主线程中设置控件的图片显示
            this.mImageView.setImageBitmap(bitmap);
            //本地缓存
            mLocalCache.setBitmap(url,bitmap);
            //内存缓存
            mMemoryCache.setBitmap(url,bitmap);
        }

        /**
         * 进度更新操作，运行在主线程中
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 取消操作，运行在主线程中
         * @param bitmap
         */
        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
        }
    }

    /**
     * 下载远程图片,并保存到对应的bitmap对象中去
     * @param url
     * @return
     */
    private Bitmap downBitmap(String url) {
        HttpURLConnection httpURLConnection = null;
        Bitmap bitmap = null;
        try {
            URL mUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) mUrl.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setRequestMethod("GET");
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                BitmapFactory.Options options = new BitmapFactory.Options();
                //宽和高是原图的1/2
                options.inSampleSize = 2;
                //设置当前保存图片的一个像素占两个字节
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
        return bitmap;
    }

}
