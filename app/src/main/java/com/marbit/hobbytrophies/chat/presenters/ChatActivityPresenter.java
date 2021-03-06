package com.marbit.hobbytrophies.chat.presenters;

import android.content.Context;

import com.marbit.hobbytrophies.chat.interactors.ChatActivityInteractor;
import com.marbit.hobbytrophies.chat.interfaces.ChatActivityPresenterInterface;
import com.marbit.hobbytrophies.chat.interfaces.ChatActivityView;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.model.market.Item;

import java.util.List;


public class ChatActivityPresenter implements ChatActivityPresenterInterface {
    private Context context;
    private ChatActivityView chatActivityView;
    private ChatActivityInteractor interactor;

    public ChatActivityPresenter(Context applicationContext, ChatActivityView chatActivityView) {
        this.context = applicationContext;
        this.chatActivityView = chatActivityView;
        this.interactor = new ChatActivityInteractor(applicationContext, this);
    }

    public void loadChat(String itemId, String titleItem, String buyer, String seller, String buyerName, String sellerName) {
        interactor.loadChat(itemId, titleItem, buyer, seller, buyerName, sellerName);
    }

    @Override
    public void loadChatSuccessful(Chat chat, List<Object> genericListMessages) {
        chatActivityView.loadChatSuccessful(chat, genericListMessages);
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

    @Override
    public void loadItemError(String message) {
        chatActivityView.showMessage(message);
    }

    @Override
    public void loadItemSuccessful(Item item) {
        if(item.getStatus() != 0){
            chatActivityView.setDisableWriteMessage();
        }
    }

    public void sendMessage(String itemId, String titleItem, String buyer, String seller, String author, String message, String buyerName, String sellerName) {
        this.interactor.sendMessage(itemId, titleItem, buyer, seller, author, message, buyerName, sellerName);
    }

    public void removeListenerAddMessageChat() {
        this.interactor.removeListenerAddMessageChat();
    }

    public void addListenerAddMessageChat(String chatId) {
        this.interactor.addListenerAddMessageChat(chatId);
    }

    public void loadPartnerUserChat(String partnerUserChat) {
        interactor.loadPartnerUserChat(partnerUserChat);
    }

    public void loadItem(String itemId) {
        interactor.loadItem(itemId);
    }

    public void loadChat(String chatId) {
        interactor.loadChat(chatId);
    }
}
