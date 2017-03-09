package com.sudaotech.chatlibrary.model;

/**
 * Created by Samuel on 2016/12/27 11:21
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class GroupInfo {

    /**
     * groupMemberNumber : 43
     * groupName : 深圳金融商会
     * isGroupUser : false
     * freeGroupId : 1
     * groupType : CHAMBER_GROUP
     */

    private int groupMemberNumber;
    private String groupName;
    private String groupPhoto;
    private boolean isGroupUser;
    private int freeGroupId;
    private String groupType;
    private String disturbStatus;

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public int getGroupMemberNumber() {
        return groupMemberNumber;
    }

    public void setGroupMemberNumber(int groupMemberNumber) {
        this.groupMemberNumber = groupMemberNumber;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isGroupUser() {
        return isGroupUser;
    }

    public void setGroupUser(boolean groupUser) {
        isGroupUser = groupUser;
    }

    public int getFreeGroupId() {
        return freeGroupId;
    }

    public void setFreeGroupId(int freeGroupId) {
        this.freeGroupId = freeGroupId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getDisturbStatus() {
        return disturbStatus;
    }

    public void setDisturbStatus(String disturbStatus) {
        this.disturbStatus = disturbStatus;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupMemberNumber=" + groupMemberNumber +
                ", groupName='" + groupName + '\'' +
                ", groupPhoto='" + groupPhoto + '\'' +
                ", isGroupUser=" + isGroupUser +
                ", freeGroupId=" + freeGroupId +
                ", groupType='" + groupType + '\'' +
                ", disturbStatus='" + disturbStatus + '\'' +
                '}';
    }
}
