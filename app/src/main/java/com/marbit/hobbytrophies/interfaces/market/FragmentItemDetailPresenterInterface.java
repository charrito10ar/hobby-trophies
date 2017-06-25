package com.marbit.hobbytrophies.interfaces.market;

import android.content.Intent;

import com.marbit.hobbytrophies.model.market.Item;

/**
 * Created by marcelo on 3/06/17.
 */

public interface FragmentItemDetailPresenterInterface {
    void startActivityForResult(Intent intent);

    void publishItemSuccess(Item item);
}
