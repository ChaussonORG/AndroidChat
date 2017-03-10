package com.sudaotech.chatlibrary.model;

/**
 * Created by Samuel on 2016/12/12 17:39
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class MessageBody {
    private String content;
    private String imageUrl;
    private String imagePath;
    private String imageName;
    private long imageSize;

    private int voiceLength;//语音长度
    private String voiceUrl;//语音url
    private String voiceName;//语音名称
    private String voicePath;//语音本地路径
    private String voiceForm;//语音文件格式
    private int isListened;//语音是否已读

    private long redEnvelopsId;//红包id
    private String redEnvelopsDesc;//红包描述

    private int fileDownloadStatus;

    public MessageBody() {
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

    public int getIsListened() {
        return isListened;
    }

    public void setIsListened(int isListened) {
        this.isListened = isListened;
    }

    public int getVoiceLength() {
        return voiceLength;
    }

    public void setVoiceLength(int voiceLength) {
        this.voiceLength = voiceLength;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getVoiceForm() {
        return voiceForm;
    }

    public void setVoiceForm(String voiceForm) {
        this.voiceForm = voiceForm;
    }

    public int getFileDownloadStatus() {
        return fileDownloadStatus;
    }

    public void setFileDownloadStatus(int fileDownloadStatus) {
        this.fileDownloadStatus = fileDownloadStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
