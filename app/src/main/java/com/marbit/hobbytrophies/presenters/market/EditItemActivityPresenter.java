package com.marbit.hobbytrophies.presenters.market;

import android.content.Context;

import com.marbit.hobbytrophies.interactors.market.EditItemActivityInteractor;
import com.marbit.hobbytrophies.interfaces.market.EditItemActivityView;
import com.marbit.hobbytrophies.model.market.Item;


public class EditItemActivityPresenter {
    private Context context;
    private EditItemActivityView editItemActivityView;
    private EditItemActivityInteractor interactor;

    public EditItemActivityPresenter(Context applicationContext, EditItemActivityView editItemActivity) {
        this.context = applicationContext;
        this.editItemActivityView = editItemActivity;
        this.interactor = new EditItemActivityInteractor(applicationContext);
    }

    public void setItem(Item item) {
        editItemActivityView.setBarter(item.isBarter());
        editItemActivityView.setDigital(item.isDigital());
        editItemActivityView.setDescription(item.getDescription());
        editItemActivityView.setPrice(item.getPrice());
        editItemActivityView.setTitle(item.getTitle());
        editItemActivityView.setCategory(item.getItemCategory() + 1);
    }
}
