package com.marbit.hobbytrophies.chat.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Chat implements Comparable<Chat>{

    private String id;
    private String seller;
    private String buyer;
    private String item;
    private String titleItem;
    private String lastMessage;
    private List<MessageChat> messages;
    private Long dateLastMessage;


    public Chat(){
    }

    public Chat(String chatId, String itemId, String buyer, String seller, ArrayList<MessageChat> messages) {
        this.id = chatId;
        this.item = itemId;
        this.buyer = buyer;
        this.seller = seller;
        this.messages = messages;
    }

    public Chat(String chatId, String message) {
        this.id = chatId;
        this.lastMessage = message;
    }

    public Chat(String chatId) {
        this.id = chatId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Exclude
    public List<MessageChat> getMessages() {
        return messages;
    }

    @Exclude
    public void setMessages(List<MessageChat> messages) {
        this.messages = messages;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Exclude
    public Long getdateLastMessageLong() {
        return dateLastMessage;
    }

    public Map<String, String> getdateLastMessage() {
        return ServerValue.TIMESTAMP;
    }

    public void setdateLastMessage(Long dateLastMessage) {
        this.dateLastMessage = dateLastMessage;
    }

    @Override
    public int compareTo(@NonNull Chat chat) {
        return (int) (this.getdateLastMessageLong() - chat.getdateLastMessageLong());
    }

    public String getTitleItem() {
        return titleItem;
    }

    public void setTitleItem(String titleItem) {
        this.titleItem = titleItem;
    }
}
