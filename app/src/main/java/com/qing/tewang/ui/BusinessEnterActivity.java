package com.qing.tewang.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.widget.SmartTitleBar;

public class BusinessEnterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_enter);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        EditText shopNameEdit = findViewById(R.id.shop_name);
        EditText addressEdit = findViewById(R.id.address);
        EditText nameEdit = findViewById(R.id.name);
        EditText mobileEdit = findViewById(R.id.mobile);


        titleBar.getRightTextView().setOnClickListener(view -> {

            String shopName = shopNameEdit.getEditableText().toString();
            String mobile = mobileEdit.getEditableText().toString();
            String address = addressEdit.getEditableText().toString();
            String name = nameEdit.getEditableText().toString();

            if (TextUtils.isEmpty(shopName)) {
                showToast("shopName不能为空");
                return;
            }
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

            APIWrapper.updateShop(address, mobile)
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


    }
}
