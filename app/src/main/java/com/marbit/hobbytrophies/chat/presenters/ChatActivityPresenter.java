package com.marbit.hobbytrophies.chat.presenters;

import android.content.Context;

import com.marbit.hobbytrophies.chat.interactors.ChatActivityInteractor;
import com.marbit.hobbytrophies.chat.interfaces.ChatActivityPresenterInterface;
import com.marbit.hobbytrophies.chat.interfaces.ChatActivityView;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.model.User;


public class ChatActivityPresenter implements ChatActivityPresenterInterface {
    private Context context;
    private ChatActivityView chatActivityView;
    private ChatActivityInteractor interactor;

    public ChatActivityPresenter(Context applicationContext, ChatActivityView chatActivityView) {
        this.context = applicationContext;
        this.chatActivityView = chatActivityView;
        this.interactor = new ChatActivityInteractor(applicationContext, this);
    }

    public void loadChat(String itemId, String buyer, String seller) {
        interactor.loadChat(itemId, buyer, seller);
    }

    @Override
    public void loadChatSuccessful(Chat chat) {
        chatActivityView.loadChatSuccessful(chat);
    }


    @Override
    public void sendMessageSuccessful(String messageId) {
        chatActivityView.sendMessageSuccessful(messageId);
    }

    @Override
    public void addMessageToChat(MessageChat messageChat) {
        chatActivityView.addMessageToChat(messageChat);
    }

    @Override
    public void loadUserBasicProfileSuccessful(User user) {
        chatActivityView.loadUserBasicProfileSuccessful(user);
    }

    public void sendMessage(String itemId, String buyer, String seller, String author, String message) {
        this.interactor.sendMessage(itemId, buyer, seller, author, message);
    }

    public void removeListenerAddMessageChat() {
        this.interactor.removeListenerAddMessageChat();
    }

    public void addListenerAddMessageChat(String chatId) {
        this.interactor.addListenerAddMessageChat(chatId);
    }

    public void loadAvatarSeller(String seller) {
        interactor.loadAvatarSeller(seller);
    }
}
