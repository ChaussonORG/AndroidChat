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
package com.sudaotech.chatlibrary.widget.chatrow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.widget.ChatMessageList;

import java.util.List;


public class MessageAdapter extends BaseAdapter {

    public static final int ITEM_TYPE_COUNT = 6;
    private final static String TAG = "msg";
    private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
    private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
    private static final int HANDLER_MESSAGE_SEEK_TO = 2;

    private static final int MESSAGE_TYPE_RECV_TXT = 0;
    private static final int MESSAGE_TYPE_SENT_TXT = 1;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 2;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 3;
    private static final int MESSAGE_TYPE_RECV_VOICE = 4;
    private static final int MESSAGE_TYPE_SENT_VOICE = 5;

    private List<BaseMessage> mMessages;
    private Context mContext;

    private long toChatUserId;

    private ChatMessageList.MessageListItemClickListener itemClickListener;
    private ChatRowProvider mChatRowProvider;

    private boolean showUserNick;
    private boolean showAvatar;
    private Drawable myBubbleBg;
    private Drawable otherBuddleBg;

    private ListView mListView;
    Handler handler = new Handler() {
        private void refreshList() {
            // you should not call getAllMessages() in UI thread
            // otherwise there is problem when refreshing UI and there is new mBaseMessage arrive

//			java.util.List<BaseMessage> var = conversation.getAllMessages();
//			mMessages = var.toArray(new BaseMessage[var.size()]);
//			conversation.markAllMessagesAsRead();


            notifyDataSetChanged();
        }

        @Override
        public void handleMessage(android.os.Message message) {
            switch (message.what) {
                case HANDLER_MESSAGE_REFRESH_LIST:
                    refreshList();
                    break;
                case HANDLER_MESSAGE_SELECT_LAST:
                    if (mMessages.size() > 0) {
                        mListView.setSelection(mMessages.size() - 1);
                    }
                    break;
                case HANDLER_MESSAGE_SEEK_TO:
                    int position = message.arg1;
                    mListView.setSelection(position);
                    break;
                default:
                    break;
            }
        }
    };

    public MessageAdapter(List<BaseMessage> messages, Context context, long toChatUserId, int chatType, ListView listView) {
        mMessages = messages;
        this.mContext = context;
        this.mListView = listView;
        this.toChatUserId = toChatUserId;
    }

    public void refresh() {
        if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
            return;
        }
        Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
        handler.sendMessage(msg);
    }

    /**
     * refresh and select the last
     */
    public void refreshSelectLast() {
        final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
        handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
        handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
        handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);
    }

    /**
     * refresh and seek to the mPosition
     */
    public void refreshSeekTo(int position) {
        handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
        android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
        msg.arg1 = position;
        handler.sendMessage(msg);
    }


    public BaseMessage getItem(int position) {
        if (mMessages != null && position < mMessages.size()) {
            return mMessages.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * get count of mMessages
     */
    public int getCount() {
        return mMessages == null ? 0 : mMessages.size();
    }

    /**
     * get number of mBaseMessage type, here 14 = (BaseMessage.Type) * 2
     */
    public int getViewTypeCount() {
        if (mChatRowProvider != null && mChatRowProvider.getCustomChatRowTypeCount() > 0) {
            return mChatRowProvider.getCustomChatRowTypeCount() + ITEM_TYPE_COUNT;
        }
        return ITEM_TYPE_COUNT;
    }


    /**
     * get type of item
     */
    public int getItemViewType(int position) {
        BaseMessage message = getItem(position);
        if (message == null) {
            return -1;
        }
        if (mChatRowProvider != null && mChatRowProvider.getCustomChatRowType(message) > 0) {
            return mChatRowProvider.getCustomChatRowType(message) + ITEM_TYPE_COUNT - 1;
        }
        int owner = message.getOwner();

        if (message.getMessageType().equals(ChatConstant.MESSAGE_TYPE_TEXT)) {
            return owner == ChatConstant.MESSAGE_IS_OTHER ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
        } else if (message.getMessageType().equals(ChatConstant.MESSAGE_TYPE_IMAGE)) {
            return owner == ChatConstant.MESSAGE_IS_OTHER ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;
        } else if (message.getMessageType().equals(ChatConstant.MESSAGE_TYPE_AUDIO)) {
            return owner == ChatConstant.MESSAGE_IS_OTHER ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
        }

        return -1;// invalid
    }

    @SuppressLint("NewApi")
    public View getView(final int position, View convertView, ViewGroup parent) {
        BaseMessage message = getItem(position);
//        BaseMessage.ChatType mChatType = message.getChatType();
        if (convertView == null) {
            convertView = createChatRow(mContext, message, position);
        }

        //refresh ui with mMessages
        ((BaseChatRow) convertView).setUpView(message, position, itemClickListener);

        return convertView;
    }


    protected BaseChatRow createChatRow(Context context, BaseMessage message, int position) {
        BaseChatRow chatRow = null;
        if (mChatRowProvider != null && mChatRowProvider.getCustomChatRow(message, position, this) != null) {
            return mChatRowProvider.getCustomChatRow(message, position, this);
        }
        if (ChatConstant.MESSAGE_TYPE_TEXT.equals(message.getMessageType())) {
            chatRow = new ChatRowText(context, message, position, this);
        } else if (ChatConstant.MESSAGE_TYPE_IMAGE.equals(message.getMessageType())) {
            chatRow = new ChatRowImage(context, message, position, this);
        } else if (ChatConstant.MESSAGE_TYPE_AUDIO.equals(message.getMessageType())) {
            chatRow = new ChatRowVoice(context, message, position, this);

        }

        return chatRow;
    }


    public long getToChatUserId() {
        return toChatUserId;
    }

    public void setItemClickListener(ChatMessageList.MessageListItemClickListener listener) {
        itemClickListener = listener;
    }

    public void setCustomChatRowProvider(ChatRowProvider rowProvider) {
        mChatRowProvider = rowProvider;
    }

    public boolean isShowUserNick() {
        return showUserNick;
    }

    public void setShowUserNick(boolean showUserNick) {
        this.showUserNick = showUserNick;
    }

    public boolean isShowAvatar() {
        return showAvatar;
    }

    public void setShowAvatar(boolean showAvatar) {
        this.showAvatar = showAvatar;
    }

    public Drawable getMyBubbleBg() {
        return myBubbleBg;
    }

    public void setMyBubbleBg(Drawable myBubbleBg) {
        this.myBubbleBg = myBubbleBg;
    }

    public Drawable getOtherBuddleBg() {
        return otherBuddleBg;
    }

    public void setOtherBuddleBg(Drawable otherBuddleBg) {
        this.otherBuddleBg = otherBuddleBg;
    }

}
