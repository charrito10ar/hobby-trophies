package com.marbit.hobbytrophies.presenters.market;

import android.content.Context;

import com.marbit.hobbytrophies.interactors.market.ProfileSoldFragmentInteractor;
import com.marbit.hobbytrophies.interfaces.market.ProfileSoldFragmentView;
import com.marbit.hobbytrophies.interfaces.market.ProfileSoldPresenterInterface;
import com.marbit.hobbytrophies.model.market.Item;

import java.util.List;

public class ProfileSoldFragmentPresenter implements ProfileSoldPresenterInterface{
    private final Context context;
    private ProfileSoldFragmentInteractor interactor;
    private ProfileSoldFragmentView profileSoldFragmentView;

    public ProfileSoldFragmentPresenter(Context context, ProfileSoldFragmentView profileSoldFragmentView) {
        this.context = context;
        this.profileSoldFragmentView = profileSoldFragmentView;
        this.interactor = new ProfileSoldFragmentInteractor(context, this);
    }

    public void loadItems() {
        profileSoldFragmentView.showLoading();
        interactor.loadItems();
    }

    @Override
    public void loadItemsSuccess(List<Item> items) {
        profileSoldFragmentView.hideLoading();
        profileSoldFragmentView.loadItemsSuccess(items);
    }
}
