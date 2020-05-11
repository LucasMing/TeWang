package com.qing.tewang.model;

import java.util.List;

public class CollectVoice {

    /**
     * total : 3
     * nextPage : 1
     * list : [{"voice_sn":"8886cc64c14b","shop_id":"80","shop_name":"Armstrong Inc","address":"463 Bette Road\nSterlingtown, TN 96104-6429","content":"Veniam consequatur iste corporis perspiciatis.","url":"http://hao.haolingsheng.com/ring/000/995/fbc33cda344ba43992d3e1b809054280.mp3"},{"voice_sn":"3e79d1ce3e55","shop_id":"94","shop_name":"Zboncak, Casper and Schmitt","address":"643 Tito Brooks Apt. 959\nNew Leopoldo, MI 77643","content":"Corrupti sunt id illo debitis.","url":"http://hao.haolingsheng.com/ring/000/995/fdd1115ac2c3e1dc84ea878082741e1b.mp3"},{"voice_sn":"a9930f3e6cee","shop_id":"71","shop_name":"Emmerich-Zieme","address":"21366 Bahringer Neck\nPort Harley, ID 41096","content":"Eum enim ducimus ea qui qui.","url":"http://hao.haolingsheng.com/ring/000/995/fdd1115ac2c3e1dc84ea878082741e1b.mp3"}]
     */

    private int total;
    private int nextPage;
    private List<Voice> list;

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

    public List<Voice> getList() {
        return list;
    }

    public void setList(List<Voice> list) {
        this.list = list;
    }
}
