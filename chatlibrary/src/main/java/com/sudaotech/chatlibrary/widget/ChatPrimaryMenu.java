package com.sudaotech.chatlibrary.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sudaotech.chatlibrary.R;


/**
 * Created by Samuel on 2016/12/3 15:56
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class ChatPrimaryMenu extends BaseChatPrimaryMenu implements View.OnClickListener {
    private LinearLayout mLlBottom;
    private Button mBtnSetModeVoice;
    private Button mBtnSetModeKeyboard;
    private LinearLayout mLlPressToSpeak;
    private RelativeLayout mRlEdittext;
    private EditText mEdtSendmessage;
    private RelativeLayout mRlFace;
    private ImageView mIvFaceNormal;
    private ImageView mIvFaceChecked;
    private Button mBtnMore;
    private Button mBtnSend;
    private boolean mFlag = true;

    public ChatPrimaryMenu(Context context) {
        super(context);
        init(context, null);
    }

    public ChatPrimaryMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatPrimaryMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_chat_primary_menu, this);

        mLlBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        mBtnSetModeVoice = (Button) findViewById(R.id.btn_set_mode_voice);
        mBtnSetModeKeyboard = (Button) findViewById(R.id.btn_set_mode_keyboard);
        mLlPressToSpeak = (LinearLayout) findViewById(R.id.ll_press_to_speak);
        mRlEdittext = (RelativeLayout) findViewById(R.id.rl_edittext);
        mEdtSendmessage = (EditText) findViewById(R.id.edt_sendmessage);
        mRlFace = (RelativeLayout) findViewById(R.id.rl_face);
        mIvFaceNormal = (ImageView) findViewById(R.id.iv_face_normal);
        mIvFaceChecked = (ImageView) findViewById(R.id.iv_face_checked);
        mBtnMore = (Button) findViewById(R.id.btn_more);
        mBtnSend = (Button) findViewById(R.id.btn_send);

        mRlEdittext.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);

        mBtnSend.setOnClickListener(this);
        mBtnSetModeKeyboard.setOnClickListener(this);
        mBtnSetModeVoice.setOnClickListener(this);
        mBtnMore.setOnClickListener(this);
        mRlFace.setOnClickListener(this);
        mEdtSendmessage.setOnClickListener(this);

        mEdtSendmessage.requestFocus();

        mEdtSendmessage.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mRlEdittext.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
                } else {
                    mRlEdittext.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
                }
            }
        });

        mEdtSendmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!TextUtils.isDigitsOnly(s)) {
//                    mBtnMore.setVisibility(GONE);
//                    mBtnSend.setVisibility(VISIBLE);
//                } else {
//                    mBtnMore.setVisibility(VISIBLE);
//                    mBtnSend.setVisibility(GONE);
//                }
                if (mFlag) {
                    if (s.length() > 0) {
                        mBtnMore.setVisibility(GONE);
                        mBtnSend.setVisibility(VISIBLE);
                    } else {
                        mBtnMore.setVisibility(VISIBLE);
                        mBtnSend.setVisibility(GONE);
                    }
                } else {
                    if (s.length() > 0) {
                        mBtnMore.setVisibility(GONE);
                        mBtnSend.setVisibility(VISIBLE);
                    } else {
                        mBtnMore.setVisibility(GONE);
                        mBtnSend.setVisibility(GONE);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLlPressToSpeak.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mChatPrimaryMenuListener != null) {
                    return mChatPrimaryMenuListener.onPressToSpeakBtnTouch(v, event);
                }
                return false;
            }
        });

    }

    public void setPressToSpeakRecorderView(ChatVoiceRecorderView voiceRecorderView) {
        ChatVoiceRecorderView voiceRecorderView1 = voiceRecorderView;
    }


    @Override
    public void onEmojiconInputEvent(CharSequence emojiContent) {
        mEdtSendmessage.append(emojiContent);
    }

    @Override
    public void onEmojiconDeleteEvent() {
        if (!TextUtils.isEmpty(mEdtSendmessage.getText())) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            mEdtSendmessage.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onExtendMenuContainerHide() {
        showNormalFaceImage();
    }

    @Override
    public void onTextInsert(CharSequence text) {

        int start = mEdtSendmessage.getSelectionStart();
        Editable editableText = mEdtSendmessage.getEditableText();
        editableText.insert(start, text);
        setModeKeyboard();

    }

    /**
     * 隐藏语音和加号按钮，只保留表情
     */
    @Override
    public void onHideVoice() {
        mFlag = false;
        mBtnSetModeVoice.setVisibility(GONE);
        mBtnSetModeKeyboard.setVisibility(GONE);
        mLlPressToSpeak.setVisibility(GONE);
        mBtnMore.setVisibility(GONE);
        mBtnSend.setVisibility(GONE);

    }

    @Override
    public void onHideFaceExpression() {
        mIvFaceChecked.setVisibility(GONE);
        mIvFaceNormal.setVisibility(GONE);
    }

    @Override
    public EditText getEditText() {
        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send) {
            if (mChatPrimaryMenuListener != null) {
                String s = mEdtSendmessage.getText().toString();
                mEdtSendmessage.setText("");
                mChatPrimaryMenuListener.onSendBtnClicked(s);
            }
        } else if (id == R.id.btn_set_mode_voice) {
            setModeVoice();
            showNormalFaceImage();
            if (mChatPrimaryMenuListener != null) {
                mChatPrimaryMenuListener.onToggleVoiceBtnClicked();
            }
        } else if (id == R.id.btn_set_mode_keyboard) {
            setModeKeyboard();
            showNormalFaceImage();
            if (mChatPrimaryMenuListener != null) {
                mChatPrimaryMenuListener.onToggleVoiceBtnClicked();
            }
        } else if (id == R.id.btn_more) {
            mBtnSetModeVoice.setVisibility(VISIBLE);
            mBtnSetModeKeyboard.setVisibility(GONE);
            mRlEdittext.setVisibility(VISIBLE);
            mLlPressToSpeak.setVisibility(GONE);
            showNormalFaceImage();
            if (mChatPrimaryMenuListener != null) {
                mChatPrimaryMenuListener.onToggleExtendClicked();
            }
        } else if (id == R.id.edt_sendmessage) {
            mRlEdittext.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
            mIvFaceNormal.setVisibility(VISIBLE);
            mIvFaceChecked.setVisibility(INVISIBLE);
//            mIvFaceNormal.setVisibility(GONE);
//            mIvFaceChecked.setVisibility(GONE);
            if (mChatPrimaryMenuListener != null) {
                mChatPrimaryMenuListener.onEditTextClicked();
            }
        } else if (id == R.id.rl_face) {
            toggleFaceImage();
            if (mChatPrimaryMenuListener != null) {
                mChatPrimaryMenuListener.onToggleEmojiconClicked();
            }
        }
    }

    private void setModeVoice() {
        hideKeyboard();
        mRlEdittext.setVisibility(GONE);
        mBtnSetModeVoice.setVisibility(View.GONE);
        mBtnSetModeKeyboard.setVisibility(View.VISIBLE);
        mBtnSend.setVisibility(View.GONE);
        mBtnMore.setVisibility(View.VISIBLE);
        mLlPressToSpeak.setVisibility(View.VISIBLE);
        mIvFaceNormal.setVisibility(View.VISIBLE);
//        mIvFaceNormal.setVisibility(View.GONE);
        mIvFaceChecked.setVisibility(View.INVISIBLE);

    }

    private void setModeKeyboard() {
        mRlEdittext.setVisibility(View.VISIBLE);
        mBtnSetModeKeyboard.setVisibility(View.GONE);
        mBtnSetModeVoice.setVisibility(View.VISIBLE);
        mEdtSendmessage.requestFocus();
        mLlPressToSpeak.setVisibility(View.GONE);

        if (TextUtils.isEmpty(mEdtSendmessage.getText())) {
            mBtnMore.setVisibility(View.VISIBLE);
            mBtnSend.setVisibility(View.GONE);
        } else {
            mBtnMore.setVisibility(View.GONE);
            mBtnSend.setVisibility(View.VISIBLE);
        }
    }

    private void toggleFaceImage() {
        if (mIvFaceNormal.getVisibility() == View.VISIBLE) {
            showSelectedFaceImage();
        } else {
            showNormalFaceImage();
        }
    }

    private void showNormalFaceImage() {
        mIvFaceNormal.setVisibility(View.VISIBLE);
        mIvFaceChecked.setVisibility(View.INVISIBLE);
//        mIvFaceNormal.setVisibility(View.GONE);
//        mIvFaceChecked.setVisibility(View.GONE);
    }

    private void showSelectedFaceImage() {
        mIvFaceNormal.setVisibility(View.INVISIBLE);
        mIvFaceChecked.setVisibility(View.VISIBLE);
//        mIvFaceNormal.setVisibility(View.GONE);
//        mIvFaceChecked.setVisibility(View.GONE);
    }


}
