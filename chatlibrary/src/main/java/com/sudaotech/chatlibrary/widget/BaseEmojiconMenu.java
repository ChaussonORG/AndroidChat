package com.sudaotech.chatlibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.sudaotech.chatlibrary.model.Emojicon;


/**
 * Created by Samuel on 2016/12/3 16:56
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class BaseEmojiconMenu extends LinearLayout {
    protected EmojiconMenuListener mEmojiconMenuListener;

    public BaseEmojiconMenu(Context context) {
        super(context);
    }

    public BaseEmojiconMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseEmojiconMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEmojiconMenuListener(EmojiconMenuListener emojiconMenuListener) {
        mEmojiconMenuListener = emojiconMenuListener;
    }

    public interface EmojiconMenuListener {
        void onExpressionClicked(Emojicon emojicon);

        void onDeleteImageClicked();
    }

}
