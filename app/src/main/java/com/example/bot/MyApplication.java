package com.example.bot;

import android.app.Application;

import com.example.bot.global.Const;
import com.example.bot.util.LogUtil;
import com.iflytek.cloud.SpeechUtility;

public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		SpeechUtility.createUtility(this, "appid="+ Const.XF_VOICE_APPID);
		LogUtil.isShowLog=true;//是否打印log
	}

}
