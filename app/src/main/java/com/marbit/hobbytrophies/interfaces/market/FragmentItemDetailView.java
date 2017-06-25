package com.marbit.hobbytrophies.interfaces.market;

import android.content.Intent;

import com.marbit.hobbytrophies.model.market.Item;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by marcelo on 1/06/17.
 */

public interface FragmentItemDetailView {
    void startActivityForResultAccessFiles(Intent intent);

    void setPhoto(ArrayList<File> listFilesItemToSend);

    void showLoading();

    void hideLoading();

    void publishItemSuccess(Item item);
}
