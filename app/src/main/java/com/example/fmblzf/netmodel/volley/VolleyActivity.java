package com.example.fmblzf.netmodel.volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.fmblzf.netmodel.BaseApplication;
import com.example.fmblzf.netmodel.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fmblzf on 2017/5/3.
 */
public class VolleyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mVolleyStringTextView;
    private TextView mVolleyJsonTextView;

    private VolleyWrapper mVolleyWrapper;

    private ImageView mImageView;
    private ImageView mLoaderImageView;
    private NetworkImageView mNetworkImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley_layout);
        //获取当前的控件
        findView();
        //初始化当前界面的实体对象
        initObject();
        //加载图片
        loadBitmapVolley();
    }

    /**
     * 加载远程图片
     */
    private void loadBitmapVolley() {
        ImageRequest im = mVolleyWrapper.createImageRequest("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1493811434862&di=1d80392610b878154529557d9cefd502&imgtype=0&src=http%3A%2F%2Fcimg.waxinxian.com%2Fupload%2F201509%2F19%2F140334oRZZU.jpg",mImageView);
        BaseApplication.getQueue().add(im);

        mVolleyWrapper.createImageLoader(BaseApplication.getQueue(),mLoaderImageView,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494054437873&di=44aedb6ac43c743fe49d4d135a80237e&imgtype=0&src=http%3A%2F%2Fimage.lxway.com%2Fupload%2Fa%2F01%2Fa01ebeb65af633b59b48789c2525d318_thumb.png");

        //NetworkImageView的使用
        ImageLoader imageLoader = new ImageLoader(BaseApplication.getQueue(),new VolleyWrapper.NativeImageCache());
        mNetworkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        mNetworkImageView.setImageUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494060574315&di=132e087e97c211a8057e3d18c92b5711&imgtype=0&src=http%3A%2F%2Fstatic.open-open.com%2Flib%2FuploadImg%2F20160501%2F20160501145318_210.png",imageLoader);
    }

    /**
     *  初始化对象实体
     */
    private void initObject() {
        mVolleyWrapper = VolleyWrapper.getInstance();
    }

    /**
     * 查找对应的View 控件
     */
    private void findView() {
        mVolleyStringTextView = (TextView) this.findViewById(R.id.volley_string_click);
        mVolleyStringTextView.setOnClickListener(this);
        mVolleyJsonTextView = (TextView) this.findViewById(R.id.volley_json_click);
        mVolleyJsonTextView.setOnClickListener(this);
        mImageView = (ImageView) this.findViewById(R.id.volley_img);
        mLoaderImageView = (ImageView) this.findViewById(R.id.volley_loader_img);
        mNetworkImageView = (NetworkImageView) this.findViewById(R.id.volley_networkimageview);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.volley_string_click:
                clickVolleyString();
                break;
            case R.id.volley_json_click:
                clickVolleyJson();
                break;
        }
    }

    /**
     *  触发Volley JSON事件
     */
    private void clickVolleyJson() {
        JSONObject params = new JSONObject();
        try {
            params.put("type","top");
            params.put("key","dd7bba438f17877f6790d49087424418");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //
        JsonObjectRequest jsonObjectRequest = mVolleyWrapper.createJsonRequest(Request.Method.POST,"http://v.juhe.cn/toutiao/index?type=top&key=dd7bba438f17877f6790d49087424418",null);
        BaseApplication.getQueue().add(jsonObjectRequest);
    }

    /**
     * 触发Volley String事件
     */
    private void clickVolleyString() {
        StringRequest stringRequest = mVolleyWrapper.createStringRequest(Request.Method.POST,"http://www.baidu.com");
        BaseApplication.getQueue().add(stringRequest);
    }
}
