package com.marbit.hobbytrophies.chat.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private final static int MESSAGE_ITEM_SOLD_TO_RATE = 4;
    private final static int MESSAGE_ITEM_SOLD_RATED = 5;
    private final static int HEADER_DATE = 3;
    private SimpleDateFormat sdfHour;
    private SimpleDateFormat sdfDate;
    private ChatAdapterListener chatAdapterListener;

    public ChatAdapter(Context context, ChatAdapterListener chatAdapterListener) {
        this.genericList = new ArrayList<>();
        this.context = context;
        this.genericList = new ArrayList<>();
        this.sdfHour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        this.sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        this.chatAdapterListener = chatAdapterListener;
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
            case MESSAGE_ITEM_SOLD_TO_RATE:
                View itemViewChatItemSold = inflater.inflate(R.layout.list_item_sold, parent, false);
                viewHolder = new ItemSoldViewHolder(itemViewChatItemSold);
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
            case MESSAGE_ITEM_SOLD_TO_RATE:
                ItemSoldViewHolder itemSoldViewHolder = (ItemSoldViewHolder) holder;
                itemSoldViewHolder.bindItemSold((MessageChat) object);
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = this.genericList.get(position);
        if (MessageChat.class.isInstance(object)) {
            MessageChat chat = (MessageChat) object;
            if(chat.getType().equals(MessageChat.NORMAL_MESSAGE)){
                if(Preferences.getUserId(context).equals(chat.getAuthor())){
                    return MESSAGE_RIGHT;
                }else {
                    return MESSAGE_LEFT;
                }
            }else {
                return MESSAGE_ITEM_SOLD_TO_RATE;
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
            this.textViewMessage = itemView.findViewById(R.id.text_view_chat_left);
            this.textViewHour = itemView.findViewById(R.id.text_view_hour_chat);
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
            this.textViewMessage = itemView.findViewById(R.id.text_view_chat_right);
            this.textViewHour = itemView.findViewById(R.id.text_view_hour_chat);
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

        private HeaderDateViewHolder(View itemView) {
            super(itemView);
            this.textViewDate = itemView.findViewById(R.id.text_View_date);
        }

        private void bindMessage(long dateLong){
            Date date = new Date(dateLong);
            String formattedTime = sdfDate.format(date);
            this.textViewDate.setText(formattedTime);
        }
    }


    private class ItemSoldViewHolder extends RecyclerView.ViewHolder {

        private Button buttonRateSeller;
        private LinearLayout itemBought;
        private TextView itemSold;
        private TextView itemBoughtTitle;

        private ItemSoldViewHolder(View itemView) {
            super(itemView);
            this.buttonRateSeller = itemView.findViewById(R.id.button_rate_seller);
            this.itemBought = itemView.findViewById(R.id.item_bought);
            this.itemSold = itemView.findViewById(R.id.item_sold);
            this.itemBoughtTitle = itemView.findViewById(R.id.item_bought_title);
        }

        private void bindItemSold(MessageChat messageChat){
            if(messageChat.getType().equals("SOLD_MESSAGE_RATED")){
                buttonRateSeller.setVisibility(View.INVISIBLE);
                itemSold.setVisibility(View.INVISIBLE);
                itemBoughtTitle.setText("Has comprado este artículo");
            }else {
                if(messageChat.getAuthor().equals(Preferences.getUserId(context))){
                    this.itemSold.setVisibility(View.GONE);
                    this.itemBought.setVisibility(View.VISIBLE);
                }else {
                    this.itemSold.setVisibility(View.VISIBLE);
                    this.itemBought.setVisibility(View.GONE);
                }
                chatAdapterListener.itemSoldSendMessageDisable();
                this.buttonRateSeller.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chatAdapterListener.clickRateSeller();
                    }
                });
            }

        }
    }

    public interface ChatAdapterListener{
        void clickRateSeller();
        void itemSoldSendMessageDisable();
    }
}

