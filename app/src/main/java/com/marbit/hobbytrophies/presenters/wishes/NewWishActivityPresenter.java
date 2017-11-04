package com.marbit.hobbytrophies.presenters.wishes;

import android.content.Context;

import com.marbit.hobbytrophies.interactors.market.NewItemActivityInteractor;
import com.marbit.hobbytrophies.interfaces.wishes.NewWishActivityView;
import com.marbit.hobbytrophies.wishes.model.Wish;

public class NewWishActivityPresenter {
    private Context context;
    private NewWishActivityView newWishActivityView;
    private NewItemActivityInteractor interactor;

    public NewWishActivityPresenter(Context context, NewWishActivityView newWishActivityView) {
        this.context = context;
        this.newWishActivityView = newWishActivityView;
        this.interactor = new NewItemActivityInteractor(context);
    }

    public void addWish(Wish item) {
        interactor.addWish(item);
        newWishActivityView.addWishSuccessful();
    }
}
