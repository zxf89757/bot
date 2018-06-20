package com.example.bot.activitys.tab.tab2;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bot.R;
import com.example.bot.model.Msg;
import com.example.bot.global.Const;
import com.example.bot.db.ChatMsgDao;
import com.example.bot.activitys.base.BaseActivity;
import com.example.bot.util.ExpressionUtil;
import com.example.bot.util.SysUtils;

/**
 * 主程序
 */
public class Tab2Activity extends BaseActivity {

    private TextView tv_content;
    private TextView tv_time;

    private ChatMsgDao chatMsgDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);
        setPadding(R.id.activity_tab2);
        initTitleBar("", "消息", null);
        chatMsgDao = new ChatMsgDao(this);
        initView();
    }


    private void initView() {
        LinearLayout ll_chat = findViewById(R.id.ll_chat);
        tv_content = findViewById(R.id.tv_content);
        tv_time = findViewById(R.id.tv_time);
        ll_chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.ll_chat:
                SysUtils.startActivity(getParent(), ChatActivity.class);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Msg msg = chatMsgDao.queryTheLastMsg();
        if (msg != null) {
            tv_time.setText(msg.getDate());
            switch (msg.getType()) {
                case Const.MSG_TYPE_TEXT:
                    tv_content.setText(ExpressionUtil.prase(this,tv_content,msg.getContent()));
                    break;
                case Const.MSG_TYPE_LOCATION:
                    tv_content.setText("[位置]");
                    break;
            }
        }
    }
}
