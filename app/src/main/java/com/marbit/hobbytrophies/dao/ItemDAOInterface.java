package com.marbit.hobbytrophies.dao;

import com.marbit.hobbytrophies.market.model.UserMarket;
import com.marbit.hobbytrophies.model.market.Filter;
import com.marbit.hobbytrophies.model.market.Item;

import org.jetbrains.annotations.NotNull;

public interface ItemDAOInterface {
    void loadItems();
    void loadItemById(String itemId, ItemDAO.SingleItemDAOListener singleItemDAOListener);
    void loadUserItems(String UserId);
    void loadItemsByFilter(Filter filter);
    void markSold(Item item);
    void markSold(@NotNull Item item, @NotNull UserMarket userMarket);
    void updateItem(Item item, ItemDAO.EditItemDAOListener mListener);
    void loadPossiblesBuyers(String itemId, ItemDAO.ItemSoldDAOListener mListener);

}

