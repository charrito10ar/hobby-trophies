package com.marbit.hobbytrophies.interactors.market;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.marbit.hobbytrophies.dao.ItemDAO;
import com.marbit.hobbytrophies.model.market.Item;
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

    public void markAsSold(Item item) {
        item.setStatus(1);
        ItemDAO itemDAO = new ItemDAO(this);
        itemDAO.markSold(item);
    }

    public void unmarkAsSold(Item item) {
        item.setStatus(0);
        ItemDAO itemDAO = new ItemDAO(this);
        itemDAO.unmarkAsSold(item);
    }

    public void delete(Item item) {
        ItemDAO itemDAO = new ItemDAO(this);
        itemDAO.delete(item);
    }

    @Override
    public void deleteItemSuccess() {
        this.presenterInterface.deleteItemSuccess();
    }

    @Override
    public void markAsSoldSuccess() {
        this.presenterInterface.markAsSoldSuccess();
    }

    @Override
    public void unmarkAsSoldSuccess() {
        this.presenterInterface.unmarkAsSoldSuccess();
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
        ItemDAO itemDAO = new ItemDAO();
        itemDAO.loadItemById(itemId, new ItemDAO.SingleItemDAOListener() {
            @Override
            public void loadItemByIdSuccess(Item item) {
                presenterInterface.loadRemoteItemSuccess(item);
            }

            @Override
            public void loadItemByIdError(String message) {
                presenterInterface.loadRemoteItemError(message);
            }
        });

    }
}
