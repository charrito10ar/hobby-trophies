package com.marbit.hobbytrophies.chat.presenters;

import android.content.Context;

import com.marbit.hobbytrophies.chat.interactors.MessagesFragmentInteractor;
import com.marbit.hobbytrophies.chat.interfaces.MessagesFragmentPresenterInterface;
import com.marbit.hobbytrophies.chat.interfaces.MessagesFragmentView;
import com.marbit.hobbytrophies.chat.model.Chat;

import java.util.List;

public class MessagesFragmentPresenter implements MessagesFragmentPresenterInterface {

    private MessagesFragmentView messagesFragmentView;
    private MessagesFragmentInteractor interactor;

    public MessagesFragmentPresenter(Context context, MessagesFragmentView messagesFragmentView) {
        this.messagesFragmentView = messagesFragmentView;
        this.interactor = new MessagesFragmentInteractor(context, this);
    }

    public void loadChatsHeaders(String userName) {
        messagesFragmentView.showLoading();
        interactor.loadChatsHeaders(userName);
    }

    @Override
    public void loadChatHeadersSuccessful(List<Chat> chatList) {
        messagesFragmentView.hideLoading();
        messagesFragmentView.loadChatHeadersSuccessful(chatList);
    }

    @Override
    public void addMessage(Chat chat) {
        messagesFragmentView.addMessage(chat);
    }

    @Override
    public void insertNewHeaderChat(Chat chat) {
        messagesFragmentView.insertNewHeaderChat(chat);
    }

    public void removeListenerUpdateHeadersChat() {
        interactor.removeListenerUpdateHeadersChat();
    }

    public void addListenerAddHeaderChat(String userName) {
        interactor.addListenerAddHeaderChat(userName);
    }
}
