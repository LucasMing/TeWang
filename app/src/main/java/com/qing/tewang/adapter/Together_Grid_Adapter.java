package com.qing.tewang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.util.ViewHolder;

public class Together_Grid_Adapter extends BaseAdapter {
    public Context context;
    public LayoutInflater layoutInflater;
    private int[] imgList;
    private String[] strList;

    public Together_Grid_Adapter(Context context, int[] imgList, String[] strList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.imgList = imgList;
        this.strList = strList;
    }

    @Override
    public int getCount() {
        return strList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyViewHolder();
            convertView = layoutInflater.inflate(R.layout.together_grid_item2, null);
            convertView.setTag(viewHolder);
            viewHolder.imageView = convertView.findViewById(R.id.image);
            viewHolder.textView = convertView.findViewById(R.id.text);
            viewHolder.ll_grid_item = convertView.findViewById(R.id.ll_grid_item);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        if(position/4 == 0){
            viewHolder.ll_grid_item.setBackgroundColor(context.getResources().getColor(R.color.color_fef2e6));
        }else if(position/4 == 1){
            viewHolder.ll_grid_item.setBackgroundColor(context.getResources().getColor(R.color.color_e8f5fe));
        }else if(position/4 == 2){
            viewHolder.ll_grid_item.setBackgroundColor(context.getResources().getColor(R.color.color_e1faf6));
        }else if(position/4 == 3){
            viewHolder.ll_grid_item.setBackgroundColor(context.getResources().getColor(R.color.color_f7effe));
        }
        viewHolder.imageView.setImageResource(imgList[position]);
        viewHolder.textView.setText(strList[position]);

        return convertView;
    }
    class MyViewHolder extends ViewHolder {
        LinearLayout ll_grid_item;
        ImageView imageView;
        TextView textView;
    }

}