package com.marbit.hobbytrophies.presenters.market;

import android.content.Context;

import com.marbit.hobbytrophies.interactors.market.ProfileSalesFragmentInteractor;
import com.marbit.hobbytrophies.interfaces.market.ProfileSalesFragmentPresenterInterface;
import com.marbit.hobbytrophies.interfaces.market.ProfileSalesFragmentView;
import com.marbit.hobbytrophies.model.market.Item;

import java.util.List;


public class ProfileSalesFragmentPresenter implements ProfileSalesFragmentPresenterInterface {
    private ProfileSalesFragmentView profileSalesFragmentView;
    private ProfileSalesFragmentInteractor interactor;
    private Context context;

    public ProfileSalesFragmentPresenter(Context context, ProfileSalesFragmentView profileSalesFragmentView) {
        this.interactor = new ProfileSalesFragmentInteractor(context, this);
        this.context = context;
        this.profileSalesFragmentView = profileSalesFragmentView;
    }

    public void loadItems() {
        profileSalesFragmentView.showLoading();
        interactor.loadItems();
    }

    @Override
    public void loadItemsSuccess(List<Item> items) {
        profileSalesFragmentView.hideLoading();
        profileSalesFragmentView.loadItemsSuccess(items);
    }
}
