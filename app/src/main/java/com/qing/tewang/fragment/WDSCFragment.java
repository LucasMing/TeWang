package com.qing.tewang.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.adapter.Together_Grid_Adapter;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.widget.NoScrollGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WDSCFragment extends Fragment {


    private View view;
    private static WDSCFragment jzzs;
    public static WDSCFragment getFragmentA() {
        if (jzzs == null) {
            jzzs = new WDSCFragment();
        }
        return jzzs;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_wdsc, container, false);
        return   view;
    }

}
