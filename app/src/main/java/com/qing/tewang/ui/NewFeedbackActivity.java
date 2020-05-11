package com.qing.tewang.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.widget.SmartTitleBar;

public class NewFeedbackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feedback);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        EditText content = findViewById(R.id.content);

        findViewById(R.id.commit)
                .setOnClickListener(view -> {
                    if (!TextUtils.isEmpty(content.getText().toString())) {
                        APIWrapper.sendFeedBack(content.getEditableText().toString())
                                .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                                    @Override
                                    public void onNext(CommonData data) {
                                        if (data.getErrno() == 0) {
                                            showToast("感谢您的反馈！");
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
