package com.marbit.hobbytrophies.interfaces.market;

import com.marbit.hobbytrophies.model.market.Item;

import java.util.List;

public interface MarketFragmentView {
    void showLoading();

    void hideLoading();

    void loadItemSuccess(List<Item> items);
}
