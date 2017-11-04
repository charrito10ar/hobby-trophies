package com.marbit.hobbytrophies.interfaces.market;


import com.marbit.hobbytrophies.model.market.Item;

import java.util.List;

public interface ProfileSoldFragmentView {
    void loadItemsSuccess(List<Item> items);
    void showLoading();
    void hideLoading();
}
