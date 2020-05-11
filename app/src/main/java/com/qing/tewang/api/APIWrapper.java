package com.qing.tewang.api;

import android.content.Context;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.qing.tewang.app.MyApplication;
import com.qing.tewang.model.CollectVoice;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.Cover;
import com.qing.tewang.model.HomeAd;
import com.qing.tewang.model.Location;
import com.qing.tewang.model.ManagerMessage;
import com.qing.tewang.model.MessageList;
import com.qing.tewang.model.MessageResult;
import com.qing.tewang.model.MyShop;
import com.qing.tewang.model.OrderResult;
import com.qing.tewang.model.RecorderResult;
import com.qing.tewang.model.ShopDetail;
import com.qing.tewang.model.User;
import com.qing.tewang.model.UserRecordList;
import com.qing.tewang.model.Voice;
import com.qing.tewang.model.Wallet;
import com.qing.tewang.model.Withdraw;
import com.qing.tewang.util.APIUtilImpl;
import com.qing.tewang.util.AppUtil;
import com.qing.tewang.util.FileUtils;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by wuliao on 2018/4/19.
 */

public class APIWrapper {

    private static AppUtil API_UTIL = new APIUtilImpl();

    public static Observable<CommonData<User>> login(String mobile, String password) {

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("password", password);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .login(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public static Observable<JsonObject> sendCode(String mobile, String templateId) {

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("templateId", templateId);
        params.put("deviceId", JPushInterface.getRegistrationID(MyApplication.getInstance()));

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .sendCode(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public static Observable<CommonData> sendFeedBack(String message) {

        Map<String, Object> params = new HashMap<>();
        params.put("content", message);
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("deviceId", JPushInterface.getRegistrationID(MyApplication.getInstance()));

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .sendFeedBack(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<CommonData> sendComment(String message, String shopId) {

        Map<String, Object> params = new HashMap<>();
        params.put("content", message);
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("deviceId", JPushInterface.getRegistrationID(MyApplication.getInstance()));
        params.put("shopId", shopId);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .sendFeedBack(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<List<Voice>>> getNearVoice(double lat, double lng) {
        Map<String, Object> params = new HashMap<>();
        params.put("lat", lat);
        params.put("lng", lng);
        if (SPUtils.isLogin(MyApplication.getInstance())) {
            params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        }

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getNearVoice(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public static Observable<JsonObject> getWeChatToken(String loginUrl) {
        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getWeChatToken(loginUrl);

    }

    public static JsonObject getWeChatUserInfo(String url) {

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getWeChatUserInfo(url);


    }

    public static Observable<JsonObject> register(String mobile, String code, String pwd, String inviteId) {

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("password", pwd);
        params.put("deviceId", JPushInterface.getRegistrationID(MyApplication.getInstance()));

        if (!TextUtils.isEmpty(inviteId)) {
            params.put("inviteId", inviteId);
        }
        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .register(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<MessageResult>> getUnReadMessage() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getUnReadMessage(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void bindDevice(Context context) {
        User user = SPUtils.getUser(context);

        String token = user.getToken();
        String pushId = JPushInterface.getRegistrationID(context);

        if (user == null || TextUtils.isEmpty(pushId)) {
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("deviceId", pushId);


        MarketRetrofit
                .getsInstance()
                .getMarketService()
                .bindDevice(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();


    }

    public static Observable<JsonObject> getVoiceDetail(String sn) {

        Map<String, Object> params = new HashMap<>();
        params.put("voiceSn", sn);


        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getVoiceDetail(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<JsonObject> collect(String sn, int status) {

        Map<String, Object> params = new HashMap<>();
        params.put("voiceSn", sn);
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("status", status);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .collect(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<CommonData<CollectVoice>> getCollect(int page, String kw) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("page", page);
        if (!TextUtils.isEmpty(kw)) {
            params.put("kw", kw);
        }

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getCollect(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<List<HomeAd>>> getHomeAd() {

        Map<String, Object> params = new HashMap<>();

        Location location = SPUtils.getLocation(MyApplication.getInstance());
        if (location != null) {
            params.put("cityId", location.getCityId());
            params.put("areaId", location.getAreaId());
        }

        if (SPUtils.isLogin(MyApplication.getInstance())) {
            params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        }

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getHomeAd(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<List<Cover>>> getStartAd(int cityId, int areaId) {
        Map<String, Object> params = new HashMap<>();
        params.put("cityId", cityId);
        params.put("areaId", areaId);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getStartAd(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<ResponseBody> downloadFile(String uri) {
        return MarketRetrofit.getsInstance()
                .getMarketService()
                .downloadFile(uri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<File> downloadAndGet(String uri) {
        return MarketRetrofit.getsInstance()
                .getMarketService()
                .downloadFile(uri)
                .map(body -> {
                    File file = new File(FileUtils.getPackageFile("temp"), uri.replaceAll("/", ""));
                    FileUtils.writeResponseBodyToDisk(body, file);
                    return file;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<CommonData<Wallet>> getWallet() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getWallet(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<User>> updateUser() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .updateUser(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<MessageList>> getSystemMessage(int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("page", page);
        params.put("type", 2);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getMessage(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<MessageList>> getMoneyMessage(int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("type", 1);
        params.put("page", page);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getMessage(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<String>> readMessage() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("type", 1);

        return null;
    }

    public static Observable<CommonData<JsonObject>> getMoney(String sn) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("voiceSn", sn);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getMoney(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<JsonObject>> getShopDetail(String shopId) {
        Map<String, Object> params = new HashMap<>();
        if (SPUtils.isLogin(MyApplication.getInstance())) {
            params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        }
        params.put("shopId", shopId);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getShopDetail(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<ShopDetail>> getVoiceById(String sn) {
        Map<String, Object> params = new HashMap<>();
        if (SPUtils.isLogin(MyApplication.getInstance())) {
            params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        }
        params.put("voiceSn", sn);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getVoiceById(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<JsonObject> readMoneyMsg() {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("type", 1);
        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .readAllMsg(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<JsonObject> readSystemMsg() {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("type", 2);
        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .readAllMsg(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData> withdraw() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .withdraw(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<JsonObject>> bindOpenId() {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .bindOpenId(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public static Observable<CommonData<List<Withdraw>>> withdrawOrders() {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .withdrawOrders(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<JsonObject> resetPwd(String mobile, String code, String pwd) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("password", pwd);
        params.put("deviceId", JPushInterface.getRegistrationID(MyApplication.getInstance()));

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .resetPwd(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<Wallet>> getUserInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getWallet(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData> updateEmployee(String address, String mobile) {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("level", 4);
        params.put("address", address);
        params.put("mobile", mobile);


        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .updateEmployee(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<CommonData> updateShop(String address, String mobile) {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("level", 3);
        params.put("address", address);
        params.put("mobile", mobile);


        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .updateEmployee(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<CommonData> uploadFile(String path, String sn) {

        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"),
                SPUtils.getUser(MyApplication.getInstance()).getToken()));
        params.put("voiceSn", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), sn));

        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        params.put("file\"; filename=\"" + file.getName() + "", requestFile);


        return MarketRetrofit.getsInstance()
                .getMarketService()
                .uploadFile(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<CommonData<JsonObject>> updateAvatar(String avatarPath) {
        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"),
                SPUtils.getUser(MyApplication.getInstance()).getToken()));

        File file = new File(avatarPath);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        params.put("avatar\"; filename=\"" + file.getName() + "", requestFile);


        return MarketRetrofit.getsInstance()
                .getMarketService()
                .updateAvatar(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<User>> updateName(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("name", name);


        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .updateName(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<List<RecorderResult>>> getRecorder() {
        Map<String, Object> params = new HashMap<>();
        if (SPUtils.isLogin(MyApplication.getInstance())) {
            params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        }

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getRecorder(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public static Observable<CommonData<JsonObject>> updateCommonFile(String filePath) {
        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"),
                SPUtils.getUser(MyApplication.getInstance()).getToken()));

        File file = new File(filePath);

        file = FileUtils.getCompressImage(file.getAbsolutePath());

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        params.put("file\"; filename=\"" + file.getName() + "", requestFile);


        return MarketRetrofit.getsInstance()
                .getMarketService()
                .uploadCommonFile(params);
    }


    public static Observable<CommonData<JsonObject>> updateWavFile(String filePath) {
        Map<String, RequestBody> params = new HashMap<>();
        params.put("token", RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"),
                SPUtils.getUser(MyApplication.getInstance()).getToken()));

        File file = new File(filePath);

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        params.put("file\"; filename=\"" + file.getName() + "", requestFile);


        return MarketRetrofit.getsInstance()
                .getMarketService()
                .uploadCommonFile(params);
    }

    public static Observable<CommonData<MyShop>> getMyShop() {

        Map<String, Object> params = new HashMap<>();
        if (SPUtils.isLogin(MyApplication.getInstance())) {
            params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        }

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getMyShop(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<CommonData<JsonObject>> recharge(int money) {
        Map<String, Object> params = new HashMap<>();
        if (SPUtils.isLogin(MyApplication.getInstance())) {
            params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        }
        params.put("money", money);


        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .recharge(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<CommonData> updateShopDetail(String name, String contact,
                                                          String jianjie, String des,
                                                          String zone, String address,
                                                          List<String> imgs, List<String> tags, String ext) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("shop_name", name);
        params.put("address", address);
        params.put("contact", contact);
        params.put("zone", zone);
        params.put("img", StringUtils.join(imgs));
        params.put("description", jianjie);
        params.put("content", des);
        if (tags.size() > 0) {
            params.put("tags", StringUtils.join(tags));
        }
        if (!TextUtils.isEmpty(ext)) {
            params.put("ext", ext);
        }

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .updateShopDetail(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<CommonData> updateVoice(int redNum, String voice_url) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("voice_url", voice_url);
        params.put("red_packet_money", redNum);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .updateVoice(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    public static Observable<CommonData> updateVoiceAndActivity(int redNum, String voice_url,
                                                                String content, String condition, String money) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("voice_url", voice_url);
        params.put("red_packet_money", redNum);

        params.put("condition", condition);
        params.put("voiceContent", content);
        int award = (int) (Float.parseFloat(money) * 100);
        params.put("award_money", award);
        params.put("is_award", 1);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .updateVoiceAndActivity(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<CommonData<JsonObject>> getShopMoney() {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getShopMoney(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData> updateLevel() {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .updateLevel(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<UserRecordList>> getUserRecords(int page) {


        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("page", page);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getUserRecords(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<CommonData> chooseRecord(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("id", id);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .chooseRecord(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<List<String>> getTag() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getTag(API_UTIL.signParam(params))
                .map(data -> {
                    if (data.getErrno() == 0) {
                        List<String> tags = new Gson().fromJson(data.getData()
                                        .get("tags")
                                        .getAsJsonArray(),
                                new TypeToken<List<String>>() {
                                }.getType());
                        return tags;
                    } else {
                        return new ArrayList<String>();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<List<String>> getPackets() {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getTag(API_UTIL.signParam(params))
                .map(data -> {
                    if (data.getErrno() == 0) {
                        List<String> tags = new Gson().fromJson(data.getData()
                                        .get("packets")
                                        .getAsJsonArray(),
                                new TypeToken<List<String>>() {
                                }.getType());
                        return tags;
                    } else {
                        return new ArrayList<String>();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<CommonData> getPacketOrder(int page) {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("page", page);
        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getPacketOrder(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<OrderResult>> getOrder(int page) {

        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("page", page);
        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getOrder(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<CommonData<Location>> getLocation(double lat, double lng) {

        Map<String, Object> params = new HashMap<>();
        params.put("lat", lat);
        params.put("lng", lng);
        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getLocation(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    public static Observable<CommonData<ManagerMessage>> getComments(int page) {
        Map<String, Object> params = new HashMap<>();
        params.put("token", SPUtils.getUser(MyApplication.getInstance()).getToken());
        params.put("page", page);

        return MarketRetrofit
                .getsInstance()
                .getMarketService()
                .getComments(API_UTIL.signParam(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
