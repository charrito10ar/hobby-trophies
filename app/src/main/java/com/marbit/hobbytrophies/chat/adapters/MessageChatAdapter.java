package com.marbit.hobbytrophies.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.fragments.MessagesFragment.MessagesFragmentInteractionListener;
import com.marbit.hobbytrophies.utilities.Preferences;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MessageChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {

    private final MessagesFragmentInteractionListener mListener;
    private List<Chat> chatList;
    private Context context;

    public MessageChatAdapter(Context context, MessagesFragmentInteractionListener listener) {
        this.chatList = new ArrayList<>();
        this.mListener = listener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_messages_chat, parent, false);
        return new MessageChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  holder, int position) {
        MessageChatViewHolder messageChatViewHolder = (MessageChatViewHolder) holder;
        Chat chat = chatList.get(position);
        messageChatViewHolder.messageChatBin(chat);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public void setList(List<Chat> list) {
        this.chatList.addAll(list);
    }

    public void clearAll() {
        this.chatList.clear();
    }

    public void updateChat(Chat chat) {
        for(int i = 0; i < chatList.size(); i++){
            if (chatList.get(i).getId().equals(chat.getId())){
                chatList.remove(i);
                notifyItemRemoved(i);
                chatList.add(chat);
                notifyItemInserted(0);
            }
        }
    }

    public void insertChat(Chat chat) {
        chatList.add(0, chat);
        notifyItemInserted(0);
    }


    private class MessageChatViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView lastMessage;
        TextView partnerChat;
        TextView dateLastMessage;
        PrettyTime prettyTime;

        MessageChatViewHolder(View view) {
            super(view);
            mView = view;
            lastMessage = (TextView) view.findViewById(R.id.last_message);
            partnerChat = (TextView) view.findViewById(R.id.text_view_chat_partner);
            dateLastMessage = (TextView) view.findViewById(R.id.text_view_date);
            prettyTime = new PrettyTime();
        }

        void messageChatBin(Chat chat){
            this.lastMessage.setText(chat.getLastMessage());
            itemView.setOnClickListener(new ClickHeaderChat(chat));
            partnerChat.setText(chat.getBuyer().equals(Preferences.getUserName(context)) ? chat.getSeller() : chat.getBuyer());
            dateLastMessage.setText(prettyTime.format(new Date(chat.getdateLastMessageLong())));
        }

        private class ClickHeaderChat implements View.OnClickListener{
            private Chat chat;
            ClickHeaderChat(Chat chat) {
                this.chat = chat;
            }

            @Override
            public void onClick(View v) {
                mListener.openChat(chat);
            }
        }
    }
}
