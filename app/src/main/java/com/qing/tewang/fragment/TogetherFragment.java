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
public class TogetherFragment extends BaseFragment {


    public TogetherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_together, container, false);
        return  rootView;
    }

}
