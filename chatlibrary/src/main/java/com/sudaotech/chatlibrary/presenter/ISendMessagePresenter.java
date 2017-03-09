package com.sudaotech.chatlibrary.presenter;

import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.network.request.GroupMessageRequest;
import com.sudaotech.chatlibrary.network.request.SingleMessageRequest;

/**
 * Created by Samuel on 2016/12/14 11:23
 * Email:xuzhou40@gmail.com
 * desc:
 */

public interface ISendMessagePresenter {
    void sendSingleMessage(SingleMessageRequest request, BaseMessage baseMessage, String messageType);

    void sendGroupMessage(GroupMessageRequest request, BaseMessage baseMessage, String messageType);
}
