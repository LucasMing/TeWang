package com.qing.tewang.util;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by 伍燎 on 2015/8/18.
 * 邮箱：dazhengxie@gmail.com
 */

public class ViewHolder {
    public static <T extends View> T findViewById(View view, int id) {
        @SuppressWarnings("unchecked")
        SparseArray<T> viewHolder = (SparseArray<T>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        T childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return childView;
    }
}
