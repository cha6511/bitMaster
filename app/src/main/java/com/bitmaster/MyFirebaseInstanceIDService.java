package com.bitmaster;

import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by Administrator on 2018-03-07.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private String token = "";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

    }

}
