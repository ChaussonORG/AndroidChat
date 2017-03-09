package com.sudaotech.chatlibrary.constroller;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.sudao.basemodule.common.util.LogUtil;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.model.ChatUser;
import com.sudaotech.chatlibrary.model.Emojicon;
import com.sudaotech.chatlibrary.utils.ChatNotifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class ChatUI {
    private static final String TAG = ChatUI.class.getSimpleName();

    /**
     * the global ChatUI instance
     */
    private static ChatUI instance = null;

    /**
     * user profile provider
     */
    private ChatUserProfileProvider userProvider;

    private ChatSettingsProvider settingsProvider;

    /**
     * application mContext
     */
    private Context appContext = null;

    /**
     * init flag: test if the sdk has been inited before, we don't need to init again
     */
    private boolean sdkInited = false;

    /**
     * the notifier
     */
    private ChatNotifier notifier = null;

    /**
     * save foreground Activity which registered eventlistener
     */
    private List<Activity> activityList = new ArrayList<Activity>();
    private EmojiconInfoProvider emojiconInfoProvider;

    private ChatUI() {
    }

    /**
     * get instance of ChatUI
     *
     * @return
     */
    public synchronized static ChatUI getInstance() {
        if (instance == null) {
            instance = new ChatUI();
        }
        return instance;
    }

    public void pushActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(0, activity);
        }
    }

    public void popActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * this function will initialize the SDK and easeUI kit
     *
     * @param context
     * @param options use default if options is null
     * @return
     */
    public synchronized boolean init(Context context, EMOptions options) {
        if (sdkInited) {
            return true;
        }
        appContext = context;

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);

        LogUtil.d(TAG, "process app name : " + processAppName);

        // if there is application has remote service, application:onCreate() maybe called twice
        // this check is to make sure SDK will initialized only once
        // return if process name is not application's name since the package name is the default process name
        if (processAppName == null || !processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            LogUtil.e(TAG, "enter the service process!");
            return false;
        }
        if (options == null) {
            EMClient.getInstance().init(context, initChatOptions());
        } else {
            EMClient.getInstance().init(context, options);
        }

        initNotifier();
//        registerMessageListener();

        if (settingsProvider == null) {
            settingsProvider = new DefaultSettingsProvider();
        }

        sdkInited = true;
        return true;
    }

    protected EMOptions initChatOptions() {
        LogUtil.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();
        // change to need confirm contact invitation
        options.setAcceptInvitationAlways(false);
        // set if need read ack
        options.setRequireAck(true);
        // set if need delivery ack
        options.setRequireDeliveryAck(false);

        return options;
    }

    void initNotifier() {
        notifier = createNotifier();
        notifier.init(appContext);
    }

    private void registerMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
//                ChatAtMessageHelper.get().parseMessages(messages);
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {

            }
        });
    }

    protected ChatNotifier createNotifier() {
        return new ChatNotifier();
    }

    public ChatNotifier getNotifier() {
        return notifier;
    }

    public boolean hasForegroundActivies() {
        return activityList.size() != 0;
    }

    /**
     * get user profile provider
     *
     * @return
     */
    public ChatUserProfileProvider getUserProfileProvider() {
        return userProvider;
    }

    /**
     * set user profile provider
     *
     * @param userProvider
     */
    public void setUserProfileProvider(ChatUserProfileProvider userProvider) {
        this.userProvider = userProvider;
    }

    public ChatSettingsProvider getSettingsProvider() {
        return settingsProvider;
    }

    public void setSettingsProvider(ChatSettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
    }

    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = appContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // LogUtil.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // LogUtil.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * Emojicon provider
     *
     * @return
     */
    public EmojiconInfoProvider getEmojiconInfoProvider() {
        return emojiconInfoProvider;
    }

    /**
     * set Emojicon provider
     *
     * @param emojiconInfoProvider
     */
    public void setEmojiconInfoProvider(EmojiconInfoProvider emojiconInfoProvider) {
        this.emojiconInfoProvider = emojiconInfoProvider;
    }

    public Context getContext() {
        return appContext;
    }

    /**
     * User profile provider
     *
     * @author wei
     */
    public interface ChatUserProfileProvider {
        /**
         * return EaseUser for input username
         *
         * @param username
         * @return
         */
        ChatUser getUser(String username);
    }

    /**
     * Emojicon provider
     */
    public interface EmojiconInfoProvider {
        /**
         * return EaseEmojicon for input emojiconIdentityCode
         *
         * @param emojiconIdentityCode
         * @return
         */
        Emojicon getEmojiconInfo(String emojiconIdentityCode);

        /**
         * get Emojicon map, key is the text of emoji, value is the resource id or local path of emoji icon(can't be URL on internet)
         *
         * @return
         */
        Map<String, Object> getTextEmojiconMapping();
    }

    /**
     * new mBaseMessage options provider
     */
    public interface ChatSettingsProvider {
        boolean isMsgNotifyAllowed(BaseMessage message);

        boolean isMsgSoundAllowed(BaseMessage message);

        boolean isMsgVibrateAllowed(BaseMessage message);

        boolean isSpeakerOpened();
    }

    /**
     * default settings provider
     */
    protected class DefaultSettingsProvider implements ChatSettingsProvider {

        @Override
        public boolean isMsgNotifyAllowed(BaseMessage message) {
            return true;
        }

        @Override
        public boolean isMsgSoundAllowed(BaseMessage message) {
            return true;
        }

        @Override
        public boolean isMsgVibrateAllowed(BaseMessage message) {
            return true;
        }

        @Override
        public boolean isSpeakerOpened() {
            return true;
        }
    }
}
