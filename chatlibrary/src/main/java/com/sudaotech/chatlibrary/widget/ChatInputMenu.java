package com.sudaotech.chatlibrary.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.emojicon.EmojiconMenu;
import com.sudaotech.chatlibrary.model.DefaultEmojiconDatas;
import com.sudaotech.chatlibrary.model.Emojicon;
import com.sudaotech.chatlibrary.model.EmojiconGroupEntity;
import com.sudaotech.chatlibrary.utils.SmileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Samuel on 2016/12/3 15:12
 * Email:xuzhou40@gmail.com
 * desc:输入菜单
 */

public class ChatInputMenu extends LinearLayout {
    protected LayoutInflater mLayoutInflater;
    protected FrameLayout mPrimaryMenuContainer;
    protected FrameLayout mEmojiconMenuContainer;
    protected FrameLayout mChatExtendMenuContainer;
    protected ChatExtendMenu mChatExtendMenu;
    protected BaseChatPrimaryMenu mBaseChatPrimaryMenu;
    protected BaseEmojiconMenu mBaseEmojiconMenu;
    private Context mContext;
    private Handler handler = new Handler();
    private boolean mInited;
    private ChatInputMenuListener mChatInputMenuListener;


    public ChatInputMenu(Context context) {
        super(context);
        init(context, null);
    }

    public ChatInputMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChatInputMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mLayoutInflater.inflate(R.layout.widget_chat_input_menu, this);
        mPrimaryMenuContainer = (FrameLayout) findViewById(R.id.primary_menu_container);
        mEmojiconMenuContainer = (FrameLayout) findViewById(R.id.emojicon_menu_container);
        mChatExtendMenuContainer = (FrameLayout) findViewById(R.id.extend_menu_container);

        // extend menu
        mChatExtendMenu = (ChatExtendMenu) findViewById(R.id.extend_menu);
    }

    public void init(List<EmojiconGroupEntity> emojiconGroupEntityList) {
        if (mInited) {
            return;
        }
        if (mBaseChatPrimaryMenu == null) {
            mBaseChatPrimaryMenu = (ChatPrimaryMenu) mLayoutInflater.inflate(R.layout.layout_chat_primary_menu, null);
        }
        mPrimaryMenuContainer.addView(mBaseChatPrimaryMenu);

        if (mBaseEmojiconMenu == null) {
            mBaseEmojiconMenu = (EmojiconMenu) mLayoutInflater.inflate(R.layout.layout_emojicon_menu, null);
            if (emojiconGroupEntityList == null) {
                emojiconGroupEntityList = new ArrayList<>();
                emojiconGroupEntityList.add(new EmojiconGroupEntity(R.drawable.emo_001, Arrays.asList(DefaultEmojiconDatas.getData())));
            }
            ((EmojiconMenu) mBaseEmojiconMenu).init(emojiconGroupEntityList);
        }
        mEmojiconMenuContainer.addView(mBaseEmojiconMenu);

        processChatMenu();
        mChatExtendMenu.init();

        mInited = true;
    }

    public void init() {
        init(null);
    }

    public void hideVoiceAndButton() {
        mBaseChatPrimaryMenu.onHideVoice();
    }

    public void hideFaceExpression() {
        mBaseChatPrimaryMenu.onHideFaceExpression();
    }

    public void setBaseEmojiconMenu(BaseEmojiconMenu customEmojiconMenu) {
        mBaseEmojiconMenu = customEmojiconMenu;
    }

    public void setBaseChatPrimaryMenu(BaseChatPrimaryMenu customChatPrimaryMenu) {
        mBaseChatPrimaryMenu = customChatPrimaryMenu;
    }

    public BaseChatPrimaryMenu getChatPrimaryMenu() {
        return mBaseChatPrimaryMenu;
    }

    public BaseEmojiconMenu getEmojiconMenu() {
        return mBaseEmojiconMenu;
    }

    /**
     * register menu item
     *
     * @param name        item name
     * @param drawableRes background of item
     * @param itemId      id
     * @param listener    on click event of item
     */
    public void registerExtendMenuItem(String name, int drawableRes, int itemId,
                                       ChatExtendMenu.ChatExtendMenuItemClickListener listener) {
        mChatExtendMenu.registerMenuItem(name, drawableRes, itemId, listener);
    }

    /**
     * register menu item
     *
     * @param nameRes     resource id of item name
     * @param drawableRes background of item
     * @param itemId      id
     * @param listener    on click event of item
     */
    public void registerExtendMenuItem(int nameRes, int drawableRes, int itemId,
                                       ChatExtendMenu.ChatExtendMenuItemClickListener listener) {
        mChatExtendMenu.registerMenuItem(nameRes, drawableRes, itemId, listener);
    }

    private void processChatMenu() {
        mBaseChatPrimaryMenu.setChatPrimaryMenuListener(new BaseChatPrimaryMenu.ChatPrimaryMenuListener() {
            @Override
            public void onSendBtnClicked(String content) {
                if (mChatInputMenuListener != null) {
                    mChatInputMenuListener.onSendMessage(content);
                }
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                if (mChatInputMenuListener != null) {
                    return mChatInputMenuListener.onPressToSpeakBtnTouch(v, event);
                }
                return false;
            }

            @Override
            public void onToggleVoiceBtnClicked() {
                hideExtendMenuContainer();
            }

            @Override
            public void onToggleExtendClicked() {
                toggleMore();
            }

            @Override
            public void onToggleEmojiconClicked() {
                toggleEmojicon();

            }

            @Override
            public void onEditTextClicked() {
                hideExtendMenuContainer();
            }
        });

        mBaseEmojiconMenu.setEmojiconMenuListener(new BaseEmojiconMenu.EmojiconMenuListener() {
            @Override
            public void onExpressionClicked(Emojicon emojicon) {
                if (emojicon.getType() != Emojicon.Type.BIG_EXPRESSION) {
                    if (emojicon.getEmojiText() != null) {
                        mBaseChatPrimaryMenu.onEmojiconInputEvent(SmileUtils.getSmiledText(mContext, emojicon.getEmojiText()));
                    }
                } else {
                    if (mChatInputMenuListener != null) {
                        mChatInputMenuListener.onBigExpressionClicked(emojicon);
                    }
                }
            }

            @Override
            public void onDeleteImageClicked() {
                mBaseChatPrimaryMenu.onEmojiconDeleteEvent();
            }
        });
    }

    public void insertText(String text) {
        getChatPrimaryMenu().onTextInsert(text);
    }

    /**
     * show or hide extend menu
     */
    private void toggleMore() {
        if (mChatExtendMenuContainer.getVisibility() == View.GONE) {
            hideKeyboard();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mChatExtendMenuContainer.setVisibility(View.VISIBLE);
                    mChatExtendMenu.setVisibility(View.VISIBLE);
                    mBaseEmojiconMenu.setVisibility(View.GONE);
                }
            }, 50);
        } else {
            if (mBaseEmojiconMenu.getVisibility() == View.VISIBLE) {
                mBaseEmojiconMenu.setVisibility(View.GONE);
                mChatExtendMenu.setVisibility(View.VISIBLE);
            } else {
                mChatExtendMenuContainer.setVisibility(View.GONE);
            }
        }
    }

    private void toggleEmojicon() {
        if (mChatExtendMenuContainer.getVisibility() == View.GONE) {
            hideKeyboard();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mChatExtendMenuContainer.setVisibility(View.VISIBLE);
                    mChatExtendMenu.setVisibility(View.GONE);
                    mBaseEmojiconMenu.setVisibility(View.VISIBLE);
                }
            }, 50);
        } else {
            if (mBaseEmojiconMenu.getVisibility() == View.VISIBLE) {
                mChatExtendMenuContainer.setVisibility(View.GONE);
                mBaseEmojiconMenu.setVisibility(View.GONE);
            } else {
                mChatExtendMenu.setVisibility(View.GONE);
                mBaseEmojiconMenu.setVisibility(View.VISIBLE);
            }

        }
    }

    private void hideKeyboard() {
        mBaseChatPrimaryMenu.hideKeyboard();
    }

    public void hideExtendMenuContainer() {
        mChatExtendMenu.setVisibility(View.GONE);
        mBaseEmojiconMenu.setVisibility(View.GONE);
        mChatExtendMenuContainer.setVisibility(View.GONE);
        mBaseChatPrimaryMenu.onExtendMenuContainerHide();
    }

    /**
     * when back key pressed
     *
     * @return false--extend menu is on, will hide it first
     * true --extend menu is off
     */
    public boolean onBackPressed() {
        if (mChatExtendMenuContainer.getVisibility() == View.VISIBLE) {
            hideExtendMenuContainer();
            return false;
        } else {
            return true;
        }

    }

    public void setChatInputMenuListener(ChatInputMenuListener chatInputMenuListener) {
        mChatInputMenuListener = chatInputMenuListener;
    }

    public interface ChatInputMenuListener {
        /**
         * when send mBaseMessage button pressed
         *
         * @param content mBaseMessage content
         */
        void onSendMessage(String content);

        /**
         * when big icon pressed
         *
         * @param emojicon
         */
        void onBigExpressionClicked(Emojicon emojicon);

        /**
         * when speak button is touched
         *
         * @param v
         * @param event
         * @return
         */
        boolean onPressToSpeakBtnTouch(View v, MotionEvent event);
    }

}
