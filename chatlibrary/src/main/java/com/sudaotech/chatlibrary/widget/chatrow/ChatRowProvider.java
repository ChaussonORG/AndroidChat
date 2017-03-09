package com.sudaotech.chatlibrary.widget.chatrow;

import android.widget.BaseAdapter;

import com.sudaotech.chatlibrary.model.BaseMessage;


public interface ChatRowProvider {
    /**
     * 获取多少种类型的自定义chatrow
     * 注意，每一种chatrow至少有两种type：发送type和接收type
     *
     * @return
     */
    int getCustomChatRowTypeCount();

    /**
     * 获取chatrow type，必须大于0, 从1开始有序排列
     *
     * @return
     */
    int getCustomChatRowType(BaseMessage message);

    /**
     * 根据给定message返回chat row
     *
     * @return
     */
    BaseChatRow getCustomChatRow(BaseMessage message, int position, BaseAdapter adapter);
}
