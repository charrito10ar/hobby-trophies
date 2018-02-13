package com.marbit.hobbytrophies.interactors.market;

import android.content.Context;

import com.marbit.hobbytrophies.wishes.dao.WishDAO;
import com.marbit.hobbytrophies.wishes.model.Wish;
import com.marbit.hobbytrophies.utilities.Preferences;

public class NewItemActivityInteractor {
    private Context context;

    public NewItemActivityInteractor(Context applicationContext) {
        this.context = applicationContext;
    }

    public void addWish(Wish wish) {
        WishDAO wishDAO = new WishDAO(context);
        wishDAO.addWish(Preferences.getUserId(context), wish);
    }
}
