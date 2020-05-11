package com.qing.tewang.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qing.tewang.R;
import com.qing.tewang.util.ImageUtils;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class ScrollPagerAdapter extends PagerAdapter {
    private ArrayList<View> images;
    private ArrayList<String> urls;
    private Context context;

    public ScrollPagerAdapter(Context context, ArrayList<View> images,
                              ArrayList<String> urls) {
        this.context = context;
        this.images = images;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        final View view = images.get(position);
        PhotoView pv = view.findViewById(R.id.photo_view);


        pv.setOnClickListener(v -> {
            if (context instanceof Activity)
                ((Activity) context).finish();
        });

        loadImage(context, pv, urls.get(position));
        container.addView(view);
        return view;
    }

    private void loadImage(final Context context, final ImageView view,
                           String url) {
        if (url != null) {
            ImageUtils.load(context, url, view);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(images.get(position));
    }
}
