package com.marbit.hobbytrophies.interactors.wishes;


import android.content.Context;

import com.marbit.hobbytrophies.wishes.dao.WishDAO;
import com.marbit.hobbytrophies.interfaces.wishes.WishListFragmentPresenterInterface;
import com.marbit.hobbytrophies.wishes.model.Wish;
import com.marbit.hobbytrophies.presenters.wishes.WishListFragmentPresenter;
import com.marbit.hobbytrophies.utilities.Preferences;

import java.util.List;

public class WishListFragmentInteractor {
    private final Context context;
    private WishListFragmentPresenterInterface presenterInterface;

    public WishListFragmentInteractor(Context context, WishListFragmentPresenter wishListFragmentPresenter) {
        this.context = context;
        this.presenterInterface = wishListFragmentPresenter;
    }

    public void loadWishList() {
        WishDAO wishDAO = new WishDAO(context);
        wishDAO.getWishesByUser(Preferences.getUserId(context), new WishDAO.WishDAOListener() {
            @Override
            public void loadWishesSuccessful(List<Wish> wishList) {
                presenterInterface.loadWishListSuccessful(wishList);
            }
        });
    }
}
