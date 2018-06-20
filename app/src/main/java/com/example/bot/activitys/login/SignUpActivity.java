package com.example.bot.activitys.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.bot.R;
import com.example.bot.activitys.MainActivity;
import com.example.bot.global.Const;
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


public class SignUpActivity extends Activity implements OnClickListener{
    private MyEditText et_account, et_verificode, et_pwd, et_nickname;
    private String account = null;
    private String verificode = null;
    private String nickname = null;
    private String password = null;
    private String smsid = null;

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
            PreferencesUtils.putSharePre(SignUpActivity.this, Const.LOGIN_PHONE, account);
            PreferencesUtils.putSharePre(SignUpActivity.this, Const.LOGIN_PWD, password);
            PreferencesUtils.putSharePre(SignUpActivity.this, Const.UUID, uuid);
            PreferencesUtils.putSharePre(SignUpActivity.this, Const.LOGIN_USERNAME, login_username);
            //跳转至首页
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            LoginActivity.loginActivity.finish();
            finish();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled();
            tintManager.setStatusBarTintResource(R.color.common_title_bg);//通知栏所需颜色
        }
        fh = new FinalHttp();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initTitleBar();
        initView();
    }

    private void initTitleBar() {
        TextView tv_back = findViewById(R.id.tv_left);
        tv_back.setVisibility(View.VISIBLE);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("创建账号");

        tv_back.setOnClickListener(this);
    }

    private void initView() {
        Button btn_signup = findViewById(R.id.btn_create_account);
        TextView send = findViewById(R.id.send_smss);
        et_account = findViewById(R.id.et_accounts);//账号
        et_verificode = findViewById(R.id.et_verificodes);//验证码
        et_pwd = findViewById(R.id.et_pwds);//密码
        et_nickname = findViewById(R.id.et_nicknames);//昵称

        btn_signup.setOnClickListener(this);
        send.setOnClickListener(this); // 发送验证码

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.tv_right:
                break;
            case R.id.send_smss:
                Send_Smss();
                break;
            case R.id.btn_create_account:
                enterRegister();
                break;
            default:
                break;
        }
    }

    private void enterRegister() {
        account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            DialogUtil.showErrorMsg(SignUpActivity.this, "请输入手机号码");
            return;
        }
        if (!RegexUtil.checkMobile(account)) {
            DialogUtil.showErrorMsg(SignUpActivity.this, "请输入正确的手机号码");
            return;
        }
        if(TextUtils.isEmpty(smsid)){
            DialogUtil.showErrorMsg(SignUpActivity.this, "未获取验证码");
            return;
        }
        verificode = et_verificode.getText().toString().trim();
        if (TextUtils.isEmpty(verificode) || verificode.length() != 6) {
            DialogUtil.showErrorMsg(SignUpActivity.this, "请输入6位随机验证码");
            return;
        }
        password = et_pwd.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            DialogUtil.showErrorMsg(SignUpActivity.this, "请输入密码");
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            DialogUtil.showErrorMsg(SignUpActivity.this, "请输入6-16位英文字母、数字");
            return;
        }
        nickname = et_nickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            DialogUtil.showErrorMsg(SignUpActivity.this, "请输入昵称");
            return;
        }

        loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle("正在登录...");
        loadingDialog.show();
        doPost();

    }

    private void Send_Smss() {
        account = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            DialogUtil.showErrorMsg(SignUpActivity.this, "请输入手机号码");
            return;
        }
        if (!RegexUtil.checkMobile(account)) {
            DialogUtil.showErrorMsg(SignUpActivity.this, "请输入正确的手机号码");
            return;
        }
        doSend_Smss();
    }

    private void doPost() {
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("telno",account);
        String md5_password = RegexUtil.encrypt(password);
        ajaxParams.put("password", md5_password);
        ajaxParams.put("username",nickname);
        ajaxParams.put("code",verificode);
        ajaxParams.put("smsid",smsid);
        //String s="{\"valid\":true,\"data\":{\"uid\":10002,\"dateCreated\":\"20180517\",\"username\":\"\",\"password\":\"1928301839039107\",\"telno\":\"13275631119\",\"sex\":\"男\",\"dateLastActived\":\"20180517\",\"ipCreated\":\"127.0.0.1\",\"ipLastActived\":\"127.0.0.1\",\"image\":null,\"count\":0,\"uuid\":\"3d8b6ed1c7854dbf8b3ebefd8c69baf5\"},\"error\":null}";
        fh.post(Const.SIGNUP_URL, ajaxParams, new AjaxCallBack<Object>() {
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
                        DialogUtil.showErrorMsg(SignUpActivity.this, jsonObject.getString("error"));
                    }
                } catch (JSONException e) {
                    DialogUtil.showErrorMsg(SignUpActivity.this, "网络连接失败");
                }
            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.showErrorMsg(SignUpActivity.this, "网络连接失败");
            }
        });
    }
    private void doSend_Smss(){
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("telno",account);
        fh.post(Const.GetCode_URL, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                LogUtil.e("response>>" + o);
                try {
                    jsonObject = new JSONObject((String) o);
                    if(jsonObject.getString("valid").equals("true")){
                        jsonObject = new JSONObject(jsonObject.getString("data"));
                        smsid = jsonObject.getString("smsid");
                        LogUtil.e(smsid);
                        DialogUtil.showErrorMsg(SignUpActivity.this, "验证码已发送");
                    }
                    else {
                        DialogUtil.showErrorMsg(SignUpActivity.this, jsonObject.getString("error"));
                    }
                } catch (JSONException e) {
                    DialogUtil.showErrorMsg(SignUpActivity.this, "网络连接失败");
                }
            }
            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                DialogUtil.showErrorMsg(SignUpActivity.this, "网络连接失败");
            }
        });
    }
}
