package com.qing.tewang.model;

import java.util.List;

public class OrderResult {

    /**
     * list : [{"pay_time":"2018-11-18 14:40:59","create_time":"2018-11-18 14:40:59","pay_status":"1","title":"录用语音奖励","pay_price":"120","money":"120","type":"5"},{"pay_time":null,"create_time":"2018-11-16 16:32:36","pay_status":"0","title":"特网用户充值","pay_price":"0","money":"1","type":"2"},{"pay_time":null,"create_time":"2018-11-14 00:35:52","pay_status":"0","title":"红包提现","pay_price":"0","money":"2","type":"4"},{"pay_time":null,"create_time":"2018-11-14 00:35:23","pay_status":"0","title":"红包提现","pay_price":"0","money":"2","type":"4"},{"pay_time":null,"create_time":"2018-11-14 00:31:19","pay_status":"0","title":"红包提现","pay_price":"0","money":"100","type":"4"},{"pay_time":null,"create_time":"2018-11-14 00:31:01","pay_status":"0","title":"红包提现","pay_price":"0","money":"100","type":"4"},{"pay_time":null,"create_time":"2018-11-14 00:30:16","pay_status":"0","title":"红包提现","pay_price":"0","money":"100","type":"4"},{"pay_time":null,"create_time":"2018-11-14 00:29:40","pay_status":"0","title":"红包提现","pay_price":"0","money":"4729","type":"4"}]
     * nextPage : 1
     * total : 8
     */

    private int nextPage;
    private int total;
    private List<ListBean> list;

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * pay_time : 2018-11-18 14:40:59
         * create_time : 2018-11-18 14:40:59
         * pay_status : 1
         * title : 录用语音奖励
         * pay_price : 120
         * money : 120
         * type : 5
         */

        private String pay_time;
        private String create_time;
        private String pay_status;
        private String title;
        private String pay_price;
        private String money;
        private String type;

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPay_price() {
            return pay_price;
        }

        public void setPay_price(String pay_price) {
            this.pay_price = pay_price;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
