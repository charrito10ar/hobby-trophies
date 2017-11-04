package com.marbit.hobbytrophies.interfaces.wishes;

import com.marbit.hobbytrophies.wishes.model.Wish;

import java.util.List;

public interface WishListFragmentView {
    void loadWishListSuccessful(List<Wish> wishList);
    void showEmptyListMessage();
    void hideEmptyListMessage();
}
