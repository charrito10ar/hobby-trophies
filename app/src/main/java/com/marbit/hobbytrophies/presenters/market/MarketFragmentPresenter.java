package com.marbit.hobbytrophies.presenters.market;

import android.content.Context;

import com.marbit.hobbytrophies.interactors.market.MarketFragmentInteractor;
import com.marbit.hobbytrophies.interfaces.market.MarketFragmentPresenterInterface;
import com.marbit.hobbytrophies.interfaces.market.MarketFragmentView;
import com.marbit.hobbytrophies.model.market.Item;

import java.util.List;

public class MarketFragmentPresenter implements MarketFragmentPresenterInterface {
    private Context context;
    private MarketFragmentView marketFragmentView;
    private MarketFragmentInteractor interactor;

    public MarketFragmentPresenter(Context context, MarketFragmentView marketFragment) {
        this.context = context;
        this.marketFragmentView = marketFragment;
        this.interactor = new MarketFragmentInteractor(context, this);
    }

    public void loadItems() {
        marketFragmentView.showLoading();
        interactor.loadItems();
    }

    @Override
    public void loadItemsSuccess(List<Item> items) {
        marketFragmentView.hideLoading();
        marketFragmentView.loadItemSuccess(items);
    }
}
