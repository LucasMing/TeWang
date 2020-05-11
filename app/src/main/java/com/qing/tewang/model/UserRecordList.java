package com.qing.tewang.model;

import java.util.List;

public class UserRecordList {

    /**
     * list : [{"voice_url":"http://media.vlean.xyz/201811/Hgo1Qx134JIjJe6yScuMaNMBi9WmplvTx3nmVOeK.wav","created_at":"2018-11-13 00:16:12","status":"0","name":"wuliao123","id":1},{"voice_url":"http://media.vlean.xyz/201811/bKYGlZWV3M4hvloghnpbyAiZ6gffBw2Di1DHs12D.wav","created_at":"2018-11-17 19:19:46","status":"0","name":"匿名用户","id":2},{"voice_url":"http://media.vlean.xyz/201811/ghEeUrkzXlB3C4Hd2jWGEg0QXLt8YKIdcoYRRou4.wav","created_at":"2018-11-17 19:19:57","status":"0","name":"匿名用户","id":3},{"voice_url":"http://media.vlean.xyz/201811/q3AHr9tNn5mo7YoaMHWtucLrJfD25NX31p5ldtah.wav","created_at":"2018-11-18 02:55:03","status":"0","name":"哎 嘿","id":4}]
     * nextPage : 1
     * total : 4
     */

    private String nextPage;
    private int total;
    private List<ListBean> list;

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
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
         * voice_url : http://media.vlean.xyz/201811/Hgo1Qx134JIjJe6yScuMaNMBi9WmplvTx3nmVOeK.wav
         * created_at : 2018-11-13 00:16:12
         * status : 0
         * name : wuliao123
         * id : 1
         */

        private String voice_url;
        private String created_at;
        private int status;
        private String name;
        private int id;

        public String getVoice_url() {
            return voice_url;
        }

        public void setVoice_url(String voice_url) {
            this.voice_url = voice_url;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
