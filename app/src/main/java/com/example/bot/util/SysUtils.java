package com.example.bot.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.example.bot.R;
import com.example.bot.global.Const;

import java.io.File;
import java.util.Collection;

public class SysUtils {

    // 获取当前APP版本号
    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            return "1";
        }
        return String.valueOf(packInfo.versionCode);
    }

    /**
     * 创建文件夹
     */
    public static void initFiles() {
        File file = new File(Environment.getExternalStorageDirectory(), "bot/data");
        if (!file.exists())
            file.mkdirs();
        file = new File(Environment.getExternalStorageDirectory(), "bot/images/upload");
        if (!file.exists())
            file.mkdirs();
        file = new File(Environment.getExternalStorageDirectory(), "bot/images/cache");
        if (!file.exists())
            file.mkdirs();
        file = new File(Environment.getExternalStorageDirectory(), "bot/download");
        if (!file.exists())
            file.mkdirs();
        file = new File(Environment.getExternalStorageDirectory(), "bot/voice");
        if (!file.exists())
            file.mkdirs();
    }

    /**
     * 判断集合是否为空
     *
     * @param c
     * @return
     */
    public static boolean isEmpty(Collection<?> c) {
        if (c == null || c.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否登录（UUID是否为空）
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        boolean isLogin = false;
        if (!TextUtils.isEmpty(PreferencesUtils.getSharePreStr(context, Const.UUID))) {
            isLogin = true;
        }
        return isLogin;
    }

    /**
     * 无参数跳转
     *
     * @param activity
     * @param cla
     */
    public static <T> void startActivity(Activity activity, Class<T> cla) {
        Intent intent = new Intent(activity, cla);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    /**
     * 带参数跳转
     *
     * @param activity
     * @param cla
     * @param b        注意，接收Bundle的key为“b”
     */
    public static <T> void startActivity(Activity activity, Class<T> cla, Bundle b) {
        Intent intent = new Intent(activity, cla);
        if (b != null) {
            intent.putExtra("b", b);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
    }

    /**
     * 防止滑动listView到顶部或底部时出现蓝边现象
     *
     * @param listView
     */
    public static void setOverScrollMode(ListView listView) {
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }


    /**
     * 状态栏高度算法
     *
     * @param activity
     * @return
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

}
