package com.marbit.hobbytrophies.presenters.market;

import android.content.Context;

import com.marbit.hobbytrophies.interactors.market.EditItemActivityInteractor;
import com.marbit.hobbytrophies.interfaces.market.EditItemActivityPresenterInterface;
import com.marbit.hobbytrophies.interfaces.market.EditItemActivityView;
import com.marbit.hobbytrophies.model.market.Item;


public class EditItemActivityPresenter implements EditItemActivityPresenterInterface {
    private Context context;
    private EditItemActivityView editItemActivityView;
    private EditItemActivityInteractor interactor;

    public EditItemActivityPresenter(Context applicationContext, EditItemActivityView editItemActivity) {
        this.context = applicationContext;
        this.editItemActivityView = editItemActivity;
        this.interactor = new EditItemActivityInteractor(applicationContext, this);
    }

    public void setItem(Item item) {
        editItemActivityView.setBarter(item.isBarter());
        editItemActivityView.setDigital(item.isDigital());
        editItemActivityView.setDescription(item.getDescription());
        editItemActivityView.setPrice(item.getPrice());
        editItemActivityView.setTitle(item.getTitle());
        editItemActivityView.setCategory(item.getItemCategory() + 1);
        editItemActivityView.setPhotos(item.getId());
    }

    public void updateItem(Item item) {
        interactor.updateItem(item);
    }

    @Override
    public void editItemSuccess() {
        editItemActivityView.editItemSuccess();
    }
}
