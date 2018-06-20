package com.example.bot.model;

/**
 * 列表信息，包括list和
 * name	菜名
 * infoarticle	菜谱信息
 * detailurl	详情链接
 * icon	信息图标
 */
public class ResponseList {

    private String namesource;
    private String infoarticle;
    private String icon;
    private String detailurl;

    public String getNamesource() {
        return namesource;
    }

    public void setNamesource(String namesource) {
        this.namesource = namesource;
    }

    public String getInfoarticle() {
        return infoarticle;
    }

    public void setInfoarticle(String infoarticle) {
        this.infoarticle = infoarticle;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }
}
