package com.sudaotech.chatlibrary;

/**
 * Created by Samuel on 2016/12/4 14:21
 * Email:xuzhou40@gmail.com
 * desc:
 */

public interface MessageCallback {
    void onSuccess();

    void onError(int var1, String var2);

    void onProgress(int progress, String status);
}
