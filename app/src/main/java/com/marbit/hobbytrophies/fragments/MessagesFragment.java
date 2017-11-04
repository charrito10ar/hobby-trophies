package com.marbit.hobbytrophies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.chat.adapters.MessageChatAdapter;
import com.marbit.hobbytrophies.chat.interfaces.MessagesFragmentView;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.presenters.MessagesFragmentPresenter;
import com.marbit.hobbytrophies.utilities.Preferences;

import java.util.Collections;
import java.util.List;


public class MessagesFragment extends Fragment implements MessagesFragmentView{

    private MessagesFragmentInteractionListener mListener;
    private MessagesFragmentPresenter presenter;
    private MessageChatAdapter messageChatAdapter;
    private List<Chat> chatList;
    private int count;

    public MessagesFragment() {
    }

    @SuppressWarnings("unused")
    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new MessagesFragmentPresenter(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_chat_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        messageChatAdapter = new MessageChatAdapter(getContext(), mListener);
        recyclerView.setAdapter(messageChatAdapter);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        this.count = 0;
        this.presenter.loadChatsHeaders(Preferences.getUserName(getContext()));
    }

    @Override
    public void onPause() {
        presenter.removeListenerUpdateHeadersChat();
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessagesFragmentInteractionListener) {
            mListener = (MessagesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MessagesFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void loadChatHeadersSuccessful(List<Chat> chatList) {
        Collections.sort(chatList);
        this.chatList = chatList;
        messageChatAdapter.clearAll();
        messageChatAdapter.setList(chatList);
        messageChatAdapter.notifyDataSetChanged();
        this.presenter.addListenerAddHeaderChat(Preferences.getUserName(getContext()));
    }

    @Override
    public void addMessage(Chat chat) {
        messageChatAdapter.updateChat(chat);
    }

    @Override
    public void insertNewHeaderChat(Chat chat) {
        count++;
        if(count > chatList.size()) {
            messageChatAdapter.insertChat(chat);
        }
    }

    public interface MessagesFragmentInteractionListener {
        void openChat(Chat chat);
    }
}
