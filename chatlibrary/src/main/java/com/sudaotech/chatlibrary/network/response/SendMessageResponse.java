package com.sudaotech.chatlibrary.network.response;


/**
 * Created by Samuel on 2016/12/14 11:06
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class SendMessageResponse {
    private String code;
    private String message;
    private MessageData data;

    public SendMessageResponse() {
    }

    public SendMessageResponse(MessageData data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageData getData() {
        return data;
    }

    public void setData(MessageData data) {
        this.data = data;
    }

    public class MessageData {
        private long messageTime;
        private long messageId;

        public MessageData() {
        }

        public MessageData(long messageTime, long messageId) {
            this.messageTime = messageTime;
            this.messageId = messageId;
        }

        public long getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(long messageTime) {
            this.messageTime = messageTime;
        }

        public long getMessageId() {
            return messageId;
        }

        public void setMessageId(long messageId) {
            this.messageId = messageId;
        }
    }
}
