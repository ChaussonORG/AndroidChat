package com.sudaotech.chatlibrary.utils;

import com.sudao.basemodule.base.BaseApplication;

import java.io.File;

/**
 * Created by Samuel on 2016/12/29 18:04
 * Email:xuzhou40@gmail.com
 * desc:聊天缓存目录相关
 */

public class ChatPathUtil {
    //    public static final String chatPath = "/Android/data/" + BundleConst.PACKAGE_NAME + "/chat/" + ChatUtil.getUserId();
    public static final String chatPath = BaseApplication.getInstance().getExternalFilesDir(null) + "/chat/" + ChatUtil.getUserId();
    public static final String imagePathName = "/image/";
    public static final String voicePathName = "/voice/";
    public static final String videoPathName = "/video/";
    public static final String filePathName = "/file/";
    public static String pathPrefix;
    private static ChatPathUtil instance = null;
    public String imagePath = chatPath + imagePathName;
    public String voicePath = chatPath + voicePathName;
    public String videoPath = chatPath + videoPathName;
    public String filePath = chatPath + filePathName;

    private ChatPathUtil() {
        initDirs();
    }

    public static ChatPathUtil getInstance() {
        if (instance == null) {
            instance = new ChatPathUtil();
        }

        return instance;
    }

    /**
     * 创建缓存目录
     */
    private void initDirs() {
        File imageDirectory = new File(imagePath);
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
        }

        File voiceDirectory = new File(voicePath);
        if (!voiceDirectory.exists()) {
            voiceDirectory.mkdirs();
        }


        File videoDirectory = new File(videoPath);
        if (!videoDirectory.exists()) {
            videoDirectory.mkdirs();
        }

        File fileDirectory = new File(filePath);
        if (!fileDirectory.exists()) {
            fileDirectory.mkdirs();
        }
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getFilePath() {
        return filePath;
    }
}
