package com.qing.tewang.fragment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.adapter.Together_Grid_Adapter;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.widget.BannerLayout;
import com.qing.tewang.widget.NoScrollGridView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class JZZSFragment extends Fragment {


    private View view;
    private NoScrollGridView myGridView;
    private NoScrollGridView myBottomGridView;
    private static JZZSFragment jzzs;
    public static JZZSFragment getFragmentA() {
        if (jzzs == null) {
            jzzs = new JZZSFragment();
        }
        return jzzs;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_jzz, container, false);
        myGridView = view.findViewById(R.id.my_grid_view);
        myBottomGridView = view.findViewById(R.id.my_bottom_grid);
        initBanner(view);
        myGridView.setAdapter(new GridListAdapter(getActivity()));
        myBottomGridView.setAdapter(new Together_Grid_Adapter(getActivity(),imgList2,strList2));
        return   view;
    }
    private void initBanner(View view) {
        ArrayList<String> adList = new ArrayList<>();
        adList.add("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
        adList.add("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
        adList.add("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");

        BannerLayout bannerLayout = view.findViewById(R.id.banner_ad);
        bannerLayout.setAutoPlay(true);
        bannerLayout.setMargin(false);
        bannerLayout.setIndicatorState(true);

        bannerLayout.setViewUrls(adList);
    }
    //中间图片和文字
    private int[] imgList = {R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,};
    private String[] strList = {"超市","生鲜","米面","鲜花","新车","婚摄","家政","家居","母婴","电影"};
    private int[] imgList2 = {R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,
            R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close,R.drawable.close};
    private String[] strList2 = {"食宿","KTV","美容","护理","服装","房地产","二手","维修","建材","五金","工业品","农贸","旅游","客运","货运","保险"};

    class GridListAdapter extends BaseAdapter {
        public Context context;
        public LayoutInflater layoutInflater;

        public GridListAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
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
            if(convertView == null){
                viewHolder = new MyViewHolder();
                convertView = layoutInflater.inflate(R.layout.together_grid_item, null);
                convertView.setTag(viewHolder);
                viewHolder.imageView = convertView.findViewById(R.id.image);
                viewHolder.textView = convertView.findViewById(R.id.text);
            }else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            viewHolder.imageView.setImageResource(imgList[position]);
            viewHolder.textView.setText(strList[position]);

            return convertView;
        }
    }
    class MyViewHolder extends ViewHolder{
        ImageView imageView;
        TextView textView;
    }

}
