package com.marbit.hobbytrophies.dao;


import com.marbit.hobbytrophies.model.User;

public class UserDAO implements UserDAOInterface {

    @Override
    public void getUserBasicProfile(String user, ListenerUserDAO listenerUserDAO) {
            //Hago el get en internet lueg
        listenerUserDAO.loadUserBasicProfileSuccessful(null);
    }

    public interface ListenerUserDAO{
        void loadUserBasicProfileSuccessful(User user);
    }
}
