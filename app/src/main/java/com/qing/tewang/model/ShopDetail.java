package com.qing.tewang.model;

import com.google.gson.JsonObject;

import java.util.List;

public class ShopDetail {
    /**
     * id : 58
     * create_time : 2018-11-08 22:05:45
     * update_time : 2018-11-08 22:05:45
     * shop_id : 31
     * content : 挖掘机技术哪家强,中国山东找蓝翔
     * condition : 使用方言,语调缓慢
     * status : 2
     * online_time : 2018-11-08 22:05:39
     * offline_time : 2018-12-08 22:05:39
     * red_packet_money : 48
     * url : http://media.vlean.xyz/201811/fdd1115ac2c3e1dc84ea878082741e1b.mp3
     * is_award : 0
     * award_money : 800
     * voice_sn : 95b2532aff54
     * shop_name : 浙大万朋科技有限公司
     * address : 济南高明区
     * description : 37 竺 Street
     * shop_content : Natus neque ut beatae illo autem exercitationem quia. Nesciunt et sed omnis blanditiis praesentium nobis dolores. Esse rerum ipsa aut rerum voluptatem voluptatem. Eius nulla rem ad. Et rerum facilis cum. Quis sit enim expedita earum enim. Voluptas porro delectus ullam voluptate ipsa est. Expedita earum repellat adipisci et illo ea quisquam. Omnis dolor voluptatem et. Repudiandae tempora dolor eveniet ullam excepturi earum. Ut ab quas amet harum deserunt. Voluptatem architecto sunt omnis et. Sint fuga aspernatur aut. Nobis aspernatur delectus voluptatibus tempora aut nam maiores mollitia. Aperiam quam velit aperiam non blanditiis vitae eum. Sint iste sequi eius dolor dolorem voluptatem magnam inventore. Rerum temporibus neque atque quas temporibus. Accusantium non eum magnam in magni fuga sed reiciendis. Molestiae aut quasi adipisci nesciunt. Deserunt consequatur sed labore sunt ipsa et. Ut quibusdam unde eum exercitationem.
     * img : ["http://media.vlean.xyz/201811/uV9Jq2OnIhz0g7lQiLBiWuFCsGoZZxbIehgKdLSI.jpeg","http://media.vlean.xyz/201811/RiQx6FLKoI4rzLdpHadzWoSJiX9G95nWjwipobNz.jpeg"]
     * contact :
     * shareTitle : 浙大万朋科技有限公司
     * shareDesc : 37 竺 Street
     * shareUrl : http://test.vlean.xyz/shop/31
     * shareImage : http://media.vlean.xyz/201811/uV9Jq2OnIhz0g7lQiLBiWuFCsGoZZxbIehgKdLSI.jpeg
     */

    private int id;
    private String create_time;
    private String update_time;
    private String shop_id;
    private String content;
    private String condition;
    private String status;
    private String online_time;
    private String offline_time;
    private String red_packet_money;
    private String url;
    private int is_award;
    private String award_money;
    private String voice_sn;
    private String shop_name;
    private String address;
    private String description;
    private String shop_content;
    private String contact;
    private String shareTitle;
    private String shareDesc;
    private String shareUrl;
    private String shareImage;
    private List<String> img;
    private PayResult ext;

    public PayResult getExt() {
        return ext;
    }

    public void setExt(PayResult ext) {
        this.ext = ext;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOnline_time() {
        return online_time;
    }

    public void setOnline_time(String online_time) {
        this.online_time = online_time;
    }

    public String getOffline_time() {
        return offline_time;
    }

    public void setOffline_time(String offline_time) {
        this.offline_time = offline_time;
    }

    public String getRed_packet_money() {
        return red_packet_money;
    }

    public void setRed_packet_money(String red_packet_money) {
        this.red_packet_money = red_packet_money;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getVoice_sn() {
        return voice_sn;
    }

    public void setVoice_sn(String voice_sn) {
        this.voice_sn = voice_sn;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShop_content() {
        return shop_content;
    }

    public void setShop_content(String shop_content) {
        this.shop_content = shop_content;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public Voice getVoice() {
        Voice voice = new Voice();
        voice.setImg(getImg());
        voice.setShop_id(getShop_id());
        voice.setAddress(getAddress());
        voice.setShop_name(getShop_name());
        voice.setDescription(getDescription());
        voice.setUrl(getUrl());
        voice.setVoice_sn(getVoice_sn());
        voice.setRed_packet_money(getRed_packet_money());
        return voice;
    }
}
