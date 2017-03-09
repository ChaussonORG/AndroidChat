/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sudaotech.chatlibrary;

import android.os.Environment;

import java.io.File;

public class ChatConstant {
    //消息状态
    public static final int MESSAGE_STATUS_CREATE = 0;
    public static final int MESSAGE_STATUS_SUCCESS = 1;
    public static final int MESSAGE_STATUS_FAIL = 2;
    public static final int MESSAGE_STATUS_INPROGRESS = 3;

    //聊天类型:单聊，群聊(本地数据库保存的是int类型)
    public static final int TYPE_SINGLE = 1;//单聊
    public static final int TYPE_GROUP = 2;//群聊

    //聊天类型:单聊，群聊(后台保存的是String)
    public static final String MESSAGE_TARGET_SINGLE = "SINGAL_CHAT";//单聊
    public static final String MESSAGE_TARGET_GROUP = "GROUP_CHAT";//群聊


    //业务聊天类型：单聊，自由群，商会群
    public static final String CHAT_TYPE_USER = "USER";//单聊
    public static final String CHAT_TYPE_GROUP = "FREE_GROUP";//自由群
    public static final String CHAT_TYPE_CHAMBER = "CHAMBER_GROUP";//商会群
    public static final String CHAT_TYPE_GROUPP = "GROUP";//群(统称)

    public static final String CONTACT_TYPE_USER = "user";//个人
    public static final String CONTACT_TYPE_GROUP = "group";//群


    //消息归属,1是自己，0是其他人
    public static final int MESSAGE_IS_OWNER = 1;
    public static final int MESSAGE_IS_OTHER = 0;

    //消息类型
    public static final String MESSAGE_TYPE_TEXT = "TEXT";//文本
    public static final String MESSAGE_TYPE_IMAGE = "IMAGE";//图片
    public static final String MESSAGE_TYPE_AUDIO = "AUDIO";//语音
    public static final String MESSAGE_TYPE_REDBAG = "REDBAG";//红包

    public static final String NOTIFY_TYPE_CHAMBER_NOTIFY = "CHAMBER_NOTIFY";//商会通知
    public static final String NOTIFY_TYPE_SYSTEM_NOTIFY = "SYSTEM_NOTIFY";//系统通知
    public static final String NOTIFY_TYPE_PRAISE_NOTIFY = "PRAISE_NOTIFY";//点赞通知
    public static final String NOTIFY_TYPE_COMMENT_NOTIFY = "COMMENT_NOTIFY";//评论通知
    public static final String NOTIFY_TYPE_ACTITY_NOTIFY = "ACTITY_NOTIFY";//活动通知
    public static final String NOTIFY_TYPE_CANCEL_FREE_GROUP_NOTIFY = "CANCEL_FREE_GROUP_NOTIFY";//退出自由群通知
    public static final String NOTIFY_TYPE_DEL_ADD_GROUP_USER_NOTIFY = "ADD_GROUP_USER_NOTIFY";//添加群成员通知
    public static final String NOTIFY_TYPE_DEL_DEL_GROUP_USER_NOTIFY = "DEL_GROUP_USER_NOTIFY";//删除群成员通知
    public static final String NOTIFY_TYPE_ADD_FRIEND_NOTIFY = "ADD_FRIEND_NOTIFY";//添加好友通知
    public static final String NOTIFY_TYPE_DEL_FRIEND_NOTIFY = "DEL_FRIEND_NOTIFY";//删除好友通知

    //语音消息是否已听
    public static final int VOICE_HAVE_LISTENED = 1;//已听
    public static final int VOICE_NOT_LISTEN = 0;//未听

    //消息是否已读
    public static final int MESSAGE_HAVE_READ = 1;//已读
    public static final int MESSAGE_UNREAD = 0;//未读

    public static final String AUDIO_FILE_FORMAT = ".mp3";
    public static final String IMAGE_FILE_FORMAT = ".jpg";


    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_USER_NAME = "userName";

    public static final String EXTRA_GROUP_ID = "groupId";
    public static final String EXTRA_GROUP_TYPE = "groupType";
    public static final String EXTRA_GROUP_NAME = "groupName";
    public static final String EXTRA_FILE_PATH = "filePath";
    public static final String EXTRA_FILE_URL = "fileUrl";
    public static final String EXTRA_GROUP_MEMBER_COUNT = "groupMemberCount";
    public static final String EXTRA_RED_PACKET_ID = "groupRedPacketId";
    public static final String EXTRA_RED_PACKET_DESC = "redPacketDesc";


    public static final String USER_INFO = "user_info"; //用户全部信息


    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";


    public static final String MESSAGE_ATTR_AT_MSG = "em_at_list";
    public static final String MESSAGE_ATTR_VALUE_AT_MSG_ALL = "ALL";
    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String CHAT_ROOM = "item_chatroom";
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static final String ACCOUNT_CONFLICT = "conflict";
    public static final String ACCOUNT_FORBIDDEN = "user_forbidden";
    public static final String CHAT_ROBOT = "item_robots";
    public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";

    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";


    public static final String EXTRA_RED_PACKET_USER_INFO = "redPacketUserInfo";

    //是否有未读消息
    public static final String HINT_NEW_MESSAGE = "newMessage";

    //聊天图片保存路径
    public static final String APP_PICTURE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Ecoc" + File.separator + "pictures" + File.separator;
}
