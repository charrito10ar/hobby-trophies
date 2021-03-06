package com.marbit.hobbytrophies.interactors.market;


import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marbit.hobbytrophies.interfaces.market.ProfileSoldPresenterInterface;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;
import com.marbit.hobbytrophies.utilities.Preferences;

import java.util.ArrayList;
import java.util.List;

public class ProfileSoldFragmentInteractor {
    private final FirebaseDatabase database;
    private final DatabaseReference databaseReference;
    private ProfileSoldPresenterInterface presenterInterface;
    private Context context;

    public ProfileSoldFragmentInteractor(Context context, ProfileSoldPresenterInterface presenterInterface) {
        this.context = context;
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(DataBaseConstants.COLUMN_ITEMS);
        this.presenterInterface = presenterInterface;
    }

    public void loadItems() {
        Query recentPostsQuery = databaseReference.orderByChild(DataBaseConstants.CHILD_USER_NAME).equalTo(Preferences.getUserId(context));
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    if(item.getStatus() == Item.SOLD)
                        items.add(item);
                }
                presenterInterface.loadItemsSuccess(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
