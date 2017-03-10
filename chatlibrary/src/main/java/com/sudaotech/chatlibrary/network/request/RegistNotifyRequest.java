package com.sudaotech.chatlibrary.network.request;

/**
 * Created by Samuel on 2016/12/6 14:30
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class RegistNotifyRequest {

    /**
     * deviceId :设备ID
     */

    private String deviceId;

    public RegistNotifyRequest(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
