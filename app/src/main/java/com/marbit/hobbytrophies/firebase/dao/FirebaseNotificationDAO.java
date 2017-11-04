package com.marbit.hobbytrophies.firebase.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class FirebaseNotificationDAO implements FirebaseNotificationDAOInterface {


    public FirebaseNotificationDAO() {
    }

    @Override
    public void registerToken(String userId, String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(DataBaseConstants.COLUMN_USER_FB_TOKENS).child(userId).child(token);
        databaseReference.setValue(true);
    }

    @Override
    public void unregisterToken(String userId, String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(DataBaseConstants.COLUMN_USER_FB_TOKENS).child(userId).child(token);
        databaseReference.removeValue();
    }

    @Override
    public void creteDevicesGroup(String userId, String token) {

    }

    @Override
    public void addTokenToDeviceGroup(String userId, String token) {

    }

    @Override
    public void getRegistrationTokens(String userId, final FirebaseNotificationDAOListener mListener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(DataBaseConstants.COLUMN_USER_FB_TOKENS).child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                JSONArray jsonArray = new JSONArray();
                for(DataSnapshot token: dataSnapshot.getChildren()){
                    jsonArray.put(token.getKey());
                }
                try {
                    mListener.loadRegistrationTokensSuccess(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getRegistrationTokens(List<String> userIdList, final FirebaseNotificationDAOListener mListener) {
        final JSONArray jsonArray = new JSONArray();
        for (String userId: userIdList) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_USER_FB_TOKENS).child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot token: dataSnapshot.getChildren()){
                        jsonArray.put(token.getKey());
                    }
                    try {
                        mListener.loadRegistrationTokensSuccess(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public interface FirebaseNotificationDAOListener{
        void loadRegistrationTokensSuccess(JSONArray registrationTokens) throws JSONException;
    }
}
