package com.marbit.hobbytrophies.chat.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.Map;

public class MessageChat {
    public static final String SOLD_MESSAGE = "SOLD_MESSAGE";
    public static final String NORMAL_MESSAGE = "NORMAL_MESSAGE";

    private String id;
    private String message;
    private String author;
    private Long date;
    private String type = NORMAL_MESSAGE;

    public MessageChat(){
    }

    public MessageChat(String author, String message) {
        this.author = author;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Exclude
    public Long getdateLong() {
        return date;
    }
    public Map<String, String> getdate() {
        return ServerValue.TIMESTAMP;
    }

    public void setdate(Long date) {
        this.date = date;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
