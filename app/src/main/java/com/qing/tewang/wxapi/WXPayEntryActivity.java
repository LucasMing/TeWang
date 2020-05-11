package com.qing.tewang.wxapi;

import android.content.Intent;
import android.os.Bundle;


import com.qing.tewang.ui.BaseActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.utils.Log;

/**
 * Created by wuliao on 2018/4/16.
 */

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private static final String APP_ID = "wxbf5b9f97d7d03572";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    private Intent intent;

    /**
     * 得到支付结果回调
     */
    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);// 支付结果码
        intent = new Intent();
        if (resp.errCode == 0) {
            showToast("支付成功！");
            intent.setAction("com.pay.success");
        } else {
            intent.setAction("com.pay.fail");
            showToast("支付失败！");
        }
        sendBroadcast(intent);
        finish();
    }
}