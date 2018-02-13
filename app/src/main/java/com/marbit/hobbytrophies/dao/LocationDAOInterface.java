package com.marbit.hobbytrophies.dao;


import com.marbit.hobbytrophies.dao.bodies.LocationUser;

public interface LocationDAOInterface {
    void insertLocation(String userId, LocationUser location);
    void updateLocation(String userId, LocationUser location);
}
