package com.example.bot.util;


import android.content.Context;
import android.text.TextUtils;
import android.view.View.OnClickListener;

import com.example.bot.view.AlertDialog;

public class DialogUtil {

	/**
	 * 单按钮Dialog
	 * @param context
	 * @param msg 内容显示
	 */
	public static void showErrorMsg(Context context,String msg){
		try {
			new AlertDialog(context).builder()
					.setMsg(msg)
					.setCancelable()
					.setNegativeButton("确定").show();
		}catch (Exception e){

		}
	}


	/**
	 *
	 * @param context
	 * @param title
	 * @param msg
	 * @param ok
	 * @param cancel
	 * @param onClickListener
	 */
	public static void showChooseDialog(Context context,String title,String msg,String ok,String cancel,OnClickListener onClickListener){
		try {
			if(TextUtils.isEmpty(ok)){
				ok="确定";
			}
			if(TextUtils.isEmpty(cancel)){
				cancel="取消";
			}
			if(TextUtils.isEmpty(title)){
				new AlertDialog(context).builder()
				.setMsg(msg)
				.setCancelable()
				.setPositiveButton(ok, onClickListener).setNegativeButton(cancel).show();
			}else{
				new AlertDialog(context).builder().setTitle(title)
				.setMsg(msg)
				.setCancelable()
				.setPositiveButton(ok, onClickListener).setNegativeButton(cancel).show();
			}
		}catch (Exception e){

		}
		
	}

}
