package com.sudaotech.chatlibrary.emojicon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sudao.basemodule.common.util.ScreenHelper;
import com.sudaotech.chatlibrary.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class EmojiconIndicatorView extends LinearLayout {

    private Context mContext;
    private Bitmap mSelectedBitmap;
    private Bitmap mUnselectedBitmap;

    private List<ImageView> dotViews;

    private int dotHeight = 12;

    public EmojiconIndicatorView(Context context, AttributeSet attrs, int defStyle) {
        this(context, null);
    }

    public EmojiconIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EmojiconIndicatorView(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        dotHeight = ScreenHelper.dp2px(context, dotHeight);
        mSelectedBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ease_dot_emojicon_selected);
        mUnselectedBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ease_dot_emojicon_unselected);
        setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void init(int count) {
        dotViews = new ArrayList<ImageView>();
        for (int i = 0; i < count; i++) {
            RelativeLayout rl = new RelativeLayout(mContext);
            LayoutParams params = new LayoutParams(dotHeight, dotHeight);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            ImageView imageView = new ImageView(mContext);

            if (i == 0) {
                imageView.setImageBitmap(mSelectedBitmap);
                rl.addView(imageView, layoutParams);
            } else {
                imageView.setImageBitmap(mUnselectedBitmap);
                rl.addView(imageView, layoutParams);
            }
            this.addView(rl, params);
            dotViews.add(imageView);
        }
    }

    public void updateIndicator(int count) {
        if (dotViews == null) {
            return;
        }
        for (int i = 0; i < dotViews.size(); i++) {
            if (i >= count) {
                dotViews.get(i).setVisibility(GONE);
                ((View) dotViews.get(i).getParent()).setVisibility(GONE);
            } else {
                dotViews.get(i).setVisibility(VISIBLE);
                ((View) dotViews.get(i).getParent()).setVisibility(VISIBLE);
            }
        }
        if (count > dotViews.size()) {
            int diff = count - dotViews.size();
            for (int i = 0; i < diff; i++) {
                RelativeLayout rl = new RelativeLayout(mContext);
                LayoutParams params = new LayoutParams(dotHeight, dotHeight);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                ImageView imageView = new ImageView(mContext);
                imageView.setImageBitmap(mUnselectedBitmap);
                rl.addView(imageView, layoutParams);
                rl.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                this.addView(rl, params);
                dotViews.add(imageView);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSelectedBitmap != null) {
            mSelectedBitmap.recycle();
        }
        if (mUnselectedBitmap != null) {
            mUnselectedBitmap.recycle();
        }
    }

    public void selectTo(int position) {
        for (ImageView iv : dotViews) {
            iv.setImageBitmap(mUnselectedBitmap);
        }
        dotViews.get(position).setImageBitmap(mSelectedBitmap);
    }


    public void selectTo(int startPosition, int targetPostion) {
        ImageView startView = dotViews.get(startPosition);
        ImageView targetView = dotViews.get(targetPostion);
        startView.setImageBitmap(mUnselectedBitmap);
        targetView.setImageBitmap(mSelectedBitmap);
    }

}   
