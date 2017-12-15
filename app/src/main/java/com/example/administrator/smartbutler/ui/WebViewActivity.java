package com.example.administrator.smartbutler.ui;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.administrator.smartbutler.R;

/**
 * 项目名：    SmartButler
 * 包名：      com.example.administrator.smartbutler.ui
 * 创建者：    Kamh
 * 创建时间：  2017/12/721:51
 * 描述：      新闻详情
 */
public class WebViewActivity extends BaseActivity {

    //进度
    private ProgressBar mProgressBar;
    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
    }

    //初始化VIew
    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        mWebView = (WebView) findViewById(R.id.mWebView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String url = intent.getStringExtra("url");

        //设置标题
        getSupportActionBar().setTitle(title);

        //支持Js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //接口回调
        mWebView.setWebChromeClient(new WebViewClient());
        //加载网页
        mWebView.loadUrl(url);
        //本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    mWebView.loadUrl(request.getUrl().toString());
                }else{
                    mWebView.loadUrl(request.toString());
                }
                return true;
            }
        });

    }

    public class WebViewClient extends WebChromeClient {
        //进度变化监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
