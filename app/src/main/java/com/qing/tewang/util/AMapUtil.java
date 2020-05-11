package com.qing.tewang.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.qing.tewang.app.MyApplication;

public class AMapUtil {

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    private AMapLocationListener mMyListener = null;


    private AMapUtil() {

    }

    private static volatile AMapUtil sInstance = null;

    public static AMapUtil getInstance() {
        if (sInstance == null) {
            synchronized (AMapUtil.class) {
                if (sInstance == null) {
                    sInstance = new AMapUtil();
                }
            }
        }
        return sInstance;
    }


    public void startLocation() {
        if (mMyListener == null) {
            throw new RuntimeException("请设置Listener");
        }

        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(MyApplication.getInstance());
            mLocationClient.setLocationListener(mMyListener);

            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setOnceLocationLatest(true);
            option.setNeedAddress(true);
            option.setHttpTimeOut(8000);
            option.setMockEnable(true);
            option.setLocationCacheEnable(false);

            mLocationClient.setLocationOption(option);
        }
        mLocationClient.startLocation();
    }

    public void destroyLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            if (mMyListener != null) {
                mLocationClient.unRegisterLocationListener(mMyListener);
                mMyListener = null;
            }
            mLocationClient = null;
        }
        sInstance = null;
    }

    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    public AMapUtil setLocationListener(AMapLocationListener myListener) {
        mMyListener = myListener;
        return this;
    }

    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }



    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


}
