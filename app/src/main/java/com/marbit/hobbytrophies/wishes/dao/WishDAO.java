package com.marbit.hobbytrophies.wishes.dao;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marbit.hobbytrophies.dao.ItemDAO;
import com.marbit.hobbytrophies.firebase.FirebaseNotifications;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.wishes.model.Wish;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.marbit.hobbytrophies.utilities.DataBaseConstants.COLUMN_USER_WISH_LIST;
import static com.marbit.hobbytrophies.utilities.DataBaseConstants.COLUMN_WISH_ITEMS;
import static com.marbit.hobbytrophies.utilities.DataBaseConstants.COLUMN_WISH_LIST;

public class WishDAO implements WishDAOInterface{


    private final FirebaseDatabase database;
    private final DatabaseReference databaseReference;
    private WishDAOListener mListener;

    public WishDAO(){
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
    }

    @Override
    public void addWish(String userId, Wish wish) {
        String id = databaseReference.child(COLUMN_WISH_LIST).push().getKey();
        wish.setId(id);
        databaseReference.child(COLUMN_WISH_LIST).child(id).setValue(wish);
        databaseReference.child(COLUMN_USER_WISH_LIST).child(userId).child(id).setValue(true); // Se agrega al usuario
    }

    @Override
    public void deleteWish(String userId, Wish wish, final WishDAODeleteListener mListener) {
        databaseReference.child(COLUMN_USER_WISH_LIST).child(userId).child(wish.getId()).removeValue();
        databaseReference.child(COLUMN_WISH_LIST).child(wish.getId()).removeValue();
        databaseReference.child(COLUMN_WISH_ITEMS).child(wish.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mListener.deleteWishSuccessful();
            }
        });
    }

    @Override
    public void getWishesByUser(String userId, final WishDAOListener mListener) {
        databaseReference.child(COLUMN_USER_WISH_LIST).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long wishListSize = dataSnapshot.getChildrenCount();
                final List<Wish> wishList = new ArrayList<>();
                if(wishListSize > 0){
                    for (DataSnapshot wishDataSnapshot :dataSnapshot.getChildren()){
                        String wishId = wishDataSnapshot.getKey();
                        databaseReference.child(COLUMN_WISH_LIST).child(wishId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Wish wish = dataSnapshot.getValue(Wish.class);
                                wishList.add(wish);
                                if(wishList.size() >= wishListSize){
                                    mListener.loadWishesSuccessful(wishList);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }else {
                    mListener.loadWishesSuccessful(wishList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void checkWishesListWithNewItem(final Context context, final Item item) {
        int itemCategory = item.getItemCategory();
        Query query = databaseReference.child(COLUMN_WISH_LIST).orderByChild(DataBaseConstants.CHILD_ITEM_TYPE).equalTo(itemCategory);
        switch (itemCategory){
            case Constants.PREFERENCE_ITEM_CATEGORY_GAME:
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ArrayList<Wish> wishArrayList = new ArrayList<>();
                            for (DataSnapshot wishDS : dataSnapshot.getChildren()) {
                                Wish wish = wishDS.getValue(Wish.class);
                                if(matchWishAndGameItem(wish, item)){
                                    wishArrayList.add(wish);
                                }
                            }
                            if(wishArrayList.size()>0) {
                                sendNotificationsToUsersWishList(context, wishArrayList, item);
                                addItemToWish(wishArrayList, item);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;
            case Constants.PREFERENCE_ITEM_CATEGORY_CONSOLE:
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ArrayList<Wish> wishArrayList = new ArrayList<>();
                            for (DataSnapshot wishDS : dataSnapshot.getChildren()) {
                                Wish wish = wishDS.getValue(Wish.class);
                                if(matchWishAndConsoleItem(wish, item)){
                                    wishArrayList.add(wish);
                                }
                            }
                            if(wishArrayList.size()>0){
                                sendNotificationsToUsersWishList(context, wishArrayList, item);
                                addItemToWish(wishArrayList, item);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
        }
    }

    @Override
    public void loadItemsByWish(Wish wish, final WishDAOItemsListener wishDAOItemsListener) {
        final List<Item> itemList = new ArrayList<>();
        databaseReference.child(COLUMN_WISH_ITEMS).child(wish.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long itemListSize = dataSnapshot.getChildrenCount();
                for (DataSnapshot itemKey :dataSnapshot.getChildren()){
                    ItemDAO itemDAO = new ItemDAO();
                    itemDAO.loadItemById(itemKey.getKey(), new ItemDAO.SingleItemDAOListener() {
                        @Override
                        public void loadItemByIdSuccess(Item item) {
                            itemList.add(item);
                            if(itemListSize == itemList.size()){
                                wishDAOItemsListener.loadItemsByWishSuccessful(itemList);
                            }
                        }

                        @Override
                        public void loadItemByIdError(String message) {

                        }
                    });
                }
                if(itemListSize == 0)
                    wishDAOItemsListener.loadItemsByWishSuccessful(itemList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Agrega el item a los deseos que correspondan.
     * @param wishArrayList
     * @param item
     */
    private void addItemToWish(ArrayList<Wish> wishArrayList, Item item) {
        for (Wish wish : wishArrayList) {
            databaseReference.child(COLUMN_WISH_ITEMS).child(wish.getId()).child(item.getId()).setValue(true);
        }
    }

    private void sendNotificationsToUsersWishList(Context context, ArrayList<Wish> wishArrayList, Item item) {
        FirebaseNotifications firebaseNotifications = new FirebaseNotifications(context);
        List<String> userIdsList = getUserIdsList(wishArrayList);
        try {
            firebaseNotifications.sendNotificationWishListMatch(userIdsList, item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<String> getUserIdsList(ArrayList<Wish> wishArrayList) {
        List<String> userIdsList = new ArrayList<>();
        for (Wish wish: wishArrayList) {
            userIdsList.add(wish.getUserId());
        }
        return userIdsList;
    }

    private boolean matchWishAndGameItem(Wish wish, Item item) {
        return !wish.getUserId().equals(item.getUserId())
                && wish.getGameId().equals(item.getGameId())
                && isInPrice(wish, item)
                && (wish.isBarter() == item.isBarter() || item.isBarter());
    }

    private boolean isInPrice(Wish wish, Item item) {
        return item.getPrice() <= wish.getMaxPrice() && item.getPrice() >= wish.getMinPrice();
    }

    private boolean matchWishAndConsoleItem(Wish wish, Item item) {
        return !wish.getUserId().equals(item.getUserId())
                && wish.getConsoleId() == item.getConsoleId()
                && isInPrice(wish, item)
                && (wish.isBarter() == item.isBarter() || item.isBarter());
    }


    public interface WishDAOListener{
        void loadWishesSuccessful(List<Wish> wishList);
    }

    public interface WishDAOItemsListener{
        void loadItemsByWishSuccessful(List<Item> itemList);
    }

    public interface WishDAODeleteListener{
        void deleteWishSuccessful();
    }
}
