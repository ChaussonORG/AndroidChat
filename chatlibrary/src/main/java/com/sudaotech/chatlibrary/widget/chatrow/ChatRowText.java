package com.sudaotech.chatlibrary.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.utils.SmileUtils;


public class ChatRowText extends BaseChatRow {

    private TextView contentView;

    public ChatRowText(Context context, BaseMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        mLayoutInflater.inflate(mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER ?
                R.layout.item_row_received_message : R.layout.item_row_sent_message, this);
    }

    @Override
    protected void onFindViewById() {
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
    }

    @Override
    public void onSetUpView() {

//        TextMessageBody txtBody = (TextMessageBody) mBaseMessage.getMessageBody();
        String messageContent = mBaseMessage.getMessageContent();
        Spannable span = SmileUtils.getSmiledText(mContext, messageContent);
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);

        handleTextMessage();
    }

    protected void handleTextMessage() {
        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OWNER) {
            setMessageSendCallback();
            switch (mBaseMessage.getSendingState()) {
                case ChatConstant.MESSAGE_STATUS_CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case ChatConstant.MESSAGE_STATUS_SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);

                    break;
                case ChatConstant.MESSAGE_STATUS_FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.VISIBLE);
                    break;
                case ChatConstant.MESSAGE_STATUS_INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onUpdateView() {
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {


    }


}
