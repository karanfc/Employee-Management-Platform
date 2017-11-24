package com.pkf.karan.admin.weapp;

import android.app.Application;

/**
 * Created by Aakash on 22-Nov-17.
 */

public class UserInformation extends Application {

    private static UserInformation userInfo;

    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
