package com.marbit.hobbytrophies.interactors.market;

import android.content.Context;

import com.marbit.hobbytrophies.dao.ItemDAO;
import com.marbit.hobbytrophies.dao.UserDAO;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.model.meeting.Location;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.marbit.hobbytrophies.utilities.Utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ItemDetailActivityInteractor implements ItemDAO.ItemDAOEditListener{
    private Context context;
    private ItemDetailActivityPresenterInterface presenterInterface;

    public ItemDetailActivityInteractor(Context context, ItemDetailActivityPresenterInterface presenterInterface) {
        this.context = context;
        this.presenterInterface = presenterInterface;
    }

    public void delete(Item item) {
        ItemDAO itemDAO = new ItemDAO(context, this);
        itemDAO.delete(item);
    }

    @Override
    public void deleteItemSuccess() {
        this.presenterInterface.deleteItemSuccess();
    }

    public void addFavourite(Item item) {
        Utilities.addFavourite(context, item);
        this.presenterInterface.markFavourite();
    }

    public void deleteFavourite(Item item) {
        Utilities.removeFavorite(context, item);
        this.presenterInterface.deleteFavourite();
    }

    public void shareItem(Item item) {
        try {
            String longItemLink = "https://zj77k.app.goo.gl/?link=" + URLEncoder.encode("https://hobbytrophies.com/foros?type=ITEM&itemId=" + item.getId(), "UTF-8") + "&apn=com.marbit.hobbytrophies";
            presenterInterface.shareItemLink(item, longItemLink);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void loadRemoteItem(String itemId) {
        ItemDAO itemDAO = new ItemDAO(context);
        itemDAO.loadItemById(context, itemId, new ItemDAO.SingleItemDAOListener() {
            @Override
            public void loadItemByIdSuccess(final Item item) {
                UserDAO userDAO = new UserDAO(context);
                userDAO.getUserLocation(Preferences.getUserId(context), new UserDAO.ListenerUserLocationDAO() {
                    @Override
                    public void loadUserLocationSuccessful(Location location) {
                        item.setLocation(location);
                        presenterInterface.loadRemoteItemSuccess(item);
                    }

                    @Override
                    public void loadUserLocationError(String errorMessage) {
                        presenterInterface.loadRemoteItemError(errorMessage);
                    }
                });
            }
            @Override
            public void loadItemByIdError(String message) {
                presenterInterface.loadRemoteItemError(message);
            }
        });
    }
}