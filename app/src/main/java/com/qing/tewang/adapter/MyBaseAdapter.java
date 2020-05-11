package com.qing.tewang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuliao on 2017/12/8.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    public List<T> mList;
    public Context mContext;
    public LayoutInflater mInflater;

    public MyBaseAdapter(Context mContext, List<T> mList) {
        this.mList = mList;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public MyBaseAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int i) {
        return mList == null ? null : mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public void addItem(T t) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.add(t);
    }

    public void clear() {
        if (mList != null) {
            mList.clear();
        }

    }

    public void addItems(List<T> t) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.addAll(t);
    }

    public void removeItem(T t) {
        if (mList != null) {
            mList.remove(t);
        }
    }

    public void removeAll() {
        if (mList != null) {
            mList.clear();
        }
    }

    public List<T> getData() {
        return mList;
    }
}
