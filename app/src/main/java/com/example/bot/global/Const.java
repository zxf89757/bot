package com.example.bot.global;

import android.os.Environment;

public class Const {

    public static final String FILE_VOICE_CACHE = Environment.getExternalStorageDirectory() + "/bot/voice/";
    public final static String VERSION_CODE = "VERSION_CODE";//版本号

    public static final String XF_VOICE_APPID="5ae92fb8";//讯飞语音appid
    public final static String XF_SET_VOICE_RECORD="VOICE_RECORD";//录音语言
    public final static String XF_SET_VOICE_READ="XF_SET_VOICE_READ";//朗读语言
    public final static String IM_SPEECH_TPPE="IM_SPEECH_TPPE";//聊天回复是否直接朗读

    public final static String LOGIN_PHONE = "LOGIN_PHONE";//登录手机号
    public final static String LOGIN_PWD = "LOGIN_PWD";    //登录密码
    public final static String UUID = "UUID";//用户唯一标示
    public final static String LOGIN_USERNAME = "LOGIN_USERNAME";//用户名

    public static final String MSG_TYPE_TEXT="msg_type_text";//文本消息
    public static final String MSG_TYPE_LOCATION="msg_type_location";//位置
    public static final String MSG_TYPE_LIST="msg_type_list";//列表

    //静态地图API
    public static  final String LOCATION_URL_S = "http://api.map.baidu.com/staticimage?width=320&height=240&zoom=13&center=";

    //机器人api
    public static final String ROBOT_URL="http://106.15.200.12:8080/chatbot/service/dialog/getanswer";
    public static final String SIGNUP_URL="http://106.15.200.12:8080/chatbot/user/regist";
    public static final String GetCode_URL="http://106.15.200.12:8080/chatbot/user/getcode";
    public static final String LOGIN_URL="http://106.15.200.12:8080/chatbot/user/login";
    public static final String RESETPWD_URL="http://106.15.200.12:8080/chatbot/user/updatePass";

}
