package com.qing.tewang.ui.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import com.qing.tewang.R;
import com.qing.tewang.ui.BaseActivity;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.widget.SmartTitleBar;

import java.util.ArrayList;

/**
 * Created by wuliao on 16/7/25.
 */
public class SelectLocation02 extends BaseActivity {
    private ListView mListView;
    private ArrayList<CityModel> cityModels;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location02);
        SmartTitleBar titleBar =  findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cityModels = (ArrayList<CityModel>) getIntent().getExtras().get("data");

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new PinBaseAdapter<CityModel>(getApplication(), cityModels) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_city01, null);
                }

                TextView name = ViewHolder.findViewById(convertView, R.id.city_name);
                name.setText(mList.get(position).getCity());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        city = mList.get(position).getCity();
                        Intent intent = new Intent();
                        intent.setClass(SelectLocation02.this, SelectLocation03.class);
                        intent.putExtra("data2", mList.get(position).getCountyList());
                        startActivityForResult(intent, 200);
                    }
                });
                return convertView;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED || data == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("city", city);
        intent.putExtra("county", data.getExtras().getString("county"));
        setResult(400, intent);
        SelectLocation02.this.finish();
    }
}

