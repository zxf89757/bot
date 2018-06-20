package com.example.bot.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

public class ExpressionUtil {
	
	public static SpannableStringBuilder prase(Context mContext,final TextView gifTextView,String content) {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		String regex = "\\[[^\\]]+\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
			try {
				String num = tempText.substring("[p/_".length(), tempText.length()- ".png]".length());
				String gif = "g/" + num + ".gif";
				/*
				  如果open这里不抛异常说明存在gif，则显示对应的gif
				  否则说明gif找不到，则显示png\\[[^\\]]+\\]
				  */
				InputStream is = mContext.getAssets().open(gif);
				is.close();
			} catch (Exception e) {
				String png = tempText.substring("[".length(),tempText.length() - "]".length());
				try {
					sb.setSpan(new ImageSpan(mContext, BitmapFactory.decodeStream(mContext.getAssets().open(png))), m.start(), m.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return sb;
	}

}
