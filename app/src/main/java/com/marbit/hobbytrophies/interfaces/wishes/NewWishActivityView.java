package com.marbit.hobbytrophies.interfaces.wishes;

public interface NewWishActivityView {
    void setGameSelected(String gameSelected);
    void setConsoleSelected(String consoleSelected);
    void gameSelected();
    void consoleSelected();
    void showCheckBoxDigital();
    void hideCheckBoxDigital();

    void addWishSuccessful();
}
