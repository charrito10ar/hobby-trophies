package com.marbit.hobbytrophies.interfaces.wishes;

import com.marbit.hobbytrophies.wishes.model.Wish;

import java.util.List;

public interface WishListFragmentPresenterInterface {
    void loadWishListSuccessful(List<Wish> wishList);
}
