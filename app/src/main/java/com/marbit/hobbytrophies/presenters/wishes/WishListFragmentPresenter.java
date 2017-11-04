package com.marbit.hobbytrophies.presenters.wishes;


import android.content.Context;

import com.marbit.hobbytrophies.interactors.wishes.WishListFragmentInteractor;
import com.marbit.hobbytrophies.interfaces.wishes.WishListFragmentPresenterInterface;
import com.marbit.hobbytrophies.interfaces.wishes.WishListFragmentView;
import com.marbit.hobbytrophies.wishes.model.Wish;

import java.util.List;

public class WishListFragmentPresenter implements WishListFragmentPresenterInterface {
    private WishListFragmentView wishListFragmentView;
    private WishListFragmentInteractor interactor;

    public WishListFragmentPresenter(Context context, WishListFragmentView wishListFragmentView) {
        this.wishListFragmentView = wishListFragmentView;
        this.interactor = new WishListFragmentInteractor(context, this);
    }

    public void loadWishList() {
        interactor.loadWishList();
    }

    @Override
    public void loadWishListSuccessful(List<Wish> wishList) {
        if(wishList.size() > 0){
            wishListFragmentView.hideEmptyListMessage();
        }else {
            wishListFragmentView.showEmptyListMessage();
        }
        wishListFragmentView.loadWishListSuccessful(wishList);
    }
}
