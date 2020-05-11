package com.qing.tewang.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qing.tewang.R;
import com.qing.tewang.widget.SmartTitleBar;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());


        findViewById(R.id.employee_enter)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), EmployeeEnterActivity.class);
                    startActivity(intent);
                });


        findViewById(R.id.business_enter)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(getApplication(), CreateShopActivity.class);
                    startActivityForResult(intent, 200);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            finish();
        }
    }
}
