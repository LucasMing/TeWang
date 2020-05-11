package com.qing.tewang.model;

public class Withdraw {

    /**
     * id : 10070
     * create_time : 2018-11-14 00:35:52
     * title : 红包提现
     * money : 2
     * pay_status : 0
     */

    private int id;
    private String create_time;
    private String title;
    private String money;
    private String pay_status;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }
}
