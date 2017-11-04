package com.marbit.hobbytrophies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.marbit.hobbytrophies.MeetingDetailActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.fragments.MeetingFragment.MeetingInteractionListener;
import com.marbit.hobbytrophies.model.HeaderListTrophy;
import com.marbit.hobbytrophies.model.Meeting;
import com.marbit.hobbytrophies.model.Trophy;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.marbit.hobbytrophies.utilities.DateUtils;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;



public class MeetingAdapter extends RecyclerView.Adapter{

    private final List<Object> genericList;
    private final MeetingInteractionListener mListener;
    private Context context;

    public MeetingAdapter(MeetingInteractionListener listener, Context context) {
        this.genericList = new ArrayList<>();
        mListener = listener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 0:
                View itemViewMeeting = inflater.inflate(R.layout.list_item_meeting, parent, false);
                viewHolder = new MeetingViewHolder(itemViewMeeting);
                break;
            default:
                View itemViewHeader = inflater.inflate(R.layout.list_item_header_meeting, parent, false);
                viewHolder = new HeaderMeetingViewHolder(itemViewHeader);
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object object = genericList.get(position);
        if(object instanceof Meeting){
            MeetingViewHolder meetingViewHolder = (MeetingViewHolder) holder;
            try {
                meetingViewHolder.bindMeeting((Meeting) object);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            HeaderMeetingViewHolder headerMeetingViewHolder= (HeaderMeetingViewHolder) holder;
            headerMeetingViewHolder.bindHeader(((String)object));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = this.genericList.get(position);
        if (Meeting.class.isInstance(object)) {
            return 0;
        }else {
            return 1;
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


    public class MeetingViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView description;
        private TextView time;
        private TextView textViewAmountPlayers;
        private TextView textViewType;
        private ImageView imageGame;
        private ImageView imageAvatarAuthor;
        private FrameLayout layoutMeetingFinished;


        public MeetingViewHolder(View view) {
            super(view);
            mView = view;
            this.description = (TextView) view.findViewById(R.id.description_meeting);
            this.imageGame = (ImageView) view.findViewById(R.id.meeting_game_img);
            this.time = (TextView) view.findViewById(R.id.meeting_time);
            this.textViewAmountPlayers = (TextView) view.findViewById(R.id.text_view_amount_players);
            this.textViewType = (TextView) view.findViewById(R.id.text_view_type_meeting);
            this.imageAvatarAuthor = (ImageView) view.findViewById(R.id.image_avatar);
            this.layoutMeetingFinished = (FrameLayout) view.findViewById(R.id.layout_meeting_finished);
        }


        public void bindMeeting(final Meeting meeting) throws ParseException {
            this.description.setText(meeting.getDescription());
            this.time.setText(meeting.getTime());
            this.textViewAmountPlayers.setText(meeting.getReserved() + "/" + meeting.getLimitMembers());
            String[] gamesTypeArray = context.getResources().getStringArray(R.array.games_type);
            this.textViewType.setText(gamesTypeArray[meeting.getType()]);

            Picasso.with(context).load("http://hobbytrophies.com/i/j/" + meeting.getGame().getId() + ".png").placeholder(R.drawable.placeholder).into(this.imageGame);
            Picasso.with(context).load(meeting.getUserOwnerAvatar()).placeholder(R.drawable.avatar).transform(new CircleTransform()).into(this.imageAvatarAuthor);

            this.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentMeeting = new Intent(context, MeetingDetailActivity.class);
                    intentMeeting.putExtra("MEETING", meeting);
                    intentMeeting.putExtra("FROM", "LOCAL");
                    context.startActivity(intentMeeting);
                }
            });

            if(DateUtils.getInstance().meetingIsFinish(meeting.getDate())){
                this.layoutMeetingFinished.setVisibility(View.VISIBLE);
            }else {
                this.layoutMeetingFinished.setVisibility(View.GONE);
            }
        }
    }


    private class HeaderMeetingViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private View view;

        public HeaderMeetingViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.textViewDate = (TextView) itemView.findViewById(R.id.text_view_header_meeting_date);

        }

        public void bindHeader(String date) {
            this.textViewDate.setText(date);
        }
    }


}
