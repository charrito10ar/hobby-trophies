package com.marbit.hobbytrophies.presenters;

import android.content.Context;

import com.marbit.hobbytrophies.interfaces.FavouritesFragmentView;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.Utilities;

import java.util.List;


public class FavouritesPresenter {
    private final Context context;
    private FavouritesFragmentView favouritesFragmentView;

    public FavouritesPresenter(Context context, FavouritesFragmentView favouritesFragmentView) {
        this.favouritesFragmentView = favouritesFragmentView;
        this.context = context;
    }

    public void loadFavourites() {
        List<Item> list = Utilities.loadFavorites(context);
        favouritesFragmentView.loadFavouritesSuccessful(list);
    }
}
