package com.marbit.hobbytrophies.dao;


import com.marbit.hobbytrophies.model.meeting.Location;

public interface LocationDAOInterface {
    void insertLocation(String userId, Location location);
    void updateLocation(String userId, Location location);
}
