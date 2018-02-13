package com.marbit.hobbytrophies.wishes.interactors;

import android.content.Context;

import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.marbit.hobbytrophies.utilities.UserStats;
import com.marbit.hobbytrophies.wishes.dao.WishDAO;
import com.marbit.hobbytrophies.wishes.interfaces.DetailWishActivityPresenterInterface;
import com.marbit.hobbytrophies.wishes.model.Wish;

import java.util.List;

public class DetailWishActivityInteractor {
    private Context context;
    private DetailWishActivityPresenterInterface presenterInterface;

    public DetailWishActivityInteractor(Context applicationContext, DetailWishActivityPresenterInterface presenterInterface) {
        this.context = applicationContext;
        this.presenterInterface = presenterInterface;
    }

    public void loadItemsByWish(Wish wish) {
        WishDAO wishDAO = new WishDAO(context);
        wishDAO.loadItemsByWish(wish, new WishDAO.WishDAOItemsListener() {
            @Override
            public void loadItemsByWishSuccessful(List<Item> itemList) {
                presenterInterface.loadItemsByWishSuccessful(itemList);
            }
        });
    }

    public void deleteWish(Wish wish) {
        WishDAO wishDAO = new WishDAO(context);
        wishDAO.deleteWish(Preferences.getUserId(context), wish, new WishDAO.WishDAODeleteListener() {
            @Override
            public void deleteWishSuccessful() {
                presenterInterface.deleteWishSuccessful();
            }
        });
    }
}
