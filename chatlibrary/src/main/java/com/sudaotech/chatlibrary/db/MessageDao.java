package com.sudaotech.chatlibrary.db;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.sudao.basemodule.basicapi.UserBean;
import com.sudao.basemodule.common.util.GsonHelper;
import com.sudao.basemodule.common.util.LogUtil;
import com.sudao.basemodule.common.util.SPHelper;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.model.ChatMessage;
import com.sudaotech.chatlibrary.utils.ChatUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Samuel on 2016/12/10 11:59
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class MessageDao {

    private final Realm mRealm;
    protected Context mContext;
    private long mUserId;


    public MessageDao(Context context) {
        mContext = context;
        String userInfoJson = SPHelper.getString(ChatConstant.USER_INFO, "");//获取当前用户资料
        if (userInfoJson.length() > 0) {
            UserBean userBean = GsonHelper.fromJson(userInfoJson, UserBean.class);
            mUserId = userBean.getUserId();
        }
        mRealm = RealmUtil.getInstance(context).getRealm("chat_message" + String.valueOf(mUserId) + ".realm");

    }


    public void insertMessage(final ChatMessage chatMessage) {
        if (ChatUtil.isInMainThread()) {
            long startTime = System.currentTimeMillis();   //获取开始时间
            mRealm.beginTransaction();
            mRealm.insertOrUpdate(chatMessage);
            mRealm.commitTransaction();
            long endTime = System.currentTimeMillis(); //获取结束时间
            LogUtil.e("插入一条数据时间： " + (endTime - startTime) + "ms");
        } else {

            ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();   //获取开始时间
                    mRealm.beginTransaction();
                    mRealm.insertOrUpdate(chatMessage);
                    mRealm.commitTransaction();
                    long endTime = System.currentTimeMillis(); //获取结束时间
                    LogUtil.e("插入一条数据时间： " + (endTime - startTime) + "ms");
                }
            });
        }
    }


    public void updateMessage(ChatMessage chatMessage) {

    }


    public void deleteOneMessage(ChatMessage chatMessage) {

    }


    public void deleteAllMessage() {

    }


    public RealmResults<ChatMessage> getMessages() {
        long startTime = System.currentTimeMillis();   //获取开始时间

        RealmResults<ChatMessage> chatMessages = mRealm.where(ChatMessage.class).findAllSorted("messageTime", Sort.DESCENDING);

        long endTime = System.currentTimeMillis(); //获取结束时间
        LogUtil.e("查询数据用时： " + (endTime - startTime) + "ms");

        return chatMessages;
    }

    public void insertTest(ChatMessage chatMessage) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(chatMessage);
        mRealm.commitTransaction();
    }

    public boolean queryMessage(String senderId) {
        mRealm.beginTransaction();

        RealmResults<ChatMessage> messages = mRealm.where(ChatMessage.class).equalTo("senderId", senderId).findAll();

        mRealm.commitTransaction();

        if (messages.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取聊天记录
     *
     * @param conversationId
     * @param type           1 单聊，2 群聊
     * @return
     */
    public RealmResults<ChatMessage> getMessagesById(long conversationId, int type) {
        long startTime = System.currentTimeMillis();   //获取开始时间
        RealmResults<ChatMessage> chatMessages;

        chatMessages = mRealm.where(ChatMessage.class).equalTo("conversationId", conversationId).equalTo("chatType", type).findAllSorted("messageTime", Sort.DESCENDING);


        long endTime = System.currentTimeMillis(); //获取结束时间
        LogUtil.e("查询数据用时： " + (endTime - startTime) + "ms");

        return chatMessages;
    }

    /**
     * 获取最新一条消息
     *
     * @param conversationId
     * @return
     */
    public ChatMessage getLatestMessage(long conversationId, int chatType) {
        mRealm.beginTransaction();

        RealmResults<ChatMessage> messages = mRealm.where(ChatMessage.class).equalTo("conversationId", conversationId).
                equalTo("chatType", chatType).findAllSorted("messageTime", Sort.DESCENDING);

        mRealm.commitTransaction();

        if (messages.size() > 0) {
            return messages.first();
        }
        return null;
    }

    /**
     * 获取所有未读消息
     *
     * @param conversationId
     * @param chatType
     * @return
     */
    public List<BaseMessage> getUnreadMessages(long conversationId, int chatType) {
        List<BaseMessage> baseMessages = new ArrayList<>();
        mRealm.beginTransaction();

        RealmResults<ChatMessage> messages = mRealm.where(ChatMessage.class).equalTo("conversationId", conversationId).
                equalTo("chatType", chatType).equalTo("isRead", ChatConstant.MESSAGE_UNREAD).findAll();

        mRealm.commitTransaction();

        for (ChatMessage message : messages) {
            baseMessages.add(ChatUtil.emMessagetoBaseMessage(message));
        }
        return baseMessages;
    }

    /**
     * 更新消息状态为已读
     *
     * @param chatMessage
     */
    public void updateMessageReadStatus(ChatMessage chatMessage) {
        mRealm.beginTransaction();
        mRealm.insertOrUpdate(chatMessage);
        mRealm.commitTransaction();
    }

}
