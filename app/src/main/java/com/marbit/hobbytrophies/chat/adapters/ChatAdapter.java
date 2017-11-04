package com.marbit.hobbytrophies.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.chat.model.MessageChat;
import com.marbit.hobbytrophies.utilities.Preferences;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> genericList;
    private Context context;
    private final static int MESSAGE_LEFT = 0;
    private final static int MESSAGE_RIGHT = 1;
    private final static int HEADER = 2;
    private final static int HEADER_DATE = 3;
    private SimpleDateFormat sdfHour;
    private SimpleDateFormat sdfDate;

    public ChatAdapter(Context context) {
        this.genericList = new ArrayList<>();
        this.context = context;
        this.genericList = new ArrayList<>();
        this.sdfHour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        this.sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
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
                viewHolder = new HeaderDateViewHolder(itemViewHeaderDate);
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
                HeaderDateViewHolder headerDateViewHolder = (HeaderDateViewHolder) holder;
                headerDateViewHolder.bindMessage((Long) object);
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

    public void setList(List<Object> list) {
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
        private TextView textViewHour;

        public LeftMessageViewHolder(View itemView) {
            super(itemView);
            this.textViewMessage = (TextView) itemView.findViewById(R.id.text_view_chat_left);
            this.textViewHour = (TextView) itemView.findViewById(R.id.text_view_hour_chat);
        }

        public void bindMessage(MessageChat messageChat) {
            this.textViewMessage.setText(messageChat.getMessage());
            Date date = new Date(messageChat.getdateLong());
            String formattedTime = sdfHour.format(date);
            this.textViewHour.setText(formattedTime);
        }
    }

    private class RightMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMessage;
        private TextView textViewHour;

        public RightMessageViewHolder(View itemView) {
            super(itemView);
            this.textViewMessage = (TextView) itemView.findViewById(R.id.text_view_chat_right);
            this.textViewHour = (TextView) itemView.findViewById(R.id.text_view_hour_chat);
        }

        public void bindMessage(MessageChat messageChat){
            this.textViewMessage.setText(messageChat.getMessage());
            Date date = new Date(messageChat.getdateLong());
            String formattedTime = sdfHour.format(date);
            this.textViewHour.setText(formattedTime);
        }
    }

    private class HeaderDateViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewDate;

        public HeaderDateViewHolder(View itemView) {
            super(itemView);
            this.textViewDate = (TextView) itemView.findViewById(R.id.text_View_date);
        }

        public void bindMessage(long dateLong){
            Date date = new Date(dateLong);
            String formattedTime = sdfDate.format(date);
            this.textViewDate.setText(formattedTime);
        }
    }
}

