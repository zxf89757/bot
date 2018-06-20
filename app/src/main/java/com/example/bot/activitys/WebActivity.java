package com.example.bot.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.bot.R;
import com.example.bot.activitys.base.AppBaseActivity;
import com.example.bot.util.LogUtil;
import com.example.bot.view.ProgressWebView;

public class WebActivity extends AppBaseActivity implements OnClickListener, ProgressWebView.OnWebTitleChangedListener {
    private ProgressWebView webview;
    private String url = "";
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initTitleBar("返回", "", this);
        Bundle b = getIntent().getBundleExtra("b");
        url = b.getString("url");
        LogUtil.e(url);
        //初始化控件
        initView();
        initData();
    }

    private void initView() {
        pb = findViewById(R.id.progressBar);
        //实例化WebView对象
        webview = findViewById(R.id.webview);
        webview.setOnWebTitleChangedListener(this);
        webview.setOnLoadingListener(new ProgressWebView.OnLoadingListener() {
            @Override
            public void onStart() {
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                pb.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initData() {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);   //在当前的webview中跳转到新的url
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {  //网页加载开始
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {//网页加载完成
                super.onPageFinished(view, url);
            }

        });

        /*
          设置WebView属性，能够执行Javascript脚本
         */
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUseWideViewPort(true);//设置是当前html界面自适应屏幕
        webview.getSettings().setSupportZoom(true); //设置支持缩放
        webview.getSettings().setBuiltInZoomControls(false);//显示缩放控件
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式

        //加载需要显示的网页
        webview.requestFocus();

        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        webview.loadUrl(url);

    }

    @Override
    public void OnWebTitleChangedFinish(String title) {
        tv_title.setText(title);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }
}