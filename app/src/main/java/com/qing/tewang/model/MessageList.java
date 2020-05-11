package com.qing.tewang.model;

import java.util.List;

public class MessageList {
    /**
     * total : 56
     * list : [{"title":"红包消息","content":"您于10月31日收到Armstrong Inc的1.00元红包，已放入余额账户","created_at":"2018-10-31 13:02:09"},{"title":"红包消息","content":"您于01月01日收到的5.00元红包，已放入余额账户","created_at":null},{"title":"红包消息","content":"您于10月31日收到Towne PLC的4.00元红包，已放入余额账户","created_at":"2018-10-31 13:01:39"},{"title":"红包消息","content":"您于10月31日收到Nicolas-Stanton的5.00元红包，已放入余额账户","created_at":"2018-10-31 12:53:50"},{"title":"红包消息","content":"您于10月31日收到Hintz-Howell的4.00元红包，已放入余额账户","created_at":"2018-10-31 13:01:22"},{"title":"红包消息","content":"您于10月31日收到Harber-Stehr的1.00元红包，已放入余额账户","created_at":"2018-10-31 13:00:49"},{"title":"红包消息","content":"您于10月31日收到Durgan Group的8.00元红包，已放入余额账户","created_at":"2018-10-31 13:02:08"},{"title":"红包消息","content":"您于10月31日收到Johns Inc的3.00元红包，已放入余额账户","created_at":"2018-10-31 13:01:49"},{"title":"红包消息","content":"您于10月31日收到Rohan, Huel and Wolff的6.00元红包，已放入余额账户","created_at":"2018-10-31 13:01:49"},{"title":"红包消息","content":"您于10月31日收到Stracke Ltd的2.00元红包，已放入余额账户","created_at":"2018-10-31 13:01:38"}]
     * nextPage : 2
     */

    private int total;
    private int nextPage;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * title : 红包消息
         * content : 您于10月31日收到Armstrong Inc的1.00元红包，已放入余额账户
         * created_at : 2018-10-31 13:02:09
         */

        private String title;
        private String content;
        private String created_at;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
