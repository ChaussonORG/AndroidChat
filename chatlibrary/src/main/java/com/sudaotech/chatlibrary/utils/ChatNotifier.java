/************************************************************
 * * Hyphenate CONFIDENTIAL
 * __________________
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * NOTICE: All information contained herein is, and remains
 * the property of Hyphenate Inc.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Hyphenate Inc.
 */
package com.sudaotech.chatlibrary.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.hyphenate.util.EasyUtils;
import com.sudao.basemodule.common.util.LogUtil;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.constroller.ChatUI;
import com.sudaotech.chatlibrary.model.BaseMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;


/**
 * new mBaseMessage notifier class
 * <p>
 * this class is subject to be inherited and implement the relative APIs
 */
public class ChatNotifier {
    protected final static String[] msg_eng = {"sent a mBaseMessage", "sent a picture", "sent a voice",
            "sent location mBaseMessage", "sent a video", "sent a file", "%1 contacts sent %2 messages"
    };
    protected final static String[] msg_ch = {"发来一条消息", "发来一张图片", "发来一段语音", "发来位置信息", "发来一个视频", "发来一个文件",
            "%1个联系人发来%2条消息"
    };
    private final static String TAG = "notify";
    protected static int messageNotifyID = 0525; // start notification id
    protected static int otherNotifyID = 0526; // start notification id
    protected static int foregroundNotifyID = 0555;
    protected NotificationManager notificationManager = null;
    protected HashSet<Long> fromUsers = new HashSet<Long>();
    protected int notificationNum = 0;
    protected Context appContext;
    protected String packageName;
    protected String[] msgs;
    protected long lastNotifiyTime;
    protected AudioManager audioManager;
    protected Vibrator vibrator;
    protected ChatNotificationInfoProvider notificationInfoProvider;
    Ringtone ringtone = null;

    public ChatNotifier() {
    }

    /**
     * this function can be override
     *
     * @param context
     * @return
     */
    public ChatNotifier init(Context context) {
        appContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        packageName = appContext.getApplicationInfo().packageName;
        if (Locale.getDefault().getLanguage().equals("zh")) {
            msgs = msg_ch;
        } else {
            msgs = msg_eng;
        }

        audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);

        return this;
    }

    /**
     * this function can be override
     */
    public void reset() {
        resetNotificationCount();
        cancelNotificaton();
    }

    void resetNotificationCount() {
        notificationNum = 0;
        fromUsers.clear();
    }

    void cancelNotificaton() {
        if (notificationManager != null) {
            notificationManager.cancel(messageNotifyID);
        }
    }

    /**
     * handle the new mBaseMessage
     * this function can be override
     *
     * @param message
     */
    public synchronized void onNewMsg(BaseMessage message) {
        ChatUI.ChatSettingsProvider settingsProvider = ChatUI.getInstance().getSettingsProvider();
        if (!settingsProvider.isMsgNotifyAllowed(message)) {
            return;
        }

        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            LogUtil.d(TAG, "app is running in backgroud");
            sendMessageNotification(message, false);
        } else {
            sendMessageNotification(message, true);

        }

        vibrateAndPlayTone(message);
    }


    public synchronized void onNewMesg(List<BaseMessage> messages) {

        ChatUI.ChatSettingsProvider settingsProvider = ChatUI.getInstance().getSettingsProvider();
        if (!settingsProvider.isMsgNotifyAllowed(null)) {
            return;
        }
        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            LogUtil.d(TAG, "app is running in backgroud");
            sendMessageNotification(messages, false);
        } else {
            sendMessageNotification(messages, true);
        }
        vibrateAndPlayTone(messages.get(messages.size() - 1));
    }

    /**
     * 新的通知
     *
     * @param message
     */
    public synchronized void onNewNotification(BaseMessage message) {
        ChatUI.ChatSettingsProvider settingsProvider = ChatUI.getInstance().getSettingsProvider();
        if (!settingsProvider.isMsgNotifyAllowed(message)) {
            return;
        }

        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            LogUtil.d(TAG, "app is running in backgroud");
            sendOtherNotification(message, false);
        } else {
            sendOtherNotification(message, true);

        }

        vibrateAndPlayTone();
    }

    /**
     * send it to notification bar
     * This can be override by subclass to provide customer implementation
     *
     * @param messages
     * @param isForeground
     */
    protected void sendMessageNotification(List<BaseMessage> messages, boolean isForeground) {
        for (BaseMessage message : messages) {
            if (!isForeground) {
                notificationNum++;
                fromUsers.add(message.getSenderId());
            }
        }
        sendMessageNotification(messages.get(messages.size() - 1), isForeground, false);
    }

    protected void sendMessageNotification(BaseMessage message, boolean isForeground) {
        sendMessageNotification(message, isForeground, true);
    }

    /**
     * 消息通知
     *
     * @param message
     * @param isForeground
     * @param numIncrease
     */
    protected void sendMessageNotification(BaseMessage message, boolean isForeground, boolean numIncrease) {
        PackageManager packageManager = appContext.getPackageManager();
        String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());

        String username = appname;

        String notifyText = username + " ";
        String messageType = message.getMessageType();
        if (ChatConstant.MESSAGE_TYPE_TEXT.equals(messageType)) {
            notifyText += msgs[0];
        } else if (ChatConstant.MESSAGE_TYPE_IMAGE.equals(messageType)) {
            notifyText += msgs[1];

        } else if (ChatConstant.MESSAGE_TYPE_AUDIO.equals(messageType)) {
            notifyText += msgs[2];

        }


        // notification title
        String contentTitle = appname;
        if (notificationInfoProvider != null) {
            String customNotifyText = notificationInfoProvider.getDisplayedText(message);
            String customCotentTitle = notificationInfoProvider.getTitle(message);
            if (customNotifyText != null) {
                notifyText = customNotifyText;
            }

            if (customCotentTitle != null) {
                contentTitle = customCotentTitle;
            }
        }

        // create and send notificaiton
        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext)
                .setSmallIcon(appContext.getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
        if (notificationInfoProvider != null) {
            msgIntent = notificationInfoProvider.getLaunchIntent(message);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, messageNotifyID, msgIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (numIncrease) {
            // prepare latest event info section
//            if (!isForeground) {
            notificationNum++;
            fromUsers.add(message.getSenderId());
//            }
        }

        int fromUsersNum = fromUsers.size();
        String summaryBody = msgs[6].replaceFirst("%1", Integer.toString(fromUsersNum)).replaceFirst("%2", Integer.toString(notificationNum));

        if (notificationInfoProvider != null) {
            // lastest text
            String customSummaryBody = notificationInfoProvider.getLatestText(message, fromUsersNum, notificationNum);
            if (customSummaryBody != null) {
                summaryBody = customSummaryBody;
            }

            // small icon
            int smallIcon = notificationInfoProvider.getSmallIcon(message);
            if (smallIcon != 0) {
                builder.setSmallIcon(smallIcon);
            }
        }

        builder.setContentTitle(contentTitle);
        builder.setTicker(notifyText);
        builder.setContentText(summaryBody);
        builder.setContentIntent(pendingIntent);
        builder.setNumber(notificationNum);
        Notification notification = builder.build();

        if (isForeground) {
            notificationManager.notify(foregroundNotifyID, notification);
            notificationManager.cancel(foregroundNotifyID);
        } else {
            notificationManager.notify(messageNotifyID, notification);
        }


    }


    protected void sendOtherNotification(BaseMessage message, boolean isForeground) {
        PackageManager packageManager = appContext.getPackageManager();
        String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());

        String notifyText = message.getNotifyContent();

        // notification title
        String contentTitle = appname;


        // create and send notificaiton
        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext)
                .setSmallIcon(appContext.getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
        if (notificationInfoProvider != null) {
            msgIntent = notificationInfoProvider.getLaunchIntent(message);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, messageNotifyID, msgIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentTitle(contentTitle);
        builder.setTicker(notifyText);
        builder.setContentText(message.getNotifyContent());
        builder.setContentIntent(pendingIntent);
        builder.setNumber(notificationNum);
        Notification notification = builder.build();

        notificationManager.notify(otherNotifyID, notification);


    }

    public void vibrateAndPlayTone() {
        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }

        try {
            lastNotifiyTime = System.currentTimeMillis();

            // check if in silent mode
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                LogUtil.e(TAG, "in slient mode now");
                return;
            }
            ChatUI.ChatSettingsProvider settingsProvider = ChatUI.getInstance().getSettingsProvider();
            long[] pattern = new long[]{0, 180, 80, 120};
            vibrator.vibrate(pattern, -1);


            if (ringtone == null) {
                Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                ringtone = RingtoneManager.getRingtone(appContext, notificationUri);
                if (ringtone == null) {
                    LogUtil.d(TAG, "cant find ringtone at:" + notificationUri.getPath());
                    return;
                }
            }

            if (!ringtone.isPlaying()) {
                String vendor = Build.MANUFACTURER;

                ringtone.play();
                // for samsung S3, we meet a bug that the phone will
                // continue ringtone without stop
                // so add below special handler to stop it after 3s if
                // needed
                if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                    Thread ctlThread = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                if (ringtone.isPlaying()) {
                                    ringtone.stop();
                                }
                            } catch (Exception e) {
                            }
                        }
                    };
                    ctlThread.run();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * vibrate and  play tone
     *
     * @param message
     */
    public void vibrateAndPlayTone(BaseMessage message) {
        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }

        try {
            lastNotifiyTime = System.currentTimeMillis();

            // check if in silent mode
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                LogUtil.e(TAG, "in slient mode now");
                return;
            }
            ChatUI.ChatSettingsProvider settingsProvider = ChatUI.getInstance().getSettingsProvider();
            if (settingsProvider.isMsgVibrateAllowed(message)) {
                long[] pattern = new long[]{0, 180, 80, 120};
                vibrator.vibrate(pattern, -1);
            }

            if (settingsProvider.isMsgSoundAllowed(message)) {
                if (ringtone == null) {
                    Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    ringtone = RingtoneManager.getRingtone(appContext, notificationUri);
                    if (ringtone == null) {
                        LogUtil.d(TAG, "cant find ringtone at:" + notificationUri.getPath());
                        return;
                    }
                }

                if (!ringtone.isPlaying()) {
                    String vendor = Build.MANUFACTURER;

                    ringtone.play();
                    // for samsung S3, we meet a bug that the phone will
                    // continue ringtone without stop
                    // so add below special handler to stop it after 3s if
                    // needed
                    if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                        Thread ctlThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    if (ringtone.isPlaying()) {
                                        ringtone.stop();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        };
                        ctlThread.run();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * set notification info Provider
     *
     * @param provider
     */
    public void setNotificationInfoProvider(ChatNotificationInfoProvider provider) {
        notificationInfoProvider = provider;
    }

    public interface ChatNotificationInfoProvider {
        /**
         * set the notification content, such as "you received a new image from xxx"
         *
         * @param message
         * @return null-will use the default text
         */
        String getDisplayedText(BaseMessage message);

        /**
         * set the notification content: such as "you received 5 mBaseMessage from 2 contacts"
         *
         * @param message
         * @param fromUsersNum- number of mBaseMessage sender
         * @param messageNum    -number of messages
         * @return null-will use the default text
         */
        String getLatestText(BaseMessage message, int fromUsersNum, int messageNum);

        /**
         * 设置notification标题
         *
         * @param message
         * @return null- will use the default text
         */
        String getTitle(BaseMessage message);

        /**
         * set the small icon
         *
         * @param message
         * @return 0- will use the default icon
         */
        int getSmallIcon(BaseMessage message);

        /**
         * set the intent when notification is pressed
         *
         * @param message
         * @return null- will use the default icon
         */
        Intent getLaunchIntent(BaseMessage message);
    }
}
