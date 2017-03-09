package com.sudaotech.androidchat;

import android.content.Context;

import com.sudaotech.chatlibrary.utils.ChatPreferenceManager;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    //    UserDao dao = null;
    protected Context context = null;
    protected Map<Key, Object> valueCache = new HashMap<Key, Object>();

    public ChatModel(Context ctx) {
        context = ctx;
        ChatPreferenceManager.init(context);
    }

    public boolean getSettingMsgNotification() {
        Object val = valueCache.get(Key.VibrateAndPlayToneOn);

        if (val == null) {
            val = ChatPreferenceManager.getInstance().getSettingMsgNotification();
            valueCache.put(Key.VibrateAndPlayToneOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public void setSettingMsgNotification(boolean paramBoolean) {
        ChatPreferenceManager.getInstance().setSettingMsgNotification(paramBoolean);
        valueCache.put(Key.VibrateAndPlayToneOn, paramBoolean);
    }

    public boolean getSettingMsgSound() {
        Object val = valueCache.get(Key.PlayToneOn);

        if (val == null) {
            val = ChatPreferenceManager.getInstance().getSettingMsgSound();
            valueCache.put(Key.PlayToneOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public void setSettingMsgSound(boolean paramBoolean) {
        ChatPreferenceManager.getInstance().setSettingMsgSound(paramBoolean);
        valueCache.put(Key.PlayToneOn, paramBoolean);
    }

    public boolean getSettingMsgVibrate() {
        Object val = valueCache.get(Key.VibrateOn);

        if (val == null) {
            val = ChatPreferenceManager.getInstance().getSettingMsgVibrate();
            valueCache.put(Key.VibrateOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public void setSettingMsgVibrate(boolean paramBoolean) {
        ChatPreferenceManager.getInstance().setSettingMsgVibrate(paramBoolean);
        valueCache.put(Key.VibrateOn, paramBoolean);
    }

    public boolean getSettingMsgSpeaker() {
        Object val = valueCache.get(Key.SpakerOn);

        if (val == null) {
            val = ChatPreferenceManager.getInstance().getSettingMsgSpeaker();
            valueCache.put(Key.SpakerOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public void setSettingMsgSpeaker(boolean paramBoolean) {
        ChatPreferenceManager.getInstance().setSettingMsgSpeaker(paramBoolean);
        valueCache.put(Key.SpakerOn, paramBoolean);
    }


    enum Key {
        VibrateAndPlayToneOn,
        VibrateOn,
        PlayToneOn,
        SpakerOn,
        DisabledGroups,
        DisabledIds
    }
}
