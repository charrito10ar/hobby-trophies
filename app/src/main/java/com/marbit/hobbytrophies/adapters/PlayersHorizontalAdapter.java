package com.marbit.hobbytrophies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.marbit.hobbytrophies.ProfileActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;


public class PlayersHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<User> playerList;
    private Context context;
    private HashSet<Integer> selectedTrophiesList= new HashSet<>();
    private int amountTrophies;

    public PlayersHorizontalAdapter(Context context) {
        this.playerList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemViewPlayer = inflater.inflate(R.layout.list_item_player_meeting, parent, false);
        viewHolder = new PlayerViewHolder(itemViewPlayer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User user = playerList.get(position);
        PlayerViewHolder trophyViewHolder = (PlayerViewHolder) holder;
        trophyViewHolder.onBindPlayer(user);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }
    public void setList(List<User> list) {
        this.playerList.addAll(list);
    }
    public void setAmountTrophies(int amountTrophies){
        this.amountTrophies = amountTrophies;
    }
    public void clearAll() {
        this.playerList.clear();
    }

    public HashSet<Integer> getSelectedTrophiesList() {

        return selectedTrophiesList;
    }


    private class PlayerViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;
        private ImageView imagePlayer;
        private View view;
        private FrameLayout badgeAdmin;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.userName = (TextView) itemView.findViewById(R.id.text_view_name_player);
            this.imagePlayer = (ImageView) itemView.findViewById(R.id.image_player);
            this.badgeAdmin = (FrameLayout) itemView.findViewById(R.id.ic_player_admin);
        }

        public void onBindPlayer(final User user){
            this.userName.setText(String.format(context.getResources().getString(R.string.user_name_in_meeting), user.getPsnId(), user.getTrophiesWonInMeeting(), amountTrophies));
            Picasso.with(context).load(user.getAvatarUrl()).placeholder(R.drawable.ic_placeholder_trophy).fit().transform(new CircleTransform()).into(this.imagePlayer);
            if(user.getRolMeeting() == 0){
                this.badgeAdmin.setVisibility(View.VISIBLE);
            }else {
                this.badgeAdmin.setVisibility(View.GONE);
            }
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("USER-PSN-ID", user.getPsnId());
                    context.startActivity(intent);
                }
            });


        }

    }
}
