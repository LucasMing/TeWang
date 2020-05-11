package com.qing.tewang.ui.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伍燎 on 2015/8/18.
 */
public abstract class PinBaseAdapter<T> extends BaseAdapter implements Filterable {
    public List<T> mList;
    public Context mContext;
    public LayoutInflater mInflater;

    public PinBaseAdapter(Context mContext, List<T> mList) {
        this.mList = mList;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public PinBaseAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList == null ? 0 : mList.get(i);
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



    @Override
    public Filter getFilter() {
        return null;
    }

    public List<T> getData() {
        return mList;
    }
}
