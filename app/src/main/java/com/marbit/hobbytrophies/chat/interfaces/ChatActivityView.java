package com.marbit.hobbytrophies.chat.interfaces;

import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.model.User;

import java.util.List;

public interface ChatActivityView {
    void loadChatSuccessful(Chat chat, List<Object> genericListMessages);

    void loadChat();

    void sendMessageSuccessful(String messageId);

    void addMessageToChat(MessageChat messageChat);

    void loadUserBasicProfileSuccessful(User user);

    void setTitleItem(String titleItem);

    void setAvatarSeller(String urlAvatar);

    void loadChat(String chatId);

    void showMessage(String message);

    void setDisableWriteMessage();
}
