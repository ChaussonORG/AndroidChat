package com.sudaotech.chatlibrary.network;

import com.sudao.basemodule.http.BaseResponse;
import com.sudaotech.chatlibrary.network.request.GroupDisturbRequest;
import com.sudaotech.chatlibrary.network.request.GroupMessageRequest;
import com.sudaotech.chatlibrary.network.request.RegistNotifyRequest;
import com.sudaotech.chatlibrary.network.request.SingleMessageRequest;
import com.sudaotech.chatlibrary.network.response.MessageRecordResponse;
import com.sudaotech.chatlibrary.network.response.SendMessageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Samuel on 2016/12/5 14:18
 * Email:xuzhou40@gmail.com
 * desc:
 */

public interface ChatService {
    /**
     * 发送聊天信息(单聊)
     *
     * @param request
     * @return
     */
    @POST("chat/message")
    @Headers({
            "appId:1"
    })
    Call<SendMessageResponse> sendSingleMessage(@Body SingleMessageRequest request);

    /**
     * 发送聊天信息(单聊)
     *
     * @param request
     * @return
     */
    @POST("chat/groupMessage")
    @Headers({
            "appId:1"
    })
    Call<SendMessageResponse> sendGroupMessage(@Body GroupMessageRequest request);

//    GET("/tasks")
//    Call<List<Task>> getTasks(@Header("Content-Range") String contentRange);

    /**
     * 注册通知
     *
     * @param request
     * @return
     */
    @POST("notify/register")
    @Headers({
            "appId:1"
    })
    Call<BaseResponse> registNotify(@Body RegistNotifyRequest request);

    /**
     * 个人聊天记录
     *
     * @return
     */
    @GET("chat/myMessage")
    @Headers({
            "appId:1"
    })
//    Call<BaseResponse> getMessageRecord(@Body MessageRecordRequest request);
    Call<BaseResponse<MessageRecordResponse>> getMessageRecord(@Query("offset") Integer offset,
                                                               @Query("limit") Integer limit,
                                                               @Query("page") Integer page,
                                                               @Query("receiverId") String receiverId,
                                                               @Query("messageId") String messageId);


    /**
     * 设置消息免打扰
     *
     * @param request
     * @return
     */
    @PUT("chat/groupUser/disturbMode")
    @Headers({
            "appId:1"
    })
    Call<BaseResponse> dontDisturb(@Body GroupDisturbRequest request);


}
