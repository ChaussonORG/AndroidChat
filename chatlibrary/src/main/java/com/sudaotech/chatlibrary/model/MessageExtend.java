package com.sudaotech.chatlibrary.model;

/**
 * Created by Samuel on 2016/12/24 16:31
 * Email:xuzhou40@gmail.com
 * desc:消息实体类的扩展字段
 */

public class MessageExtend {
    public static final String EXT_PHOTO = "photo";
    public static final String EXT_NAME = "name";
    public static final String EXT_GROUP_NAME = "groupName";
    public static final String EXT_GROUP_PHOTO = "groupPhoto";
    private String photo;
    private String name;
    private String groupName;
    private String groupPhoto;

    public MessageExtend() {
    }

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
