package com.marbit.hobbytrophies.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marbit.hobbytrophies.market.model.Rate;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RateDAO implements RateDAOInterface{
    private final FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public RateDAO() {
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
    }


    @Override
    public void insertRate(Rate rate, RateListener rateListener) {
        databaseReference = database.getReference().child(DataBaseConstants.COLUMN_USER_RATES).child(rate.getUserId()).push();
        String rateId = databaseReference.getKey();
        rate.setId(rateId);
        databaseReference.setValue(rate);
        rateListener.rateSuccessful();
    }

    @Override
    public void loadRatesByUser(String userId, final LoadRateListener loadRateListener) {
        database.getReference().child(DataBaseConstants.COLUMN_USER_RATES).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Rate> rateList = new ArrayList<>();
                int reputationSum = 0;
                for (DataSnapshot dataSnapshotRate : dataSnapshot.getChildren()) {
                    Rate rate = dataSnapshotRate.getValue(Rate.class);
                    assert rate != null;
                    reputationSum = rate.getValue() + reputationSum;
                    rateList.add(rate);
                }
                float reputation = 0;
                if(rateList.size()>0){
                    reputation = reputationSum/rateList.size();
                }
                loadRateListener.loadRateSuccessful(rateList, reputation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface RateListener{
        void rateSuccessful();
    }

    public interface LoadRateListener{
        void loadRateSuccessful(List<Rate> rateList, float reputation);
    }
}
