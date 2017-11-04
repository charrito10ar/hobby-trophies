package com.marbit.hobbytrophies.interfaces.market;

import android.content.Intent;

import com.marbit.hobbytrophies.model.market.Item;

public interface FragmentItemDetailPresenterInterface {
    void startActivityForResult(Intent intent);

    void publishItemSuccess(Item item);
}
