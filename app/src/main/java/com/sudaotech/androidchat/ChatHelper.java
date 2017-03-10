package com.sudaotech.androidchat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.sudao.basemodule.base.BundleConst;
import com.sudao.basemodule.basicapi.UserBean;
import com.sudao.basemodule.common.util.GsonHelper;
import com.sudao.basemodule.common.util.LogUtil;
import com.sudao.basemodule.common.util.SPHelper;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.constroller.ChatUI;
import com.sudaotech.chatlibrary.db.MessageDao;
import com.sudaotech.chatlibrary.db.UserDao;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.model.ChatUser;
import com.sudaotech.chatlibrary.model.Emojicon;
import com.sudaotech.chatlibrary.model.MessageExtend;
import com.sudaotech.chatlibrary.model.UserInfo;
import com.sudaotech.chatlibrary.utils.ChatNotifier;
import com.sudaotech.chatlibrary.utils.ChatPreferenceManager;
import com.sudaotech.chatlibrary.utils.ChatUtil;

import java.util.List;
import java.util.Map;

public class ChatHelper {
    protected static final String TAG = "ChatHelper";
    private static ChatHelper instance = null;

    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private com.sudaotech.chatlibrary.constroller.ChatUI mChatUI;
    private ChatModel mChatModel = null;

    private String username;

    private Context mContext;
    private MessageDao mMessageDao;
    private UserDao mUserDao;
    private long mOwnUserId;

    private ChatHelper() {
    }

    public synchronized static ChatHelper getInstance() {
        if (instance == null) {
            instance = new ChatHelper();
        }
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context) {
        mChatModel = new ChatModel(context);
        mMessageDao = new MessageDao(context);
        mUserDao = new UserDao(mContext);
        String userInfoJson = SPHelper.getString(ChatConstant.USER_INFO, "");//获取当前用户资料
        if (userInfoJson.length() > 0) {
            UserBean userBean = GsonHelper.fromJson(userInfoJson, UserBean.class);
            mOwnUserId = userBean.getUserId();

        }
        EMOptions options = initChatOptions();
        //use default options if options is null
        if (ChatUI.getInstance().init(context, options)) {
            mContext = context;

            // TODO: 2016/12/20 set false when release
            EMClient.getInstance().setDebugMode(false);
            //get mChatUI instance
            mChatUI = ChatUI.getInstance();
            //to set user's profile and avatar
            setEaseUIProviders();
            //initialize preference manager
            ChatPreferenceManager.init(context);
            //initialize profile manager


            registerMessageListener();

        }
    }


    private EMOptions initChatOptions() {
        LogUtil.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
//        options.setAcceptInvitationAlways(false);
        // set if you need read ack
//        options.setRequireAck(true);
        // set if you need delivery ack
//        options.setRequireDeliveryAck(false);

        //you need apply & set your own id if you want to use google cloud messaging.
//        options.setGCMNumber("324169311137");
        //you need apply & set your own id if you want to use Mi push notification
//        options.setMipushConfig(BundleConst.XIAOMI_APP_ID, BundleConst.XIAOMI_APP_KEY);
        //you need apply & set your own id if you want to use Huawei push notification
        options.setHuaweiPushAppId(BundleConst.HUAWEI_APP_ID);

        //set custom servers, commonly used in private deployment


        return options;
    }

    protected void setEaseUIProviders() {
        // set profile provider if you want mChatUI to handle avatar and nickname
        mChatUI.setUserProfileProvider(new ChatUI.ChatUserProfileProvider() {

            @Override
            public ChatUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        //set options
        mChatUI.setSettingsProvider(new com.sudaotech.chatlibrary.constroller.ChatUI.ChatSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return mChatModel.getSettingMsgSpeaker();
            }

            @Override
            public boolean isMsgVibrateAllowed(BaseMessage message) {
                return mChatModel.getSettingMsgVibrate();
            }

            @Override
            public boolean isMsgSoundAllowed(BaseMessage message) {
                return mChatModel.getSettingMsgSound();
            }

            @Override
            public boolean isMsgNotifyAllowed(BaseMessage message) {
                if (message == null) {
                    return mChatModel.getSettingMsgNotification();
                }
                if (!mChatModel.getSettingMsgNotification()) {
                    return false;
                } else {
                    long chatUsername = 0;
                    List<Long> notNotifyIds = null;
                    // get user or group id which was blocked to show message notifications
                    if (message.getMessageTarget() == ChatConstant.MESSAGE_TARGET_SINGLE) {
                        chatUsername = message.getSenderId();
                    } else {
                        chatUsername = message.getReceiverId();
                    }

                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsername)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });
        //set emoji icon provider
        mChatUI.setEmojiconInfoProvider(new com.sudaotech.chatlibrary.constroller.ChatUI.EmojiconInfoProvider() {

            @Override
            public Emojicon getEmojiconInfo(String emojiconIdentityCode) {

                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });

        //set notification options, will use default if you don't set it
        mChatUI.getNotifier().setNotificationInfoProvider(new ChatNotifier.ChatNotificationInfoProvider() {

            @Override
            public String getTitle(BaseMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(BaseMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(BaseMessage message) {
                // be used on notification bar, different text according the message type.
                /*String ticker = ChatUtil.getMessageDigest(message, mContext);
                if (message.getType() == Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                ChatUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (ChatAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(mContext.getString(R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                } else {
                    if (ChatAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(mContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }*/

                return message.getMessageContent();
            }

            @Override
            public String getLatestText(BaseMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(BaseMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(mContext, MainActivity.class);
                return intent;
            }
        });
    }


    /**
     * user met some exception: conflict, removed or forbidden
     */
    protected void onUserException(String exception) {
        LogUtil.e(TAG, "onUserException: " + exception);
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(exception, true);
        mContext.startActivity(intent);
    }

    private ChatUser getUserInfo(String username) {
        // To get instance of ChatUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        ChatUser user = null;
//        if (username.equals(EMClient.getInstance().getCurrentUser()))
//            return getUserProfileManager().getCurrentUserInfo();
//        user = getContactList().get(username);

        // if user is not in your contacts, set inital letter for him/her
        if (user == null) {
            user = new ChatUser(username);
            ChatUtil.setUserInitialLetter(user);
        }
        return user;
    }

    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    // in background, do not refresh UI, notify it in notification bar
                    if (!mChatUI.hasForegroundActivies()) {
                        BaseMessage baseMessage = ChatUtil.emMessagetoBaseMessage(message);
                        baseMessage.setIsRead(ChatConstant.MESSAGE_UNREAD);

                        if (!ChatUtil.isNotification(baseMessage)) {
                            mMessageDao.insertMessage(ChatUtil.toChatMessage(baseMessage));
                            getNotifier().onNewMsg(baseMessage);

                            Map<String, Object> extMap = baseMessage.getExtMap();
                            String photo = (String) extMap.get(MessageExtend.EXT_PHOTO);
                            String name = (String) extMap.get(MessageExtend.EXT_NAME);

                            addToRecentContact(baseMessage);

                            UserInfo userInfo = new UserInfo();
                            userInfo.setContactId(baseMessage.getSenderId() + ChatConstant.CONTACT_TYPE_USER);
                            userInfo.setUserId(baseMessage.getSenderId());
                            userInfo.setPhoto(photo);
                            userInfo.setName(name);
                            mUserDao.insertUser(userInfo);


                            // TODO: 2017/2/24 群信息暂时不处理
                            /*if (ChatConstant.MESSAGE_TARGET_GROUP.equals(baseMessage.getMessageTarget())) {
                                UserInfo groupInfo = new UserInfo();
                                String groupPhoto = (String) extMap.get(MessageExtend.EXT_PHOTO);
                                String groupName = (String) extMap.get(MessageExtend.EXT_NAME);
                                groupInfo.setContactId(baseMessage.getChatGroupId() + ChatConstant.CONTACT_TYPE_GROUP);
                                groupInfo.setUserId(baseMessage.getChatGroupId());
                                groupInfo.setPhoto(groupPhoto);
                                groupInfo.setName(groupName);
                                mUserDao.insertUser(groupInfo);
                            }*/
                        } else {
                            ChatHelper.getInstance().getNotifier().onNewNotification(baseMessage);
                            ChatUtil.saveUnreadNotification(baseMessage.getNotifyType());
                        }
                        SPHelper.putInt(ChatConstant.HINT_NEW_MESSAGE, 1);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    // in background, do not refresh UI, notify it in notification bar
                    if (!mChatUI.hasForegroundActivies()) {
                        BaseMessage baseMessage = ChatUtil.cmdMessagetoBaseMessage(message);
                        baseMessage.setIsRead(ChatConstant.MESSAGE_UNREAD);

                        if (!ChatUtil.isNotification(baseMessage)) {
                            mMessageDao.insertMessage(ChatUtil.toChatMessage(baseMessage));

                            Map<String, Object> extMap = baseMessage.getExtMap();
                            String photo = (String) extMap.get(MessageExtend.EXT_PHOTO);
                            String name = (String) extMap.get(MessageExtend.EXT_NAME);

                            addToRecentContact(baseMessage);

                            UserInfo userInfo = new UserInfo();
                            userInfo.setContactId(baseMessage.getSenderId() + ChatConstant.CONTACT_TYPE_USER);
                            userInfo.setUserId(baseMessage.getSenderId());
                            userInfo.setPhoto(photo);
                            userInfo.setName(name);
                            mUserDao.insertUser(userInfo);

                            // TODO: 2017/2/24 群信息暂时不处理
                            /*if (ChatConstant.MESSAGE_TARGET_GROUP.equals(baseMessage.getMessageTarget())) {
                                UserInfo groupInfo = new UserInfo();
                                String groupPhoto = (String) extMap.get(MessageExtend.EXT_PHOTO);
                                String groupName = (String) extMap.get(MessageExtend.EXT_NAME);
                                groupInfo.setContactId(baseMessage.getChatGroupId() + ChatConstant.CONTACT_TYPE_GROUP);
                                groupInfo.setUserId(baseMessage.getChatGroupId());
                                groupInfo.setPhoto(groupPhoto);
                                groupInfo.setName(groupName);
                                mUserDao.insertUser(groupInfo);
                            }*/
                        } else {
                            ChatHelper.getInstance().getNotifier().onNewNotification(baseMessage);
                            ChatUtil.saveUnreadNotification(baseMessage.getNotifyType());
                        }
                        SPHelper.putInt(ChatConstant.HINT_NEW_MESSAGE, 1);
                    }
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    public void addToRecentContact(BaseMessage baseMessage) {


    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        LogUtil.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                LogUtil.d(TAG, "logout: onSuccess");
//                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                LogUtil.d(TAG, "logout: onSuccess");
//                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public ChatNotifier getNotifier() {
        return mChatUI.getNotifier();
    }

    public ChatModel getModel() {
        return (ChatModel) mChatModel;
    }

    /**
     * set current username
     *
     * @param username
     */
    public void setCurrentUserName(String username) {
        this.username = username;
//        mChatModel.setCurrentUserName(username);
    }

    /**
     * get current user's id
     */
    public String getCurrentUsernName() {
        if (username == null) {
//            username = mChatModel.getCurrentUsernName();
        }
        return username;
    }

    private void endCall() {
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pushActivity(Activity activity) {
        mChatUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        mChatUI.popActivity(activity);
    }
}
