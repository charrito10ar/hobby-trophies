package com.marbit.hobbytrophies.presenters;

import android.content.Context;

import com.marbit.hobbytrophies.dao.ItemDAO;
import com.marbit.hobbytrophies.interfaces.FavouritesFragmentView;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class FavouritesPresenter {
    private final Context context;
    private FavouritesFragmentView favouritesFragmentView;

    public FavouritesPresenter(Context context, FavouritesFragmentView favouritesFragmentView) {
        this.favouritesFragmentView = favouritesFragmentView;
        this.context = context;
    }

    public void loadFavourites() {
        favouritesFragmentView.showProgressBar();
        final List<Item> remoteList = new ArrayList<>();
        List<Item> list = Utilities.loadFavorites(context);
        final int sizeList = list.size();
        ItemDAO itemDAO = new ItemDAO(context);
        if(sizeList > 0){
            for (Item item : list) {
                itemDAO.loadItemById(context, item.getId(), new ItemDAO.SingleItemDAOListener() {
                    @Override
                    public void loadItemByIdSuccess(Item item) {
                        remoteList.add(item);
                        if(remoteList.size() == sizeList){
                            favouritesFragmentView.hideProgressBar();
                            favouritesFragmentView.hideEmptyView();
                            favouritesFragmentView.loadFavouritesSuccessful(remoteList);
                        }
                    }

                    @Override
                    public void loadItemByIdError(String message) {
                        favouritesFragmentView.hideProgressBar();
                        favouritesFragmentView.loadFavouritesError(message);
                    }
                });
            }
        }else {
            favouritesFragmentView.hideProgressBar();
            favouritesFragmentView.showEmptyView();
        }
    }
}