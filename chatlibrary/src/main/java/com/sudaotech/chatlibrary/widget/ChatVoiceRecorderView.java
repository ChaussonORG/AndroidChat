package com.sudaotech.chatlibrary.widget;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.sudao.basemodule.common.util.LogUtil;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.model.ChatVoiceRecorder;
import com.sudaotech.chatlibrary.utils.ChatPathUtil;
import com.sudaotech.chatlibrary.utils.ChatUtil;
import com.sudaotech.chatlibrary.widget.chatrow.ChatRowVoicePlayClickListener;

/**
 * Created by Samuel on 2016/12/3 15:11
 * Email:xuzhou40@gmail.com
 * desc:Voice recorder view
 */

/**
 * 聊天-录制声音UI
 */
public class ChatVoiceRecorderView extends RelativeLayout {
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected Drawable[] mDrawables;
    protected ChatVoiceRecorder mChatVoiceRecorder;

    protected PowerManager.WakeLock mWakeLock;
    protected ImageView mImageView;
    protected TextView mTvRecordingHint;

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // change image
            mImageView.setImageDrawable(mDrawables[msg.what]);
        }
    };

    public ChatVoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public ChatVoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatVoiceRecorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_voice_recorder, this);

        String voicePath = ChatPathUtil.getInstance().getVoicePath();
        LogUtil.e("voicePath " + voicePath);
        mImageView = (ImageView) findViewById(R.id.mic_image);
        mTvRecordingHint = (TextView) findViewById(R.id.recording_hint);

        mChatVoiceRecorder = new ChatVoiceRecorder(micImageHandler);

        // animation resources, used for recording
        mDrawables = new Drawable[]{
                getResources().getDrawable(R.drawable.chat_record_animate_01),
                getResources().getDrawable(R.drawable.chat_record_animate_02),
                getResources().getDrawable(R.drawable.chat_record_animate_03),
                getResources().getDrawable(R.drawable.chat_record_animate_04),
                getResources().getDrawable(R.drawable.chat_record_animate_05),
                getResources().getDrawable(R.drawable.chat_record_animate_06),
                getResources().getDrawable(R.drawable.chat_record_animate_07),
                getResources().getDrawable(R.drawable.chat_record_animate_08),
                getResources().getDrawable(R.drawable.chat_record_animate_09),
                getResources().getDrawable(R.drawable.chat_record_animate_10),
                getResources().getDrawable(R.drawable.chat_record_animate_11),
                getResources().getDrawable(R.drawable.chat_record_animate_12),
                getResources().getDrawable(R.drawable.chat_record_animate_13),
                getResources().getDrawable(R.drawable.chat_record_animate_14),};

        mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
    }

    /**
     * on speak button touched
     *
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, ChatVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    if (ChatRowVoicePlayClickListener.isPlaying) {
                        ChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
                    }
                    v.setPressed(true);
                    startRecording();
                } catch (Exception e) {
                    v.setPressed(false);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() < 0) {
                    showReleaseToCancelHint();
                } else {
                    showMoveUpToCancelHint();
                }
                return true;
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                if (event.getY() < 0) {
                    // discard the recorded audio.
                    discardRecording();
                } else {
                    // stop recording and send voice file
                    try {
                        int length = stopRecoding();
                        if (length > 0) {
                            if (recorderCallback != null) {
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            }
                        } else if (length == EMError.FILE_INVALID) {
                            Toast.makeText(mContext, R.string.Recording_without_permission, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, R.string.The_recording_time_is_too_short, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, R.string.send_failure_please, Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            default:
                discardRecording();
                return false;
        }
    }

    public void startRecording() {
        if (!ChatUtil.isSdcardExist()) {
            Toast.makeText(mContext, R.string.Send_voice_need_sdcard_support, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mWakeLock.acquire();
            this.setVisibility(View.VISIBLE);
            mTvRecordingHint.setText(mContext.getString(R.string.move_up_to_cancel));
            mTvRecordingHint.setBackgroundColor(Color.TRANSPARENT);
            mChatVoiceRecorder.startRecording(mContext);
        } catch (Exception e) {
            e.printStackTrace();
            if (mWakeLock.isHeld())
                mWakeLock.release();
            if (mChatVoiceRecorder != null)
                mChatVoiceRecorder.discardRecording();
            this.setVisibility(View.INVISIBLE);
            Toast.makeText(mContext, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void showReleaseToCancelHint() {
        mTvRecordingHint.setText(mContext.getString(R.string.release_to_cancel));
        mTvRecordingHint.setBackgroundResource(R.drawable.ease_recording_text_hint_bg);
    }

    public void showMoveUpToCancelHint() {
        mTvRecordingHint.setText(mContext.getString(R.string.move_up_to_cancel));
        mTvRecordingHint.setBackgroundColor(Color.TRANSPARENT);
    }

    public void discardRecording() {
        if (mWakeLock.isHeld())
            mWakeLock.release();
        try {
            // stop recording
            if (mChatVoiceRecorder.isRecording()) {
                mChatVoiceRecorder.discardRecording();
            }
            this.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (mWakeLock.isHeld())
            mWakeLock.release();
        return mChatVoiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return mChatVoiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return mChatVoiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return mChatVoiceRecorder.isRecording();
    }

    public interface ChatVoiceRecorderCallback {
        /**
         * on voice record complete
         *
         * @param voiceFilePath   录音完毕后的文件路径
         * @param voiceTimeLength 录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }
}
