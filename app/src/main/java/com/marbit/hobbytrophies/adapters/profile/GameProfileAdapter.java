package com.marbit.hobbytrophies.adapters.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marbit.hobbytrophies.GameDetailProfileActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Game> gameList;
    private Context context;
    private User user;

    public GameProfileAdapter(Context context) {
        this.gameList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.list_item_game_profile, parent, false);
        viewHolder = new GameViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        GameViewHolder gameViewHolder = (GameViewHolder) holder;
        Game game = gameList.get(position);
        gameViewHolder.bindGame(game);
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public void setUserProfile(User user) {
        Collections.sort(user.getGamesList());
        this.gameList.addAll(user.getGamesList());
        this.user = user;
    }

    public void clearAll() {
        this.gameList.clear();
    }

    private class GameViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView name;
        private ImageView imageGame;
        private TextView platinumAmount;
        private TextView goldenAmount;
        private TextView silverAmount;
        private TextView bronzeAmount;
        private TextView imageVita;
        private TextView imagePs3;
        private TextView imagePs4;
        private ProgressBar progressBarGame;
        private TextView percentProgressGame;

        public GameViewHolder(View itemView) {
            super(itemView);
            this.view = (RelativeLayout) itemView.findViewById(R.id.layout_row_profile_game);
            this.name = (TextView) itemView.findViewById(R.id.text_view_name_game);
            this.imageGame = (ImageView) itemView.findViewById(R.id.image_game);
            this.platinumAmount = (TextView) itemView.findViewById(R.id.text_view_amount_platinum);
            this.goldenAmount = (TextView) itemView.findViewById(R.id.text_view_amount_gold_medal);
            this.silverAmount = (TextView) itemView.findViewById(R.id.text_view_amount_silver_medal);
            this.bronzeAmount = (TextView) itemView.findViewById(R.id.text_view_amount_bronze_medal);

            this.imagePs3 = (TextView) itemView.findViewById(R.id.ic_ps3);
            this.imagePs4 = (TextView) itemView.findViewById(R.id.ic_ps4);
            this.imageVita = (TextView) itemView.findViewById(R.id.ic_vita);
            this.progressBarGame = (ProgressBar) itemView.findViewById(R.id.progress_bar_game_profile);
            this.percentProgressGame = (TextView) itemView.findViewById(R.id.text_view_percent_game_complete);
        }

        public void bindGame(final Game game) {
            this.name.setText(game.getName());
            Picasso.with(context).load(game.getImg()).placeholder(R.drawable.placeholder).into(this.imageGame);
            this.platinumAmount.setText(game.getPlatinumAmount());
            this.goldenAmount.setText(game.getGoldenAmount());
            this.silverAmount.setText(game.getSilverAmount());
            this.bronzeAmount.setText(game.getBronzeAmount());
            this.progressBarGame.setProgress(game.getPercentComplete().intValue());
            this.percentProgressGame.setText(game.getPercentComplete().intValue() + "%");

            this.imagePs4.setVisibility(View.GONE);
            this.imagePs3.setVisibility(View.GONE);
            this.imageVita.setVisibility(View.GONE);

            for(String platform: game.getPlatform()){
                if(platform.equals("psp2")){
                    this.imageVita.setVisibility(View.VISIBLE);
                }else {
                    if(platform.equals("ps3")){
                        this.imagePs3.setVisibility(View.VISIBLE);
                    }else {
                        if(platform.equals("ps4")){
                            this.imagePs4.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent gameIntent = new Intent(view.getContext(), GameDetailProfileActivity.class);
                    gameIntent.putExtra("GAME", game);
                    gameIntent.putExtra("USER", user);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                                new Pair<View, String>(view.findViewById(R.id.image_game),
                                        context.getString(R.string.transition_image_game))
                        );
                        view.getContext().startActivity(gameIntent,options.toBundle());
                    } else {
                        view.getContext().startActivity(gameIntent);
                    }
                }
            });
        }
    }
}
