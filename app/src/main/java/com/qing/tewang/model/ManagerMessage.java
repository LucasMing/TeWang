package com.qing.tewang.model;

import java.util.List;

public class ManagerMessage {
    /**
     * list : [{"id":6,"message":"留言测试","created_at":"2018-12-02 12:25:45"}]
     * nextPage : 1
     * total : 1
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
         * id : 6
         * message : 留言测试
         * created_at : 2018-12-02 12:25:45
         */

        private int id;
        private String message;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
