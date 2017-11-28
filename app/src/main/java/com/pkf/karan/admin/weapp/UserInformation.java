package com.pkf.karan.admin.weapp;

import android.app.Application;

/**
 * Created by Aakash on 22-Nov-17.
 */

public class UserInformation extends Application {

    private static UserInformation userInfo;

    String userId;
    String userName;

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
}
