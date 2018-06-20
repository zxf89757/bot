package com.example.bot.activitys.tab.tab3;


import android.os.Bundle;

import com.example.bot.R;
import com.example.bot.activitys.base.SlideBackActivity;

/**
 * 关于
 */
public class AboutActivity extends SlideBackActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initTitleBar("我的","关于", this);
    }
}
