package com.example.bot.activitys.base;

import android.app.Activity;
import android.app.Service;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import com.example.bot.R;
import com.example.bot.util.SystemBarTintManager;

import net.tsz.afinal.FinalBitmap;

import java.io.File;

public class AppBaseActivity extends Activity implements OnClickListener {

    protected TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled();
            tintManager.setStatusBarTintResource(R.color.common_title_bg);//通知栏所需颜色
        }
        super.onCreate(savedInstanceState);

        FinalBitmap finalImageLoader = FinalBitmap.create(this);
        finalImageLoader.configDiskCachePath(new File(Environment.getExternalStorageDirectory(), "bot/images/cache").getAbsolutePath());

    }

    /**
     * 隐藏软键盘
     * hideSoftInputView
     *
     * @param
     * @return void
     * @throws
     * @Title: hideSoftInputView
     */
    protected void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                assert manager != null;
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 弹出输入法窗口
     */
    protected void showSoftInputView(final EditText et) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                assert ((InputMethodManager) et.getContext().getSystemService(Service.INPUT_METHOD_SERVICE)) != null;
                ((InputMethodManager) et.getContext().getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    /**
     * 初始化titlebar，该方法只有在标题栏布局符合此规则时才能调用
     * @param left titlebar左按钮
     * @param title titlebar标题
     * @param onClickListener 左右按钮点击事件
     */
    protected void initTitleBar(String left, String title, OnClickListener onClickListener){
        TextView tv_left = findViewById(R.id.tv_left);
        tv_title= findViewById(R.id.tv_title);//标题
        TextView tv_right = findViewById(R.id.tv_right);

        if(!TextUtils.isEmpty(left)){
            tv_left.setText(left);
            tv_left.setVisibility(View.VISIBLE);
            tv_left.setOnClickListener(onClickListener);
        }

        if(!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }

        if(!TextUtils.isEmpty("")){
            tv_right.setText("");
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_in_left, R.anim.push_out_right);
    }

    /**
     * 如果子类支持点击左上角返回按钮返回，则在子类的onClick方法中需添加super.onClick(View view);
     */
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_left:
                finish();
                break;
        }
    }

}
