package com.qing.tewang.fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.qing.tewang.R;
import com.qing.tewang.widget.BannerLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class TogetherFragment extends BaseFragment {


    public TogetherFragment() {
        // Required empty public constructor
    }

    private ImageView float_button;
    private RadioGroup radioGroup;
    private View rootView;
    private boolean canShow = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_together, container, false);
        float_button = rootView.findViewById(R.id.float_button);
        initView();
        float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canShow) {
                    ShowRotateAnimation(float_button);
                } else {
                    HideRotateAnimation(float_button);
                }
            }
        });
        return rootView;
    }

    private void ShowRotateAnimation(ImageView imageView) {
        canShow = false;
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(imageView, "translationX", 0, -200f);
        ObjectAnimator ra = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationX, ra); //设置动画
        animatorSet.setDuration(1000);  //设置动画时间
        animatorSet.start(); //启动
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //要推迟执行的方法
                if (!canShow) {
                    mHandler.sendEmptyMessage(100);
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 5000, 500);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            if (msg.what == 100) {
                HideRotateAnimation(float_button);
            }
        }
    };

    private void HideRotateAnimation(ImageView imageView) {
        canShow = true;
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(imageView, "translationX", -200f, 0);
        ObjectAnimator ra = ObjectAnimator.ofFloat(imageView, "rotation", 360f, 0f);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationX, ra); //设置动画
        animatorSet.setDuration(1000);  //设置动画时间
        animatorSet.start(); //启动
    }

    private void initView() {
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int arg1) {
                //遍历RadioGroup 里面所有的子控件。
                for (int index = 0; index < group.getChildCount(); index++) {
                    //获取到指定位置的RadioButton
                    RadioButton rb = (RadioButton) group.getChildAt(index);
                    //如果被选中
                    if (rb.isChecked()) {
                        setIndexSelectedTwo(index);  //方法二
                        break;
                    }
                }

            }
        });
        radioGroup.check(R.id.rb_jzzs);

    }

    //方法二，选中替换
    private void setIndexSelectedTwo(int index) {
        switch (index) {
            case 0:
                changeFragment(new JZZSFragment().getFragmentA());
                break;
            case 1:
                changeFragment(new WDSCFragment().getFragmentA());
                break;
            case 2:
                changeFragment(new JZZSFragment().getFragmentA());
                break;
            case 3:
                changeFragment(new JZZSFragment().getFragmentA());
                break;


            default:
                break;
        }
    }

    //方法二，默认第一fragment
    private void changeFragment(Fragment fm) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.content, fm);
        transaction.commit();
    }


}
