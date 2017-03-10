package com.sudaotech.chatlibrary.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sudao.basemodule.common.util.FileUtil;
import com.sudao.basemodule.component.file.FileDownloader;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.db.MessageDao;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.ui.ShowImageActivity;
import com.sudaotech.chatlibrary.utils.ChatPathUtil;
import com.sudaotech.chatlibrary.utils.ChatUtil;

import java.io.File;

public class ChatRowImage extends ChatRowFile {

    private static final int WIDTH = 160;
    private static final int HEIGHT = 160;
    private final FileDownloader mFileDownloader;
    private final MessageDao mMessageDao;
    protected ImageView imageView;
    //    private ImageMessageBody imgBody;

    public ChatRowImage(Context context, BaseMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        mFileDownloader = new FileDownloader(context);
        mMessageDao = new MessageDao(context);
    }

    @Override
    protected void onInflateView() {
        mLayoutInflater.inflate(mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER ? R.layout.item_row_received_picture : R.layout.item_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
        progressBar.setVisibility(View.GONE);
        percentageView.setVisibility(View.GONE);
    }


    @Override
    protected void onSetUpView() {

        // received mMessages
//        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OTHER) {
//
//            setMessageReceiveCallback();
//
//            progressBar.setVisibility(View.GONE);
//            percentageView.setVisibility(View.GONE);
//            imageView.setImageResource(R.drawable.ease_default_image);
//
////            showImageView(thumbPath, imageView, imgBody.getLocalUrl(), mBaseMessage);
//
//            return;
//        }

        showImageView(imageView, mBaseMessage);
        if (mBaseMessage.getOwner() == ChatConstant.MESSAGE_IS_OWNER) {
            handleSendMessage();
        }

    }

    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(mContext, ShowImageActivity.class);
        intent.putExtra(ChatConstant.EXTRA_FILE_PATH, mBaseMessage.getImagePath());
        intent.putExtra(ChatConstant.EXTRA_FILE_URL, mBaseMessage.getFullPath());
        mContext.startActivity(intent);
    }

    /**
     * @param imageView
     * @param message
     * @return
     */
    private void showImageView(ImageView imageView, BaseMessage message) {
        String filePath = message.getImagePath();
        if (FileUtil.isFileExists(filePath)) {
//            ImageHelper.load(mContext, new File(filePath), imageView);
            ChatUtil.loadChatImage(mContext, new File(filePath), imageView);
        } else {
//            ImageHelper.load(mContext, message.getFullPath(), imageView);
            ChatUtil.loadChatImage(mContext, message.getFullPath(), imageView);
            mFileDownloader.onStartDownload(message.getFullPath(), message.getImageName(), ChatPathUtil.getInstance().getImagePath());
            message.setImagePath(ChatPathUtil.getInstance().getImagePath() + message.getImageName());
            mMessageDao.insertMessage(ChatUtil.toChatMessage(message));
        }
    }

}
