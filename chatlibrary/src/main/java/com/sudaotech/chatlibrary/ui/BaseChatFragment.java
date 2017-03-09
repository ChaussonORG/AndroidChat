package com.sudaotech.chatlibrary.ui;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.sudao.basemodule.base.BaseFragment;
import com.sudao.basemodule.base.BundleConst;
import com.sudao.basemodule.basicapi.UserBean;
import com.sudao.basemodule.common.util.FileUtil;
import com.sudao.basemodule.common.util.GsonHelper;
import com.sudao.basemodule.common.util.LogUtil;
import com.sudao.basemodule.common.util.SPHelper;
import com.sudao.basemodule.common.util.ToolbarUtil;
import com.sudao.basemodule.component.file.FileUploader;
import com.sudao.basemodule.component.file.OnUploadListener;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.R;
import com.sudaotech.chatlibrary.constroller.ChatUI;
import com.sudaotech.chatlibrary.db.MessageDao;
import com.sudaotech.chatlibrary.db.UserDao;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.model.ChatMessage;
import com.sudaotech.chatlibrary.model.ChatUser;
import com.sudaotech.chatlibrary.model.Emojicon;
import com.sudaotech.chatlibrary.model.GroupInfo;
import com.sudaotech.chatlibrary.model.MessageExtend;
import com.sudaotech.chatlibrary.model.UserInfo;
import com.sudaotech.chatlibrary.network.request.GroupMessageRequest;
import com.sudaotech.chatlibrary.network.request.SingleMessageRequest;
import com.sudaotech.chatlibrary.network.response.SendMessageResponse;
import com.sudaotech.chatlibrary.presenter.ISendMessageView;
import com.sudaotech.chatlibrary.presenter.SendMessagePresenterCompl;
import com.sudaotech.chatlibrary.utils.ChatAtMessageHelper;
import com.sudaotech.chatlibrary.utils.ChatPathUtil;
import com.sudaotech.chatlibrary.utils.ChatUserUtils;
import com.sudaotech.chatlibrary.utils.ChatUtil;
import com.sudaotech.chatlibrary.widget.ChatExtendMenu;
import com.sudaotech.chatlibrary.widget.ChatInputMenu;
import com.sudaotech.chatlibrary.widget.ChatMessageList;
import com.sudaotech.chatlibrary.widget.ChatVoiceRecorderView;
import com.sudaotech.chatlibrary.widget.chatrow.ChatRowProvider;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

import static com.sudaotech.chatlibrary.ChatConstant.AUDIO_FILE_FORMAT;
import static com.sudaotech.chatlibrary.ChatConstant.MESSAGE_TYPE_AUDIO;
import static com.sudaotech.chatlibrary.ChatConstant.MESSAGE_TYPE_IMAGE;
import static com.sudaotech.chatlibrary.ChatConstant.MESSAGE_TYPE_TEXT;


/**
 * 基础聊天功能
 */
public class BaseChatFragment extends BaseFragment implements EMMessageListener, ISendMessageView {
    protected static final String TAG = "ChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;

    protected Toolbar mToolbar;
    protected TextView mTvToolbarTitle;
    protected ChatVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView mListView;

    protected Bundle mFragmentArgs;
    protected int mChatType;
    protected ChatMessageList mChatMessageList;
    protected ChatInputMenu mChatInputMenu;
    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;
    protected File mCameraFile;

    protected boolean mIsloading;
    protected boolean mHaveMoreData = true;

    protected String mContactType;//业务聊天类型：单聊，自由群，商会群
    protected BaseMessage contextMenuMessage;
    protected int[] itemStrings = {R.string.attach_picture, R.string.attach_take_pic};
    protected int[] itemdrawables = {R.drawable.selector_chat_pick_image, R.drawable.selector_chat_take_photo};
    protected int[] itemIds = {ITEM_PICTURE, ITEM_TAKE_PICTURE};
    protected MyItemClickListener mExtendMenuItemClickListener;
    protected ChatFragmentHelper mChatFragmentHelper;

    protected int mTotal;
    protected int mPagesize = 10;
    protected int mOffset = 0;
    protected List<BaseMessage> mMessages = new ArrayList<>();
    protected MessageDao mMessageDao;
    protected RealmResults<ChatMessage> mChatMessages;
    protected HashMap<String, Object> mExtMap = new HashMap<>();

    protected long mOwnUserId;//自己用户id
    protected long mToChatUserId;
    protected String mToUserName;
    protected SendMessagePresenterCompl mSendMessagePresenterCompl;
    protected UserBean mUserBean;
    protected UserDao mUserDao;
    protected GroupInfoHelper mGroupInfoHelper;

    //压缩图片
    private static Bitmap compressBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = 500.0f / height;
        if (width > 500 || height > 500) {
            width = Math.round(width * scale);
            height = Math.round(height * scale);
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    /**
     * 旋转图片
     *
     * @param bitmap
     * @param degree
     * @return
     */
    private static Bitmap rotate(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);//注意Out of memory
        return bitmap;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFragmentArgs = getArguments();

        mContactType = mFragmentArgs.getString(ChatConstant.EXTRA_CHAT_TYPE, ChatConstant.CHAT_TYPE_USER);
        mToChatUserId = mFragmentArgs.getLong(ChatConstant.EXTRA_USER_ID);
        mToUserName = mFragmentArgs.getString(ChatConstant.EXTRA_USER_NAME);

        mChatType = ChatUtil.getChatType(mContactType);

        initFragment();
        onMessageListInit();
        setRefreshLayoutListener();


        ToolbarUtil.setupToolbarRed(mContext, mToolbar);

        mChatMessages = mMessageDao.getMessagesById(mToChatUserId, mChatType);
        mTotal = mChatMessages.size();

        getMessageRecord(mOffset, mPagesize);

    }

    /**
     * init view
     */
    @Override
    public void initView() {
        mMessageDao = new MessageDao(mContext);
        mUserDao = new UserDao(mContext);
        mSendMessagePresenterCompl = new SendMessagePresenterCompl(this);

        String userInfoJson = SPHelper.getString(ChatConstant.USER_INFO, "");//获取当前用户资料
        if (userInfoJson.length() > 0) {
            mUserBean = GsonHelper.fromJson(userInfoJson, UserBean.class);
            mOwnUserId = mUserBean.getUserId();

            mExtMap.put(MessageExtend.EXT_PHOTO, mUserBean.getPhoto());
            mExtMap.put(MessageExtend.EXT_NAME, mUserBean.getName());
        }


    }

    /**
     * 发送时，带上群的名称和头像
     */
    protected void setGroupInfoExtMap() {
        // TODO: 2017/2/24 群信息暂时不处理
//        GroupInfo groupInfo = mGroupInfoHelper.obtainGroupInfo();
//        if (groupInfo != null) {
//            mExtMap.put(MessageExtend.EXT_GROUP_NAME, groupInfo.getGroupName());
//        }
    }

    /**
     * 获取消息记录
     */
    private void getMessageRecord(int offset, int pagesize) {
        if (mChatMessages != null && mChatMessages.size() > 0) {
            int end = offset + pagesize >= mTotal - 1 ? mTotal : offset + pagesize;
            List<ChatMessage> list = mChatMessages.subList(offset, end);
            for (ChatMessage chatMessage : list) {
                mMessages.add(0, ChatUtil.emMessagetoBaseMessage(chatMessage));
            }
        }

    }

    protected void initFragment() {
        voiceRecorderView = (ChatVoiceRecorderView) getView().findViewById(R.id.voice_recorder);
        mChatMessageList = (ChatMessageList) getView().findViewById(R.id.message_list);
        mToolbar = ((Toolbar) getView().findViewById(R.id.toolbar));
        mTvToolbarTitle = ((TextView) getView().findViewById(R.id.tv_toolbar_title));
        mChatInputMenu = (ChatInputMenu) getView().findViewById(R.id.input_menu);

        if (mChatType == ChatConstant.TYPE_SINGLE) {
            mTvToolbarTitle.setText(mToUserName);
        }

        if (mChatType != ChatConstant.TYPE_SINGLE) {
            //群聊也不显示昵称
            mChatMessageList.setShowUserNick(true);
        } else {
            mChatMessageList.setShowUserNick(false);
        }
        mListView = mChatMessageList.getListView();

        mExtendMenuItemClickListener = new MyItemClickListener();
        registerExtendMenuItem();
        // init input menu
        mChatInputMenu.init(null);
//        mChatInputMenu.hideFaceExpression();

        mChatInputMenu.setChatInputMenuListener(new ChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                addToRecentContact();
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new ChatVoiceRecorderView.ChatVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        uploadAudio(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(Emojicon emojicon) {
//                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });

        swipeRefreshLayout = mChatMessageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    //添加到最近联系人
    private void addToRecentContact() {
        if (mChatFragmentHelper != null) {
            mChatFragmentHelper.addToRecentContact();
        }
    }

    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem() {
        for (int i = 0; i < itemStrings.length; i++) {
            mChatInputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], mExtendMenuItemClickListener);
        }
    }

    protected void onMessageListInit() {
        mChatMessageList.init(mMessages, mToChatUserId, mChatType, mChatFragmentHelper != null ? mChatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();

        mChatMessageList.getListView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                mChatInputMenu.hideExtendMenuContainer();
                return false;
            }
        });

    }

    protected void setListItemClickListener() {
        mChatMessageList.setItemClickListener(new ChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(long userId) {
                if (mChatFragmentHelper != null) {
                    mChatFragmentHelper.onAvatarClick(userId);
                }
            }

            @Override
            public void onUserAvatarLongClick(long userId) {
                if (mChatFragmentHelper != null) {
                    mChatFragmentHelper.onAvatarLongClick(userId);
                }
            }

            @Override
            public void onResendClick(final BaseMessage message) {
                /*new EaseAlertDialog(getActivity(), R.string.resend, R.string.confirm_resend, null, new AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        resendMessage(mBaseMessage);
                    }
                }, true).show();*/
            }

            @Override
            public void onBubbleLongClick(BaseMessage message) {
                contextMenuMessage = message;
                if (mChatFragmentHelper != null) {
                    mChatFragmentHelper.onMessageBubbleLongClick(message);
                }
            }

            @Override
            public boolean onBubbleClick(BaseMessage message) {
                if (mChatFragmentHelper == null) {
                    return false;
                }
                return mChatFragmentHelper.onMessageBubbleClick(message);
            }

        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (mListView.getFirstVisiblePosition() == 0 && !mIsloading && mHaveMoreData) {

                            // TODO: 2016/12/17 加载更多聊天记录(刷新问题)
                            mOffset = mOffset + mPagesize;

                            if (mOffset <= mTotal) {
                                getMessageRecord(mOffset, mPagesize);
                                mChatMessageList.refreshSeekTo(mMessages.size() - mPagesize - 1);

                            } else {
                                mHaveMoreData = false;

                            }


                            mIsloading = false;
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 600);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);

        ChatUI.getInstance().pushActivity(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this mActivity enters the background
        EMClient.getInstance().chatManager().removeMessageListener(this);
        ChatUI.getInstance().popActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        /*if (EMClient.getInstance().getCurrentUser().equals(username) ||
                mChatType != ChatConstant.TYPE_GROUP) {
            return;
        }*/
        ChatAtMessageHelper.get().addAtUser(username);
        ChatUser user = ChatUserUtils.getUserInfo(username);
        if (user != null) {
            username = user.getNick();
        }
        if (autoAddAtSymbol)
            mChatInputMenu.insertText("@" + username + " ");
        else
            mChatInputMenu.insertText(username + " ");
    }

    /**
     * input @
     *
     * @param username
     */
    protected void inputAtUsername(String username) {
        inputAtUsername(username, true);
    }

    /**
     * 发送文本消息
     *
     * @param content
     */
    protected void sendTextMessage(final String content) {
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setMessageContent(content);
        baseMessage.setSendingState(ChatConstant.MESSAGE_STATUS_INPROGRESS);
        baseMessage.setMessageType(MESSAGE_TYPE_TEXT);
        baseMessage.setSenderId(mOwnUserId);
        baseMessage.setConversationId(mToChatUserId);
        if (mChatType == ChatConstant.TYPE_SINGLE) {
            baseMessage.setReceiverId(mToChatUserId);
            baseMessage.setMessageTarget(ChatConstant.MESSAGE_TARGET_SINGLE);
        } else {
            baseMessage.setChatGroupId(mToChatUserId);
            baseMessage.setMessageTarget(ChatConstant.MESSAGE_TARGET_GROUP);

        }
        baseMessage.setShowTime(ChatUtil.getCurrentTime());
        baseMessage.setOwner(ChatConstant.MESSAGE_IS_OWNER);
        baseMessage.setIsRead(ChatConstant.MESSAGE_HAVE_READ);
        baseMessage.setExtMap(mExtMap);

        mMessages.add(baseMessage);
        mChatMessageList.refreshSelectLast();

        if (mChatType == ChatConstant.TYPE_SINGLE) {
            SingleMessageRequest request = new SingleMessageRequest();
            request.setReceiverId(mToChatUserId);
            request.setMessageContent(StringEscapeUtils.unescapeJava(content));
            request.setMessageType(MESSAGE_TYPE_TEXT);
            request.setExtMap(mExtMap);

            mSendMessagePresenterCompl.sendSingleMessage(request, baseMessage, MESSAGE_TYPE_TEXT);
        } else {
            GroupMessageRequest request = new GroupMessageRequest();
            request.setChatGroupId(mToChatUserId);
            request.setMessageContent(StringEscapeUtils.unescapeJava(content));
            request.setMessageType(MESSAGE_TYPE_TEXT);
            request.setExtMap(mExtMap);

            mSendMessagePresenterCompl.sendGroupMessage(request, baseMessage, MESSAGE_TYPE_TEXT);
        }

    }

    /**
     * 上传图片文件
     *
     * @param absolutePath
     */
    private void uploadImage(final String absolutePath) {
        final BaseMessage baseMessage = new BaseMessage();
        baseMessage.setConversationId(mToChatUserId);
        baseMessage.setMessageType(MESSAGE_TYPE_IMAGE);
        baseMessage.setShowTime(ChatUtil.getCurrentTime());
        baseMessage.setOwner(ChatConstant.MESSAGE_IS_OWNER);
        baseMessage.setIsRead(ChatConstant.MESSAGE_HAVE_READ);

        if (mChatType == ChatConstant.TYPE_SINGLE) {
            baseMessage.setReceiverId(mToChatUserId);
            baseMessage.setMessageTarget(ChatConstant.MESSAGE_TARGET_SINGLE);
        } else {
            baseMessage.setChatGroupId(mToChatUserId);
            baseMessage.setMessageTarget(ChatConstant.MESSAGE_TARGET_GROUP);

        }

        baseMessage.setSenderId(mOwnUserId);
        baseMessage.setSendingState(ChatConstant.MESSAGE_STATUS_INPROGRESS);
        baseMessage.setExtMap(mExtMap);

        baseMessage.setImageName(FileUtil.getFileName(absolutePath));
        baseMessage.setSize(FileUtil.getFileLength(absolutePath));
        baseMessage.setImagePath(absolutePath);


        mMessages.add(baseMessage);
        mChatMessageList.refreshSelectLast();

        LogUtil.e("camera file path " + absolutePath);
        FileUploader fileUploader = new FileUploader(mContext);


        //处理照片旋转问题
        int pictureDegree = readPictureDegree(absolutePath);
        LogUtil.e("处理前照片角度 " + pictureDegree);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(absolutePath, options);

        Bitmap compressedBitmap = compressBitmap(bitmap);
        Bitmap rotatedBitmap = rotate(compressedBitmap, pictureDegree);
        File rotatedBitmapFile = getFile(mContext, rotatedBitmap);

        fileUploader.setOnUploadListener(new OnUploadListener() {
            @Override
            public void onUploadComplete(List<String> urlList) {
                if (urlList.size() > 0) {
                    String imageUrl = BundleConst.getImageUrl() + urlList.get(0);
                    sendImageMessage(absolutePath, imageUrl, baseMessage);
                }
            }

            @Override
            public void onUploadFailure(String code, String message) {

            }
        });
        List<File> fileList = new ArrayList<>();
//        fileList.add(new File(absolutePath));
        fileList.add(rotatedBitmapFile);
        fileUploader.startUploadImages(fileList);

        int pictureDegree2 = readPictureDegree(rotatedBitmapFile.getAbsolutePath());
        LogUtil.e("处理后照片角度 " + pictureDegree2);


    }

    /**
     * 发送图片消息
     *
     * @param absolutePath
     * @param imageUrl
     * @param baseMessage
     */
    private void sendImageMessage(String absolutePath, final String imageUrl, BaseMessage baseMessage) {
        addToRecentContact();

        baseMessage.setFullPath(imageUrl);

        if (mChatType == ChatConstant.TYPE_SINGLE) {
            SingleMessageRequest request = new SingleMessageRequest();

            request.setReceiverId(mToChatUserId);
            request.setMessageType(MESSAGE_TYPE_IMAGE);
            request.setFullPath(imageUrl);
            request.setImageName(FileUtil.getFileName(absolutePath));
            request.setSize(FileUtil.getFileLength(absolutePath));
            request.setExtMap(mExtMap);

            mSendMessagePresenterCompl.sendSingleMessage(request, baseMessage, ChatConstant.MESSAGE_TYPE_IMAGE);
        } else {
            GroupMessageRequest request = new GroupMessageRequest();

            request.setChatGroupId(mToChatUserId);
            request.setMessageType(MESSAGE_TYPE_IMAGE);
            request.setFullPath(imageUrl);
            request.setImageName(FileUtil.getFileName(absolutePath));
            request.setSize(FileUtil.getFileLength(absolutePath));
            request.setExtMap(mExtMap);

            mSendMessagePresenterCompl.sendGroupMessage(request, baseMessage, ChatConstant.MESSAGE_TYPE_IMAGE);
        }


    }

    /**
     * 上传音频文件
     *
     * @param voiceFilePath
     * @param voiceTimeLength
     */
    private void uploadAudio(final String voiceFilePath, final int voiceTimeLength) {
        LogUtil.e("voiceFilePath " + voiceFilePath);
        LogUtil.e("voiceTimeLength " + voiceTimeLength);

        final BaseMessage baseMessage = new BaseMessage();
        baseMessage.setConversationId(mToChatUserId);
        baseMessage.setMessageType(MESSAGE_TYPE_AUDIO);
        baseMessage.setShowTime(ChatUtil.getCurrentTime());
        baseMessage.setOwner(ChatConstant.MESSAGE_IS_OWNER);
        baseMessage.setIsRead(ChatConstant.MESSAGE_HAVE_READ);

        if (mChatType == ChatConstant.TYPE_SINGLE) {
            baseMessage.setReceiverId(mToChatUserId);
            baseMessage.setMessageTarget(ChatConstant.MESSAGE_TARGET_SINGLE);
        } else {
            baseMessage.setChatGroupId(mToChatUserId);
            baseMessage.setMessageTarget(ChatConstant.MESSAGE_TARGET_GROUP);

        }
        baseMessage.setSenderId(mOwnUserId);
        baseMessage.setSendingState(ChatConstant.MESSAGE_STATUS_INPROGRESS);
        baseMessage.setExtMap(mExtMap);

        baseMessage.setVoiceName(FileUtil.getFileName(voiceFilePath));
        baseMessage.setLength(voiceTimeLength);
        baseMessage.setVoicePath(voiceFilePath);
        baseMessage.setForm(ChatConstant.AUDIO_FILE_FORMAT);


        mMessages.add(baseMessage);
        mChatMessageList.refreshSelectLast();

        FileUploader fileUploader = new FileUploader(mContext);

        fileUploader.setOnUploadListener(new OnUploadListener() {
            @Override
            public void onUploadComplete(List<String> urlList) {
                if (urlList.size() > 0) {
                    String fileUrl = BundleConst.getFileUrl() + urlList.get(0);
                    sendVoiceMessage(voiceTimeLength, fileUrl, voiceFilePath, baseMessage);
                }
            }

            @Override
            public void onUploadFailure(String code, String message) {

            }
        });
        List<File> fileList = new ArrayList<>();
        fileList.add(new File(voiceFilePath));
        fileUploader.startUploadFiles(fileList);


    }

    private void sendVoiceMessage(int voiceTimeLength, String fileUrl, String voiceFilePath, BaseMessage baseMessage) {
        addToRecentContact();

        baseMessage.setFilePath(fileUrl);

        if (mChatType == ChatConstant.TYPE_SINGLE) {
            SingleMessageRequest request = new SingleMessageRequest();
            request.setReceiverId(mToChatUserId);
            request.setMessageType(MESSAGE_TYPE_AUDIO);
            request.setExtMap(mExtMap);

            request.setLength(voiceTimeLength);
            request.setForm(AUDIO_FILE_FORMAT);
            request.setFilePath(fileUrl);
            request.setVoiceName(FileUtil.getFileName(voiceFilePath));

            mSendMessagePresenterCompl.sendSingleMessage(request, baseMessage, MESSAGE_TYPE_AUDIO);
        } else {
            GroupMessageRequest request = new GroupMessageRequest();
            request.setChatGroupId(mToChatUserId);
            request.setMessageType(MESSAGE_TYPE_AUDIO);
            request.setExtMap(mExtMap);

            request.setLength(voiceTimeLength);
            request.setForm(AUDIO_FILE_FORMAT);
            request.setFilePath(fileUrl);
            request.setVoiceName(FileUtil.getFileName(voiceFilePath));

            mSendMessagePresenterCompl.sendGroupMessage(request, baseMessage, MESSAGE_TYPE_AUDIO);
        }

    }

    /**
     * 消息发送成功，改变消息状态，插入数据库
     *
     * @param response
     * @param messageType
     */
    @Override
    public void singleMessageResponse(SendMessageResponse response, BaseMessage baseMessage, String messageType) {
        SendMessageResponse.MessageData messageData = response.getData();
        if (ChatConstant.MESSAGE_TYPE_TEXT.equals(messageType) || ChatConstant.MESSAGE_TYPE_IMAGE.equals(messageType)
                || ChatConstant.MESSAGE_TYPE_AUDIO.equals(messageType)) {

            baseMessage.setMessageId(messageData.getMessageId());
            baseMessage.setMessageTime(messageData.getMessageTime());
            baseMessage.setSendingState(ChatConstant.MESSAGE_STATUS_SUCCESS);

            mChatMessageList.refreshSelectLast();
            mMessageDao.insertMessage(ChatUtil.toChatMessage(baseMessage));

        }
    }

    @Override
    public void groupMessageResponse(SendMessageResponse response, BaseMessage baseMessage, String messageType) {
        SendMessageResponse.MessageData messageData = response.getData();
        if (ChatConstant.MESSAGE_TYPE_TEXT.equals(messageType) || ChatConstant.MESSAGE_TYPE_IMAGE.equals(messageType)
                || ChatConstant.MESSAGE_TYPE_AUDIO.equals(messageType)) {

            baseMessage.setMessageId(messageData.getMessageId());
            baseMessage.setMessageTime(messageData.getMessageTime());
            baseMessage.setSendingState(ChatConstant.MESSAGE_STATUS_SUCCESS);


            mChatMessageList.refreshSelectLast();
            mMessageDao.insertMessage(ChatUtil.toChatMessage(baseMessage));

        }
    }

    /**
     * hide
     */
    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setChatFragmentListener(ChatFragmentHelper chatFragmentHelper) {
        this.mChatFragmentHelper = chatFragmentHelper;
    }

    // TODO: 2016/12/17 本地数据库有50万数据时，插入卡顿
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (EMMessage message : list) {
            long userId = 0;
            BaseMessage baseMessage = ChatUtil.emMessagetoBaseMessage(message);

            baseMessage.setIsRead(ChatConstant.MESSAGE_HAVE_READ);
            if (baseMessage.getMessageTarget().equals(ChatConstant.MESSAGE_TARGET_SINGLE)) {
                userId = baseMessage.getSenderId();
            } else if (baseMessage.getMessageTarget().equals(ChatConstant.MESSAGE_TARGET_GROUP)) {
                userId = baseMessage.getChatGroupId();
            }
            if (userId == mToChatUserId) {
                mMessages.add(baseMessage);
                mChatMessageList.refreshSelectLast();

                ChatUI.getInstance().getNotifier().vibrateAndPlayTone(baseMessage);
            } else {
                ChatUI.getInstance().getNotifier().onNewMsg(baseMessage);
            }
            mMessageDao.insertMessage(ChatUtil.toChatMessage(baseMessage));

            Map<String, Object> extMap = baseMessage.getExtMap();
            String photo = (String) extMap.get(MessageExtend.EXT_PHOTO);
            String name = (String) extMap.get(MessageExtend.EXT_NAME);

            UserInfo userInfo = new UserInfo();
            userInfo.setContactId(baseMessage.getSenderId() + ChatConstant.CONTACT_TYPE_USER);
            userInfo.setUserId(baseMessage.getSenderId());
            userInfo.setContactType(ChatConstant.TYPE_SINGLE);
            userInfo.setPhoto(photo);
            userInfo.setName(name);
            mUserDao.insertUser(userInfo);

        }


    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (EMMessage message : list) {
            long userId = 0;
            BaseMessage baseMessage = ChatUtil.cmdMessagetoBaseMessage(message);

            baseMessage.setIsRead(ChatConstant.MESSAGE_HAVE_READ);
            if (baseMessage.getMessageTarget().equals(ChatConstant.MESSAGE_TARGET_SINGLE)) {
                userId = baseMessage.getSenderId();
            } else if (baseMessage.getMessageTarget().equals(ChatConstant.MESSAGE_TARGET_GROUP)) {
                userId = baseMessage.getChatGroupId();
            }
            if (userId == mToChatUserId) {
                mMessages.add(baseMessage);
                mChatMessageList.refreshSelectLast();

            }
            mMessageDao.insertMessage(ChatUtil.toChatMessage(baseMessage));

            Map<String, Object> extMap = baseMessage.getExtMap();
            String photo = (String) extMap.get(MessageExtend.EXT_PHOTO);
            String name = (String) extMap.get(MessageExtend.EXT_NAME);

            UserInfo userInfo = new UserInfo();
            userInfo.setContactId(baseMessage.getSenderId() + ChatConstant.CONTACT_TYPE_USER);
            userInfo.setUserId(baseMessage.getSenderId());
            userInfo.setContactType(ChatConstant.TYPE_SINGLE);
            userInfo.setPhoto(photo);
            userInfo.setName(name);
            mUserDao.insertUser(userInfo);

        }

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                if (mCameraFile != null && mCameraFile.exists()) {
                    uploadImage(mCameraFile.getAbsolutePath());

//                    int pictureDegree = readPictureDegree(mCameraFile.getAbsolutePath());
//                    LogUtil.e("照片的旋转角度为" + pictureDegree);
//                    String path = Environment.getExternalStorageDirectory() + "/helloooo.jpg";
//                    LogUtil.e("照片路径是 " + path);
//                    FileUtil.copyFile(mCameraFile, new File(path));
                }
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            }
        }
    }

    private void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            uploadImage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            uploadImage(file.getAbsolutePath());
        }
    }

    /**
     * 拍照
     */
    private void selectPicFromCamera() {
        if (!ChatUtil.isSdcardExist()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        LogUtil.e(ChatPathUtil.getInstance().getImagePath());
        mCameraFile = new File(ChatPathUtil.getInstance().getImagePath(),
                ChatUtil.getUserId() + "" + System.currentTimeMillis() + ChatConstant.IMAGE_FILE_FORMAT);
        //noinspection ResultOfMethodCallIgnored
        mCameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile)),
                REQUEST_CODE_CAMERA);
    }

    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 把图片转化为文件
     *
     * @param context
     * @param sourceImg
     * @return
     */
    public File getFile(Context context, Bitmap sourceImg) {
        try {
            File file = new File(context.getCacheDir(), System.currentTimeMillis() + ".jpg");
            LogUtil.d("filePath = ", file.getAbsolutePath());
            file.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int options = 100;
            sourceImg.compress(Bitmap.CompressFormat.JPEG, options, bos);
            while (bos.toByteArray().length / 1024 > 100) {
                bos.reset();
                options -= 10;
                sourceImg.compress(Bitmap.CompressFormat.JPEG, options, bos);
            }
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从相册中选择照片
     */
    private void selectPicFromLocal() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_LOCAL);

    }

    public void setGroupInfoHelper(GroupInfoHelper groupInfoHelper) {
        mGroupInfoHelper = groupInfoHelper;
    }


    public interface ChatFragmentHelper {
        /**
         * set mBaseMessage attribute
         */
        void onSetMessageAttributes(BaseMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();

        /**
         * on avatar clicked
         *
         * @param userId
         */
        void onAvatarClick(long userId);

        /**
         * on avatar long pressed
         *
         * @param userId
         */
        void onAvatarLongClick(long userId);

        /**
         * on mBaseMessage bubble clicked
         */
        boolean onMessageBubbleClick(BaseMessage message);

        /**
         * on mBaseMessage bubble long pressed
         */
        void onMessageBubbleLongClick(BaseMessage message);

        /**
         * on extend menu item clicked, return true if you want to override
         *
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);


        /**
         * 添加到最近联系人
         */
        void addToRecentContact();

        /**
         * on set custom chat row provider
         *
         * @return
         */
        ChatRowProvider onSetCustomChatRowProvider();
    }

    public interface GroupInfoHelper {
        GroupInfo obtainGroupInfo();
    }


    /**
     * handle the click event for extend menu
     */
    class MyItemClickListener implements ChatExtendMenu.ChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if (mChatFragmentHelper != null) {
                if (mChatFragmentHelper.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal();
                    break;
                case ITEM_LOCATION:
//                    startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                    break;

                default:
                    break;
            }
        }

    }

}
