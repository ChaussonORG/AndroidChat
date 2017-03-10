package com.sudaotech.chatlibrary.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Samuel on 2016/12/24 14:42
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class UserInfo extends RealmObject {
    @PrimaryKey
    private String contactId;
    private long userId;//用户id和群id
    private int contactType;//对话类型，1代表单聊，2代表群聊
    private String name;
    private String photo;
    private String extend;

    public UserInfo() {
    }


    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
