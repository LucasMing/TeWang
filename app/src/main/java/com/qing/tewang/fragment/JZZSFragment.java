package com.qing.tewang.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qing.tewang.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JZZSFragment extends Fragment {


    private View view;
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
        return   view;
    }


}
