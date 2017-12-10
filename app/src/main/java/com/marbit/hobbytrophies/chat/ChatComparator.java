package com.marbit.hobbytrophies.chat;

import com.marbit.hobbytrophies.chat.model.Chat;

import java.util.Comparator;

public class ChatComparator implements Comparator<Chat> {
    @Override
    public int compare(Chat chatOne, Chat chatTwo) {
        int result = (int) (chatOne.getdateLastMessageLong() - chatTwo.getdateLastMessageLong());
        return result;
    }
}
