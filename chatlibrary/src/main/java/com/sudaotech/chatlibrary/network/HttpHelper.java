package com.sudaotech.chatlibrary.network;

/**
 * Created by Samuel on 2016/12/5 14:17
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class HttpHelper {
    private static ChatService chatService;

    public HttpHelper() {
    }

    public static ChatService getChatService() {
        if (chatService == null) {
            chatService = ChatServiceGenerator.createService(ChatService.class);
        }
        return chatService;
    }
}
