package com.qing.tewang.api;


import com.google.gson.JsonObject;
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

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by wuliao on 2018/3/22.
 */

public interface MarketApi {

    @FormUrlEncoded
    @POST("user/login/")
    Observable<CommonData<User>> login(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("user/sendCode/")
    Observable<JsonObject> sendCode(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("voice/near/")
    Observable<CommonData<List<Voice>>> getNearVoice(@FieldMap Map<String, Object> params);

    @GET
    Observable<JsonObject> getWeChatToken(@Url String loginUrl);

    @GET
    JsonObject getWeChatUserInfo(@Url String loginUrl);

    @FormUrlEncoded
    @POST("user/register")
    Observable<JsonObject> register(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("message/lastInfo")
    Observable<CommonData<MessageResult>> getUnReadMessage(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/upInfo")
    Observable<JsonObject> bindDevice(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("voice/detail")
    Observable<JsonObject> getVoiceDetail(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("voice/like")
    Observable<JsonObject> collect(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("voice/likeList")
    Observable<CommonData<CollectVoice>> getCollect(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/homeAd")
    Observable<CommonData<List<HomeAd>>> getHomeAd(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/startAd")
    Observable<CommonData<List<Cover>>> getStartAd(@FieldMap Map<String, Object> params);

    @GET
    Observable<ResponseBody> downloadFile(@Url String url);

    @FormUrlEncoded
    @POST("user/info")
    Observable<CommonData<Wallet>> getWallet(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/info")
    Observable<CommonData<User>> updateUser(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("message/list")
    Observable<CommonData<MessageList>> getMessage(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("voice/redPacket")
    Observable<CommonData<JsonObject>> getMoney(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("shop/detail")
    Observable<CommonData<JsonObject>> getShopDetail(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("voice/detail")
    Observable<CommonData<ShopDetail>> getVoiceById(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("message/readAll")
    Observable<JsonObject> readAllMsg(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/withDrawMoney")
    Observable<CommonData> withdraw(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/bindOpenidCode")
    Observable<CommonData<JsonObject>> bindOpenId(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/withDrawOrders")
    Observable<CommonData<List<Withdraw>>> withdrawOrders(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/resetPwd")
    Observable<JsonObject> resetPwd(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/upLevel")
    Observable<CommonData> updateEmployee(@FieldMap Map<String, Object> params);

    @Multipart
    @POST("voice/record")
    Observable<CommonData> uploadFile(@PartMap Map<String, RequestBody> data);

    @Multipart
    @POST("user/avatar")
    Observable<CommonData<JsonObject>> updateAvatar(@PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("user/updateInfo")
    Observable<CommonData<User>> updateName(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("voice/awards")
    Observable<CommonData<List<RecorderResult>>> getRecorder(@FieldMap Map<String, Object> params);


    @Multipart
    @POST("app/uploadFile")
    Observable<CommonData<JsonObject>> uploadCommonFile(@PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("shop/my")
    Observable<CommonData<MyShop>> getMyShop(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/recharge")
    Observable<CommonData<JsonObject>> recharge(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("shop/updateShop")
    Observable<CommonData> updateShopDetail(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("shop/updateVoice")
    Observable<CommonData> updateVoice(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("shop/updateVoice")
    Observable<CommonData> updateVoiceAndActivity(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("shop/stats")
    Observable<CommonData<JsonObject>> getShopMoney(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("user/upLevel")
    Observable<CommonData> updateLevel(@FieldMap Map<String, Object> params);


    @FormUrlEncoded
    @POST("voice/userRecords")
    Observable<CommonData<UserRecordList>> getUserRecords(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("voice/chooseRecord")
    Observable<CommonData> chooseRecord(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/feedback")
    Observable<CommonData> sendFeedBack(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/config")
    Observable<CommonData<JsonObject>> getTag(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("shop/redPacketOrders")
    Observable<CommonData> getPacketOrder(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("shop/orders")
    Observable<CommonData<OrderResult>> getOrder(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("app/location")
    Observable<CommonData<Location>> getLocation(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("shop/feedbacks")
    Observable<CommonData<ManagerMessage>> getComments(@FieldMap Map<String, Object> params);
}
