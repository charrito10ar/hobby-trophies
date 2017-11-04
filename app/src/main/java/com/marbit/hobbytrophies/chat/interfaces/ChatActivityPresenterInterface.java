package com.marbit.hobbytrophies.chat.interfaces;

import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.model.User;

import java.util.List;

public interface ChatActivityPresenterInterface {
    void loadChatSuccessful(Chat chat, List<Object> genericListMessages);

    void sendMessageSuccessful(String messageId);

    void addMessageToChat(MessageChat messageChat);

    void loadUserBasicProfileSuccessful(User user);
}
