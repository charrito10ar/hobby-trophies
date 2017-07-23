package com.marbit.hobbytrophies.chat.interactors;


import android.content.Context;

import com.marbit.hobbytrophies.chat.dao.ChatDAO;
import com.marbit.hobbytrophies.chat.interfaces.MessagesFragmentPresenterInterface;
import com.marbit.hobbytrophies.chat.model.Chat;

import java.util.List;

public class MessagesFragmentInteractor implements ChatDAO.ChatDAOHeadersListener {
    private final ChatDAO chatDAO;
    private Context context;
    private MessagesFragmentPresenterInterface presenter;

    public MessagesFragmentInteractor(Context context, MessagesFragmentPresenterInterface presenter) {
        this.context = context;
        this.presenter = presenter;
        chatDAO = new ChatDAO(context, this);
    }

    public void loadChatsHeaders(String userName) {
        chatDAO.loadChatsHeaders(userName);
    }

    @Override
    public void loadChatHeadersSuccessful(List<Chat> chatList) {
        presenter.loadChatHeadersSuccessful(chatList);
    }

    @Override
    public void addMessage(Chat chat) {
        presenter.addMessage(chat);
    }

    @Override
    public void insertNewHeaderChat(Chat chat) {
        presenter.insertNewHeaderChat(chat);
    }

    public void removeListenerUpdateHeadersChat() {
        chatDAO.removeListenerUpdateHeadersChat();
    }

    public void addListenerAddHeaderChat(String userName) {
        chatDAO.addListenerAddHeaderChat(userName);
    }
}
