package com.qing.tewang.ui;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.widget.SmartTitleBar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TagActivity extends BaseActivity {

    private TagFlowLayout mFlowLayout;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> {
            finish();
        });
        titleBar.getRightTextView()
                .setOnClickListener(view -> {
                    if (mList == null) {
                        showToast("数据还没有加载！");
                        return;
                    }
                    Set<Integer> sets = mFlowLayout.getSelectedList();
                    Intent intent = new Intent();
                    List<String> strs = Stream.of(sets)
                            .map(index -> mList.get(index))
                            .collect(Collectors.toList());

                    intent.putExtra("data", new ArrayList<>(strs));
                    setResult(RESULT_OK, intent);
                    finish();
                });

        mFlowLayout = findViewById(R.id.tag_layout);

        loadData();

    }

    private void loadData() {
        APIWrapper.getTag()
                .subscribe(new SimpleDialogObserver<List<String>>(getActivity()) {
                    @Override
                    public void onNext(List<String> commonData) {


                        mList = commonData;
                        mFlowLayout.setAdapter(new TagAdapter<String>(mList) {

                            @Override
                            public View getView(final FlowLayout parent, int position, final String text) {
                                View view = LayoutInflater.from(getApplicationContext())
                                        .inflate(R.layout.item_flow_text, parent, false);
                                TextView tv = view.findViewById(R.id.text);
                                tv.setText(text);
                                return view;
                            }

                            @Override
                            public void onSelected(int position, View view) {
                                super.onSelected(position, view);
                                view.setBackground(getResources().getDrawable(R.drawable.checked_bg));

                            }

                            @Override
                            public void unSelected(int position, View view) {
                                super.unSelected(position, view);
                                view.setBackground(getResources().getDrawable(R.drawable.normal_bg));
                            }
                        });

                    }
                });
    }
}
