package com.example.bot.activitys;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bot.R;
import com.example.bot.global.Const;
import com.example.bot.activitys.login.LoginActivity;
import com.example.bot.util.PreferencesUtils;
import com.example.bot.util.SysUtils;
import com.example.bot.util.SystemBarTintManager;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * 安装启动页面
 */
public class GuidActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled();
			tintManager.setStatusBarTintResource(android.R.color.transparent);//通知栏所需颜色
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guid);
		initView();

	}

	private void initView() {
		TextView tv_ignore = findViewById(R.id.tv_ignore);
		tv_ignore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finishActivity();
			}
		});
		ViewPager mViewPager = findViewById(R.id.vPager);
		List<View> views = new ArrayList<>();
		LayoutInflater inflater = getLayoutInflater();
		for(int i=0;i<3;i++){
			View view=inflater.inflate(R.layout.activity_guid_item_view, null);
			RelativeLayout rl_guid= view.findViewById(R.id.rl_guid);
			TextView tv_guid= view.findViewById(R.id.tv_guid);
			switch (i) {
			case 0:
				tv_guid.setText("chatbot启动页1");
				rl_guid.setBackgroundResource(R.drawable.welcome_01);
				break;
			case 1:
				tv_guid.setText("chatbot启动页2");
				rl_guid.setBackgroundResource(R.drawable.welcome_02);
				break;
			case 2:
				tv_guid.setText("chatbot启动页3");
				rl_guid.setBackgroundResource(R.drawable.welcome_03);
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finishActivity();
					}
				});
				break;
			}
			views.add(view);
		}

		mViewPager.setAdapter(new MyViewPagerAdapter(views));
		mViewPager.setCurrentItem(0);

	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private final List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	private void finishActivity(){
		PreferencesUtils.putSharePre(GuidActivity.this, Const.VERSION_CODE, SysUtils.getVersionCode(GuidActivity.this));//保存当前版本号
		if(SysUtils.isLogin(GuidActivity.this)){//已登录
			SysUtils.startActivity(GuidActivity.this, MainActivity.class);//已登录进入首页
		}else{
			SysUtils.startActivity(GuidActivity.this, LoginActivity.class);
		}
		finish();
	}

}
