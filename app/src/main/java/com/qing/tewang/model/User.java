package com.qing.tewang.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class User implements Parcelable {

    public int getRed_packet_num() {
        return red_packet_num;
    }

    public void setRed_packet_num(int red_packet_num) {
        this.red_packet_num = red_packet_num;
    }

    /**
     * id : 12
     * username : 18638280159
     * name : 18638280159
     * avatar : null
     * <p>
     * created_at : 2018-10-29 06:47:53
     * updated_at : 2018-10-30 04:29:10
     * mobile : 18638280159
     * balance : 0
     * withdraw_money : 0
     * invite_user_id : 1
     * openid : null
     * level : 2
     * level_name : 普通用户
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC90ZXN0LnZsZWFuLnh5elwvYXBpXC91c2VyXC9sb2dpbiIsImlhdCI6MTU0MDg3Mzc3MiwiZXhwIjoxNTQxMDg5NzcyLCJuYmYiOjE1NDA4NzM3NzIsImp0aSI6IkRSaGYxYjJnZGJKSDlta3YiLCJzdWIiOjEyLCJwcnYiOiI4N2UwYWYxZWY5ZmQxNTgxMmZkZWM5NzE1M2ExNGUwYjA0NzU0NmFhIn0.or1U9fIOV6hLbxW836iDHbRcMl7KYxR-D6m4HfQhIuw
     */

    private int id;
    private String username;
    private String name;
    private String avatar;
    private String created_at;
    private String updated_at;
    private String mobile;
    private String balance;
    private String withdraw_money;
    private String invite_user_id;
    private String openid;
    private String level;
    private String level_name;
    private String token;
    private int red_packet_num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getWithdraw_money() {
        return withdraw_money;
    }

    public void setWithdraw_money(String withdraw_money) {
        this.withdraw_money = withdraw_money;
    }

    public String getInvite_user_id() {
        return invite_user_id;
    }

    public void setInvite_user_id(String invite_user_id) {
        this.invite_user_id = invite_user_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.red_packet_num);
        dest.writeString(this.username);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.mobile);
        dest.writeString(this.balance);
        dest.writeString(this.withdraw_money);
        dest.writeString(this.invite_user_id);
        dest.writeString(this.openid);
        dest.writeString(this.level);
        dest.writeString(this.level_name);
        dest.writeString(this.token);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.red_packet_num = in.readInt();
        this.username = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.mobile = in.readString();
        this.balance = in.readString();
        this.withdraw_money = in.readString();
        this.invite_user_id = in.readString();
        this.openid = in.readString();
        this.level = in.readString();
        this.level_name = in.readString();
        this.token = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
