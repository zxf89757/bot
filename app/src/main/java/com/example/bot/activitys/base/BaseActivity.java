package com.example.bot.activitys.base;


import android.os.Build;

import com.example.bot.util.SysUtils;

/**
 * 实现沉浸式通知栏
 */
public class BaseActivity extends AppBaseActivity{

	/**
	 * 设置Activity标题栏距系统状态栏高度
	 * @param id
	 */
	protected void setPadding(int id){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			(findViewById(id)).setPadding(0, SysUtils.getStatusHeight(this), 0,0);
		}
	}

}
