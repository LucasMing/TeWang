package com.qing.tewang.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.User;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.widget.SmartTitleBar;

public class UserInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());


        EditText nameEdit = findViewById(R.id.name_edit);
        User user = SPUtils.getUser(getApplicationContext());
        nameEdit.setText(user.getName());


        titleBar.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showToast("不能为空！");
                    return;
                }
                if (name.equals(user.getName())) {
                    showToast("不能与原来名称相同！");
                    return;
                }

                APIWrapper.updateName(name)
                        .subscribe(new SimpleDialogObserver<CommonData<User>>(getActivity()) {
                            @Override
                            public void onNext(CommonData<User> data) {
                                if (data.getErrno() == 0) {
                                    User temp = data.getData();
                                    if (temp != null) {
                                        showToast("修改成功！");
                                        SPUtils.clearUser(getApplicationContext());
                                        SPUtils.saveUser(getApplicationContext(), temp);
                                        finish();
                                    } else {
                                        showToast(data.getMsg());
                                    }
                                }
                            }
                        });


            }
        });

    }
}
