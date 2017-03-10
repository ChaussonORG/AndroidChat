package com.sudaotech.chatlibrary.model;

/**
 * Created by Samuel on 2016/12/22 17:19
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class Conversation {
    private String conversationId;
    private int chatType;
    private String ext;
    private int unreadCount;

    public Conversation() {
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
