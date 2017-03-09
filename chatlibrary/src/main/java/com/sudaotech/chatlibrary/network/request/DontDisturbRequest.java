package com.sudaotech.chatlibrary.network.request;

/**
 * Created by Samuel on 2017/1/17 18:34
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class DontDisturbRequest {
//
//    /**
//     * chatGroupId : 1
//     * chatUserId : 140
//     */
//
//    private long chatGroupId;
//    private long chatUserId;
//
//    public DontDisturbRequest() {
//    }
//
//    public DontDisturbRequest(long chatGroupId, long chatUserId) {
//        this.chatGroupId = chatGroupId;
//        this.chatUserId = chatUserId;
//    }
//
//    public long getChatGroupId() {
//        return chatGroupId;
//    }
//
//    public void setChatGroupId(long chatGroupId) {
//        this.chatGroupId = chatGroupId;
//    }
//
//    public long getChatUserId() {
//        return chatUserId;
//    }
//
//    public void setChatUserId(long chatUserId) {
//        this.chatUserId = chatUserId;
//    }

    private String disturbStatus;

    public DontDisturbRequest(String disturbStatus) {
        this.disturbStatus = disturbStatus;
    }

    public String getDisturbStatus() {
        return disturbStatus;
    }

    public void setDisturbStatus(String disturbStatus) {
        this.disturbStatus = disturbStatus;
    }
}
