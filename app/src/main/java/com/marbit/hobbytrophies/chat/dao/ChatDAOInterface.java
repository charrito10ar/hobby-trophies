package com.marbit.hobbytrophies.chat.dao;

public interface ChatDAOInterface {
    void addListenerAddMessageChat(String chatId);

    void addListenerAddHeaderChat(String userName);

    void loadChat(String itemId, String buyer, String seller);

    String createChat(String id, String itemId, String buyer);

    void sendMessage(String itemId, String buyer, String seller, String author, String message);

    void removeListenerAddMessageChat();

    void removeListenerUpdateHeadersChat();
}
