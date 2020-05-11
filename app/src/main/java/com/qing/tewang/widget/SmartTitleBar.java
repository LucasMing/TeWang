package com.qing.tewang.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qing.tewang.R;


/**
 * Created by Administrator on 2018/4/2 0002.
 */
@SuppressLint("NewApi")
public class SmartTitleBar extends RelativeLayout {

    private ImageView mLeftImageView, mRightImageView;
    private View mRightLayout;
    private TextView mRightTextView,mTitleView;


    public SmartTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.SmartTitleBar);
        String titleText = ta.getString(R.styleable.SmartTitleBar_smartTitleBarTitleText);
        String rightText = ta.getString(R.styleable.SmartTitleBar_smartTitleBarRightText);
        Drawable leftResource = ta.getDrawable(R.styleable.SmartTitleBar_smartTitleBarLeftResource);
        Drawable rightResource = ta.getDrawable(R.styleable.SmartTitleBar_smartTitleBarRightResource);
        int backColor = ta.getColor(R.styleable.SmartTitleBar_smartTitleBarTitleBackground,
                context.getResources().getColor(R.color.mainColor));
        ta.recycle();

        inflate(context, R.layout.view_title_bar, this);

        mLeftImageView = findViewById(R.id.left_iv);
        mRightImageView = findViewById(R.id.right_iv);
        mRightTextView = findViewById(R.id.right_tv);
        mRightLayout = findViewById(R.id.right_layout);

        mTitleView = findViewById(R.id.title_tv);
        mTitleView.setText(titleText);

        View titleLayout = findViewById(R.id.layout_title_bar);
        titleLayout.setBackgroundColor(backColor);

        if (leftResource != null) {
            mLeftImageView.setImageDrawable(leftResource);
        }


        if (rightResource != null) {
            mRightImageView.setVisibility(View.VISIBLE);
            mRightImageView.setImageDrawable(rightResource);
            mRightTextView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(rightText)) {
            mRightTextView.setVisibility(View.VISIBLE);
            mRightTextView.setText(rightText);
            mRightImageView.setVisibility(View.GONE);
        }

    }

    public void hideLeftImageView() {
        mLeftImageView.setVisibility(View.GONE);
    }

    public void setRightText(String text, @NonNull OnClickListener listener) {
        mRightTextView.setText(text);
        mRightImageView.setVisibility(View.GONE);
        mRightLayout.setOnClickListener(listener);
    }

    public void setRightText(@NonNull OnClickListener listener) {
        mRightImageView.setVisibility(View.GONE);
        mRightTextView.setOnClickListener(listener);
    }

    public void setRightImage(int resId, @NonNull OnClickListener listener) {
        mRightTextView.setVisibility(View.GONE);
        mRightImageView.setImageResource(resId);
        mRightTextView.setOnClickListener(listener);
    }

    public void setRightImage(@NonNull OnClickListener listener) {
        mRightTextView.setVisibility(View.GONE);
        mRightTextView.setOnClickListener(listener);
    }

    public View getLeftView() {
        return mLeftImageView;
    }

    public View getRightView() {
        return mRightLayout;
    }

    public TextView getRightTextView() {
        return mRightTextView;
    }

    public TextView getTitleTextView() {
        return mTitleView;
    }
}
