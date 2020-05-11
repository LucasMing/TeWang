package com.qing.tewang.wxapi;

import android.os.Bundle;
import android.view.WindowManager;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.MarketRetrofit;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.ui.BaseActivity;
import com.qing.tewang.util.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI iwxapi;
    private String unionid;
    private String openid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);

        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi = WXAPIFactory.createWXAPI(this, "wxbf5b9f97d7d03572", true);
        iwxapi.registerApp("wxbf5b9f97d7d03572");
        iwxapi.handleIntent(getIntent(), this);


    }

    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.e("jiang...", "baseReq");
    }

    @Override
    public void onResp(BaseResp baseResp) {



        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
//                String code = ((SendAuth.Resp) baseResp).code;
                //获取用户信息
//                getAccessToken(code);
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    private void getAccessToken(String code) {
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=")
                .append("wxbf5b9f97d7d03572")
                .append("&secret=")
                .append("")
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");

        APIWrapper.
                getWeChatToken(loginUrl.toString())
                .map(json -> {

                    String access = json.get("access_token").getAsString();
                    String openId = json.get("openid").getAsString();

                    String getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" + openId;
                    return APIWrapper.
                            getWeChatUserInfo(getUserInfo);

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<JsonObject>(getActivity()) {
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        finish();
                        String nickName = null;
                        String sex = null;
                        String city = null;
                        String province = null;
                        String country = null;
                        String headimgurl = null;

                        openid = jsonObject.get("openid").getAsString();
                        nickName = jsonObject.get("nickname").getAsString();
                        sex = jsonObject.get("sex").getAsString();
                        city = jsonObject.get("city").getAsString();
                        province = jsonObject.get("province").getAsString();
                        country = jsonObject.get("country").getAsString();
                        headimgurl = jsonObject.get("headimgurl").getAsString();
                        unionid = jsonObject.get("unionid").getAsString();

                    }
                });


    }
}
