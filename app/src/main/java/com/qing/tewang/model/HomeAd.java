package com.qing.tewang.model;

public class HomeAd {

    /**
     * type : 1
     * title : 测试标题-488
     * url : http://www.baidu.com
     * stay_time : 3
     * voice_sn : 8546
     * img : null
     * expired_time : 2019-08-26 11:57:44
     */

    private String type;
    private String title;
    private String url;
    private String stay_time;
    private String voice_sn;
    private Object img;
    private String expired_time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStay_time() {
        return stay_time;
    }

    public void setStay_time(String stay_time) {
        this.stay_time = stay_time;
    }

    public String getVoice_sn() {
        return voice_sn;
    }

    public void setVoice_sn(String voice_sn) {
        this.voice_sn = voice_sn;
    }

    public Object getImg() {
        return img;
    }

    public void setImg(Object img) {
        this.img = img;
    }

    public String getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(String expired_time) {
        this.expired_time = expired_time;
    }
}
