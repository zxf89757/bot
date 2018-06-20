package com.example.bot.adapters;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bot.R;
import com.example.bot.model.Answer;
import com.example.bot.model.Msg;
import com.example.bot.global.Const;
import com.example.bot.activitys.WebActivity;
import com.example.bot.model.ResponseList;
import com.example.bot.util.ExpressionUtil;
import com.example.bot.util.PraseUtil;
import com.example.bot.util.SysUtils;


/**
 * 聊天适配器
 *
 * @ClassName: MessageChatAdapter
 */
/*@Override
public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView == null){
        viewHolder = new ViewHolder();
        convertView = mLayoutInflater.inflate(R.layout.lv_item, null);
        viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv);
        viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv);
        convertView.setTag(viewHolder);
        }else{
        viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mImageView.setBackgroundResource(R.mipmap.ic_launcher);
        viewHolder.mTextView.setText(mData.get(position));

        return convertView;
        }

class ViewHolder{
    ImageView mImageView;
    TextView mTextView;
}*/
public class ChatAdapter extends BaseListAdapter<Msg> {

    //文本
    private final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;

    private final int TYPE_RECEIVER_LOCATION = 2;
    private final int TYPE_SEND_LOCATION = 3;

    private final FinalBitmap finalImageLoader;
    private final OnClickMsgListener onClickMsgListener;

    public ChatAdapter(Context context, List<Msg> msgList, OnClickMsgListener onClickMsgListener) {
        super(context, msgList);
        mContext = context;
        finalImageLoader = FinalBitmap.create(context);
        this.onClickMsgListener = onClickMsgListener;
    }

    //获取item类型
    @Override
    public int getItemViewType(int position) {
        Msg msg = list.get(position);
        switch (msg.getType()) {
            case Const.MSG_TYPE_TEXT:
                return msg.getIsComing() == 0 ? TYPE_RECEIVER_TXT : TYPE_SEND_TXT;
            case Const.MSG_TYPE_LOCATION:
                return msg.getIsComing() == 0 ? TYPE_RECEIVER_LOCATION : TYPE_SEND_LOCATION;
            case Const.MSG_TYPE_LIST:
                return 4;
            default:
                return -1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    /**
     * 根据消息类型，使用对应布局
     *
     * @param msg
     * @param position
     * @return
     */
    private View createViewByType(Msg msg, int position) {
        switch (msg.getType()) {
            case Const.MSG_TYPE_TEXT://文本
                return getItemViewType(position) == TYPE_RECEIVER_TXT ? createView(R.layout.item_chat_text_rece) : createView(R.layout.item_chat_text_sent);
            case Const.MSG_TYPE_LOCATION://位置
                return getItemViewType(position) == TYPE_RECEIVER_LOCATION ? createView(R.layout.item_chat_location_rece) : createView(R.layout.item_chat_location_sent);
            case Const.MSG_TYPE_LIST://列表
                return createView(R.layout.item_chat_news_rece);
            default:
                return null;
        }
    }

    private View createView(int id) {
        return mInflater.inflate(id, null);
    }

    @Override
    public View bindView(final int position, View convertView, ViewGroup parent) {
        final Msg msg = list.get(position);
        if (convertView == null) {
            convertView = createViewByType(msg, position);
        }

        TextView chat_time = ViewHolder.get(convertView, R.id.chat_time);//时间
        TextView tv_text = ViewHolder.get(convertView, R.id.tv_text);//文本
        ImageView iv_location = ViewHolder.get(convertView, R.id.iv_location);//位置

        LinearLayout ll_news_list = convertView.findViewById(R.id.ll_news_list);
        ImageView iv_news_top_img = convertView.findViewById(R.id.iv_news_top_img);
        TextView tv_news_top_title = convertView.findViewById(R.id.tv_news_top_title);

        chat_time.setText(msg.getDate());//时间

        switch (msg.getType()) {
            case Const.MSG_TYPE_TEXT://文本
                tv_text.setText(ExpressionUtil.prase(mContext, tv_text, msg.getContent()));
                Linkify.addLinks(tv_text, Linkify.ALL);
                tv_text.setOnLongClickListener(new onLongCilck(position));
                break;
            case Const.MSG_TYPE_LOCATION://位置
                finalImageLoader.display(iv_location, msg.getContent());
                iv_location.setOnLongClickListener(new onLongCilck(position));
                break;
            case Const.MSG_TYPE_LIST:
                Answer answer = PraseUtil.praseMsgText(msg.getContent());
                viewList(ll_news_list,iv_news_top_img,tv_news_top_title,answer);
                iv_news_top_img.setOnLongClickListener(new onLongCilck(position));
                break;
        }

        return convertView;
    }



    /**
     * 屏蔽listitem的所有事件
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }


    /**
     * 列表类型
     */
    private void viewList(LinearLayout ll_news_list,ImageView iv_news_top_img,TextView tv_news_top_title,Answer answer) {
        List<ResponseList> listResponseList = answer.getListResponseList();
        final ResponseList responseList = listResponseList.get(0);
        if (TextUtils.isEmpty(responseList.getIcon())) {
            iv_news_top_img.setImageResource(R.drawable.splash_screen_b);
        }else{
            if(!responseList.getIcon().startsWith("http://")&&!responseList.getIcon().startsWith("http://")){
                finalImageLoader.display(iv_news_top_img,"http://"+responseList.getIcon());
            }
            else{
                finalImageLoader.display(iv_news_top_img,responseList.getIcon());
            }
        }
        iv_news_top_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                b.putString("url",responseList.getDetailurl());
                SysUtils.startActivity((Activity) mContext, WebActivity.class,b);
            }
        });
        ll_news_list.removeAllViews();
        for(int i=1;i<4;i++){
            ll_news_list.addView(createResponseListView(listResponseList.get(i)));
        }
        tv_news_top_title.setText(responseList.getInfoarticle());
    }

    private View createResponseListView(final ResponseList responseList){
        View view =mInflater.inflate(R.layout.item_list,null);
        ImageView iv= view.findViewById(R.id.iv);
        TextView tv= view.findViewById(R.id.tv);
        if (TextUtils.isEmpty(responseList.getIcon())) {
            iv.setImageResource(R.drawable.splash_screen_b);
        }else{
            if(!responseList.getIcon().startsWith("http://")&&!responseList.getIcon().startsWith("http://")){
                finalImageLoader.display(iv,"http://"+responseList.getIcon());
            }
            else{
                finalImageLoader.display(iv,responseList.getIcon());
            }
        }
        tv.setText(responseList.getInfoarticle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                b.putString("url",responseList.getDetailurl());
                SysUtils.startActivity((Activity) mContext, WebActivity.class,b);
            }
        });
        return view;
    }

    /**
     * 长按监听
     *
     */
    class onLongCilck implements View.OnLongClickListener {
        final int position;

        public onLongCilck(int position) {
            this.position = position;
        }

        @Override
        public boolean onLongClick(View arg0) {
            onClickMsgListener.longClick(position);
            return true;
        }
    }

    public interface OnClickMsgListener {
        void longClick(int position);
    }

}
