package com.sudaotech.chatlibrary.model;

import java.util.List;

/**
 * Created by Samuel on 2016/12/3 15:43
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class EmojiconGroupEntity {

    /**
     * 表情数据
     */
    private List<Emojicon> emojiconList;
    /**
     * 图片
     */
    private int icon;
    /**
     * 组名
     */
    private String name;
    /**
     * 表情类型
     */
    private Emojicon.Type type;

    public EmojiconGroupEntity() {
    }

    public EmojiconGroupEntity(int icon, List<Emojicon> emojiconList) {
        this.icon = icon;
        this.emojiconList = emojiconList;
        type = Emojicon.Type.NORMAL;
    }

    public EmojiconGroupEntity(int icon, List<Emojicon> emojiconList, Emojicon.Type type) {
        this.icon = icon;
        this.emojiconList = emojiconList;
        this.type = type;
    }

    public List<Emojicon> getEmojiconList() {
        return emojiconList;
    }

    public void setEmojiconList(List<Emojicon> emojiconList) {
        this.emojiconList = emojiconList;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Emojicon.Type getType() {
        return type;
    }

    public void setType(Emojicon.Type type) {
        this.type = type;
    }

}
