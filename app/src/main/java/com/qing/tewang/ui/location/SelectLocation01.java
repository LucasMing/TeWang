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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by wuliao on 16/7/25.
 */
public class SelectLocation01 extends BaseActivity {
    private ArrayList<ProvinceModel> provinceList;
    private ListView mListView;
    private String province;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location01);
        mListView = findViewById(R.id.list_view);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        initData();
        mListView.setAdapter(new PinBaseAdapter<ProvinceModel>(getApplication(), provinceList) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_city01, null);
                }

                TextView name = ViewHolder.findViewById(convertView, R.id.city_name);
                name.setText(mList.get(position).getProvince());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        province = mList.get(position).getProvince();
                        Intent intent = new Intent();
                        intent.setClass(SelectLocation01.this, SelectLocation02.class);
                        intent.putExtra("data", mList.get(position).getCityList());
                        startActivityForResult(intent, 200);
                    }
                });
                return convertView;
            }
        });

    }


    public void initData() {
        StringBuffer buffer;
        try {
            buffer = new StringBuffer();
            int len;
            byte[] data = new byte[1024];
            InputStream in = getAssets().open("ChinaProvincesInfo.json");
            while ((len = in.read(data)) != -1) {
                buffer.append(new String(data, 0, len));
            }
            in.close();
            analysisJSON(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void analysisJSON(String string) {
        try {
            JSONArray array = new JSONArray(string);
            provinceList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONArray names = object.names();
                for (int j = 0; j < names.length(); j++) {
                    ProvinceModel provinceModel = new ProvinceModel();
                    provinceModel.setProvince(names.getString(j));
                    provinceList.add(provinceModel);
                    JSONArray cityArray = object.getJSONArray(names.getString(j));
                    ArrayList<CityModel> cityList = new ArrayList<CityModel>();
                    for (int k = 0; k < cityArray.length(); k++) {
                        JSONObject cityObject = cityArray.getJSONObject(k);
                        JSONArray cityNameArray = cityObject.names();
                        for (int a = 0; a < cityNameArray.length(); a++) {
                            ArrayList<CountyModel> countyList = new ArrayList<CountyModel>();
                            CityModel cityModel = new CityModel();
                            cityModel.setCity(cityNameArray.getString(a));
                            cityList.add(cityModel);
                            JSONArray areaArray = cityObject.getJSONArray(cityNameArray.getString(a));
                            for (int b = 0; b < areaArray.length(); b++) {
                                CountyModel model = new CountyModel();
                                model.setCounty(areaArray.getString(b));
                                countyList.add(model);
                            }
                            cityModel.setCounty_list(countyList);
                        }
                    }
                    provinceModel.setCity_list(cityList);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED || data == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("province", province);
        intent.putExtra("city", data.getExtras().getString("city"));
        intent.putExtra("county", data.getExtras().getString("county"));
        setResult(400, intent);
        SelectLocation01.this.finish();
    }
}
