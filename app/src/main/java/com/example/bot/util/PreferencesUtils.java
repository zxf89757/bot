package com.example.bot.util;

import android.content.Context;
import android.content.SharedPreferences;
public class PreferencesUtils {
    /**
     * 普通字段存放地址
     */
    private static final String TAG = "CHATBOT";

    public static String getSharePreStr(Context mContext, String field) {
        SharedPreferences sp = mContext.getSharedPreferences(TAG, 0);
        return sp.getString(field, "");
    }



    //保存string类型的value到whichSp中的field字段
    public static void putSharePre(Context mContext, String field, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(TAG, 0);
        sp.edit().putString(field, value).apply();
    }


}
