package com.marbit.hobbytrophies.wishes.interfaces;


import com.marbit.hobbytrophies.model.market.Item;

import java.util.List;

public interface DetailWishActivityPresenterInterface {
    void loadItemsByWishSuccessful(List<Item> itemList);

    void deleteWishSuccessful();

}
