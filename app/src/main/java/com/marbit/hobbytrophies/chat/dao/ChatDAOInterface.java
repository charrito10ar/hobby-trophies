package com.marbit.hobbytrophies.chat.dao;

public interface ChatDAOInterface {
    void addListenerAddMessageChat(String chatId);

    void addListenerAddHeaderChat(String userName);

    void loadChat(String itemId, String titleItem, String buyer, String seller);

    void sendMessage(String itemId, String titleItem, String buyer, String seller, String author, String message);

    void removeListenerAddMessageChat();

    void removeListenerUpdateHeadersChat();

    String createChat(String itemId, String titleItem, String buyer, String seller);

    void loadChat(String chatId);
}
