package com.sudaotech.chatlibrary.widget;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by Samuel on 2016/12/3 15:46
 * Email:xuzhou40@gmail.com
 * desc:
 */

public abstract class BaseChatPrimaryMenu extends RelativeLayout {
    protected AppCompatActivity mAppCompatActivity;
    protected InputMethodManager mInputMethodManager;
    protected ChatPrimaryMenuListener mChatPrimaryMenuListener;


    public BaseChatPrimaryMenu(Context context) {
        super(context);
        init(context);
    }

    public BaseChatPrimaryMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseChatPrimaryMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mAppCompatActivity = (AppCompatActivity) context;
        mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void setChatPrimaryMenuListener(ChatPrimaryMenuListener chatPrimaryMenuListener) {
        mChatPrimaryMenuListener = chatPrimaryMenuListener;
    }

    public abstract void onEmojiconInputEvent(CharSequence emojiContent);

    public abstract void onEmojiconDeleteEvent();

    public abstract void onExtendMenuContainerHide();

    public abstract void onTextInsert(CharSequence text);

    public abstract void onHideVoice();

    public abstract void onHideFaceExpression();

    public abstract EditText getEditText();

    public void hideKeyboard() {
        if (mAppCompatActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (mAppCompatActivity.getCurrentFocus() != null) {
                mInputMethodManager.hideSoftInputFromWindow(mAppCompatActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public interface ChatPrimaryMenuListener {
        /**
         * when send button clicked
         *
         * @param content
         */
        void onSendBtnClicked(String content);

        /**
         * when speak button is touched
         *
         * @return
         */
        boolean onPressToSpeakBtnTouch(View v, MotionEvent event);

        /**
         * toggle on/off voice button
         */
        void onToggleVoiceBtnClicked();

        /**
         * toggle on/off extend menu
         */
        void onToggleExtendClicked();

        /**
         * toggle on/off emoji icon
         */
        void onToggleEmojiconClicked();

        /**
         * on text input is clicked
         */
        void onEditTextClicked();

    }


}
