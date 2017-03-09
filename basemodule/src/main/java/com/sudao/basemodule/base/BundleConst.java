package com.sudao.basemodule.base;

import android.os.Environment;

import com.sudao.basemodule.common.util.SPHelper;

import java.io.File;

/**
 * Created by Samuel on 5/11/16 18:49
 * Email:xuzhou40@gmail.com
 * desc:
 */
public class BundleConst {
    public static final String APP_NAME = "ecoc";
    public static final String PACKAGE_NAME = "com.sudaotech.ecoc";//应用包名

    public static final String CHAT_BASE_URL_TEST = "http://chat.sudaotech.com/chat/";//聊天测试服务器
    public static final String BASE_URL_TEST = "http://ecoc.dev.sudaotech.com/";//测试环境

    public static final String CHAT_BASE_URL = "http://chat.cnecoc.com/chat/";//聊天服务器-正式环境
    public static final String BASE_URL = "http://cnecoc.com/";//正式环境

    public static final String BASE_URL_IMAGE = "platform/image";//图片地址
    public static final String BASE_URL_FILE = "platform/file";//文件地址

    public static final String APP_CACHE_NAME = APP_NAME + "_cache";
    public static final String APP_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + APP_NAME + File.separator + "download_file";
    // 第三方登录中accountTypeKey字段
    public static final String ACCOUNTTYPEKEY_QQ = "qq";
    public static final String ACCOUNTTYPEKEY_WEIXIN = "wx";
    public static final String ACCOUNTTYPEKEY_WEIBO = "wb";
    //阿里妈妈淘客PID
    public static final String ALIMAMA_TAOKE_PID = "";
    //微信 AppID
    public static final String WECHAT_APP_ID = "";
    //微信 AppSecret
    public static final String WECHAT_APP_SECRET = "";

    //小米AppID，AppKey，AppSecret
    public static final String XIAOMI_APP_ID = "";
    public static final String XIAOMI_APP_KEY = "";
    public static final String XIAOMI_APP_SECRET = "";

    //华为AppID，AppSecret
    public static final String HUAWEI_APP_ID = "";
    public static final String HUAWEI_APP_SECRET = "";
    //微博 App Key
    public static final String WEIBO_APP_KEY = "";
    //微博 App Secret
    public static final String WEIBO_APP_SECRET = "";
    //QQ APP ID
    public static final String QQ_APP_ID = "";
    //QQ APP KEY
    public static final String QQ_APP_KEY = "";
    public static final String RMB = "¥";
    public static final String PLUS = "+";
    public static final String MINUS = "-";

    public static String BASE_ENVIRONMENT = "environment";
    public static String BASE_CHAT_ENVIRONMENT = "chatEnvironment";

    public BundleConst() {
    }

    public static String getUrl() {
        String url = SPHelper.getString(BASE_ENVIRONMENT, BASE_URL_TEST);
        return url;
    }

    public static String getChatUrl() {
        String url = SPHelper.getString(BASE_CHAT_ENVIRONMENT, CHAT_BASE_URL_TEST);
        return url;
    }

    public static String getImageUrl() {
        String url = SPHelper.getString(BASE_ENVIRONMENT, BASE_URL_TEST) + BASE_URL_IMAGE;
        return url;
    }

    public static String getFileUrl() {
        String url = SPHelper.getString(BASE_ENVIRONMENT, BASE_URL_TEST) + BASE_URL_FILE;
        return url;
    }

}
