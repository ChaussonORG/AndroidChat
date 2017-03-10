package com.sudaotech.chatlibrary.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.util.DateUtils;
import com.sudao.basemodule.common.util.GsonHelper;
import com.sudao.basemodule.common.util.ImageHelper;
import com.sudao.basemodule.common.util.SPHelper;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.MessageCallback;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.db.UserDao;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.model.MessageExtend;
import com.sudaotech.chatlibrary.model.UserInfo;
import com.sudaotech.chatlibrary.widget.ChatMessageList;

import java.util.Date;
import java.util.Map;


public abstract class BaseChatRow extends LinearLayout {
    protected static final String TAG = BaseChatRow.class.getSimpleName();
    private final UserDao mUserDao;

    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected BaseAdapter mBaseAdapter;
    protected BaseMessage mBaseMessage;
    protected int mPosition;

    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected Activity mActivity;

//    protected TextView ackedView;
//    protected TextView deliveredView;

    protected MessageCallback messageSendCallback;
    protected MessageCallback messageReceiveCallback;

    protected ChatMessageList.MessageListItemClickListener itemClickListener;
    private UserInfo mUserInfo;

    public BaseChatRow(Context context, BaseMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.mContext = context;
        this.mActivity = (Activity) context;
        this.mBaseMessage = message;
        this.mPosition = position;
        this.mBaseAdapter = adapter;
        mLayoutInflater = LayoutInflater.from(context);
        mUserDao = new UserDao(context);
        initView();
    }

    private void initView() {
        onInflateView();
        String userInfoJson = SPHelper.getString(ChatConstant.USER_INFO, "");
        if (userInfoJson.length() > 0) {
            mUserInfo = GsonHelper.fromJson(userInfoJson, UserInfo.class);
        }
        timeStampView = (TextView) findViewById(R.id.timestamp);
        userAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
//        ackedView = (TextView) findViewById(R.id.tv_ack);
//        deliveredView = (TextView) findViewById(R.id.tv_delivered);

        onFindViewById();
    }

    /**
     * set property according mBaseMessage and postion
     *
     * @param message
     * @param position
     */
    public void setUpView(BaseMessage message, int position, ChatMessageList.MessageListItemClickListener itemClickListener) {
        this.mBaseMessage = message;
        this.mPosition = position;
        this.itemClickListener = itemClickListener;

        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

    private void setUpBaseView() {
        // set nickname, avatar and background of bubble
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (mPosition == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(mBaseMessage.getMessageTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // show time stamp if interval with last mBaseMessage is > 30 seconds
                BaseMessage prevMessage = (BaseMessage) mBaseAdapter.getItem(mPosition - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(mBaseMessage.getMessageTime(), prevMessage.getMessageTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setVisibility(View.VISIBLE);
                    timestamp.setText(DateUtils.getTimestampString(new Date(mBaseMessage.getMessageTime())));
                }
            }
        }
        //set nickname and avatar
        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OWNER) {
            if (mUserInfo != null) {
                ImageHelper.load(mContext, mUserInfo.getPhoto(), userAvatarView);
            }
        } else {
            if (mUserDao.queryUser(mBaseMessage.getSenderId() + ChatConstant.CONTACT_TYPE_USER)) {
                UserInfo userInfo = mUserDao.findUser(mBaseMessage.getSenderId() + ChatConstant.CONTACT_TYPE_USER);
                if (userInfo != null) {
                    ImageHelper.load(mContext, userInfo.getPhoto(), userAvatarView);
                    usernickView.setText(userInfo.getName());
                }
            } else {
                Map<String, Object> extMap = mBaseMessage.getExtMap();
                if (extMap != null) {
                    String photo = (String) extMap.get(MessageExtend.EXT_PHOTO);
                    String name = (String) extMap.get(MessageExtend.EXT_NAME);
                    ImageHelper.load(mContext, photo, userAvatarView);
                    usernickView.setText(name);

                    UserInfo userInfo = new UserInfo();
                    userInfo.setContactId(mBaseMessage.getSenderId() + ChatConstant.CONTACT_TYPE_USER);
                    userInfo.setUserId(mBaseMessage.getSenderId());
                    userInfo.setContactType(ChatConstant.TYPE_SINGLE);
                    userInfo.setPhoto(photo);
                    userInfo.setName(name);
                    mUserDao.insertUser(userInfo);
                }

            }

        }

//        if (deliveredView != null) {
//            if (mBaseMessage.isDelivered()) {
//                deliveredView.setVisibility(View.VISIBLE);
//            } else {
//                deliveredView.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        if (ackedView != null) {
//            if (mBaseMessage.isAcked()) {
//                if (deliveredView != null) {
//                    deliveredView.setVisibility(View.INVISIBLE);
//                }
//                ackedView.setVisibility(View.VISIBLE);
//            } else {
//                ackedView.setVisibility(View.INVISIBLE);
//            }
//        }


        if (mBaseAdapter instanceof MessageAdapter) {
            if (((MessageAdapter) mBaseAdapter).isShowAvatar())
                userAvatarView.setVisibility(View.VISIBLE);
            else
                userAvatarView.setVisibility(View.GONE);
            if (usernickView != null) {
                if (((MessageAdapter) mBaseAdapter).isShowUserNick())
                    usernickView.setVisibility(View.VISIBLE);
                else
                    usernickView.setVisibility(View.GONE);
            }
            if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OWNER) {
                if (((MessageAdapter) mBaseAdapter).getMyBubbleBg() != null) {
                    bubbleLayout.setBackgroundDrawable(((MessageAdapter) mBaseAdapter).getMyBubbleBg());
                }
            } else if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER) {
                if (((MessageAdapter) mBaseAdapter).getOtherBuddleBg() != null) {
                    bubbleLayout.setBackgroundDrawable(((MessageAdapter) mBaseAdapter).getOtherBuddleBg());
                }
            }
        }
    }

    /**
     * set callback for sending mBaseMessage
     */
    protected void setMessageSendCallback() {
        if (messageSendCallback == null) {
            messageSendCallback = new MessageCallback() {
                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onError(int var1, String var2) {
                    updateView();

                }

                @Override
                public void onProgress(final int progress, String status) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (percentageView != null)
                                percentageView.setText(progress + "%");

                        }
                    });
                }
            };
        }

        mBaseMessage.setMessageStatusCallback(messageSendCallback);
    }

    /**
     * set callback for receiving mBaseMessage
     */
    protected void setMessageReceiveCallback() {
        if (messageReceiveCallback == null) {
            messageReceiveCallback = new MessageCallback() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    mActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (percentageView != null) {
                                percentageView.setText(progress + "%");
                            }
                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        mBaseMessage.setMessageStatusCallback(messageReceiveCallback);
    }


    private void setClickListener() {
        if (bubbleLayout != null) {
            bubbleLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (!itemClickListener.onBubbleClick(mBaseMessage)) {
                            // if listener return false, we call default handling
                            onBubbleClick();
                        }
                    }
                }
            });

            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(mBaseMessage);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onResendClick(mBaseMessage);
                    }
                }
            });
        }

        if (userAvatarView != null) {
            userAvatarView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OWNER) {
                            itemClickListener.onUserAvatarClick(mBaseMessage.getSenderId());
                        } else {
                            if (ChatConstant.MESSAGE_TARGET_SINGLE.equals(mBaseMessage.getMessageTarget())) {
                                itemClickListener.onUserAvatarClick(mBaseMessage.getReceiverId());
                            } else {
                                itemClickListener.onUserAvatarClick(mBaseMessage.getSenderId());
                            }
                        }
                    }
                }
            });
            userAvatarView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OWNER) {
                            itemClickListener.onUserAvatarLongClick(mBaseMessage.getSenderId());
                        } else {
                            if (ChatConstant.MESSAGE_TARGET_SINGLE.equals(mBaseMessage.getMessageTarget())) {
                                itemClickListener.onUserAvatarClick(mBaseMessage.getReceiverId());
                            } else {
                                itemClickListener.onUserAvatarClick(mBaseMessage.getSenderId());
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }


    protected void updateView() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                if (mBaseMessage.getSendingState() == ChatConstant.MESSAGE_STATUS_FAIL) {

//                    if (mBaseMessage.getError() == ChatError.MESSAGE_INCLUDE_ILLEGAL_CONTENT) {
//                        Toast.makeText(mActivity, mActivity.getString(R.string.send_fail) + mActivity.getString(R.string.error_send_invalid_content), Toast.LENGTH_SHORT).show();
//                    } else if (mBaseMessage.getError() == ChatError.GROUP_NOT_JOINED) {
//                        Toast.makeText(mActivity, mActivity.getString(R.string.send_fail) + mActivity.getString(R.string.error_send_not_in_the_group), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(mActivity, mActivity.getString(R.string.send_fail) + mActivity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
//                    }
                }

                onUpdateView();
            }
        });

    }

    protected abstract void onInflateView();

    /**
     * find view by id
     */
    protected abstract void onFindViewById();

    /**
     * refresh list view when mBaseMessage status change
     */
    protected abstract void onUpdateView();

    /**
     * setup view
     */
    protected abstract void onSetUpView();

    /**
     * on bubble clicked
     */
    protected abstract void onBubbleClick();

}
