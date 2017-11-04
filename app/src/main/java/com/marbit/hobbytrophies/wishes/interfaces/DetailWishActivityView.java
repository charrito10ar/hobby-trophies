package com.marbit.hobbytrophies.wishes.interfaces;

import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.wishes.model.Wish;

import java.util.List;

public interface DetailWishActivityView {

    void initWish(Wish wish);

    void loadItemsByWishSuccessful(List<Item> itemList);

    void deleteWishSuccessful();

    void showEmptyLayout();

    void hideEmptyLayout();
}
