package com.pkf.karan.admin.weapp;

import android.app.Application;



public class UserInformation extends Application {

    private static UserInformation userInfo;

    String userId;
    String userName;
    String userPhone;
    String userEmail;
    Boolean hasRejected = false;
    String serverUrl;
    String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Boolean getHasRejected() {
        return hasRejected;
    }

    public void setHasRejected(Boolean hasRejected) {
        this.hasRejected = hasRejected;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
