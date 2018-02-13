package com.marbit.hobbytrophies.dao;

public interface UserDAOInterface {
    void getUserBasicProfile(String user, UserDAO.ListenerUserDAO listenerUserDAO);
    void getUserLocation(String userId, UserDAO.ListenerUserLocationDAO listenerUserDAO);
}
