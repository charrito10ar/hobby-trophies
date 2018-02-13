package com.marbit.hobbytrophies.dao;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marbit.hobbytrophies.dao.bodies.LocationUser;
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
    public void insertLocation(String userId, LocationUser location) {
        databaseReference = database.getReference().child(DataBaseConstants.COLUMN_USER_LOCATION).child(userId);
        databaseReference.setValue(location);
    }

    @Override
    public void updateLocation(String userId, LocationUser location) {

    }
}
