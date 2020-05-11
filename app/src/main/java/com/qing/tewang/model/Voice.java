package com.qing.tewang.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Voice implements Parcelable {


    /**
     * distance : 5909.3409762454385
     * url : 201811/fdd1115ac2c3e1dc84ea878082741e1b.mp3
     * voice_id : 126
     * is_award : 1
     * award_money : 1800
     * red_packet_money : 100
     * shop_id : 48
     * shop_name : 迪摩科技有限公司
     * description : 58 曹 Street
     * img : ["https://lorempixel.com/200/200/?74319"]
     * address : 福州清城区
     * voice_sn : 625bc8bed137
     * more : 1
     * is_collect : 0
     * shareTitle : 迪摩科技有限公司
     * shareDesc : 58 曹 Street
     * shareImg : https://lorempixel.com/200/200/?74319
     * shopUrl : http://tw.com/shop/48
     * shareUrl : http://tw.com/shop/48
     */

    private String distance;
    private String url;
    private String voice_id;
    private int is_award;
    private String award_money;
    private String red_packet_money;
    private String shop_id;
    private String shop_name;
    private String description;
    private String address;
    private String voice_sn;
    private int more;
    private int is_collect;
    private String shareTitle;
    private String shareDesc;
    private String shareImg;
    private String shopUrl;
    private String shareUrl;
    private List<String> img;
    private List<String> tags;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVoice_id() {
        return voice_id;
    }

    public void setVoice_id(String voice_id) {
        this.voice_id = voice_id;
    }

    public int getIs_award() {
        return is_award;
    }

    public void setIs_award(int is_award) {
        this.is_award = is_award;
    }

    public String getAward_money() {
        return award_money;
    }

    public void setAward_money(String award_money) {
        this.award_money = award_money;
    }

    public String getRed_packet_money() {
        return red_packet_money;
    }

    public void setRed_packet_money(String red_packet_money) {
        this.red_packet_money = red_packet_money;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVoice_sn() {
        return voice_sn;
    }

    public void setVoice_sn(String voice_sn) {
        this.voice_sn = voice_sn;
    }

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.distance);
        dest.writeString(this.url);
        dest.writeString(this.voice_id);
        dest.writeInt(this.is_award);
        dest.writeString(this.award_money);
        dest.writeString(this.red_packet_money);
        dest.writeString(this.shop_id);
        dest.writeString(this.shop_name);
        dest.writeString(this.description);
        dest.writeString(this.address);
        dest.writeString(this.voice_sn);
        dest.writeInt(this.more);
        dest.writeInt(this.is_collect);
        dest.writeString(this.shareTitle);
        dest.writeString(this.shareDesc);
        dest.writeString(this.shareImg);
        dest.writeString(this.shopUrl);
        dest.writeString(this.shareUrl);
        dest.writeStringList(this.img);
    }

    public Voice() {
    }

    protected Voice(Parcel in) {
        this.distance = in.readString();
        this.url = in.readString();
        this.voice_id = in.readString();
        this.is_award = in.readInt();
        this.award_money = in.readString();
        this.red_packet_money = in.readString();
        this.shop_id = in.readString();
        this.shop_name = in.readString();
        this.description = in.readString();
        this.address = in.readString();
        this.voice_sn = in.readString();
        this.more = in.readInt();
        this.is_collect = in.readInt();
        this.shareTitle = in.readString();
        this.shareDesc = in.readString();
        this.shareImg = in.readString();
        this.shopUrl = in.readString();
        this.shareUrl = in.readString();
        this.img = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Voice> CREATOR = new Parcelable.Creator<Voice>() {
        @Override
        public Voice createFromParcel(Parcel source) {
            return new Voice(source);
        }

        @Override
        public Voice[] newArray(int size) {
            return new Voice[size];
        }
    };
}
