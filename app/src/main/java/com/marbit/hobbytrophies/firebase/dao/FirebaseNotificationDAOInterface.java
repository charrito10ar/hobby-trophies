package com.marbit.hobbytrophies.firebase.dao;

import java.util.List;

public interface FirebaseNotificationDAOInterface {
    void registerToken(String userId, String token);

    void unregisterToken(String userId, String token);

    void creteDevicesGroup(String userId, String token);

    void addTokenToDeviceGroup(String userId, String token);

    void getRegistrationTokens(String userId, FirebaseNotificationDAO.FirebaseNotificationDAOListener mListener);

    void getRegistrationTokens(List<String> userIdList, FirebaseNotificationDAO.FirebaseNotificationDAOListener mListener);
}
