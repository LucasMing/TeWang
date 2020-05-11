package com.qing.tewang.util;

import android.content.Context;
import android.text.TextUtils;

import com.qing.tewang.app.MyApplication;
import com.qing.tewang.model.Location;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import cn.jpush.android.api.JPushInterface;


public class APIUtilImpl extends AppUtil {


    @Override
    public Map<String, Object> signParam(Map<String, Object> params) {
        //填充数据
        flatData(params);
        SortedMap<String, Object> sortMap = new TreeMap<>(String::compareTo);
        //所有参与传参的参数按照ascii排序（升序）
        sortMap.putAll(params);

        sortMap.put("sign", createSign("UTF-8", sortMap));
        return sortMap;
    }

    private void flatData(Map<String, Object> params) {
        params.put("nonce", StringUtils.getRandomString(10));
        params.put("timestamp", System.currentTimeMillis() / 1000);
        Context context = MyApplication.getInstance();
        String deviceId = JPushInterface.getRegistrationID(context);
        if (!TextUtils.isEmpty(deviceId)) {
            params.put("deviceId", deviceId);
        }
        if (!params.containsKey("token")) {
            if (SPUtils.getUser(context) != null
                    && !TextUtils.isEmpty(SPUtils.getUser(context).getToken())) {
                params.put("token", SPUtils.getUser(context).getToken());
            }
        }
        Location location = SPUtils.getLocation(context);

        //cityId
        if (!params.containsKey("cityId")) {
            if (location != null
                    && !TextUtils.isEmpty(location.getCityId())) {
                params.put("cityId", location.getCityId());
            }
        }
        //areaId
        if (!params.containsKey("areaId")) {
            if (location != null
                    && !TextUtils.isEmpty(location.getAreaId())) {
                params.put("areaId", location.getAreaId());
            }
        }

        //lat
        if (!params.containsKey("lat")) {
            if (location != null
                    && !TextUtils.isEmpty(String.valueOf(location.getLat()))) {
                params.put("lat", location.getLat());
            }
        }

        //lng
        if (!params.containsKey("lng")) {
            if (location != null
                    && !TextUtils.isEmpty(String.valueOf(location.getLng()))) {
                params.put("lng", location.getLng());
            }
        }


    }

}
