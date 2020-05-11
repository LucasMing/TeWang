package com.qing.tewang.model;

import java.util.List;

public class RecorderResult {


    /**
     * content : At dolores est et debitis quos voluptatem.
     * condition : Quis qui sed et nostrum.
     * voice_sn : f4c0513030b1
     * award_money : 2600
     * address : 乌鲁木齐高港区
     * shop_id : 23
     * shop_name : 超艺科技有限公司
     * img : ["http://media.vlean.xyz/201811/uV9Jq2OnIhz0g7lQiLBiWuFCsGoZZxbIehgKdLSI.jpeg","http://media.vlean.xyz/201811/RiQx6FLKoI4rzLdpHadzWoSJiX9G95nWjwipobNz.jpeg"]
     * description : 55 闵 Street
     * distance : 12949828.650289224
     */

    private String content;
    private String condition;
    private String voice_sn;
    private String award_money;
    private String address;
    private String shop_id;
    private String shop_name;
    private String description;
    private String distance;
    private List<String> img;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getVoice_sn() {
        return voice_sn;
    }

    public void setVoice_sn(String voice_sn) {
        this.voice_sn = voice_sn;
    }

    public String getAward_money() {
        return award_money;
    }

    public void setAward_money(String award_money) {
        this.award_money = award_money;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }
}
