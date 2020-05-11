package com.qing.tewang.model;

import com.google.gson.JsonObject;

import java.util.List;

public class MyShop {


    /**
     * shop_name : 测试
     * img : ["http://media.vlean.xyz/201811/f9aXFGMPiHsZ6zwL8KhkVCQ2eC786RiecI3t68Jh.png"]
     * id : 127
     * description : 测试
     * address : 测试？
     * status : 0
     * contact : 12345678900
     * content : 测试
     * zone : 北京东城区
     * url : http://test.vlean.xyz/shop/127
     * shareTitle : 测试
     * shareDesc : 测试
     * shareUrl : http://test.vlean.xyz/shop/127
     * shareImage : http://media.vlean.xyz/201811/f9aXFGMPiHsZ6zwL8KhkVCQ2eC786RiecI3t68Jh.png
     * hasVoice : 1
     * voice_sn : lrrMX0Of9rpUysqw
     * voice_content :
     * condition :
     * red_packet_money : 12
     * voice_url : http://media.vlean.xyz/
     * is_award : 0
     * award_money : 0
     */

    private String shop_name;
    private int id;
    private String description;
    private String address;
    private String status;
    private String contact;
    private String content;
    private String zone;
    private String url;
    private String shareTitle;
    private String shareDesc;
    private String shareUrl;
    private String shareImage;
    private int hasVoice;
    private String voice_sn;
    private String voiceContent;
    private String condition;
    private String red_packet_money;
    private String voice_url;
    private int is_award;
    private int award_money;
    private List<String> img;
    private List<String> tags;
    private PayResult ext;

    public PayResult getExt() {
        return ext;
    }

    public void setExt(PayResult ext) {
        this.ext = ext;
    }

    public String getVoiceContent() {
        return voiceContent;
    }

    public void setVoiceContent(String voiceContent) {
        this.voiceContent = voiceContent;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareImage() {
        return shareImage;
    }

    public void setShareImage(String shareImage) {
        this.shareImage = shareImage;
    }

    public int getHasVoice() {
        return hasVoice;
    }

    public void setHasVoice(int hasVoice) {
        this.hasVoice = hasVoice;
    }

    public String getVoice_sn() {
        return voice_sn;
    }

    public void setVoice_sn(String voice_sn) {
        this.voice_sn = voice_sn;
    }

    public String getVoice_content() {
        return voiceContent;
    }

    public void setVoice_content(String voice_content) {
        this.voiceContent = voice_content;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getRed_packet_money() {
        return red_packet_money;
    }

    public void setRed_packet_money(String red_packet_money) {
        this.red_packet_money = red_packet_money;
    }

    public String getVoice_url() {
        return voice_url;
    }

    public void setVoice_url(String voice_url) {
        this.voice_url = voice_url;
    }

    public int getIs_award() {
        return is_award;
    }

    public void setIs_award(int is_award) {
        this.is_award = is_award;
    }

    public int getAward_money() {
        return award_money;
    }

    public void setAward_money(int award_money) {
        this.award_money = award_money;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }
}
