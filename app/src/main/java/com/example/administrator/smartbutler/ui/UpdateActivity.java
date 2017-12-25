package com.example.administrator.smartbutler.ui;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * 下载
 */

import com.example.administrator.smartbutler.R;
import com.example.administrator.smartbutler.utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;
import android.os.Handler;

public class UpdateActivity extends BaseActivity {

    //正在下载
    public static final int HEADLER_LOADING = 10001;
    //下载完成
    public static final int HEADLER_OK = 10002;
    //下载失败
    public static final int HEADLER_ON = 10003;

    private TextView tv_size;
    private String url;
    private String path;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HEADLER_LOADING:
                    Bundle bundle = msg.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    //实时跟新进度
                    tv_size.setText(transferredBytes + "/" + totalSize);
                    break;
                case HEADLER_OK:
                    tv_size.setText("下载成功");
                    break;
                case HEADLER_ON:
                    tv_size.setText("下载失败");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView();
    }

    private void initView() {
        tv_size = (TextView) findViewById(R.id.tv_size);

        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";
        //下载
        url = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(url)){
            //下载
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    L.i("transferredBytes:" + transferredBytes + "totalSize:" + totalSize);
                    Message msg = new Message();
                    msg.what = HEADLER_LOADING;
                    Bundle bundle = new Bundle();
                    bundle.putLong("transferredBytes", transferredBytes);
                    bundle.putLong("totalSize", totalSize);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    L.e("成功");
                    mHandler.sendEmptyMessage(HEADLER_OK);
                }

                @Override
                public void onFailure(VolleyError error) {
                    L.e("失败");
                    mHandler.sendEmptyMessage(HEADLER_ON);
                }
            });
        }
    }
}
