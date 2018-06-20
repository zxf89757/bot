package com.example.bot.activitys.tab.tab2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bot.R;
import com.example.bot.adapters.ChatAdapter;
import com.example.bot.model.Answer;
import com.example.bot.model.Msg;
import com.example.bot.global.Const;
import com.example.bot.db.ChatMsgDao;
import com.example.bot.speech.SpeechRecognizerUtil;
import com.example.bot.speech.SpeechSynthesizerUtil;
import com.example.bot.activitys.base.AppBaseActivity;
import com.example.bot.util.LocationUtils;
import com.example.bot.util.LogUtil;
import com.example.bot.util.PraseUtil;
import com.example.bot.util.PreferencesUtils;
import com.example.bot.util.SysUtils;
import com.example.bot.util.ToastUtil;
import com.example.bot.view.ActionSheetBottomDialog;
import com.example.bot.view.DropdownListView;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 聊天界面
 *
 */
@SuppressLint("SimpleDateFormat")
public class ChatActivity extends AppBaseActivity implements DropdownListView.OnRefreshListenerHeader,
        ChatAdapter.OnClickMsgListener {

    private FinalHttp fh;

    private List<Msg> listMsg;//消息列表
    private SimpleDateFormat sd;//时间格式
    private ChatMsgDao msgDao;
    private ChatAdapter mLvAdapter;

    private SpeechRecognizerUtil speechRecognizerUtil;// 语音听写工具
    private SpeechSynthesizerUtil speechSynthesizerUtil;// 语音合成工具

    private DropdownListView mListView;//列表
    private LinearLayout chat_add_container;//辅助输入
    private EditText input;//编辑框
    private ImageView image_voice;//语音
    private TextView send;//发送按钮

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initTitleBar("消息", "chatbot", this);
        fh = new FinalHttp();
        LogUtil.e(PreferencesUtils.getSharePreStr(ChatActivity.this, Const.UUID));
        fh.addHeader("uuid",PreferencesUtils.getSharePreStr(ChatActivity.this, Const.UUID));
        sd = new SimpleDateFormat("MM-dd HH:mm");
        msgDao = new ChatMsgDao(this);
        //初始化控件
        initViews();
        //初始化更多选项（即表情图标右侧"+"号内容）
        initAdd();
        //初始化数据
        initData();
        //初始化语音听写及合成部分
        initSpeech();
    }

    private void initSpeech() {
        speechRecognizerUtil = new SpeechRecognizerUtil(this);
        speechSynthesizerUtil = new SpeechSynthesizerUtil(this);
    }

    /**
     * 初始化控件
     */
    private void initViews() {

        mListView = findViewById(R.id.message_chat_listview);
        SysUtils.setOverScrollMode(mListView);

        ImageView image_add = findViewById(R.id.image_add);
        image_voice = findViewById(R.id.image_voice);//语音
        chat_add_container = findViewById(R.id.chat_add_container);//更多
        input = findViewById(R.id.input_sms);
        send = findViewById(R.id.send_sms);

        input.setOnClickListener(this);
        //添加输入框文本变化监听
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    send.setVisibility(View.VISIBLE);
                    image_voice.setVisibility(View.GONE);
                } else {
                    send.setVisibility(View.GONE);
                    image_voice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        image_add.setOnClickListener(this);//更多按钮
        image_voice.setOnClickListener(this);//语音按钮
        send.setOnClickListener(this); // 发送

        //滑动监听
        mListView.setOnRefreshListenerHead(this);
        //触摸监听
        mListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    if (chat_add_container.getVisibility() == View.VISIBLE) {
                        chat_add_container.setVisibility(View.GONE);
                    }
                    hideSoftInputView();
                }
                return false;
            }
        });
    }

    private void initAdd() {
        TextView tv_weather = findViewById(R.id.tv_weather);
        TextView tv_xingzuo = findViewById(R.id.tv_xingzuo);
        TextView tv_joke = findViewById(R.id.tv_joke);
        TextView tv_loc = findViewById(R.id.tv_loc);
        TextView tv_story = findViewById(R.id.tv_story);
        TextView tv_picture = findViewById(R.id.tv_picture);
        TextView tv_express = findViewById(R.id.tv_express);

        tv_weather.setOnClickListener(this);
        tv_xingzuo.setOnClickListener(this);
        tv_joke.setOnClickListener(this);
        tv_loc.setOnClickListener(this);
        tv_story.setOnClickListener(this);
        tv_picture.setOnClickListener(this);
        tv_express.setOnClickListener(this);
    }

    private void initData() {
        listMsg = msgDao.queryMsg(0);//数据源
        mLvAdapter = new ChatAdapter(this, listMsg, this);//适配器
        mListView.setAdapter(mLvAdapter);//视图绑定适配器
        mListView.setSelection(listMsg.size());
        //mLvAdapter.notifyDataSetChanged();//通知更新视图
    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
            case R.id.send_sms://发送
                String content = input.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                sendMsgText(content, true);
                break;
            case R.id.input_sms://点击输入框
                if (chat_add_container.getVisibility() == View.VISIBLE) {
                    chat_add_container.setVisibility(View.GONE);
                }
                break;
            case R.id.image_add://点击加号按钮
                hideSoftInputView();//隐藏软键盘
                if (chat_add_container.getVisibility() == View.GONE) {
                    chat_add_container.setVisibility(View.VISIBLE);
                } else {
                    chat_add_container.setVisibility(View.GONE);
                }
                break;
            case R.id.image_voice://点击语音按钮
                speechRecognizerUtil.say(input);
                break;
            case R.id.tv_weather:
                input.setText("天气#");
                input.setSelection(input.getText().toString().length());//光标移至最后
                changeList(Const.MSG_TYPE_TEXT, "请输入天气#您的查询城市");
                chat_add_container.setVisibility(View.GONE);
                showSoftInputView(input);
                break;
            case R.id.tv_xingzuo:
                input.setText("星座#");
                input.setSelection(input.getText().toString().length());//光标移至最后
                changeList(Const.MSG_TYPE_TEXT, "请输入星座#您的星座查询");
                chat_add_container.setVisibility(View.GONE);
                showSoftInputView(input);
                break;
            case R.id.tv_joke:
                sendMsgText("笑话#", true);
                break;
            case R.id.tv_loc:
                sendMsgText("位置#", false);
                Location location = LocationUtils.getInstance(ChatActivity.this).showLocation();
                String lat="";
                if(location!=null) {
                    lat = "" + location.getLongitude() + "," + location.getLatitude();
                }
                if (TextUtils.isEmpty(lat)) {
                    lat = "116.404,39.915";//北京
                }
                //lat = "116.404,39.915";
                changeList(Const.MSG_TYPE_LOCATION, Const.LOCATION_URL_S + lat + "&markers=|" + lat + "&markerStyles=l,A,0xFF0000");//传入地图（图片）路径
                LocationUtils.getInstance(this).removeLocationUpdatesListener();
                break;
            case R.id.tv_picture:
                input.setText("菜谱#");
                input.setSelection(input.getText().toString().length());
                changeList(Const.MSG_TYPE_TEXT, "请输入：菜谱#菜名");
                chat_add_container.setVisibility(View.GONE);
                showSoftInputView(input);
                break;
            case R.id.tv_story:
                sendMsgText("讲故事#", true);
                break;
            case R.id.tv_express:
                input.setText("查快递#");
                input.setSelection(input.getText().toString().length());
                changeList(Const.MSG_TYPE_TEXT, "请输入：查快递#快递单号");
                chat_add_container.setVisibility(View.GONE);
                showSoftInputView(input);
                break;
        }
    }

    /**
     * 刷新接收数据
     */
    private void changeList(String msgtype, String responeContent) {
        Msg msg = new Msg();
        msg.setIsComing(0);
        msg.setContent(responeContent);
        msg.setType(msgtype);
        msg.setDate(sd.format(new Date()));
        msg.setMsgId(msgDao.insert(msg));
        listMsg.add(msg);
        mLvAdapter.notifyDataSetChanged();
        /*if (msg.getType().equals(Const.MSG_TYPE_TEXT)) {
            String speech_type = PreferencesUtils.getSharePreStr(this, Const.IM_SPEECH_TPPE);
            if (!TextUtils.isEmpty(speech_type) && speech_type.equals("1")) {
                speechSynthesizerUtil.speech(msg.getContent());
            }
        }*/

    }

    /**
     * 刷新发送数据
     * isReqApi 是否调用api回答问题
     */
    private void sendMsgText(String content, boolean isReqApi) {
        Msg msg = new Msg();
        msg.setIsComing(1);
        msg.setContent(content);
        msg.setType(Const.MSG_TYPE_TEXT);
        msg.setDate(sd.format(new Date()));
        msg.setMsgId(msgDao.insert(msg));
        listMsg.add(msg);
        mLvAdapter.notifyDataSetChanged();
        input.setText("");
        if (isReqApi) getFromMsg(content);
    }

    /**
     * 获取结果
     *
     * @param info
     */
    private void getFromMsg(String info) {
        if (info.startsWith("天气#") && info.length() >= 3) {
            if(info.length() == 3){
                changeList(Const.MSG_TYPE_TEXT, "请输入天气#您的查询城市");
            }else{
                getResponse(info.split("#")[1] + "天气", true);
            }
        }
        else if (info.startsWith("星座#") && info.length() >= 3) {
            if(info.length() == 3){
                changeList(Const.MSG_TYPE_TEXT, "请输入星座#您的星座查询");
            }else {
                getResponse(info.split("#")[1] + "运势", true);
            }
        }
        else if (info.startsWith("笑话#") && info.length() >= 3){
            getResponse(info, true);
        }
        else if(info.startsWith("查快递#") && info.length() >= 4){
            getResponse(info, true);
        }
        else if(info.startsWith("讲故事#") && info.length() >= 4){
            getResponse(info, true);
        }
        else if(info.startsWith("菜谱#") && info.length() >= 3){
            if(info.length() == 3){
                changeList(Const.MSG_TYPE_TEXT, "请输入：菜谱#菜名");
            }else {
                getResponse(info.split("#")[1] + "菜谱", true);
            }
        }
        else{
            getResponse(info, false);
        }
    }

    /**
     * 调用后台api获取回答结果
     *
     * @param info
     */
    private void getResponse(String info, boolean onlytuling) {
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("question",info);
        if(onlytuling){
            ajaxParams.put("onlytuling","onlytuling");
        }
        else {
            ajaxParams.put("onlytuling"," ");
        }
        fh.post(Const.ROBOT_URL, ajaxParams, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                LogUtil.e("response>>" + o);
                try {
                    JSONObject jsonObject = new JSONObject((String) o);
                    if(jsonObject.getString("valid").equals("true")){
                        jsonObject = new JSONObject(jsonObject.getString("data"));
                        Answer answer = PraseUtil.praseMsgText(jsonObject.getString("answer"));
                        String responeContent;
                        if (answer == null) {
                            responeContent = "网络错误";
                            changeList(Const.MSG_TYPE_TEXT, responeContent);
                        } else {
                            switch (answer.getCode()) {
                                case "40001"://参数key错误
                                case "40002"://请求内容info为空
                                case "40004"://当天请求次数已使用完
                                case "40007"://数据格式异常
                                case "100000"://文本
                                    responeContent = answer.getText();
                                    changeList(Const.MSG_TYPE_TEXT, responeContent);
                                    break;
                                case "200000"://链接
                                    responeContent = answer.getText() + answer.getUrl();
                                    changeList(Const.MSG_TYPE_TEXT, responeContent);
                                    break;
                                case "302000"://新闻
                                case "308000"://菜谱
                                    responeContent=answer.getJsoninfo();
                                    changeList(Const.MSG_TYPE_LIST, responeContent);
                                    break;
                            }
                        }
                    }
                    else{
                        changeList(Const.MSG_TYPE_TEXT, jsonObject.getString("error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                changeList(Const.MSG_TYPE_TEXT, "网络连接失败");
            }
        });
    }

    @Override
    public void longClick(int position) {//长按
        Msg msg = listMsg.get(position);
        switch (msg.getType()) {
            case Const.MSG_TYPE_TEXT://文本
                clip(msg, position);
                break;
            case Const.MSG_TYPE_LOCATION://位置
            case Const.MSG_TYPE_LIST://列表
                delonly(msg, position);
                break;
        }
    }

    /**
     * 带复制文本的操作
     */
    private void clip(final Msg msg, final int position) {
        new ActionSheetBottomDialog(this)
                .builder()
                .addSheetItem("复制", new ActionSheetBottomDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        ClipboardManager cmb = (ClipboardManager) ChatActivity.this.getSystemService(ChatActivity.CLIPBOARD_SERVICE);
                        assert cmb != null;
                        cmb.setText(msg.getContent());
                        ToastUtil.showToast(ChatActivity.this, "已复制到剪切板");
                    }
                })
                .addSheetItem("朗读", new ActionSheetBottomDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        speechSynthesizerUtil.speech(msg.getContent());
                    }
                })
                .addSheetItem("删除", new ActionSheetBottomDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        listMsg.remove(position);
                        mLvAdapter.notifyDataSetChanged();
                        msgDao.deleteMsgById(msg.getMsgId());
                    }
                })
                .show();
    }

    /**
     * 仅有删除操作
     */
    private void delonly(final Msg msg, final int position) {
        new ActionSheetBottomDialog(this)
                .builder()
                .addSheetItem("删除", new ActionSheetBottomDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        listMsg.remove(position);
                        mLvAdapter.notifyDataSetChanged();
                        msgDao.deleteMsgById(msg.getMsgId());
                    }
                })
                .show();
    }

    /**
     * 下拉加载更多
     */
    @Override
    public void onRefresh() {
        //List<Msg> list = msgDao.queryMsg(offset);
        List<Msg> list = msgDao.queryMsg(listMsg.size());
        if (list.size() <= 0) {
            mListView.setSelection(0);
            mListView.onRefreshCompleteHeader();
            return;
        }
        listMsg.addAll(0, list);
        mListView.onRefreshCompleteHeader();
        mLvAdapter.notifyDataSetChanged();
        mListView.setSelection(list.size());
    }

    /**
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideSoftInputView();
            if (chat_add_container.getVisibility() == View.VISIBLE) {
                chat_add_container.setVisibility(View.GONE);
            } else {
                if (speechSynthesizerUtil != null) {
                    speechSynthesizerUtil.stopSpeech();
                }
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //让输入框获取焦点
                input.requestFocus();
            }
        }, 100);

    }
}
