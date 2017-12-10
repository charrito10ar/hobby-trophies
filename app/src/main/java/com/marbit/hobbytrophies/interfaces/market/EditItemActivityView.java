package com.marbit.hobbytrophies.interfaces.market;


public interface EditItemActivityView {
    void setPrice(double price);
    void setTitle(String title);
    void setDescription(String description);
    void setDigital(boolean digital);
    void setBarter(boolean barter);
    void setCategory(int category);
    void setPhotos(String itemId);

    void editItemSuccess();
}
