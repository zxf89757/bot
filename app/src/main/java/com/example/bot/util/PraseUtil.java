package com.example.bot.util;

import com.example.bot.model.Answer;
import com.example.bot.model.ResponseList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理InfoItem的业务类
 */
public class PraseUtil {

    /**
     * 解析机器人回复信息
     * 100000	文本类
     * 200000	链接类
     * 302000	新闻类
     * 308000	菜谱类
     *
     * @param result
     * @return
     */
    public static Answer praseMsgText(String result) {
        Answer answer;
        JSONObject jsonObject;
        try {
            answer = new Answer();
            jsonObject = (JSONObject) new JSONTokener(result).nextValue();
            answer.setJsoninfo(result);
            answer.setCode(jsonObject.optString("code"));
            answer.setText(jsonObject.optString("text"));
            switch (answer.getCode()) {
                case "40001"://参数key错误
                case "40002"://请求内容info为空
                case "40004"://当天请求次数已使用完
                case "40007"://数据格式异常
                case "100000":
                    //因text 字段 各类型都会返回，answer.setText已做处理，因此这里不做处理
                    break;
                case "200000":
                    answer.setUrl(jsonObject.optString("url"));
                    break;
                case "302000":
                    try {
                        JSONArray listArray = jsonObject.getJSONArray("list");
                        List<ResponseList> listNews=new ArrayList<>();
                        for (int i = 0; i < listArray.length(); i++) {
                            jsonObject = listArray.getJSONObject(i);
                            ResponseList news=new ResponseList();
                            news.setInfoarticle(jsonObject.optString("article"));
                            news.setIcon(jsonObject.optString("icon"));
                            news.setNamesource(jsonObject.optString("source"));
                            news.setDetailurl(jsonObject.optString("detailurl"));
                            listNews.add(news);
                        }
                        answer.setListResponseList(listNews);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "308000":
                    try {
                        JSONArray listArray = jsonObject.getJSONArray("list");
                        List<ResponseList> listCook=new ArrayList<>();
                        for (int i = 0; i < listArray.length(); i++) {
                            jsonObject = listArray.getJSONObject(i);
                            ResponseList cook=new ResponseList();
                            cook.setNamesource(jsonObject.optString("name"));
                            cook.setIcon(jsonObject.optString("icon"));
                            cook.setInfoarticle(jsonObject.optString("info"));
                            cook.setDetailurl(jsonObject.optString("detailurl"));
                            listCook.add(cook);
                        }
                        answer.setListResponseList(listCook);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }

        } catch (Exception e) {
            return null;
        }
        return answer;
    }

    public static String parseIatResult(String json) {
        StringBuilder ret = new StringBuilder();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

}
