package com.sudaotech.chatlibrary.model;


import com.sudaotech.chatlibrary.MessageCallback;

import java.util.Map;


/**
 * Created by Samuel on 2016/12/3 19:52
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class BaseMessage {
    private long messageId;//消息ID
    private long conversationId;//会话id
    private String messageType;//消息类型
    private long messageTime;//消息时间(服务器时间)
    private String messageTarget;//聊天类型：单聊，群聊，通知
    private String messageContent;//消息内容
    private Map<String, Object> extMap;
    private long senderId;//发送者ID
    private long receiverId;//接收者ID
    private long chatGroupId;

    private long size;//图片大小
    private String imagePath;//图片本地路径
    private String fullPath;//图片url
    private String imageName;//图片名称

    private int length;//语音长度
    private String voicePath;//语音本地路径
    private String filePath;//语音url
    private String voiceName;//语音文件名称
    private String form;//语音文件格式
    private int isListened;//是否已听

    private long redEnvelopsId;//红包id
    private String redEnvelopsDesc;//红包描述

    private int fileDownloadStatus;


    private long showTime;//消息显示时间(本地时间)

    private int owner;//消息归属(1:自己 0:别人)
    private int sendingState;//消息发送状态(0:create,1:success,2:fail,3:inprogress)
    private String photo;//用户头像

    private String nickName;//用户昵称

    private int isRead;//是否已读
    private int isDelivered;//是否送达

    private String notifyType;
    private String notifyContent;
    private String notifyTime;

    private MessageCallback messageCallback;

    public BaseMessage() {
    }

    public long getRedEnvelopsId() {
        return redEnvelopsId;
    }

    public void setRedEnvelopsId(long redEnvelopsId) {
        this.redEnvelopsId = redEnvelopsId;
    }

    public String getRedEnvelopsDesc() {
        return redEnvelopsDesc;
    }

    public void setRedEnvelopsDesc(String redEnvelopsDesc) {
        this.redEnvelopsDesc = redEnvelopsDesc;
    }

    public String getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(String notifyType) {
        this.notifyType = notifyType;
    }

    public String getNotifyContent() {
        return notifyContent;
    }

    public void setNotifyContent(String notifyContent) {
        this.notifyContent = notifyContent;
    }

    public String getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(String notifyTime) {
        this.notifyTime = notifyTime;
    }

    public int getIsListened() {
        return isListened;
    }

    public void setIsListened(int isListened) {
        this.isListened = isListened;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public int getFileDownloadStatus() {
        return fileDownloadStatus;
    }

    public void setFileDownloadStatus(int fileDownloadStatus) {
        this.fileDownloadStatus = fileDownloadStatus;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public int getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(int isDelivered) {
        this.isDelivered = isDelivered;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
    }

    public String getMessageTarget() {
        return messageTarget;
    }

    public void setMessageTarget(String messageTarget) {
        this.messageTarget = messageTarget;
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

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public MessageCallback getMessageStatusCallback() {
        return messageCallback;
    }

    public void setMessageStatusCallback(MessageCallback messageCallback) {
        this.messageCallback = messageCallback;
    }

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
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

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getSendingState() {
        return sendingState;
    }

    public void setSendingState(int sendingState) {
        this.sendingState = sendingState;
    }


}
