package com.qing.tewang.model;

public class Wallet {

    /**
     * name : admin
     * avatar : null
     * created_at : 2018-09-23 09:04:36
     * mobile : 18638280155
     * balance : 0
     * withdraw_money : 0
     * token : string
     * level : 0
     * level_name : 普通用户
     * openid : xxx
     */

    private String name;
    private Object avatar;
    private String created_at;
    private String mobile;
    private int balance;
    private int withdraw_money;
    private String token;
    private int level;
    private String level_name;
    private String openid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getWithdraw_money() {
        return withdraw_money;
    }

    public void setWithdraw_money(int withdraw_money) {
        this.withdraw_money = withdraw_money;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
