package com.marbit.hobbytrophies.interactors.market;

import android.content.Context;

import com.marbit.hobbytrophies.dao.ItemDAO;
import com.marbit.hobbytrophies.model.market.Item;

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
}
