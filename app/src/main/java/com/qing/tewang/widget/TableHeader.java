package com.qing.tewang.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableHeader extends LinearLayout {

    public TableHeader(Context context) {
        this(context, null);
    }

    public TableHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    public void setTitles(String title, String content) {
        if (getChildCount() > 0) {
            addBottomLine();
        }

        LinearLayout parent = new LinearLayout(getContext());
        parent.setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(parent, params);

        setTitleView(title, 1, parent);
        addLine(parent);
        setContentView(content, 2, parent);
        parent.requestLayout();
    }

    private void setTitleView(String title, int weight, ViewGroup parent) {
        TextView text = new TextView(getContext());
        text.setGravity(Gravity.CENTER);
        text.setTextSize(16);
        text.setText(title);
        text.setTextColor(Color.parseColor("#333333"));
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.weight = weight;
        text.setPadding(0, 15, 0, 15);
        params.gravity = Gravity.CENTER;
        parent.addView(text, params);
    }

    private void setContentView(String title, int weight, ViewGroup parent) {
        TextView text = new TextView(getContext());
        text.setGravity(Gravity.START);
        text.setTextSize(16);
        text.setText(title);
        text.setTextColor(Color.parseColor("#999999"));
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.weight = weight;
        text.setPadding(25, 15, 25, 15);

        params.gravity = Gravity.CENTER;
        parent.addView(text, params);
    }


    private void addLine(ViewGroup parent) {
        View view = new View(getContext());
        view.setBackgroundColor(Color.parseColor("#d6dbde"));
        LayoutParams params = new LayoutParams(2, LayoutParams.MATCH_PARENT);
        parent.addView(view, params);
    }

    private void addBottomLine() {
        View view = new View(getContext());
        view.setBackgroundColor(Color.parseColor("#d6dbde"));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
        addView(view, params);
    }
}

