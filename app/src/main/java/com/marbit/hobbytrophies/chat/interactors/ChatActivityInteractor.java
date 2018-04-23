package com.marbit.hobbytrophies.chat.interactors;

import android.content.Context;

import com.marbit.hobbytrophies.chat.dao.ChatDAO;
import com.marbit.hobbytrophies.chat.interfaces.ChatActivityPresenterInterface;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.dao.ItemDAO;
import com.marbit.hobbytrophies.dao.UserDAO;
import com.marbit.hobbytrophies.firebase.FirebaseNotifications;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.model.market.Item;

import org.json.JSONException;

import java.util.List;


public class ChatActivityInteractor implements ChatDAO.ChatDAOListener {

    private final ChatDAO chatDAO;
    private ChatActivityPresenterInterface presenter;
    private Context context;

    public ChatActivityInteractor(Context applicationContext, ChatActivityPresenterInterface presenter) {
        this.presenter = presenter;
        chatDAO = new ChatDAO(this);
        this.context = applicationContext;
    }

    public void loadChat(String itemId, String titleItem, String buyer, String seller, String buyerName, String sellerName) {
        chatDAO.loadChat(itemId, titleItem, buyer, seller, buyerName, sellerName);
    }

    public void loadChat(String chatId) {
        chatDAO.loadChat(chatId);
    }

    public void sendMessage(String itemId, String titleItem, String buyer, String seller, String author, String message, String buyerName, String sellerName) {
        chatDAO.sendMessage(itemId, titleItem, buyer, seller, author, message, buyerName, sellerName);
    }

    @Override
    public void loadChatSuccessful(Chat chat, List<Object> genericListMessages) {
        presenter.loadChatSuccessful(chat, genericListMessages);
    }

    @Override
    public void sendMessageSuccessful(String chatId, String userTo, String itemTitle, String messageId, String author, String message, String itemId) {
        presenter.sendMessageSuccessful(messageId);
        FirebaseNotifications firebaseNotifications = new FirebaseNotifications(context);
        try {
            firebaseNotifications.sendNotificationMarketChat(author, userTo, itemTitle, message, itemId, chatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void loadPartnerUserChat(String seller) {
        UserDAO userDAO = new UserDAO(context);
        userDAO.getUserBasicProfile(seller, new UserDAO.ListenerUserDAO() {
            @Override
            public void loadUserBasicProfileSuccessful(User user) {
                presenter.loadUserBasicProfileSuccessful(user);
            }
        });
    }

    public void loadItem(String itemId) {
        ItemDAO itemDAO = new ItemDAO(context);
        itemDAO.loadItemById(context, itemId, new ItemDAO.SingleItemDAOListener() {
            @Override
            public void loadItemByIdSuccess(Item item) {
                presenter.loadItemSuccessful(item);
            }

            @Override
            public void loadItemByIdError(String message) {
                presenter.loadItemError(message);
            }
        });
    }
}
