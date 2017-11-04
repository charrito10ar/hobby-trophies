package com.marbit.hobbytrophies.firebase;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.marbit.hobbytrophies.firebase.dao.FirebaseNotificationDAO;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.Preferences;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";


    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN)){
            sendRegistrationToServer(refreshedToken);
        }
    }


    private void sendRegistrationToServer(String token) {
        FirebaseNotificationDAO firebaseNotificationDAO = new FirebaseNotificationDAO();
        firebaseNotificationDAO.registerToken(Preferences.getUserName(getApplicationContext()), token);
    }
}