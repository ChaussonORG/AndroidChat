package com.sudaotech.chatlibrary.network.request;

import java.util.Map;

/**
 * Created by Samuel on 2016/12/5 14:46
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class SingleMessageRequest {

    /**
     * receiverId :
     * messageType :TEXT
     * messageContent : hello world
     */


    private long receiverId;

    private String messageType;
    private String messageContent;
    private String fullPath;//图片url
    private String imageName;//图片名称
    private float width;//
    private float height;
    private long size;//图片大小

    private int length;//语音长度
    private String filePath;//语音url
    private String voiceName;//语音文件名称
    private String form;//语音文件格式

    private long redEnvelopsId;//红包id
    private String redEnvelopsDesc;//红包描述


    private Map<String, Object> extMap;

    public SingleMessageRequest() {
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
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

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
