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
        itemDetailActivityView.openItemSoldActivity();
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
    public void markFavourite() {
        itemDetailActivityView.markFavourite();
    }

    @Override
    public void deleteFavourite() {
        itemDetailActivityView.unmarkFavourite();
    }

    @Override
    public void shareItemLink(Item item, String longDynamicLink) {
        itemDetailActivityView.shareItemLink(item, longDynamicLink);
    }

    @Override
    public void loadRemoteItemSuccess(Item item) {
        itemDetailActivityView.loadRemoteItemSuccess(item);
    }

    @Override
    public void loadRemoteItemError(String message) {
        itemDetailActivityView.loadRemoteItemError(message);
    }

    public void addFavourite(Item item) {
        interactor.addFavourite(item);
    }

    public void deleteFavourite(Item item) {
        interactor.deleteFavourite(item);
    }

    public void shareItem(Item item) {
        interactor.shareItem(item);
    }

    public void loadRemoteItem(String itemId) {
        interactor.loadRemoteItem(itemId);
    }
}
