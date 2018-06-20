package com.example.bot.activitys.tab.tab3;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.bot.R;
import com.example.bot.db.ChatMsgDao;
import com.example.bot.activitys.base.SlideBackActivity;
import com.example.bot.util.ToastUtil;

/**
 * 聊天历史纪录
 */
public class MsgHistoryActivity extends SlideBackActivity {
    private ChatMsgDao chatMsgDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_history);
		chatMsgDao=new ChatMsgDao(this);
		initTitleBar("我的","设置", this);
		initView();
	}


	/**
	 * 
	 */
	private void initView() {
        RelativeLayout rl_msg_history = findViewById(R.id.rl_msg_history);
		rl_msg_history.setOnClickListener(this);
	}


	@Override
	public void onClick(View arg0) {
		super.onClick(arg0);
		switch (arg0.getId()) {//返回
			case R.id.rl_msg_history://清空所有聊天记录
				chatMsgDao.deleteTableData();
				ToastUtil.showToast(MsgHistoryActivity.this,"聊天记录已清空");
				break;
		}
	}
	

}
