package com.marbit.hobbytrophies.interfaces.market;

import com.marbit.hobbytrophies.model.market.Item;

public interface ItemDetailActivityView {
    void markAsSold();
    void unmarkAsSold();
    void markFavourite();
    void unmarkFavourite();
    void setStatus();
    void setFavourite();
    void deleteItemSuccess();
    void hideButtonChat();
    void showButtonChat();
    void shareItemLink(Item item, String longDynamicLink);

    void loadRemoteItemError(String message);

    void loadRemoteItemSuccess(Item item);
}
