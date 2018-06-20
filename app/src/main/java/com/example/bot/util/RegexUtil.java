package com.example.bot.util;

import java.security.MessageDigest;
import java.util.regex.Pattern;

/**
 * 手机号验证与加密工具
 */
public class RegexUtil {

    public static String encrypt(String plaintext) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 验证手机号码
     */
    public static boolean checkMobile(String mobile) { //验证手机
        if (mobile.length() != 11) {
            return false;
        }
        String regex = "(\\+\\d+)?1[345678]\\d{9}$";
        if (!Pattern.matches(regex, mobile)) {
            return false;
        }
        return true;
    }
}

	    			