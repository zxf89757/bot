package com.example.bot.activitys;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.bot.R;
import com.example.bot.activitys.tab.tab2.Tab2Activity;
import com.example.bot.activitys.tab.tab3.Tab3Activity;
import com.example.bot.util.SystemBarTintManager;

/**
 * 主程序
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements CompoundButton.OnCheckedChangeListener {

    private TabHost mTabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled();
            tintManager.setStatusBarTintResource(R.color.common_title_bg);//通知栏所需颜色
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setupIntent();
    }

    //初始化控件
    private void initView() {
        RadioButton rb_tab2 = this.findViewById(R.id.rb_tab2);
        RadioButton rb_tab3 = this.findViewById(R.id.rb_tab3);

        rb_tab2.setOnCheckedChangeListener(this);
        rb_tab3.setOnCheckedChangeListener(this);

    }

    //初始化选项卡
    private void setupIntent() {
        mTabHost = getTabHost();
        TabHost mainTabHost = this.mTabHost;
        Intent intent = new Intent().setClass(this, Tab2Activity.class);
        mainTabHost.addTab(buildTabSpec("tab2", intent));
        intent = new Intent().setClass(this, Tab3Activity.class);
        mainTabHost.addTab(buildTabSpec("tab3", intent));
    }

    private TabHost.TabSpec buildTabSpec(String tag, final Intent content) {
        return this.mTabHost.newTabSpec(tag).setIndicator((String) null).setContent(content);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_tab2:
                    mTabHost.setCurrentTabByTag("tab2");
                    break;
                case R.id.rb_tab3:
                    mTabHost.setCurrentTabByTag("tab3");
                    break;
            }
        }
    }


    private int keyBackClickCount=0;
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN  ) {
            switch (keyBackClickCount++) {
                case 0:
                    Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 2000);
                    break;
                case 1:
                    moveTaskToBack(true);
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}
