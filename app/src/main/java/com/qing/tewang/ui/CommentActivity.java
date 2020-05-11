package com.qing.tewang.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.widget.SmartTitleBar;

public class CommentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        EditText content = findViewById(R.id.content);
        String shopId = getIntent().getStringExtra("shopId");


        findViewById(R.id.commit)
                .setOnClickListener(view -> {
                    if (!TextUtils.isEmpty(content.getText().toString())) {
                        APIWrapper.sendComment(content.getEditableText().toString(), shopId)
                                .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                                    @Override
                                    public void onNext(CommonData data) {
                                        if (data.getErrno() == 0) {
                                            showToast("感谢您的留言！");
                                            finish();
                                        } else {
                                            showToast(data.getMsg());
                                        }
                                    }
                                });
                    }
                });
    }
}
