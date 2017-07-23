package com.marbit.hobbytrophies.interactors.market;


import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marbit.hobbytrophies.dao.ItemDAO;
import com.marbit.hobbytrophies.interfaces.market.MarketFragmentPresenterInterface;
import com.marbit.hobbytrophies.model.market.Filter;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;

import java.util.ArrayList;
import java.util.List;

public class MarketFragmentInteractor implements ItemDAO.ItemDAOListener {
    private Context context;
    private FirebaseDatabase database;
    private final DatabaseReference databaseReference;
    private MarketFragmentPresenterInterface presenterInterface;

    public MarketFragmentInteractor(Context context, MarketFragmentPresenterInterface presenterInterface) {
        this.context = context;
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(DataBaseConstants.COLUMN_ITEMS);
        this.presenterInterface = presenterInterface;
    }

    public void loadItems() {
        Query recentPostsQuery = databaseReference
                .orderByChild(DataBaseConstants.CHILD_STATUS)
                .equalTo(0);
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    items.add(item);
                }
                presenterInterface.loadItemsSuccess(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void applyFilter(Filter filter) {
        ItemDAO itemDAO = new ItemDAO(this);
        itemDAO.loadItemsByFilter(filter);
    }

    @Override
    public void loadItemsByFilterSuccess(List<Item> itemList) {
        presenterInterface.loadItemsSuccess(itemList);
    }
}
