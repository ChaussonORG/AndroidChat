package com.sudaotech.chatlibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMConversation;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.widget.chatrow.ChatRowProvider;
import com.sudaotech.chatlibrary.widget.chatrow.MessageAdapter;

import java.util.List;


public class ChatMessageList extends RelativeLayout {

    protected static final String TAG = "ChatMessageList";
    protected ListView listView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected Context context;
    protected EMConversation conversation;
    protected int chatType;
    protected long toChatUserId;
    protected MessageAdapter messageAdapter;
    protected boolean showUserNick;
    protected boolean showAvatar;
    protected Drawable myBubbleBg;
    protected Drawable otherBuddleBg;

    public ChatMessageList(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public ChatMessageList(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseStyle(context, attrs);
        init(context);
    }

    public ChatMessageList(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.list_chat_message, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
        listView = (ListView) findViewById(R.id.list);
    }

    /**
     * init widget
     *
     * @param messages
     * @param toChatUserId
     * @param chatType
     */
    public void init(List<BaseMessage> messages, long toChatUserId, int chatType, ChatRowProvider chatRowProvider) {
        this.chatType = chatType;
        this.toChatUserId = toChatUserId;

//        conversation = EMClient.getInstance().chatManager().getConversation(mToChatUserId, ChatUtil.getConversationType(mChatType), true);
        messageAdapter = new MessageAdapter(messages, context, toChatUserId, chatType, listView);
        messageAdapter.setShowAvatar(showAvatar);
        messageAdapter.setShowUserNick(showUserNick);
        messageAdapter.setMyBubbleBg(myBubbleBg);
        messageAdapter.setOtherBuddleBg(otherBuddleBg);
        messageAdapter.setCustomChatRowProvider(chatRowProvider);
        // set mBaseMessage mBaseAdapter
        listView.setAdapter(messageAdapter);

        refreshSelectLast();
    }

    protected void parseStyle(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChatMessageList);
        showAvatar = ta.getBoolean(R.styleable.ChatMessageList_msgListShowUserAvatar, true);
        myBubbleBg = ta.getDrawable(R.styleable.ChatMessageList_msgListMyBubbleBackground);
        otherBuddleBg = ta.getDrawable(R.styleable.ChatMessageList_msgListMyBubbleBackground);
        showUserNick = ta.getBoolean(R.styleable.ChatMessageList_msgListShowUserNick, false);
        ta.recycle();
    }

    /**
     * refresh
     */
    public void refresh() {
        if (messageAdapter != null) {
            messageAdapter.refresh();
        }
    }

    /**
     * refresh and jump to the last
     */
    public void refreshSelectLast() {
        if (messageAdapter != null) {
            messageAdapter.refreshSelectLast();
        }
    }

    /**
     * refresh and jump to the mPosition
     *
     * @param position
     */
    public void refreshSeekTo(int position) {
        if (messageAdapter != null) {
            messageAdapter.refreshSeekTo(position);
        }
    }

    public ListView getListView() {
        return listView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public BaseMessage getItem(int position) {
        return messageAdapter.getItem(position);
    }

    public boolean isShowUserNick() {
        return showUserNick;
    }

    public void setShowUserNick(boolean showUserNick) {
        this.showUserNick = showUserNick;
    }

    /**
     * set click listener
     *
     * @param listener
     */
    public void setItemClickListener(MessageListItemClickListener listener) {
        if (messageAdapter != null) {
            messageAdapter.setItemClickListener(listener);
        }
    }

    public interface MessageListItemClickListener {
        void onResendClick(BaseMessage message);

        /**
         * there is default handling when bubble is clicked, if you want handle it, return true
         * another way is you implement in onBubbleClick() of chat row
         *
         * @param message
         * @return
         */
        boolean onBubbleClick(BaseMessage message);

        void onBubbleLongClick(BaseMessage message);

        void onUserAvatarClick(long userId);

        void onUserAvatarLongClick(long userId);
    }
}
