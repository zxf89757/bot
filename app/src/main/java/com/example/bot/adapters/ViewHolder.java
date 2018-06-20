package com.example.bot.adapters;

import android.util.SparseArray;
import android.view.View;

@SuppressWarnings("unchecked")
/**
 * @param view 所有缓存View的根View
 * @param id   缓存View的唯一标识
 */
class ViewHolder {
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		//如果根view没有用来缓存View的集合
		if (viewHolder == null) {
			viewHolder = new SparseArray<>();
			view.setTag(viewHolder);//创建集合和根View关联
		}
		View childView = viewHolder.get(id);//获取根View储存在集合中的hz
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}