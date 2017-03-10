/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sudaotech.chatlibrary.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.HanziToPinyin;
import com.hyphenate.util.HanziToPinyin.Token;
import com.sudao.basemodule.common.util.GsonHelper;
import com.sudao.basemodule.common.util.LogUtil;
import com.sudao.basemodule.common.util.SPHelper;
import com.sudao.basemodule.common.util.ScreenHelper;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.model.ChatMessage;
import com.sudaotech.chatlibrary.model.ChatUser;
import com.sudaotech.chatlibrary.model.MessageBody;
import com.sudaotech.chatlibrary.model.UserInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.sudaotech.chatlibrary.ChatConstant.MESSAGE_IS_OTHER;
import static com.sudaotech.chatlibrary.ChatConstant.MESSAGE_STATUS_SUCCESS;
import static com.sudaotech.chatlibrary.R.string.file;

public class ChatUtil {
    private static final String TAG = "ChatUtil";

    /**
     * check if network avalable
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
            }
        }

        return false;
    }

    /**
     * check if sdcard exist
     *
     * @return
     */
    public static boolean isSdcardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static EMMessage createExpressionMessage(String toChatUsername, String expressioName, String identityCode) {
        EMMessage message = EMMessage.createTxtSendMessage("[" + expressioName + "]", toChatUsername);
        if (identityCode != null) {
            message.setAttribute(ChatConstant.MESSAGE_ATTR_EXPRESSION_ID, identityCode);
        }
        message.setAttribute(ChatConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, true);
        return message;
    }

    /**
     * Get digest according mBaseMessage type and content
     *
     * @param message
     * @param context
     * @return
     */
    public static String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION:
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    digest = getString(context, R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    digest = getString(context, R.string.location_prefix);
                }
                break;
            case IMAGE:
                digest = getString(context, R.string.picture);
                break;
            case VOICE:
                digest = getString(context, R.string.voice_prefix);
                break;
            case VIDEO:
                digest = getString(context, R.string.video);
                break;
            case TXT:
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                if (message.getBooleanAttribute(ChatConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    digest = getString(context, R.string.voice_call) + txtBody.getMessage();
                } else if (message.getBooleanAttribute(ChatConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    digest = getString(context, R.string.video_call) + txtBody.getMessage();
                } else if (message.getBooleanAttribute(ChatConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    if (!TextUtils.isEmpty(txtBody.getMessage())) {
                        digest = txtBody.getMessage();
                    } else {
                        digest = getString(context, R.string.dynamic_expression);
                    }
                } else {
                    digest = txtBody.getMessage();
                }
                break;
            case FILE:
                digest = getString(context, file);
                break;
            default:
                LogUtil.e(TAG, "error, unknow type");
                return "";
        }

        return digest;
    }

    static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * get top mActivity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return runningTaskInfos.get(0).topActivity.getClassName();
        else
            return "";
    }

    /**
     * set initial letter of according user's nickname( username if no nickname)
     *
     * @param user
     */
    public static void setUserInitialLetter(ChatUser user) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;

        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
                    Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }

        if (!TextUtils.isEmpty(user.getNick())) {
            letter = new GetInitialLetter().getLetter(user.getNick());
            user.setInitialLetter(letter);
            return;
        }
        if (letter.equals(DefaultLetter) && !TextUtils.isEmpty(user.getUsername())) {
            letter = new GetInitialLetter().getLetter(user.getUsername());
        }
        user.setInitialLetter(letter);
    }

    /**
     * EMMessage 转换为 BaseMessage
     *
     * @param emMessage
     * @return
     */
    public static BaseMessage emMessagetoBaseMessage(EMMessage emMessage) {
        EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
        String textMessage = body.getMessage();

        BaseMessage baseMessage = GsonHelper.fromJson(textMessage, BaseMessage.class);

        if (ChatConstant.MESSAGE_TARGET_SINGLE.equals(baseMessage.getMessageTarget())) {
            baseMessage.setConversationId(baseMessage.getSenderId());
        } else if (ChatConstant.MESSAGE_TARGET_GROUP.equals(baseMessage.getMessageTarget())) {
            baseMessage.setConversationId(baseMessage.getChatGroupId());
        }
        baseMessage.setShowTime(baseMessage.getMessageTime() == 0 ? 0 : baseMessage.getMessageTime());
        baseMessage.setOwner(MESSAGE_IS_OTHER);
        baseMessage.setSendingState(MESSAGE_STATUS_SUCCESS);

        return baseMessage;
    }

    /**
     * EMMessage 转换为 BaseMessage
     *
     * @param emMessage
     * @return
     */
    public static BaseMessage cmdMessagetoBaseMessage(EMMessage emMessage) {
        EMCmdMessageBody body = (EMCmdMessageBody) emMessage.getBody();
        String textMessage = body.action();
        BaseMessage baseMessage = GsonHelper.fromJson(textMessage, BaseMessage.class);

        if (ChatConstant.MESSAGE_TARGET_SINGLE.equals(baseMessage.getMessageTarget())) {
            baseMessage.setConversationId(baseMessage.getSenderId());
        } else if (ChatConstant.MESSAGE_TARGET_GROUP.equals(baseMessage.getMessageTarget())) {
            baseMessage.setConversationId(baseMessage.getChatGroupId());
        }
        baseMessage.setShowTime(baseMessage.getMessageTime() == 0 ? 0 : baseMessage.getMessageTime());
        baseMessage.setOwner(MESSAGE_IS_OTHER);
        baseMessage.setSendingState(MESSAGE_STATUS_SUCCESS);

        return baseMessage;
    }

    /**
     * ChatMessage 转换为 BaseMessage
     *
     * @param chatMessage
     * @return
     */
    public static BaseMessage emMessagetoBaseMessage(ChatMessage chatMessage) {
        BaseMessage baseMessage = new BaseMessage();
        MessageBody messageBody = GsonHelper.fromJson(chatMessage.getMessageBody(), MessageBody.class);

        baseMessage.setMessageContent(messageBody.getContent());
        baseMessage.setImageName(messageBody.getImageName());
        baseMessage.setImagePath(messageBody.getImagePath());
        baseMessage.setFullPath(messageBody.getImageUrl());
        baseMessage.setSize(messageBody.getImageSize());

        baseMessage.setFilePath(messageBody.getVoiceUrl());
        baseMessage.setLength(messageBody.getVoiceLength());
        baseMessage.setForm(messageBody.getVoiceForm());
        baseMessage.setVoiceName(messageBody.getVoiceName());
        baseMessage.setVoicePath(messageBody.getVoicePath() == null ? "" : messageBody.getVoicePath());
        baseMessage.setIsListened(messageBody.getIsListened());
        baseMessage.setRedEnvelopsId(messageBody.getRedEnvelopsId());
        baseMessage.setRedEnvelopsDesc(messageBody.getRedEnvelopsDesc());

        baseMessage.setMessageId(chatMessage.getMessageId());
        baseMessage.setMessageType(chatMessage.getMessageType());
        baseMessage.setMessageTime(chatMessage.getMessageTime());
        baseMessage.setIsRead(chatMessage.getIsRead());

        int chatType = chatMessage.getChatType();
        if (chatType == ChatConstant.TYPE_SINGLE) {
            baseMessage.setMessageTarget(ChatConstant.MESSAGE_TARGET_SINGLE);
        } else if (chatType == ChatConstant.TYPE_GROUP) {
            baseMessage.setMessageTarget(ChatConstant.MESSAGE_TARGET_GROUP);
        }

        baseMessage.setConversationId(chatMessage.getConversationId());
        baseMessage.setSenderId(chatMessage.getSenderId());
        baseMessage.setChatGroupId(chatMessage.getChatGroupId() == 0 ? 0 : chatMessage.getChatGroupId());
        baseMessage.setReceiverId(chatMessage.getReceiverId() == 0 ? 0 : chatMessage.getReceiverId());
        baseMessage.setShowTime(chatMessage.getShowTime());
        baseMessage.setOwner(chatMessage.getOwner());
        baseMessage.setSendingState(chatMessage.getSendingState());


        return baseMessage;
    }

    /**
     * BaseMessage 转换为 ChatMessage
     *
     * @param baseMessage
     * @return
     */
    public static ChatMessage toChatMessage(BaseMessage baseMessage) {
        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setMessageId(baseMessage.getMessageId());
        chatMessage.setMessageType(baseMessage.getMessageType());
        chatMessage.setMessageTime(baseMessage.getMessageTime());
        chatMessage.setShowTime(baseMessage.getShowTime());
        chatMessage.setOwner(baseMessage.getOwner());
        chatMessage.setIsRead(baseMessage.getIsRead());
        chatMessage.setConversationId(baseMessage.getConversationId());

        MessageBody messageBody = new MessageBody();

        messageBody.setContent(baseMessage.getMessageContent());
        messageBody.setImageSize(baseMessage.getSize());
        messageBody.setImageName(baseMessage.getImageName());
        messageBody.setImageUrl(baseMessage.getFullPath());
        messageBody.setImagePath(baseMessage.getImagePath());

        messageBody.setVoiceName(baseMessage.getVoiceName());
        messageBody.setVoiceForm(baseMessage.getForm());
        messageBody.setVoicePath(baseMessage.getVoicePath() == null ? "" : baseMessage.getVoicePath());

        messageBody.setVoiceUrl(baseMessage.getFilePath());
        messageBody.setVoiceLength(baseMessage.getLength());
        messageBody.setIsListened(baseMessage.getIsListened());

        messageBody.setRedEnvelopsId(baseMessage.getRedEnvelopsId());
        messageBody.setRedEnvelopsDesc(baseMessage.getRedEnvelopsDesc());

        chatMessage.setMessageBody(GsonHelper.toJson(messageBody));


        String messageTarget = baseMessage.getMessageTarget();
        if (messageTarget.equals(ChatConstant.MESSAGE_TARGET_SINGLE)) {
            chatMessage.setChatType(ChatConstant.TYPE_SINGLE);
        } else if (messageTarget.equals(ChatConstant.MESSAGE_TARGET_GROUP)) {
            chatMessage.setChatType(ChatConstant.TYPE_GROUP);
        }


        chatMessage.setSenderId(baseMessage.getSenderId());
        chatMessage.setReceiverId(baseMessage.getReceiverId());
        chatMessage.setChatGroupId(baseMessage.getChatGroupId());
        chatMessage.setSendingState(baseMessage.getSendingState());

        return chatMessage;
    }

    /**
     * 业务聊天3种类型，转化为单聊或者群聊(int)
     *
     * @param contactType
     * @return
     */
    public static int getChatType(String contactType) {
        int type;
        if (contactType.equals(ChatConstant.CHAT_TYPE_USER)) {
            type = ChatConstant.TYPE_SINGLE;
        } else {
            type = ChatConstant.TYPE_GROUP;
        }

        return type;
    }

    /**
     * 判断消息类型是否是通知
     *
     * @return
     */
    public static boolean isNotification(BaseMessage baseMessage) {
        if (baseMessage.getMessageType() == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否在主线程
     *
     * @return
     */
    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 获取用户UserId
     *
     * @return
     */
    public static long getUserId() {
        String userInfoJson = SPHelper.getString(ChatConstant.USER_INFO, "");//获取当前用户资料
        if (userInfoJson.length() > 0) {

            UserInfo userInfo = GsonHelper.fromJson(userInfoJson, UserInfo.class);
            return userInfo.getUserId();
        } else {
            return -1;
        }
    }

    public static void saveUnreadNotification(String notifyType) {
        if (ChatConstant.NOTIFY_TYPE_ADD_FRIEND_NOTIFY.equals(notifyType)) {
            SPHelper.putInt(ChatConstant.NOTIFY_TYPE_ADD_FRIEND_NOTIFY, 1);
        } else if (ChatConstant.NOTIFY_TYPE_SYSTEM_NOTIFY.equals(notifyType)) {
            SPHelper.putInt(ChatConstant.NOTIFY_TYPE_SYSTEM_NOTIFY, 1);
        } else if (ChatConstant.NOTIFY_TYPE_COMMENT_NOTIFY.equals(notifyType)) {
            SPHelper.putInt(ChatConstant.NOTIFY_TYPE_COMMENT_NOTIFY, 1);
        }
    }

    /**
     * 聊天页面加载图片
     *
     * @param context
     * @param file
     * @param imageView
     */
    public static void loadChatImage(final Context context, final File file, final ImageView imageView) {
        Glide.with(context)
                .load(file)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int imageWidth = resource.getWidth();
                        int imageHeight = resource.getHeight();

                        int width = ScreenHelper.getScreenWidth(context) / 4;
                        int height = (int) (width * imageHeight * 1.0 / imageWidth);

                        if (height > 2 * width) {
                            height = 2 * width;
                        }

                        ViewGroup.LayoutParams para = imageView.getLayoutParams();

                        para.height = height;
                        para.width = width;

                        imageView.setLayoutParams(para);

                        imageView.setImageBitmap(resource);
                    }
                });
    }

    /**
     * 聊天页面加载图片
     *
     * @param context
     * @param imageUrl
     * @param imageView
     */
    public static void loadChatImage(final Context context, final String imageUrl, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int imageWidth = resource.getWidth();
                        int imageHeight = resource.getHeight();

                        int width = ScreenHelper.getScreenWidth(context) / 4;
                        int height = (int) (width * imageHeight * 1.0 / imageWidth);

                        if (height > 2 * width) {
                            height = 2 * width;
                        }

                        ViewGroup.LayoutParams para = imageView.getLayoutParams();

                        para.height = height;
                        para.width = width;

                        imageView.setLayoutParams(para);

                        imageView.setImageBitmap(resource);

                    }
                });
    }

}
