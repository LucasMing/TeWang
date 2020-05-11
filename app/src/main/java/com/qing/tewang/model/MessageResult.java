package com.qing.tewang.model;

public class MessageResult {

    /**
     * unReadNum : 0
     * systemMessage : Est earum dolor eos veritatis in cumque saepe. Optio a aut aut quia porro minima.
     * redPackMessage : 您于10月31日收到Armstrong Inc的1.00元红包，已放入余额账户
     */

    private int unReadNum;
    private int unReadRedPacketNum;

    public int getUnReadRedPacketNum() {
        return unReadRedPacketNum;
    }

    public void setUnReadRedPacketNum(int unReadRedPacketNum) {
        this.unReadRedPacketNum = unReadRedPacketNum;
    }

    private String systemMessage;
    private String redPackMessage;

    public int getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(int unReadNum) {
        this.unReadNum = unReadNum;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public String getRedPackMessage() {
        return redPackMessage;
    }

    public void setRedPackMessage(String redPackMessage) {
        this.redPackMessage = redPackMessage;
    }
}
