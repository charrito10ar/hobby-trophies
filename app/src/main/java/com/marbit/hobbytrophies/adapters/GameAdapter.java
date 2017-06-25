package com.marbit.hobbytrophies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marbit.hobbytrophies.GameDetailActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Game> gameList;
    private Context context;

    public GameAdapter(Context context) {
        this.gameList = new ArrayList<>();
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.list_item_game, parent, false);
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

    public void setList(List<Game> list) {
        this.gameList.addAll(list);
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

        public GameViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.name = (TextView) itemView.findViewById(R.id.text_view_name_game);
            this.imageGame = (ImageView) itemView.findViewById(R.id.image_game);
            this.platinumAmount = (TextView) itemView.findViewById(R.id.text_view_amount_platinum);
            this.goldenAmount = (TextView) itemView.findViewById(R.id.text_view_amount_gold_medal);
            this.silverAmount = (TextView) itemView.findViewById(R.id.text_view_amount_silver_medal);
            this.bronzeAmount = (TextView) itemView.findViewById(R.id.text_view_amount_bronze_medal);
            this.imagePs3 = (TextView) itemView.findViewById(R.id.ic_ps3);
            this.imagePs4 = (TextView) itemView.findViewById(R.id.ic_ps4);
            this.imageVita = (TextView) itemView.findViewById(R.id.ic_vita);
        }

        public void bindGame(final Game game) {
            this.name.setText(game.getName());
            Picasso.with(context).load(game.getImg()).placeholder(R.drawable.placeholder).into(this.imageGame);
            this.platinumAmount.setText(game.getPlatinumAmount());
            this.goldenAmount.setText(game.getGoldenAmount());
            this.silverAmount.setText(game.getSilverAmount());
            this.bronzeAmount.setText(game.getBronzeAmount());

            this.imagePs4.setVisibility(View.GONE);
            this.imagePs3.setVisibility(View.GONE);
            this.imageVita.setVisibility(View.GONE);

            for(String platform: game.getPlatform()){
                if(platform.equals("vita")){
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
                    Intent gameIntent = new Intent(view.getContext(), GameDetailActivity.class);
                    gameIntent.putExtra("GAME", game);
                    view.getContext().startActivity(gameIntent);
                }
            });
        }
    }
}
