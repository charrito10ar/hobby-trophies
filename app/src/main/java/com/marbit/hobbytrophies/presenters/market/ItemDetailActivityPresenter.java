package com.marbit.hobbytrophies.presenters.market;

import android.content.Context;

import com.marbit.hobbytrophies.interactors.market.ItemDetailActivityInteractor;
import com.marbit.hobbytrophies.interactors.market.ItemDetailActivityPresenterInterface;
import com.marbit.hobbytrophies.interfaces.market.ItemDetailActivityView;
import com.marbit.hobbytrophies.model.market.Item;

public class ItemDetailActivityPresenter implements ItemDetailActivityPresenterInterface {
    private Context context;
    private ItemDetailActivityView itemDetailActivityView;
    private ItemDetailActivityInteractor interactor;

    public ItemDetailActivityPresenter(Context applicationContext, ItemDetailActivityView itemDetailActivity) {
        this.context = applicationContext;
        this.itemDetailActivityView = itemDetailActivity;
        this.interactor = new ItemDetailActivityInteractor(context, this);
    }

    public void markAsSold(Item item) {
        interactor.markAsSold(item);
    }

    public void unmarkAsSold(Item item) {
        interactor.unmarkAsSold(item);
    }

    public void delete(Item item) {
        this.interactor.delete(item);
    }

    @Override
    public void deleteItemSuccess() {
        itemDetailActivityView.deleteItemSuccess();
    }

    @Override
    public void markAsSoldSuccess() {
        itemDetailActivityView.markAsSold();
    }

    @Override
    public void unmarkAsSoldSuccess() {
        itemDetailActivityView.unmarkAsSold();
    }
}
