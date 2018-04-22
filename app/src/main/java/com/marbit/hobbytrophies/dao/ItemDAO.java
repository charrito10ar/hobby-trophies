package com.marbit.hobbytrophies.dao;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marbit.hobbytrophies.chat.dao.ChatDAO;
import com.marbit.hobbytrophies.market.model.UserMarket;
import com.marbit.hobbytrophies.model.market.Filter;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.model.meeting.Location;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.marbit.hobbytrophies.utilities.Utilities;

import org.jetbrains.annotations.NotNull;

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
    private Context context;


    public ItemDAO(Context context){
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
        this.context = context;
    }

    public ItemDAO(Context context, ItemDAOListener mListener){
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
        this.mListener = mListener;
        this.context = context;
    }

    public ItemDAO(Context context, ItemDAOEditListener mEditListener){
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
        this.mEditListener = mEditListener;
        this.context = context;
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
    public void loadItemById(final Context context, String itemId, final SingleItemDAOListener singleItemDAOListener) {
        databaseReference.child(DataBaseConstants.COLUMN_ITEMS).child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Item item = dataSnapshot.getValue(Item.class);
                UserDAO userDAO = new UserDAO(context);
                userDAO.getUserLocation(Preferences.getUserId(context), new UserDAO.ListenerUserLocationDAO() {
                    @Override
                    public void loadUserLocationSuccessful(Location location) {
                        item.setLocation(location);
                        singleItemDAOListener.loadItemByIdSuccess(item);
                    }

                    @Override
                    public void loadUserLocationError(String errorMessage) {
                        singleItemDAOListener.loadItemByIdError(errorMessage);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                singleItemDAOListener.loadItemByIdError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void loadUserItems(String UserId) {

    }

    @Override
    public void loadItemsByFilter(final Filter filter) {
        Query itemFilterQuery = null;
        switch (filter.getOrderBy()){
            case DataBaseConstants.ORDER_BY_MOST_RECENTLY:
                itemFilterQuery = databaseReference.child(DataBaseConstants.COLUMN_ITEMS).orderByChild(DataBaseConstants.CHILD_DATE).limitToLast(40);
                break;
            case DataBaseConstants.ORDER_BY_CHEAPEST:
                itemFilterQuery = databaseReference.child(DataBaseConstants.COLUMN_ITEMS).orderByChild(DataBaseConstants.CHILD_PRICE).limitToFirst(40);
                break;
            case DataBaseConstants.ORDER_BY_MOST_EXPENSIVE:
                itemFilterQuery = databaseReference.child(DataBaseConstants.COLUMN_ITEMS).orderByChild(DataBaseConstants.CHILD_PRICE).limitToLast(40);
                break;
        }

        itemFilterQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final List<Item> items = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    final Item item = postSnapshot.getValue(Item.class);
                    UserDAO userDAO = new UserDAO(context);
                    userDAO.getUserLocation(item.getUserId(), new UserDAO.ListenerUserLocationDAO() {
                        @Override
                        public void loadUserLocationSuccessful(Location location) {
                            item.setLocation(location);
                            if(item.getStatus() == 0) {// NO ESTA VENDIDO
                                if(isItemToAdd(item, filter) && isDistanceSelected(item, filter)){
                                    items.add(item);
                                }
                            }

                            if(filter.getOrderBy() == DataBaseConstants.ORDER_BY_MOST_EXPENSIVE || filter.getOrderBy() == DataBaseConstants.ORDER_BY_MOST_RECENTLY){
                                Collections.reverse(items);
                            }
                            mListener.loadItemsByFilterSuccess(items);
                        }

                        @Override
                        public void loadUserLocationError(String errorMessage) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isDistanceSelected(Item item, Filter filter) {
        if(filter.getMaxDistance() < 5000 && item.getLocation() != null){
            if(Utilities.locationIsNearBy(context, item.getLocation(), filter.getMaxDistance()* Constants.TEN_KM)){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    private boolean isItemToAdd(Item item, Filter filter) {
        if (filter.getItemCategory() == -1) {
            if(isTextMatch(item.getTitle(), filter.getText())){
                return true;
            }else {
                return false;
            }
        } else {
            if (item.getItemCategory() + 1 == filter.getItemCategory()) {
                if(isTextMatch(item.getTitle(), filter.getText())){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }
    }

    private boolean isTextMatch(String itemTitle, String filterText) {
        return filterText.equals("") || itemTitle.toLowerCase().contains(filterText);
    }

    public void delete(Item item) {
        Query applesQuery = databaseReference.child("items").child(item.getId());
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
    }

    @Override
    public void markSold(@NotNull Item item, @NotNull UserMarket userMarket) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", item.getStatus());
        databaseReference.child("items").child(item.getId()).updateChildren(map);
        // Update Message Chat
        ChatDAO chatDAO = new ChatDAO();
        chatDAO.insertItemSoldMessage(userMarket.getChatId(), userMarket.getId());
        databaseReference.child(DataBaseConstants.COLUMN_CHAT_MESSAGES).child(item.getId()).updateChildren(map);
    }


    @Override
    public void updateItem(Item item, EditItemDAOListener mListener) {
        databaseReference.child(DataBaseConstants.COLUMN_ITEMS).child(item.getId()).child(DataBaseConstants.CHILD_DESCRIPTION).setValue(item.getDescription());
        databaseReference.child(DataBaseConstants.COLUMN_ITEMS).child(item.getId()).child(DataBaseConstants.CHILD_PRICE).setValue(item.getPrice());
        databaseReference.child(DataBaseConstants.COLUMN_ITEMS).child(item.getId()).child(DataBaseConstants.CHILD_BARTER).setValue(item.isBarter());
        databaseReference.child(DataBaseConstants.COLUMN_ITEMS).child(item.getId()).child(DataBaseConstants.CHILD_DIGITAL).setValue(item.isDigital());
        mListener.editItemSuccess();
    }

    @Override
    public void loadPossiblesBuyers(String itemId, final ItemSoldDAOListener mListener) {
        databaseReference.child(DataBaseConstants.COLUMN_ITEM_CHATS).child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    List<UserMarket> userMarkets = new ArrayList<>();
                    for (DataSnapshot possibleBuyer :dataSnapshot.getChildren()) {
                        UserMarket userMarket = new UserMarket();
                        userMarket.setId(possibleBuyer.getKey());
                        userMarket.setName(possibleBuyer.getKey());
                        userMarket.setChatId(possibleBuyer.getValue().toString());
                        userMarkets.add(userMarket);
                    }
                    mListener.loadPossiblesBuyersSuccessful(userMarkets);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public interface ItemDAOListener{
        void loadItemsByFilterSuccess(List<Item> itemList);
    }

    public interface ItemDAOEditListener{
        void deleteItemSuccess();
    }

    public interface SingleItemDAOListener{
        void loadItemByIdSuccess(Item item);
        void loadItemByIdError(String message);
    }

    public interface EditItemDAOListener{
        void editItemSuccess();
        void editItemError(String message);
    }

    public interface ItemSoldDAOListener{
        void loadPossiblesBuyersSuccessful(List<UserMarket> userMarketList);
    }
}