package com.marbit.hobbytrophies.chat.dao;

public interface ChatDAOInterface {
    void addListenerAddMessageChat(String chatId);

    void addListenerAddHeaderChat(String userName);

    void loadChat(String itemId, String titleItem, String buyer, String seller, String buyerName, String sellerName);

    void sendMessage(String itemId, String titleItem, String buyer, String seller, String author, String message, String buyerName, String sellerName);

    void removeListenerAddMessageChat();

    void removeListenerUpdateHeadersChat();

    String createChat(String itemId, String titleItem, String buyer, String seller, String buyerName, String sellerName);

    void loadChat(String chatId);
}
