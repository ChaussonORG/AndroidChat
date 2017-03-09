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

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage.ChatType;
import com.sudao.basemodule.common.util.LogUtil;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.constroller.ChatUI;
import com.sudaotech.chatlibrary.db.MessageDao;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.utils.ChatUtil;

import java.io.File;

/**
 * 语音row播放点击事件监听
 */
public class ChatRowVoicePlayClickListener implements View.OnClickListener {
    private static final String TAG = "VoicePlayClickListener";
    public static boolean isPlaying = false;
    public static ChatRowVoicePlayClickListener currentPlayListener = null;
    public static long playMsgId;
    private final MessageDao mMessageDao;
    private BaseMessage mBaseMessage;
    //	EMVoiceMessageBody voiceBody;
    private ImageView voiceIconView;
    private AnimationDrawable voiceAnimation = null;
    private MediaPlayer mediaPlayer = null;
    private ImageView iv_read_status;
    private Activity activity;
    private ChatType chatType;
    private BaseAdapter adapter;

    public ChatRowVoicePlayClickListener(BaseMessage message, ImageView v, ImageView iv_read_status, BaseAdapter adapter, Activity context) {
        this.mBaseMessage = message;
//        voiceBody = (EMVoiceMessageBody) message.getBody();
        this.iv_read_status = iv_read_status;
        this.adapter = adapter;
        voiceIconView = v;
        this.activity = context;
        mMessageDao = new MessageDao(context);
//        this.chatType = message.getMessageTarget();
    }

    public void stopPlayVoice() {
        voiceAnimation.stop();
        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER) {
            voiceIconView.setImageResource(R.drawable.chat_from_voice_playing);
        } else {
            voiceIconView.setImageResource(R.drawable.chat_to_voice_playing);
        }
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
        playMsgId = 0;
        adapter.notifyDataSetChanged();
    }

    public void playVoice(String filePath) {
        if (!(new File(filePath).exists())) {
            return;
        }
        playMsgId = mBaseMessage.getMessageId();
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = new MediaPlayer();
        if (ChatUI.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(); // stop animation
                }

            });
            isPlaying = true;
            currentPlayListener = this;
            mediaPlayer.start();
            showAnimation();

            // 如果是接收的消息
            if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER) {

                /*if (!message.isListened() && iv_read_status != null && iv_read_status.getVisibility() == View.VISIBLE) {
                    // 隐藏自己未播放这条语音消息的标志
                    iv_read_status.setVisibility(View.INVISIBLE);
                    message.setListened(true);
                    EMClient.getInstance().chatManager().setMessageListened(message);
                }*/
            }

        } catch (Exception e) {
            System.out.println();
        }
    }

    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation
        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER) {
            voiceIconView.setImageResource(R.drawable.voice_from_icon);
        } else {
            voiceIconView.setImageResource(R.drawable.voice_to_icon);
        }
        voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
        voiceAnimation.start();
    }

    @Override
    public void onClick(View v) {
        String st = activity.getResources().getString(R.string.Is_download_voice_click_later);
        if (isPlaying) {
            if (playMsgId != 0 && playMsgId == mBaseMessage.getMessageId()) {
                currentPlayListener.stopPlayVoice();
                return;
            }
            currentPlayListener.stopPlayVoice();
        }

        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OWNER) {
            // for sent msg, we will try to play the voice file directly
            playVoice(mBaseMessage.getVoicePath());
        } else {
            if (mBaseMessage.getSendingState() == ChatConstant.MESSAGE_STATUS_SUCCESS) {
                File file = new File(mBaseMessage.getVoicePath());
                if (file.exists() && file.isFile()) {
                    playVoice(mBaseMessage.getVoicePath());
                    mBaseMessage.setIsListened(ChatConstant.VOICE_HAVE_LISTENED);
                    mMessageDao.insertMessage(ChatUtil.toChatMessage(mBaseMessage));
                } else {
                    LogUtil.e(TAG, "file not exist");
                }

            } else if (mBaseMessage.getSendingState() == ChatConstant.MESSAGE_STATUS_INPROGRESS) {
                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
            } else if (mBaseMessage.getSendingState() == ChatConstant.MESSAGE_STATUS_FAIL) {
                Toast.makeText(activity, st, Toast.LENGTH_SHORT).show();
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
//                        EMClient.getInstance().chatManager().downloadAttachment(message);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        adapter.notifyDataSetChanged();
                    }

                }.execute();

            }

        }
    }
}
