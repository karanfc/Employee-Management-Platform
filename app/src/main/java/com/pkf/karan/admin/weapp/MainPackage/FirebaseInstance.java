package com.pkf.karan.admin.weapp.MainPackage;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pkf.karan.admin.weapp.UserInformation;

import static android.content.ContentValues.TAG;

/**
 * Created by karan on 16/12/2017.
 */

public class FirebaseInstance extends FirebaseInstanceIdService {

    UserInformation userInfo;
    String refreshedToken;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.


    }

    public String getRefreshedToken()
    {
        return refreshedToken;
    }
}
