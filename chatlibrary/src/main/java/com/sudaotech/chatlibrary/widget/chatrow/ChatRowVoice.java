package com.sudaotech.chatlibrary.widget.chatrow;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sudao.basemodule.common.util.LogUtil;
import com.sudao.basemodule.component.file.FileDownloader;
import com.sudao.basemodule.component.file.OnDownloadListener;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.db.MessageDao;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.utils.ChatPathUtil;
import com.sudaotech.chatlibrary.utils.ChatUtil;


public class ChatRowVoice extends ChatRowFile {

    private FileDownloader mFileDownloader;
    private MessageDao mMessageDao;
    private ImageView voiceImageView;
    private TextView voiceLengthView;
    private ImageView readStatusView;

    public ChatRowVoice(Context context, BaseMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        mFileDownloader = new FileDownloader(context);
        mMessageDao = new MessageDao(context);
    }

    @Override
    protected void onInflateView() {
        mLayoutInflater.inflate(mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER ?
                R.layout.item_row_received_voice : R.layout.item_row_sent_voice, this);
    }

    @Override
    protected void onFindViewById() {
        voiceImageView = ((ImageView) findViewById(R.id.iv_voice));
        voiceLengthView = (TextView) findViewById(R.id.tv_length);
        readStatusView = (ImageView) findViewById(R.id.iv_unread_voice);
    }

    @Override
    protected void onSetUpView() {
//        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
        int len = mBaseMessage.getLength();
        if (len > 0) {
            voiceLengthView.setText(mBaseMessage.getLength() + "\"");
            voiceLengthView.setVisibility(View.VISIBLE);
        } else {
            voiceLengthView.setVisibility(View.INVISIBLE);
        }
        if (ChatRowVoicePlayClickListener.playMsgId != 0
                && ChatRowVoicePlayClickListener.playMsgId == mBaseMessage.getMessageId() && ChatRowVoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
            if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER) {
                voiceImageView.setImageResource(R.drawable.voice_from_icon);
            } else {
                voiceImageView.setImageResource(R.drawable.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) voiceImageView.getDrawable();
            voiceAnimation.start();
        } else {
            if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER) {
                voiceImageView.setImageResource(R.drawable.chat_from_voice_playing);
            } else {
                voiceImageView.setImageResource(R.drawable.chat_to_voice_playing);
            }
        }

        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER) {
            if (mBaseMessage.getIsListened() == ChatConstant.VOICE_HAVE_LISTENED) {
                // hide the unread icon
                readStatusView.setVisibility(View.INVISIBLE);
            } else {
                readStatusView.setVisibility(View.VISIBLE);
                mBaseMessage.setSendingState(ChatConstant.MESSAGE_STATUS_INPROGRESS);
                downloadVoice();
            }
            LogUtil.d(TAG, "it is receive msg");
//            if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
//                    voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
//                progressBar.setVisibility(View.VISIBLE);
//                setMessageReceiveCallback();
//            } else {
//                progressBar.setVisibility(View.INVISIBLE);
//
//            }
            return;
        }

        // until here, handle sending voice message
        handleSendMessage();
    }

    /**
     * 下载声音
     */
    private void downloadVoice() {
        mFileDownloader.onStartDownload(mBaseMessage.getFilePath(), mBaseMessage.getVoiceName(), ChatPathUtil.getInstance().getVoicePath());
        mFileDownloader.setOnDownloadListener(new OnDownloadListener() {
            @Override
            public void onDownloadComplete(String file) {
                mBaseMessage.setSendingState(ChatConstant.MESSAGE_STATUS_SUCCESS);
                mBaseMessage.setVoicePath(ChatPathUtil.getInstance().getVoicePath() + mBaseMessage.getVoiceName());
                mMessageDao.insertMessage(ChatUtil.toChatMessage(mBaseMessage));
            }

            @Override
            public void onUpdateProgress(int progress) {

            }

            @Override
            public void onDownloadFailure() {

            }
        });
    }

    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }

    @Override
    protected void onBubbleClick() {
        new ChatRowVoicePlayClickListener(mBaseMessage, voiceImageView, readStatusView, mBaseAdapter, mActivity).onClick(bubbleLayout);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (ChatRowVoicePlayClickListener.currentPlayListener != null && ChatRowVoicePlayClickListener.isPlaying) {
            ChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }
    }

}
