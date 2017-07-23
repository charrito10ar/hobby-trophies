package com.marbit.hobbytrophies.chat.interactors;

import android.content.Context;

import com.marbit.hobbytrophies.chat.dao.ChatDAO;
import com.marbit.hobbytrophies.chat.interfaces.ChatActivityPresenterInterface;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.dao.UserDAO;
import com.marbit.hobbytrophies.model.User;


public class ChatActivityInteractor implements ChatDAO.ChatDAOListener {

    private final ChatDAO chatDAO;
    private ChatActivityPresenterInterface presenter;

    public ChatActivityInteractor(Context applicationContext, ChatActivityPresenterInterface presenter) {
        this.presenter = presenter;
        chatDAO = new ChatDAO(this);
    }

    public void loadChat(String itemId, String buyer, String seller) {
        chatDAO.loadChat(itemId, buyer, seller);
    }

    public void sendMessage(String itemId, String buyer, String seller, String author, String message) {
        chatDAO.sendMessage(itemId, buyer, seller, author, message);
    }

    @Override
    public void loadChatSuccessful(Chat chat) {
        presenter.loadChatSuccessful(chat);
    }

    @Override
    public void sendMessageSuccessful(String messageId) {
        presenter.sendMessageSuccessful(messageId);
    }

    @Override
    public void addMessage(MessageChat messageChat) {
        presenter.addMessageToChat(messageChat);
    }

    public void removeListenerAddMessageChat() {
        chatDAO.removeListenerAddMessageChat();
    }

    public void addListenerAddMessageChat(String chatId) {
        chatDAO.addListenerAddMessageChat(chatId);
    }

    public void loadAvatarSeller(String seller) {
        UserDAO userDAO = new UserDAO();
        userDAO.getUserBasicProfile(seller, new UserDAO.ListenerUserDAO() {
            @Override
            public void loadUserBasicProfileSuccessful(User user) {
                presenter.loadUserBasicProfileSuccessful(user);
            }
        });

    }
}
