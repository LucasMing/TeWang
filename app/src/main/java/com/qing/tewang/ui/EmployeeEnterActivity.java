package com.qing.tewang.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.ui.location.SelectLocation01;
import com.qing.tewang.widget.SmartTitleBar;

public class EmployeeEnterActivity extends BaseActivity {

    private EditText mName, mMobile;
    private TextView mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_enter);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        titleBar.getRightTextView().setOnClickListener(view -> {

            String name = mName.getEditableText().toString();
            String mobile = mMobile.getEditableText().toString();
            String address = mAddress.getEditableText().toString();
            if (TextUtils.isEmpty(name)) {
                showToast("name不能为空");
                return;
            }

            if (TextUtils.isEmpty(mobile)) {
                showToast("mobile不能为空");
                return;
            }

            if (TextUtils.isEmpty(address)) {
                showToast("address不能为空");
                return;
            }


            APIWrapper.updateEmployee(address, mobile)
                    .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                        @Override
                        public void onNext(CommonData data) {
                            if (data.getErrno() == 0) {
                                showToast("提交成功！");
                                finish();
                            } else {
                                showToast(data.getMsg());
                            }
                        }
                    });

        });

        mName = findViewById(R.id.name);
        mMobile = findViewById(R.id.mobile);
        mAddress = findViewById(R.id.address);

        mAddress.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SelectLocation01.class);
            startActivityForResult(intent, 400);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED && data != null) {
            if (data.getStringExtra("province").equals(data.getStringExtra("city"))) {
                mAddress.setText(data.getStringExtra("province") +
                        data.getStringExtra("county"));
            } else {
                mAddress.setText(data.getStringExtra("province") +
                        data.getStringExtra("city") +
                        data.getStringExtra("county"));
            }
        }
    }

}
