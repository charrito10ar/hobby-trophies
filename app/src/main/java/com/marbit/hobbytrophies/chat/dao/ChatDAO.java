package com.marbit.hobbytrophies.chat.dao;


import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;
import com.marbit.hobbytrophies.utilities.DateUtils;
import com.marbit.hobbytrophies.utilities.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class    ChatDAO implements ChatDAOInterface {

    private ChatDAOListener chatDAOListener;
    private ChatDAOHeadersListener chatDAOHeadersListener;
    private DatabaseReference addMessageReference;
    private DatabaseReference addChatHeaderReference;
    private EventListenerAddChatInHeadersView eventListenerAddChatInHeadersView;
    private Context context;

    public ChatDAO(ChatDAOListener chatDAOListener){
        this.chatDAOListener = chatDAOListener;
    }

    public ChatDAO(Context context, ChatDAOHeadersListener chatDAOHeadersListener) {
        this.chatDAOHeadersListener = chatDAOHeadersListener;
        this.context = context;
    }

    ChildEventListener eventListenerAddChatInChatView = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageChat messageChat = dataSnapshot.getValue(MessageChat.class);
                chatDAOListener.addMessage(messageChat);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {        }
        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {        }
        @Override
        public void onCancelled(DatabaseError databaseError) {        }
    };

    public ChatDAO() {

    }

    @Override
    public void removeListenerAddMessageChat(){
        if(addMessageReference != null)
            addMessageReference.removeEventListener(eventListenerAddChatInChatView);
    }

    @Override
    public void removeListenerUpdateHeadersChat() {
        if(eventListenerAddChatInHeadersView != null)
            addChatHeaderReference.removeEventListener(eventListenerAddChatInHeadersView);
    }

    @Override
    public void addListenerAddMessageChat(String chatId) {
        addMessageReference = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHAT_MESSAGES).child(chatId);
        addMessageReference.addChildEventListener(eventListenerAddChatInChatView);
    }

    @Override
    public void addListenerAddHeaderChat(String userName){
        eventListenerAddChatInHeadersView = new EventListenerAddChatInHeadersView(userName);
        addChatHeaderReference = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHATS);
        addChatHeaderReference.addChildEventListener(eventListenerAddChatInHeadersView);
    }

    private class EventListenerAddChatInHeadersView implements ChildEventListener{
        private String userName;
        public EventListenerAddChatInHeadersView(String userName) {
            this.userName = userName;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            //Aquí manejo el primer item, ya que es un insert
            Chat chat = dataSnapshot.getValue(Chat.class);
            if(chat.getSeller().equals(Preferences.getUserId(context)) || chat.getBuyer().equals(Preferences.getUserId(context))){
                chatDAOHeadersListener.insertNewHeaderChat(chat);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            final Chat chat = dataSnapshot.getValue(Chat.class);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child(DataBaseConstants.COLUMN_USER_CHATS).child(userName).child(chat.getId());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        chatDAOHeadersListener.addMessage(chat);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    @Override
    public void loadChat(final String itemId, final String titleItem, final String buyer, final String seller, final String buyerName, final String sellerName) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_ITEM_CHATS).child(itemId).child(buyer);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String chatId = (String) dataSnapshot.getValue();
                if(chatId == null){
                    //Creo un chat y le pongo una lista de mensajes vacía. Cargo un chat vacío. Tal vez aquí hay que crear el chat
                    String chatIdCreate = createChat(itemId, titleItem, buyer, seller, buyerName, sellerName);
                    Chat chat = new Chat(chatIdCreate, itemId, buyer, seller, new ArrayList<MessageChat>());
                    chat.setBuyerName(buyerName);
                    chat.setSellerName(sellerName);
                    chatDAOListener.loadChatSuccessful(chat, new ArrayList<>());
                }else {
                    final ArrayList<MessageChat> messageChatList = new ArrayList<MessageChat>();
                    final ArrayList<Object> resultList = new ArrayList<>();
                    Query mDatabaseMessages = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHAT_MESSAGES).child(chatId);
                    mDatabaseMessages.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int aux = 0;
                            for(DataSnapshot messageChatSnapshot : dataSnapshot.getChildren()){
                                MessageChat messageChat = messageChatSnapshot.getValue(MessageChat.class);
                                if(aux > 0){
                                    if(DateUtils.getInstance().isDiferentDay(messageChat.getdateLong(), messageChatList.get(aux-1).getdateLong())){
                                        resultList.add(messageChat.getdateLong());
                                    }
                                }else {
                                    resultList.add(messageChat.getdateLong());
                                }
                                messageChatList.add(messageChat);
                                resultList.add(messageChat);
                                aux++;
                            }
                            Chat chat = new Chat(chatId, itemId, buyer, seller, messageChatList);
                            chat.setBuyerName(buyerName);
                            chat.setSellerName(sellerName);
                            chatDAOListener.loadChatSuccessful(chat, resultList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    @Override
    public void loadChat(final String chatId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHATS).child(chatId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Chat chat = dataSnapshot.getValue(Chat.class);
                final ArrayList<MessageChat> messageChatList = new ArrayList<MessageChat>();
                final ArrayList<Object> resultList = new ArrayList<>();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHAT_MESSAGES).child(chatId);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int aux = 0;
                        for(DataSnapshot messageChatSnapshot : dataSnapshot.getChildren()){
                            MessageChat messageChat = messageChatSnapshot.getValue(MessageChat.class);
                            if(aux > 0){
                                if(DateUtils.getInstance().isDiferentDay(messageChat.getdateLong(), messageChatList.get(aux-1).getdateLong())){
                                    resultList.add(messageChat.getdateLong());
                                }
                            }else {
                                resultList.add(messageChat.getdateLong());
                            }
                            messageChatList.add(messageChat);
                            resultList.add(messageChat);
                            aux++;
                        }
                        chat.setMessages(messageChatList);
                        chatDAOListener.loadChatSuccessful(chat, resultList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void sendMessage(final String itemId, final String titleItem, final String buyer, final String seller, final String author, final String message, final String buyerName, final String sellerName) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_ITEM_CHATS).child(itemId).child(buyer);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String chatId = (String) dataSnapshot.getValue();
                if(chatId == null){
                    // No debeía nunca ser null. Por las dudas Creo el chat efectivamente, ya que no existía
                    chatId = createChat(itemId, titleItem, buyer, seller, buyerName, sellerName);
                }
                String messageId = insertMessage(chatId, author, message);
                chatDAOListener.sendMessageSuccessful(chatId,author.equals(buyer)?seller : buyer, titleItem, messageId, author, message, itemId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public String createChat(String itemId, String titleItem, String buyer, String seller, String buyerName, String sellerName) {
        // -Crear DB CHATS
        // -Crear DB MEMBERS
        // -Crear DB ITEM-CHATS
        // -Crear DB USER-CHATS
        Chat chat = new Chat();
        chat.setBuyer(buyer);
        chat.setBuyerName(buyerName);
        chat.setItem(itemId);
        chat.setTitleItem(titleItem);
        chat.setSeller(seller);
        chat.setSellerName(sellerName);
        chat.setLastMessage("");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHATS).push();
        chat.setId(mDatabase.getKey());
        mDatabase.setValue(chat);
        insertMembers(chat.getId(), buyer, seller);
        insertItemChats(itemId, buyer, chat.getId());
        insertUserChats(chat.getId(), buyer, seller);
        return chat.getId();
    }

    private String insertMessage(String chatId, String author, String message) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHAT_MESSAGES).child(chatId).push();
        String messageId = mDatabase.getKey();
        MessageChat messageChat = new MessageChat(author, message);
        messageChat.setId(messageId);
        messageChat.setType(MessageChat.NORMAL_MESSAGE);
        mDatabase.setValue(messageChat);
        updateChatLastMessage(chatId, message);
        return messageId;
    }

    public String insertItemSoldMessage(String chatId, String author) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHAT_MESSAGES).child(chatId).push();
        String messageId = mDatabase.getKey();
        MessageChat messageChat = new MessageChat(author, "Califica al vendedor");
        messageChat.setId(messageId);
        messageChat.setType(MessageChat.SOLD_MESSAGE);
        mDatabase.setValue(messageChat);
        updateChatLastMessage(chatId, "Califica al vendedor");
        return messageId;
    }

    private void updateChatLastMessage(String chatId, String message) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHATS)
                .child(chatId);
        Map<String, Object> lastMessage = new HashMap<>();
        lastMessage.put("lastMessage", message);
        mDatabase.updateChildren(lastMessage);

        Map<String, Object> dateLastMessage = new HashMap<>();
        dateLastMessage.put("dateLastMessage", ServerValue.TIMESTAMP);
        mDatabase.updateChildren(dateLastMessage);

    }

    private void insertUserChats(String chatId, String buyer, String seller) {
        DatabaseReference mDatabaseBuyer = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_USER_CHATS).child(buyer).child(chatId);
        mDatabaseBuyer.setValue(true);
        DatabaseReference mDatabaseSeller = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_USER_CHATS).child(seller).child(chatId);
        mDatabaseSeller.setValue(true);
    }

    private void insertItemChats(String itemId, String buyer, String chatId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_ITEM_CHATS).child(itemId).child(buyer);
        mDatabase.setValue(chatId);
    }

    private void insertMembers(String chatId, String buyerId, String sellerId){
        Map<String, Boolean> map = new HashMap<>();
        map.put(buyerId, true);
        map.put(sellerId, true);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHAT_MEMBERS).child(chatId);
        mDatabase.setValue(map);

    }

    public void loadChatsHeaders(String userName) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_USER_CHATS).child(userName);
        final List<Chat> chatList = new ArrayList<>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long sizeList = dataSnapshot.getChildrenCount();
                for(DataSnapshot messageHeaderDataSnapshot : dataSnapshot.getChildren()){
                    String chatId = messageHeaderDataSnapshot.getKey();
                    DatabaseReference mDatabaseChat = FirebaseDatabase.getInstance().getReference().child(DataBaseConstants.COLUMN_CHATS).child(chatId);
                    mDatabaseChat.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            chatList.add(chat);
                            if(sizeList == chatList.size()){
                                //Collections.sort(chatList);
                                chatDAOHeadersListener.loadChatHeadersSuccessful(chatList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public interface ChatDAOListener{
        void loadChatSuccessful(Chat chat, List<Object> genericListMessage);

        void sendMessageSuccessful(String chatId, String userTo, String titleItem, String messageId, String author, String message, String itemId);

        void addMessage(MessageChat messageChat);
    }

    public interface ChatDAOHeadersListener{
        void loadChatHeadersSuccessful(List<Chat> chatList);

        void addMessage(Chat chat);

        void insertNewHeaderChat(Chat chat);
    }

}
