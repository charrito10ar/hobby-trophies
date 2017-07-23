package com.marbit.hobbytrophies.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marbit.hobbytrophies.model.market.Filter;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDAO implements ItemDAOInterface{

    private final FirebaseDatabase database;
    private final DatabaseReference databaseReference;
    private ItemDAOListener mListener;
    private ItemDAOEditListener mEditListener;


    public ItemDAO(ItemDAOListener mListener){
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
        this.mListener = mListener;
    }

    public ItemDAO(ItemDAOEditListener mEditListener){
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
        this.mEditListener = mEditListener;
    }
    @Override
    public void loadItems() {
//        Query recentPostsQuery = databaseReference.orderByChild(DataBaseConstants.CHILD_USER_NAME);
//        recentPostsQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                List<Item> items = new ArrayList<>();
//                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
//                    Item item = postSnapshot.getValue(Item.class);
//                    items.add(item);
//                }
//                //presenterInterface.loadItemsSuccess(items);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public void loadUserItems(String UserId) {

    }

    @Override
    public void loadItemsByFilter(final Filter filter) {
        Query itemFilterQuery = null;
        switch (filter.getOrderBy()){
            case DataBaseConstants.ORDER_BY_MOST_RECENTLY:
                itemFilterQuery = databaseReference.child(DataBaseConstants.COLUMN_ITEMS).orderByChild(DataBaseConstants.CHILD_DATE).limitToLast(10);
                break;
            case DataBaseConstants.ORDER_BY_CHEAPEST:
                itemFilterQuery = databaseReference.child(DataBaseConstants.COLUMN_ITEMS).orderByChild(DataBaseConstants.CHILD_PRICE).limitToFirst(10);
                break;
            case DataBaseConstants.ORDER_BY_MOST_EXPENSIVE:
                itemFilterQuery = databaseReference.child(DataBaseConstants.COLUMN_ITEMS).orderByChild(DataBaseConstants.CHILD_PRICE).limitToLast(10);
                break;
        }



        itemFilterQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    items.add(item);
                }
                if(filter.getOrderBy() == DataBaseConstants.ORDER_BY_MOST_EXPENSIVE || filter.getOrderBy() == DataBaseConstants.ORDER_BY_MOST_RECENTLY){
                    Collections.reverse(items);
                }
                mListener.loadItemsByFilterSuccess(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void delete(Item item) {
        Query applesQuery = databaseReference
                .child("items").child(item.getId());
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                mEditListener.deleteItemSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("", "onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void markSold(Item item) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", item.getStatus());
        databaseReference.child("items").child(item.getId()).updateChildren(map);
        mEditListener.markAsSoldSuccess();
    }

    @Override
    public void unmarkAsSold(Item item) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", item.getStatus());
        databaseReference.child("items").child(item.getId()).updateChildren(map);
        mEditListener.unmarkAsSoldSuccess();
    }

    public interface ItemDAOListener{
        void loadItemsByFilterSuccess(List<Item> itemList);
    }

    public interface ItemDAOEditListener{
        void deleteItemSuccess();
        void markAsSoldSuccess();
        void unmarkAsSoldSuccess();
    }
}
