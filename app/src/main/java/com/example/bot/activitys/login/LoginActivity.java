package com.example.bot.activitys.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.bot.R;
import com.example.bot.global.Const;
import com.example.bot.activitys.MainActivity;
import com.example.bot.util.DialogUtil;
import com.example.bot.util.LogUtil;
import com.example.bot.util.PreferencesUtils;
import com.example.bot.util.RegexUtil;
import com.example.bot.util.SystemBarTintManager;
import com.example.bot.view.LoadingDialog;
import com.example.bot.view.MyEditText;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录
 * LoginActivity.java
 */
public class LoginActivity extends Activity implements OnClickListener {
    public static LoginActivity loginActivity = null;
    private MyEditText et_account, et_pwd;
    private static final int REQUEST_CODE_TO_REGISTER = 0x001;
    private String account = null;
    private String password = null;

    private String uuid = null;
    private String login_username =null;

    private FinalHttp fh;
    private JSONObject jsonObject;
    private LoadingDialog loadingDialog;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            //保存用户名和密码
            PreferencesUtils.putSharePre(LoginActivity.this, Const.LOGIN_PHONE, account);
            PreferencesUtils.putSharePre(LoginActivity.this, Const.LOGIN_PWD, password);
            PreferencesUtils.putSharePre(LoginActivity.this, Const.UUID, uuid);
            PreferencesUtils.putSharePre(LoginActivity.this, Const.LOGIN_USERNAME, login_username);

            //跳转至首页
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled();
            tintManager.setStatusBarTintResource(R.color.common_title_bg);//通知栏所需颜色
        }
        super.onCreate(savedInstanceState);
        fh = new FinalHttp();
        setContentView(R.layout.activity_login);
        initTitleBar();
        initView();
        account = PreferencesUtils.getSharePreStr(LoginActivity.this, Const.LOGIN_PHONE);
        password = PreferencesUtils.getSharePreStr(LoginActivity.this, Const.LOGIN_PWD);
        if (!TextUtils.isEmpty(account)&&!TextUtils.isEmpty(password)) {//保存在本地的account
            et_account.setText(account);
            et_account.setSelection(account.length());
            et_pwd.setText(password);
            et_pwd.setSelection(password.length());
        }
        loginActivity = this;
    }

    private void initTitleBar() {
        TextView tv_back = findViewById(R.id.tv_left);
        tv_back.setVisibility(View.VISIBLE);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("登录");

        tv_back.setOnClickListener(this);
    }

    private void initView() {
        Button btn_login = findViewById(R.id.btn_login);
        et_account = findViewById(R.id.et_account);//账号
        et_pwd = findViewById(R.id.et_pwd);//密码

        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:
                break;
            case R.id.btn_login:
                doLogin();
                break;
            case R.id.tv_create_account:
                enterRegister();
                break;
            case R.id.tv_forget_password:
                enterForgetPwd();
                break;
            default:
                break;
        }
    }

    private void doLogin() {
        account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            DialogUtil.showErrorMsg(LoginActivity.this, "请输入手机号码");
            return;
        }
        if (!RegexUtil.checkMobile(account)) {
            DialogUtil.showErrorMsg(LoginActivity.this, "请输入正确的手机号码");
            return;
        }
        password = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            DialogUtil.showErrorMsg(LoginActivity.this, "请输入密码");
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            DialogUtil.showErrorMsg(LoginActivity.this, "请输入6-16位英文字母、数字");
            return;
        }
        //执行登录

        loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle("正在登录...");
        loadingDialog.show();
        doPost();
    }

    /**
     * 跳转到注册页面
     */
    private void enterRegister() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TO_REGISTER);
    }

    /**
     * 跳转到忘记密码
     */
    private void enterForgetPwd() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    private void doPost() {
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("telno",account);
        String md5_password = RegexUtil.encrypt(password);
        ajaxParams.put("password", md5_password);
        fh.post(Const.LOGIN_URL, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                LogUtil.e("response>>" + o);
                try {
                    jsonObject = new JSONObject((String) o);
                    if(jsonObject.getString("valid").equals("true")){
                        jsonObject = new JSONObject(jsonObject.getString("data"));
                        uuid = jsonObject.getString("uuid");
                        login_username = jsonObject.getString("username");

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(0);
                            }
                        }, 2000);
                    }
                    else{
                        DialogUtil.showErrorMsg(LoginActivity.this, jsonObject.getString("error"));
                    }
                } catch (JSONException e) {
                    DialogUtil.showErrorMsg(LoginActivity.this, "网络连接失败");
                }
            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.showErrorMsg(LoginActivity.this, "网络连接失败");
            }
        });
    }
}
