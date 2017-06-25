package com.marbit.hobbytrophies.interfaces.market;

import com.marbit.hobbytrophies.model.market.Item;

import java.util.List;

public interface ProfileSalesFragmentPresenterInterface {
    void loadItemsSuccess(List<Item> items);
}
