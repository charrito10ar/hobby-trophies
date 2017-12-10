package com.marbit.hobbytrophies.interactors.market;

import com.marbit.hobbytrophies.model.market.Item;

public interface ItemDetailActivityPresenterInterface {
    void deleteItemSuccess();

    void markAsSoldSuccess();

    void markFavourite();

    void deleteFavourite();

    void shareItemLink(Item item, String longDynamicLink);

    void loadRemoteItemSuccess(Item item);

    void loadRemoteItemError(String message);
}
