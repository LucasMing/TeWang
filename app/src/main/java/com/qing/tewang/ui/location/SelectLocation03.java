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

/**
 * Created by Administrator on 2016/7/28 0005.
 */
public class SelectLocation03 extends BaseActivity {
    private ArrayList<CountyModel> city2Models;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location02);
        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        city2Models = (ArrayList<CountyModel>) getIntent().getExtras().get("data2");
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(new PinBaseAdapter<CountyModel>(getApplication(), city2Models) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_city01, null);
                }

                TextView name = ViewHolder.findViewById(convertView, R.id.city_name);
                name.setText(mList.get(position).getCounty());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("county", mList.get(position).getCounty());
                        setResult(400, intent);
                        SelectLocation03.this.finish();
                    }
                });
                return convertView;
            }
        });
    }
}

