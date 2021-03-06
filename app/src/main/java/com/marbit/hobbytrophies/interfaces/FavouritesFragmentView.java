package com.marbit.hobbytrophies.interfaces;

import com.marbit.hobbytrophies.model.market.Item;

import java.util.List;

public interface FavouritesFragmentView {
    void loadFavouritesSuccessful(List<Item> list);

    void loadFavouritesError(String message);

    void showProgressBar();

    void hideProgressBar();

    void showEmptyView();

    void hideEmptyView();
}
