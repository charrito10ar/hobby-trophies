package com.marbit.hobbytrophies.wishes.presenters;

import android.content.Context;

import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.wishes.interactors.DetailWishActivityInteractor;
import com.marbit.hobbytrophies.wishes.interfaces.DetailWishActivityPresenterInterface;
import com.marbit.hobbytrophies.wishes.interfaces.DetailWishActivityView;
import com.marbit.hobbytrophies.wishes.model.Wish;

import java.util.List;

public class DetailWishActivityPresenter implements DetailWishActivityPresenterInterface {
    private DetailWishActivityView detailWishActivityView;
    private DetailWishActivityInteractor interactor;

    public DetailWishActivityPresenter(Context applicationContext, DetailWishActivityView detailWishActivityView) {
        this.detailWishActivityView = detailWishActivityView;
        this.interactor = new DetailWishActivityInteractor(applicationContext, this);
    }

    public void loadItemsByWish(Wish wish) {
        interactor.loadItemsByWish(wish);
    }

    @Override
    public void loadItemsByWishSuccessful(List<Item> itemList) {
        if(itemList.size() == 0){
            detailWishActivityView.showEmptyLayout();
        }else {
            detailWishActivityView.hideEmptyLayout();
            detailWishActivityView.loadItemsByWishSuccessful(itemList);
        }
    }

    @Override
    public void deleteWishSuccessful() {
        detailWishActivityView.deleteWishSuccessful();
    }

    public void deleteWish(Wish wish) {
        interactor.deleteWish(wish);
    }
}
