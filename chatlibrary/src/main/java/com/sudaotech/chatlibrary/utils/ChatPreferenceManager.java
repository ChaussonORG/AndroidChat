package com.sudaotech.chatlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.sudaotech.chatlibrary.constroller.ChatUI;

public class ChatPreferenceManager {
    public static final String PREFERENCE_NAME = "ChatInfo";
    private static final String KEY_AT_GROUPS = "AT_GROUPS";
    private static ChatPreferenceManager mChatPreferenceManager;
    private SharedPreferences.Editor editor;
    private SharedPreferences mSharedPreferences;

    private String SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification";
    private String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
    private String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
    private String SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker";


    @SuppressLint("CommitPrefEdits")
    private ChatPreferenceManager() {
        mSharedPreferences = ChatUI.getInstance().getContext().getSharedPreferences("EM_SP_AT_MESSAGE", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    private ChatPreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context context) {
        if (mChatPreferenceManager == null) {
            mChatPreferenceManager = new ChatPreferenceManager(context);
        }
    }

    public synchronized static ChatPreferenceManager getInstance() {
        if (mChatPreferenceManager == null) {
            throw new RuntimeException("please init first!");
        }
        return mChatPreferenceManager;
    }

    public boolean getSettingMsgNotification() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_NOTIFICATION, true);
    }

    public void setSettingMsgNotification(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
        editor.apply();
    }

    public boolean getSettingMsgSound() {

        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SOUND, true);
    }

    public void setSettingMsgSound(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
        editor.apply();
    }

    public boolean getSettingMsgVibrate() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_VIBRATE, true);
    }

    public void setSettingMsgVibrate(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
        editor.apply();
    }

    public boolean getSettingMsgSpeaker() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SPEAKER, true);
    }

    public void setSettingMsgSpeaker(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean);
        editor.apply();
    }


}
