package com.sudaotech.chatlibrary.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Samuel on 2016/12/14 10:14
 * Email:xuzhou40@gmail.com
 * desc:导入数据库的类型
 */

public class ChatMessage extends RealmObject {
    @PrimaryKey
    private long messageId;//消息id
    private long conversationId;//会话id
    private String messageType;//消息类型，text，image
    private long messageTime;//消息时间(服务器时间)
    private long showTime;//消息显示时间(本地时间)
    private long senderId;//发送者id
    private long receiverId;//接收者id
    //    private String messageTarget;//聊天类型:单聊，群聊
    private int chatType;//聊天类型:单聊，群聊
    private long chatGroupId;//群聊id
    private String extMap;


    private int owner;//消息归属(1:自己 0:别人)
    private String messageBody;//消息体(存储json)
    private int sendingState;//消息发送状态(0:create,1:success,2:fail,3:inprogress)

    private String photo;//用户头像
    private String nickName;//用户昵称
    private int isRead;//是否已读
    private int isDelivered;//是否送达

    public ChatMessage() {
    }

    public String getExtMap() {
        return extMap;
    }

    public void setExtMap(String ext) {
        this.extMap = ext;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public long getChatGroupId() {
        return chatGroupId;
    }

    public void setChatGroupId(long chatGroupId) {
        this.chatGroupId = chatGroupId;
    }


    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public int getSendingState() {
        return sendingState;
    }

    public void setSendingState(int sendingState) {
        this.sendingState = sendingState;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(int isDelivered) {
        this.isDelivered = isDelivered;
    }
}
