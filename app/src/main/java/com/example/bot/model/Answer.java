package com.example.bot.model;

import java.util.List;

/**
 * 回答类
 */
public class Answer {

    private String code;//返回码
    private String text;//内容
    private String url;//链接
    private List<ResponseList> listResponseList;//列表
    private String jsoninfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ResponseList> getListResponseList() {
        return listResponseList;
    }

    public void setListResponseList(List<ResponseList> listResponseList) {
        this.listResponseList = listResponseList;
    }

    public String getJsoninfo() {
        return jsoninfo;
    }

    public void setJsoninfo(String jsoninfo) {
        this.jsoninfo = jsoninfo;
    }
}
