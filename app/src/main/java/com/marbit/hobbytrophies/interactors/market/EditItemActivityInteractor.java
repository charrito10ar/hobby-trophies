package com.marbit.hobbytrophies.interactors.market;

import android.content.Context;

import com.marbit.hobbytrophies.dao.ItemDAO;
import com.marbit.hobbytrophies.interfaces.market.EditItemActivityPresenterInterface;
import com.marbit.hobbytrophies.model.market.Item;

public class EditItemActivityInteractor {
    private Context context;
    private EditItemActivityPresenterInterface presenterInterface;

    public EditItemActivityInteractor(Context applicationContext, EditItemActivityPresenterInterface presenterInterface) {
        this.context = applicationContext;
        this.presenterInterface = presenterInterface;
    }

    public void updateItem(Item item) {
        ItemDAO itemDAO = new ItemDAO(context);
        itemDAO.updateItem(item, new ItemDAO.EditItemDAOListener() {
            @Override
            public void editItemSuccess() {
                presenterInterface.editItemSuccess();
            }

            @Override
            public void editItemError(String message) {

            }
        });
    }
}
