package com.qing.tewang.util;

import android.app.Activity;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayManager {


    public static String APPID = "wxbf5b9f97d7d03572";

    // MARK: - 判断手机是否安装微信，微信版本是否支持支付
    public static Boolean checkWXCanPay(Activity activity) {

        IWXAPI api = WXAPIFactory.createWXAPI(activity, WXPayManager.APPID, false);

        Boolean isCanPay = true;

        if (!api.isWXAppInstalled()) {
            Toast.makeText(activity, "您的手机没有安装微信", Toast.LENGTH_SHORT).show();
            isCanPay = false;
        } else {
            boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;

            if (!isPaySupported) {
                Toast.makeText(activity, "您的微信版本过低，不支持支付", Toast.LENGTH_SHORT).show();
                isCanPay = false;
            }
        }
        return isCanPay;
    }



    public static void toPay(final Activity activity,
                             final String appId,
                             final String mch_id,
                             final String prepayid,
                             final String timestamp,
                             final String sign,
                             final String nonce_str) {


        // 在主线程中执行
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // IWXAPI 是第三方app和微信通信的openapi接口
                // 通过WXAPIFactory工厂，获取IWXAPI的实例
                IWXAPI api = WXAPIFactory.createWXAPI(activity, appId, false);
                // 将该app注册到微信
                api.registerApp(appId);

                String packageValue = "Sign=WXPay";

                System.out.println("appId==" + appId);
                System.out.println("mch_id==" + mch_id);
                System.out.println("prepayid==" + prepayid);
                System.out.println("noncestr==" + nonce_str);
                System.out.println("timestamp==" + timestamp);
                System.out.println("packageValue==" + packageValue);
                System.out.println("sign==" + sign);


                PayReq req = new PayReq();

                req.appId = appId;
                req.partnerId = mch_id;
                req.prepayId = prepayid;
                req.nonceStr = nonce_str;
                req.timeStamp = timestamp;
                req.packageValue = packageValue;
                req.sign = sign;


                Toast.makeText(activity, "正常调起支付", Toast.LENGTH_SHORT).show();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.sendReq(req);
            }
        });

    }


}
