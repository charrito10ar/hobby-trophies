package com.marbit.hobbytrophies.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.chat.model.Chat;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> genericList;
    private Context context;
    private final static int MESSAGE_LEFT = 0;
    private final static int MESSAGE_RIGHT = 1;
    private final static int HEADER = 2;

    public ChatAdapter(Context context) {
        this.genericList = new ArrayList<>();
        this.context = context;
        this.genericList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MESSAGE_LEFT:
                View itemViewChatLeft = inflater.inflate(R.layout.list_item_chat_left, parent, false);
                viewHolder = new LeftMessageViewHolder(itemViewChatLeft);
                break;
            case MESSAGE_RIGHT:
                View itemViewChatRight = inflater.inflate(R.layout.list_item_chat_right, parent, false);
                viewHolder = new RightMessageViewHolder(itemViewChatRight);
                break;
            default:
                View itemViewHeaderDate = inflater.inflate(R.layout.list_item_header_date, parent, false);
                viewHolder = new RightMessageViewHolder(itemViewHeaderDate);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object object = genericList.get(position);

        switch (getItemViewType(position)){
            case MESSAGE_LEFT:
                LeftMessageViewHolder leftMessageViewHolder = (LeftMessageViewHolder) holder;
                leftMessageViewHolder.bindMessage((MessageChat) object);
                break;
            case MESSAGE_RIGHT:
                RightMessageViewHolder rightMessageViewHolder = (RightMessageViewHolder) holder;
                rightMessageViewHolder.bindMessage((MessageChat) object);
                break;
            case HEADER:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = this.genericList.get(position);
        if (MessageChat.class.isInstance(object)) {
            MessageChat chat = (MessageChat) object;
            if(Preferences.getUserName(context).equals(chat.getAuthor())){
                return MESSAGE_RIGHT;
            }else {
                return MESSAGE_LEFT;
            }
        }else {
            return HEADER;
        }
    }

    @Override
    public int getItemCount() {
        return genericList.size();
    }

    public void setList(List<MessageChat> list) {
        this.genericList.addAll(list);
    }

    public void clearAll() {
        this.genericList.clear();
    }

    public void addMessage(MessageChat messageChat) {
        this.genericList.add(messageChat);
        notifyItemInserted(genericList.size());
    }

    private class LeftMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMessage;
        private ImageView avatar;

        public LeftMessageViewHolder(View itemView) {
            super(itemView);
            this.textViewMessage = (TextView) itemView.findViewById(R.id.text_view_chat_left);
            this.avatar = (ImageView) itemView.findViewById(R.id.image_view_avatar);
        }

        public void bindMessage(MessageChat messageChat) {
            this.textViewMessage.setText(messageChat.getMessage());
            Picasso.with(context).load(messageChat.getAuthor()).error(R.drawable.avatar).fit().transform(new CircleTransform()).into(avatar);
        }
    }

    private class RightMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMessage;

        public RightMessageViewHolder(View itemView) {
            super(itemView);
            this.textViewMessage = (TextView) itemView.findViewById(R.id.text_view_chat_right);
        }

        public void bindMessage(MessageChat messageChat){
            this.textViewMessage.setText(messageChat.getMessage());
        }
    }
}

