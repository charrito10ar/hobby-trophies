package com.marbit.hobbytrophies.wishes.dao;


import android.content.Context;

import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.wishes.model.Wish;

public interface WishDAOInterface {
    void addWish(String userId, Wish wish);
    void deleteWish(String userId, Wish wish, WishDAO.WishDAODeleteListener mListener);
    void getWishesByUser(String userId, WishDAO.WishDAOListener mListener);
    void checkWishesListWithNewItem(Context context, Item item);

    void loadItemsByWish(Wish wish, WishDAO.WishDAOItemsListener wishDAOItemsListener);
}
