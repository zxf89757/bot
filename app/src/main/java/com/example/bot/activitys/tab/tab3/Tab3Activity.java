package com.example.bot.activitys.tab.tab3;


import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bot.R;
import com.example.bot.global.Const;
import com.example.bot.activitys.base.BaseActivity;
import com.example.bot.util.DialogUtil;
import com.example.bot.util.PreferencesUtils;
import com.example.bot.util.SysUtils;


/**
 * 主程序
 */
public class Tab3Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab3);
        setPadding(R.id.activity_tab3);
        initTitleBar("", "我的", null);
        initView();
    }

    private void initView() {
        TextView tv_username = findViewById(R.id.tv_username);
        tv_username.setText(PreferencesUtils.getSharePreStr(Tab3Activity.this,Const.LOGIN_USERNAME));
        RelativeLayout rl_setting = findViewById(R.id.rl_setting);
        RelativeLayout rl_about = findViewById(R.id.rl_about);
        RelativeLayout rl_loginout = findViewById(R.id.rl_loginout);

        rl_setting.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_loginout.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.rl_setting:
                SysUtils.startActivity(getParent(),MsgHistoryActivity.class);
                break;
            case R.id.rl_about:
                SysUtils.startActivity(getParent(),AboutActivity.class);
                break;
            case R.id.rl_loginout:
                DialogUtil.showChooseDialog(this, "", "您确定退出吗？", null, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferencesUtils.putSharePre(Tab3Activity.this,Const.UUID,"");
                        getParent().finish();
                    }
                });
                break;
        }
    }
}
