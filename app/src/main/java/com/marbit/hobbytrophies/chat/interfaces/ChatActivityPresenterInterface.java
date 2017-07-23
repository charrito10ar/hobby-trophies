package com.marbit.hobbytrophies.chat.interfaces;

import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.model.User;

public interface ChatActivityPresenterInterface {
    void loadChatSuccessful(Chat chat);

    void sendMessageSuccessful(String messageId);

    void addMessageToChat(MessageChat messageChat);

    void loadUserBasicProfileSuccessful(User user);
}
