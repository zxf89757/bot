package com.example.bot.model;

import java.io.Serializable;

/**
 * 聊天信息表
 */
@SuppressWarnings("serial")
public class Msg implements Serializable {

	private int msgId;//id
	private String type;//信息类型
	private String content;//信息内容
	private int isComing;//0表接收的消息，1表发送的消息
	private String date;//时间
	
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIsComing() {
		return isComing;
	}
	public void setIsComing(int isComing) {
		this.isComing = isComing;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
