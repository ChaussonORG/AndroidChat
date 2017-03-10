package com.sudaotech.chatlibrary.presenter;

import com.sudao.basemodule.http.CommonCallback;
import com.sudaotech.chatlibrary.model.BaseMessage;
import com.sudaotech.chatlibrary.network.HttpHelper;
import com.sudaotech.chatlibrary.network.request.GroupMessageRequest;
import com.sudaotech.chatlibrary.network.request.SingleMessageRequest;
import com.sudaotech.chatlibrary.network.response.SendMessageResponse;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Samuel on 2016/12/14 10:49
 * Email:xuzhou40@gmail.com
 * desc:发送消息
 */

public class SendMessagePresenterCompl implements ISendMessagePresenter {

    private ISendMessageView mISendMessageView;

    public SendMessagePresenterCompl(ISendMessageView ISendMessageView) {
        mISendMessageView = ISendMessageView;
    }

    /**
     * 发送单聊消息
     *
     * @param request
     * @param baseMessage
     * @param messageType
     */
    @Override
    public void sendSingleMessage(SingleMessageRequest request, final BaseMessage baseMessage, final String messageType) {
        Call<SendMessageResponse> call = HttpHelper.getChatService().sendSingleMessage(request);
        call.enqueue(new CommonCallback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                super.onResponse(call, response);
                SendMessageResponse body = response.body();
                if (body != null && "200".equals(body.getCode())) {
                    mISendMessageView.singleMessageResponse(body, baseMessage, messageType);
                }
            }
        });

    }

    /**
     * 发送群聊消息
     *
     * @param request
     * @param baseMessage
     * @param messageType
     */
    @Override
    public void sendGroupMessage(GroupMessageRequest request, final BaseMessage baseMessage, final String messageType) {
        Call<SendMessageResponse> call = HttpHelper.getChatService().sendGroupMessage(request);
        call.enqueue(new CommonCallback<SendMessageResponse>() {
            @Override
            public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                super.onResponse(call, response);
                SendMessageResponse body = response.body();
                if (body != null && "200".equals(body.getCode())) {
                    mISendMessageView.groupMessageResponse(body, baseMessage, messageType);
                }
            }
        });
    }
}
