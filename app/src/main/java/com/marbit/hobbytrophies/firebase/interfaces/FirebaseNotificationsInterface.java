package com.marbit.hobbytrophies.firebase.interfaces;

import com.marbit.hobbytrophies.model.market.Item;

import org.json.JSONException;

import java.util.List;

public interface FirebaseNotificationsInterface {
    void sendNotificationMarketChat(String userSender, String userTo, String itemTitle, String message, String itemId, String chatId) throws JSONException;
    void sendNotificationWishListMatch(List<String> userToList, Item item) throws JSONException;
}
