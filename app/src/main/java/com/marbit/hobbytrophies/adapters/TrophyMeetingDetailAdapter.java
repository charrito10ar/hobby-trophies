package com.marbit.hobbytrophies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.TrophyDetailActivity;
import com.marbit.hobbytrophies.model.Meeting;
import com.marbit.hobbytrophies.model.MessageMeeting;
import com.marbit.hobbytrophies.model.Trophy;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.utilities.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class TrophyMeetingDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_MEETING = 0;
    private static final int TROPHY = 1;
    private static final int HEADER_MESSAGE = 2;
    private List<Object> trophyList;
    private List<User> playerList;
    private List<MessageMeeting> messageMeetingList;
    private Context context;
    private OnListenerEndListMessage listenerEndListMessage;
    private int messageAmount;
    private boolean loading = true;
    private Meeting meeting;

    public TrophyMeetingDetailAdapter(Context context, OnListenerEndListMessage onListenerEndListMessage) {
        this.trophyList = new ArrayList<>();
        this.playerList = new ArrayList<>();
        this.messageMeetingList = new ArrayList<>();
        this.context = context;
        this.listenerEndListMessage = onListenerEndListMessage;
        this.messageAmount = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case HEADER_MEETING:
                View itemViewHeader = inflater.inflate(R.layout.list_item_trophy_header_players, parent, false);
                viewHolder = new HeaderViewHolder(itemViewHeader);
                break;
            case TROPHY:
                View itemViewTrophy = inflater.inflate(R.layout.list_item_trophy_meeting, parent, false);
                viewHolder = new TrophyViewHolder(itemViewTrophy);
                break;
            default:
                View itemViewHeaderMessages = inflater.inflate(R.layout.list_item_header_meeting_messages, parent, false);
                viewHolder = new HeaderMessageViewHolder(itemViewHeaderMessages);
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case HEADER_MEETING:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.bindHeader(this.playerList);
                break;
            case TROPHY:
                Trophy trophy = (Trophy) trophyList.get(position - 1);
                TrophyViewHolder trophyViewHolder = (TrophyViewHolder) holder;
                trophyViewHolder.onBindTrophy(trophy);
                break;
            default:
                HeaderMessageViewHolder headerMessageViewHolder = (HeaderMessageViewHolder) holder;
                headerMessageViewHolder.onBindHeaderMessage(messageMeetingList);
        }
    }

    @Override
    public int getItemCount() {
        return trophyList.size() + 2;
    }
    public void setList(List<Trophy> list) {
        this.trophyList.addAll(list);
    }
    public void clearAll() {
        this.trophyList.clear();
    }

    public void setListPlayers(List<User> list) {
        this.playerList.addAll(list);
    }
    public void clearAllPlayers() {
        this.playerList.clear();
    }

    public void setListMessages(List<MessageMeeting> list) {
        this.messageMeetingList.addAll(list);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_MEETING;
        }else {
            if(position > 0 && position <= this.trophyList.size()){
                return TROPHY;
            }else {
                return HEADER_MESSAGE;
            }
        }
    }

    public void setMessageAmount(int messageAmount) {
        this.messageAmount = messageAmount;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void addMessage(MessageMeeting messageMeeting) {
        this.messageAmount++;
        this.messageMeetingList.add(0, messageMeeting);
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    private class TrophyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private ImageView imageTrophy;
        private ImageView imageTrophyType;
        private CheckBox checkBox;
        private View view;

        public TrophyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.text_title_trophy);
            this.description = (TextView) itemView.findViewById(R.id.text_description_trophy);
            this.imageTrophy = (ImageView) itemView.findViewById(R.id.ic_trophy);
            this.imageTrophyType = (ImageView) itemView.findViewById(R.id.ic_medal_type);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_trophy_meeting);
            this.view = itemView;
        }

        public void onBindTrophy(final Trophy trophy){
            this.title.setText(trophy.getTitle());
            this.description.setText(trophy.getDescription());
            Picasso.with(context).load(trophy.getImg()).placeholder(R.drawable.ic_placeholder_trophy).fit().into(this.imageTrophy);
            this.setTrophyType(trophy.getType());
            this.checkBox.setVisibility(View.GONE);
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TrophyDetailActivity.class);

                    intent.putExtra("TROPHY", trophy);
                    intent.putExtra("GAME-ID", meeting.getGame().getId());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) listenerEndListMessage,
                                new Pair<View, String>(v.findViewById(R.id.ic_trophy),
                                        context.getString(R.string.transition_image_trophy)),
                                new Pair<View, String>(v.findViewById(R.id.text_title_trophy),
                                        context.getString(R.string.transition_title_trophy))
                        );

                        v.getContext().startActivity(intent, options.toBundle());

                    } else {
                        v.getContext().startActivity(intent);
                    }
                }
            });

        }

        public void setTrophyType(String trophyType) {
            switch (trophyType.toLowerCase()){
                case Constants.TROPHY_TYPE_BRONZE:
                    this.imageTrophyType.setImageResource(R.drawable.ic_bronze_medal);
                    break;
                case Constants.TROPHY_TYPE_SILVER:
                    this.imageTrophyType.setImageResource(R.drawable.ic_silver_medal);
                    break;
                case Constants.TROPHY_TYPE_GOLD:
                    this.imageTrophyType.setImageResource(R.drawable.ic_gold_medal);
                    break;
                case Constants.TROPHY_TYPE_PLATINUM:
                    this.imageTrophyType.setImageResource(R.drawable.ic_platinum);

            }

        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerViewPlayers;
        private PlayersHorizontalAdapter playersHorizontalAdapter;


        public HeaderViewHolder(View itemViewHeader) {
            super(itemViewHeader);
            this.recyclerViewPlayers = (RecyclerView) itemViewHeader.findViewById(R.id.recycler_view_meeting_players);
            RecyclerView.LayoutManager mLayoutManagerPlayers = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            this.recyclerViewPlayers.setLayoutManager(mLayoutManagerPlayers);
            this.playersHorizontalAdapter = new PlayersHorizontalAdapter(context);
            this.recyclerViewPlayers.setAdapter(this.playersHorizontalAdapter);

            NativeExpressAdView adView = (NativeExpressAdView)itemViewHeader.findViewById(R.id.adViewMeeting);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("2843E996E3E48320E27B16741947CF56")
                    .build();
            adView.loadAd(request);
        }

        public void bindHeader(List listPlayers) {
            this.refreshContent(listPlayers);
        }

        private void refreshContent(List listPlayers) {
            this.playersHorizontalAdapter.clearAll();
            this.playersHorizontalAdapter.setList(listPlayers);
            this.playersHorizontalAdapter.setAmountTrophies(trophyList.size());
            this.playersHorizontalAdapter.notifyDataSetChanged();
        }
    }

    private class HeaderMessageViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayoutManager mLayoutManagerMessages;
        private RecyclerView recyclerViewMessages;
        private MessageMeetingAdapter messageMeetingAdapter;
        private int pastVisiblesItems, visibleItemCount, totalItemCount;
        private TextView messagesAmount;

        public HeaderMessageViewHolder(View itemView) {
            super(itemView);
            this.recyclerViewMessages = (RecyclerView) itemView.findViewById(R.id.recycler_view_messages);
            this.messagesAmount = (TextView) itemView.findViewById(R.id.text_view_comments_count);
            this.messageMeetingAdapter = new MessageMeetingAdapter(context);
            this.mLayoutManagerMessages = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            this.recyclerViewMessages.setLayoutManager(mLayoutManagerMessages);
            this.recyclerViewMessages.setAdapter(this.messageMeetingAdapter);
            recyclerViewMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                        visibleItemCount = mLayoutManagerMessages.getChildCount();
                    totalItemCount = mLayoutManagerMessages.getItemCount();
                    pastVisiblesItems = mLayoutManagerMessages.findFirstVisibleItemPosition();

                    if (loading) {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount - 5) {
                            loading = false;
                            loadNextDataFromApi();
                        }
                    }
                }
            });
        }

        private void loadNextDataFromApi() {
            listenerEndListMessage.loadMoreMessage();
        }

        public void onBindHeaderMessage(List<MessageMeeting> messageMeetings) {
            this.refreshContent(messageMeetings);
            this.messagesAmount.setText(context.getString(R.string.coments_count_text, messageAmount));
        }

        private void refreshContent(List<MessageMeeting> messageMeetings) {
            this.messageMeetingAdapter.clearAll();
            this.messageMeetingAdapter.setList(messageMeetings);
            this.messageMeetingAdapter.notifyDataSetChanged();
        }
    }

    public interface OnListenerEndListMessage{
        void loadMoreMessage();
    }


}
