package com.sudaotech.chatlibrary.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.sudao.basemodule.base.BaseActivity;
import com.sudao.basemodule.base.BundleConst;
import com.sudao.basemodule.common.util.FileUtil;
import com.sudao.basemodule.common.util.ImageHelper;
import com.sudao.basemodule.common.util.ToastHelper;
import com.sudao.basemodule.component.file.FileDownloader;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.utils.ChatUtil;

import java.io.File;

import static com.sudaotech.chatlibrary.ChatConstant.APP_PICTURE_PATH;
import static com.sudaotech.chatlibrary.ChatConstant.IMAGE_FILE_FORMAT;

/**
 * 聊天图片显示大图
 */
public class ShowImageActivity extends BaseActivity {

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private FileDownloader mFileDownloader;
    private String mFilePath;
    private String mFileUrl;

    @Override
    public void layout() {
        setContentView(R.layout.activity_show_image);
    }

    @Override
    public void initView() {
        findView();
        initData();
    }

    private void findView() {
        mImageView = (ImageView) findViewById(R.id.imageview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void initData() {
        Intent intent = getIntent();
        mFilePath = intent.getStringExtra(ChatConstant.EXTRA_FILE_PATH);
        mFileUrl = intent.getStringExtra(ChatConstant.EXTRA_FILE_URL);

        mFileDownloader = new FileDownloader(mContext);
        final GlideDrawableImageViewTarget glideDrawableImageViewTarget = new GlideDrawableImageViewTarget(mImageView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                mProgressBar.setVisibility(View.GONE);
            }
        };

        if (mFilePath != null && mFileUrl != null) {
            //判断文件是否存在
            if (FileUtil.isFileExists(mFilePath)) {
                File file = new File(mFilePath);
                ImageHelper.load(mContext, file, glideDrawableImageViewTarget);
            } else {
                ImageHelper.load(mContext, mFileUrl, glideDrawableImageViewTarget);
            }
        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.finish();
            }
        });

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showOperateMenu();
                return false;
            }
        });
    }

    /**
     * 显示长按操作菜单
     */
    private void showOperateMenu() {
        View view = mContext.getLayoutInflater().inflate(R.layout.popup_window_image_operate, null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout mLlButton = (LinearLayout) view.findViewById(R.id.ll_button);
        Button btnSaveImage = (Button) view.findViewById(R.id.btn_save_image);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        mLlButton.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in_group_setting_popupwindow));
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in_alpha_group_setting));
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);
        popupWindow.showAtLocation(mImageView, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    /**
     * 保存图片
     */
    private void saveImage() {
        if (mFilePath != null && mFileUrl != null) {
            //判断文件是否存在
            if (FileUtil.isFileExists(mFilePath)) {
                String destFilePath = APP_PICTURE_PATH + BundleConst.APP_NAME + ChatUtil.getCurrentTime() + IMAGE_FILE_FORMAT;
                FileUtil.copyFile(mFilePath, destFilePath);
            } else {
                String saveName = BundleConst.APP_NAME + ChatUtil.getCurrentTime() + IMAGE_FILE_FORMAT;
                mFileDownloader.onStartDownload(mFileUrl, saveName, APP_PICTURE_PATH);
            }
            String string = getString(R.string.picture_have_save);
            ToastHelper.showToast(mContext, string + APP_PICTURE_PATH);
        }
    }
}
