package com.marbit.hobbytrophies.presenters.market;


import android.content.Context;

import com.marbit.hobbytrophies.interactors.market.NewItemActivityInteractor;
import com.marbit.hobbytrophies.interfaces.market.NewItemActivityView;

public class NewItemActivityPresenter {
    private NewItemActivityInteractor interactor;


    public NewItemActivityPresenter(Context applicationContext, NewItemActivityView newItemActivity) {
        this.interactor = new NewItemActivityInteractor(applicationContext);
    }
}
