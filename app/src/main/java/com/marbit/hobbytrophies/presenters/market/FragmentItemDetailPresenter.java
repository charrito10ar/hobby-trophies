package com.marbit.hobbytrophies.presenters.market;

import android.content.Context;
import android.content.Intent;

import com.marbit.hobbytrophies.interactors.market.FragmentItemDetailInteractor;
import com.marbit.hobbytrophies.interfaces.market.FragmentItemDetailPresenterInterface;
import com.marbit.hobbytrophies.interfaces.market.FragmentItemDetailView;
import com.marbit.hobbytrophies.model.market.Item;

import java.io.File;
import java.util.ArrayList;


public class FragmentItemDetailPresenter implements FragmentItemDetailPresenterInterface{
    private Context context;
    private FragmentItemDetailView fragmentItemDetailView;
    private FragmentItemDetailInteractor interactor;
    private ArrayList<File> listFilesItemToSend;

    public FragmentItemDetailPresenter(Context context, FragmentItemDetailView fragmentItemDetail) {
        this.context = context;
        this.fragmentItemDetailView = fragmentItemDetail;
        this.interactor = new FragmentItemDetailInteractor(context, this);
    }

    public void publishNewItem(Item item) {
        interactor.publishNewItem(item);
    }


    public void openFileBrowser() {
        this.interactor.performFileSearch();
    }

//    public void startActivityForResult(Intent intent) {
//        this.fragmentItemDetailView.startActivityForResultAccessFiles(intent);
//    }

    public void processFilesSelected(Intent data) {
        this.listFilesItemToSend = interactor.getFilesToSend(data);
        this.fragmentItemDetailView.setPhoto(listFilesItemToSend);


    }

    @Override
    public void startActivityForResult(Intent intent) {
        this.fragmentItemDetailView.startActivityForResultAccessFiles(intent);
    }

    @Override
    public void publishItemSuccess(Item item) {
        this.fragmentItemDetailView.publishItemSuccess(item);
    }
}
