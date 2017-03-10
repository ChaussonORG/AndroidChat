package com.sudaotech.chatlibrary.emojicon;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sudao.basemodule.common.util.ScreenHelper;
import com.sudaotech.chatlibrary.R;

import java.util.ArrayList;
import java.util.List;

public class EmojiconScrollTabBar extends RelativeLayout {

    private Context mContext;
    private HorizontalScrollView mHorizontalScrollView;
    private LinearLayout mLlTabContainer;

    private List<ImageView> mTabList = new ArrayList<>();
    private ScrollTabBarItemClickListener mItemClickListener;

    public EmojiconScrollTabBar(Context context) {
        this(context, null);
    }

    public EmojiconScrollTabBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EmojiconScrollTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_emojicon_tab_bar, this);

        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.scroll_view);
        mLlTabContainer = (LinearLayout) findViewById(R.id.tab_container);
    }

    /**
     * add tab
     *
     * @param icon
     */
    public void addTab(int icon) {
        View tabView = View.inflate(mContext, R.layout.item_scroll_tab, null);
        ImageView imageView = (ImageView) tabView.findViewById(R.id.iv_icon);
        imageView.setImageResource(icon);
        int tabWidth = 60;
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(ScreenHelper.dp2px(mContext, tabWidth), LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imgParams);
        mLlTabContainer.addView(tabView);
        mTabList.add(imageView);
        final int position = mTabList.size() - 1;
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });
    }

    /**
     * remove tab
     *
     * @param position
     */
    public void removeTab(int position) {
        mLlTabContainer.removeViewAt(position);
        mTabList.remove(position);
    }

    public void selectedTo(int position) {
        scrollTo(position);
        for (int i = 0; i < mTabList.size(); i++) {
            if (position == i) {
                mTabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_selected));
            } else {
                mTabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_nomal));
            }
        }
    }

    private void scrollTo(final int position) {
        int childCount = mLlTabContainer.getChildCount();
        if (position < childCount) {
            mHorizontalScrollView.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = mLlTabContainer.getScrollX();
                    int childX = (int) ViewCompat.getX(mLlTabContainer.getChildAt(position));

                    if (childX < mScrollX) {
                        mHorizontalScrollView.scrollTo(childX, 0);
                        return;
                    }

                    int childWidth = (int) mLlTabContainer.getChildAt(position).getWidth();
                    int hsvWidth = mHorizontalScrollView.getWidth();
                    int childRight = childX + childWidth;
                    int scrollRight = mScrollX + hsvWidth;

                    if (childRight > scrollRight) {
                        mHorizontalScrollView.scrollTo(childRight - scrollRight, 0);
                        return;
                    }
                }
            });
        }
    }


    public void setTabBarItemClickListener(ScrollTabBarItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }


    public interface ScrollTabBarItemClickListener {
        void onItemClick(int position);
    }

}
