package com.marbit.hobbytrophies.chat.interfaces;

import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.model.User;

public interface ChatActivityView {
    void showRightMessage(String message);

    void showLeftMessage(String message);

    void loadChatSuccessful(Chat chat);

    void loadChat();

    void sendMessageSuccessful(String messageId);

    void addMessageToChat(MessageChat messageChat);

    void loadUserBasicProfileSuccessful(User user);
}
