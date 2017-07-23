package com.marbit.hobbytrophies.chat.interfaces;

import com.marbit.hobbytrophies.chat.model.Chat;

import java.util.List;

public interface MessagesFragmentPresenterInterface {
    void loadChatHeadersSuccessful(List<Chat> chatList);

    void addMessage(Chat chat);

    void insertNewHeaderChat(Chat chat);
}
