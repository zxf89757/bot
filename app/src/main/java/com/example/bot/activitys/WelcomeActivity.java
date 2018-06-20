package com.example.bot.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.example.bot.R;
import com.example.bot.global.Const;
import com.example.bot.activitys.login.LoginActivity;
import com.example.bot.util.PreferencesUtils;
import com.example.bot.util.SysUtils;
import com.example.bot.util.SystemBarTintManager;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled();
            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //防止点击home键，再点击APP图标时应用重新启动
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        SysUtils.initFiles();
        initData();
    }

    private void initData() {
        String versionCode = PreferencesUtils.getSharePreStr(WelcomeActivity.this, Const.VERSION_CODE);
        //第一次启动保存加载引导图；
        if (TextUtils.isEmpty(versionCode)) {
            Intent intent = new Intent(WelcomeActivity.this, GuidActivity.class);
            startActivity(intent);
            finish();
        } else {
//            //显示欢迎页，3秒后进入主页
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (SysUtils.isLogin(WelcomeActivity.this)) {
                    SysUtils.startActivity(WelcomeActivity.this, MainActivity.class);//已登录进入首页
                    } else {
                        SysUtils.startActivity(WelcomeActivity.this, LoginActivity.class);//未登录则进入登录
                    }
                    finish();
                }
            }, 3 * 1000);
        }

    }

    /**
     * 返回键监听,禁止返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

}