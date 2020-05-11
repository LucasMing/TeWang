package com.qing.tewang.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.ApiException;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.tencent.mm.opensdk.modelbiz.SubscribeMessage;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

public class WechatUtil {
    private static final String APP_ID = "wxbf5b9f97d7d03572";

    private IWXAPI mApi;

    private Context context;

    public WechatUtil(Context context) {
        this.context = context;
    }

    private void regToWx() {
        mApi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mApi.registerApp(APP_ID);
    }


    public void register() {
        mApi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mApi.registerApp(APP_ID);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "tewang";
        mApi.sendReq(req);

    }


    public void subscribe() {
        mApi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mApi.registerApp(APP_ID);
        SubscribeMessage.Req req = new SubscribeMessage.Req();
        req.scene = 99;
        req.templateID = "h3mVvcFMSiwbr-01iJ-TjuDTX3KOU9e_WNEIUlCO8ro";
        mApi.sendReq(req);
    }


    public void share(String url, String title, String description) {
        mApi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mApi.registerApp(APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description == null ? title : description;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;

        mApi.sendReq(req);

    }


    public void share(Activity activity, String url, String title, String description, String img) {
        mApi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mApi.registerApp(APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description == null ? title : description;

        if (!TextUtils.isEmpty(img)) {
            APIWrapper.downloadAndGet(img)
                    .subscribe(new SimpleDialogObserver<File>(activity) {
                        @Override
                        public void onNext(File file) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.outWidth = 100;
                            options.outHeight = 100;
                            options.inJustDecodeBounds = false;
                            Bitmap thumb = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                            msg.thumbData = FileUtils.bmpToByteArray(thumb,true);
                        }

                        @Override
                        public void onError(ApiException ex) {
                            super.onError(ex);

                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = String.valueOf(System.currentTimeMillis());
                            req.message = msg;
                            req.scene = SendMessageToWX.Req.WXSceneSession;

                            mApi.sendReq(req);
                        }
                    });
        } else {
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;

            mApi.sendReq(req);
        }


    }


}
