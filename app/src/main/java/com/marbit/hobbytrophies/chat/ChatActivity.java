package com.marbit.hobbytrophies.chat;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.chat.adapters.ChatAdapter;
import com.marbit.hobbytrophies.chat.interfaces.ChatActivityView;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.chat.presenters.ChatActivityPresenter;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.Preferences;

public class ChatActivity extends AppCompatActivity implements ChatActivityView, TextWatcher {

    public final static String PARAM_ITEM_ID = "ITEM-ID";
    public final static String PARAM_BUYER = "BUYER";
    public final static String PARAM_SELLER = "SELLER";

    private ChatActivityPresenter presenter;
    private RecyclerView recyclerViewChats;
    private ChatAdapter chatAdapter;
    private Chat chat;
    private String itemId;
    private ImageButton buttonSend;
    private EditText editTextMessage;
    private String buyer;
    private String seller;
    private Item item;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.presenter = new ChatActivityPresenter(getApplicationContext(), this);
        this.itemId = getIntent().getStringExtra(PARAM_ITEM_ID);
        this.buyer = getIntent().getStringExtra(PARAM_BUYER);
        this.seller = getIntent().getStringExtra(PARAM_SELLER);
        this.presenter = new ChatActivityPresenter(getApplicationContext(), this);
        this.recyclerViewChats = (RecyclerView) findViewById(R.id.recycler_view_chats);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerViewChats.setLayoutManager(mLayoutManager);
        this.buttonSend = (ImageButton) findViewById(R.id.button_send_message);
        this.buttonSend.setEnabled(false);
        this.editTextMessage = (EditText) findViewById(R.id.edit_text_message);
        this.editTextMessage.addTextChangedListener(this);
        this.chatAdapter = new ChatAdapter(getApplicationContext());
        this.recyclerViewChats.setAdapter(chatAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.count = 0;
        this.loadChat();
    }

    @Override
    public void onPause() {
        presenter.removeListenerAddMessageChat();
        super.onPause();
    }

    private void refreshChat(Chat chat) {
        this.chatAdapter.clearAll();
        this.chatAdapter.setList(chat.getMessages());
        this.chatAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showRightMessage(String message) {

    }

    @Override
    public void showLeftMessage(String message) {

    }

    @Override
    public void loadChatSuccessful(Chat chat) {
        this.chat = chat;
        refreshChat(chat);
        this.presenter.addListenerAddMessageChat(chat.getId());
        recyclerViewChats.scrollToPosition(chatAdapter.getItemCount() - 1);
        this.presenter.loadAvatarSeller(chat.getSeller());
    }

    @Override
    public void loadChat() {
        if(seller == null){
            this.presenter.loadChat(itemId, buyer, null);
        }else {
            this.presenter.loadChat(itemId, buyer, seller);
        }
    }

    @Override
    public void sendMessageSuccessful(String messageId) {
        // Update estado del mensaje con el tilde de enviado (Pero no tengo el id!!)
//        chatAdapter.confirmMessageSent(messageId);
//        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void addMessageToChat(MessageChat messageChat) {
        count++;
        if(count > chat.getMessages().size()) {
            chatAdapter.addMessage(messageChat);
            recyclerViewChats.scrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void loadUserBasicProfileSuccessful(User user) {
        // Cargar el avatar
    }

    public void clickSendMessage(View view) {
        presenter.sendMessage(itemId, buyer, seller , Preferences.getUserName(getApplicationContext()), editTextMessage.getText().toString());
        editTextMessage.setText("");
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() > 0){
            buttonSend.setEnabled(true);
            buttonSend.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }else {
            buttonSend.setEnabled(false);
            buttonSend.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.material_grey_600));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
