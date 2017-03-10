package com.sudaotech.chatlibrary.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.util.FileUtils;
import com.hyphenate.util.TextFormater;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.MessageCallback;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.model.BaseMessage;

import java.io.File;

public class ChatRowFile extends BaseChatRow {

    protected TextView fileNameView;
    protected TextView fileSizeView;
    protected TextView fileStateView;

    protected MessageCallback sendfileCallBack;

    protected boolean isNotifyProcessed;
//    private NormalFileMessageBody fileMessageBody;

    public ChatRowFile(Context context, BaseMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        mLayoutInflater.inflate(mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER ?
                R.layout.item_row_received_file : R.layout.item_row_sent_file, this);
    }

    @Override
    protected void onFindViewById() {
        fileNameView = (TextView) findViewById(R.id.tv_file_name);
        fileSizeView = (TextView) findViewById(R.id.tv_file_size);
        fileStateView = (TextView) findViewById(R.id.tv_file_state);
        percentageView = (TextView) findViewById(R.id.percentage);
    }


    @Override
    protected void onSetUpView() {

        fileNameView.setText(mBaseMessage.getImageName());
        fileSizeView.setText(TextFormater.getDataSize(mBaseMessage.getSize()));
        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER) {
            File file = new File(mBaseMessage.getImagePath());
            if (file.exists()) {
                fileStateView.setText(R.string.Have_downloaded);
            } else {
                fileStateView.setText(R.string.Did_not_download);
            }
            return;
        }

        // until here, to sending mBaseMessage
        handleSendMessage();
    }

    /**
     * handle sending mBaseMessage
     */
    protected void handleSendMessage() {
        setMessageSendCallback();
        switch (mBaseMessage.getSendingState()) {
            case ChatConstant.MESSAGE_STATUS_SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                if (percentageView != null) {
                    percentageView.setVisibility(View.INVISIBLE);
                }
                statusView.setVisibility(View.INVISIBLE);
                break;
            case ChatConstant.MESSAGE_STATUS_FAIL:
                progressBar.setVisibility(View.INVISIBLE);
                if (percentageView != null) {
                    percentageView.setVisibility(View.INVISIBLE);
                }
                statusView.setVisibility(View.VISIBLE);
                break;
            case ChatConstant.MESSAGE_STATUS_INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                if (percentageView != null) {
                    percentageView.setVisibility(View.INVISIBLE);
//                    percentageView.setText(mBaseMessage.getProgress() + "%");
                }
                statusView.setVisibility(View.INVISIBLE);
                break;
            default:
                progressBar.setVisibility(View.INVISIBLE);
                if (percentageView != null) {
                    percentageView.setVisibility(View.INVISIBLE);
                }
                statusView.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void onUpdateView() {
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {

        File file = new File(mBaseMessage.getImagePath());
        if (file.exists()) {
            // open files if it exist
            FileUtils.openFile(file, (Activity) mContext);
        } else {
            // download the file
//            mContext.startActivity(new Intent(mContext, EaseShowNormalFileActivity.class).putExtra("msgbody", mBaseMessage.getBody()));
        }
//        if (mBaseMessage.getDirect() == BaseMessage.MESSAGE_IS_OTHER && !mBaseMessage.isAcked() && mBaseMessage.getChatType() == BaseMessage.ChatType.Chat) {
//            try {
////                EMClient.getInstance().chatManager().ackMessageRead(mBaseMessage.getFrom(), mBaseMessage.getMsgId());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }
}
