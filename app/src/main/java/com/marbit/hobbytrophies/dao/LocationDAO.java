package com.marbit.hobbytrophies.dao;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marbit.hobbytrophies.model.meeting.Location;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;

public class LocationDAO implements LocationDAOInterface{
    private final FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Context context;

    public LocationDAO(Context context) {
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
        this.context = context;
    }

    @Override
    public void insertLocation(String userId, Location location) {
        databaseReference = database.getReference().child(DataBaseConstants.COLUMN_USER_LOCATION).child(userId);
        databaseReference.setValue(location);
    }

    @Override
    public void updateLocation(String userId, Location location) {

    }
}
