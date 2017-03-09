package com.sudaotech.chatlibrary.presenter;

import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.network.response.SendMessageResponse;

/**
 * Created by Samuel on 2016/12/14 11:22
 * Email:xuzhou40@gmail.com
 * desc:
 */

public interface ISendMessageView {
    void singleMessageResponse(SendMessageResponse response, BaseMessage baseMessage, String messageType);

    void groupMessageResponse(SendMessageResponse response, BaseMessage baseMessage, String messageType);
}
