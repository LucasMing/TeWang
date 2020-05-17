package com.qing.tewang.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qing.tewang.R;
import com.qing.tewang.widget.BannerLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TogetherFragment extends BaseFragment {


    public TogetherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_together, container, false);
        initBanner(rootView);
        return  rootView;
    }

    private void initBanner(View view){
        ArrayList<String> adList = new ArrayList<>();
        adList.add("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
        adList.add("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");
        adList.add("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");

        BannerLayout bannerLayout = view.findViewById(R.id.banner_ad);
        bannerLayout.setAutoPlay(false);
        bannerLayout.setMargin(false);
        bannerLayout.setIndicatorState(true);

        bannerLayout.setViewUrls(adList);
    }

}
