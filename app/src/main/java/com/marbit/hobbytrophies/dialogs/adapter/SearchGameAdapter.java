package com.marbit.hobbytrophies.dialogs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Game> gameList;
    private Context context;
    private SearchGameAdapterListener mListener;

    public SearchGameAdapter(Context context, SearchGameAdapterListener dialogSearchGame) {
        this.gameList = new ArrayList<>();
        this.context = context;
        this.mListener = dialogSearchGame;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.list_item_search_game, parent, false);
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
        private TextView platform;

        public GameViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.name = (TextView) itemView.findViewById(R.id.text_view_name_game);
            this.imageGame = (ImageView) itemView.findViewById(R.id.image_game);
            this.platform = (TextView) itemView.findViewById(R.id.ic_platform);
        }

        public void bindGame(final Game game) {
            this.name.setText(game.getName());
            Picasso.with(context).load(game.getImg()).placeholder(R.drawable.placeholder).into(this.imageGame);

            for(String platform: game.getPlatform()){
                if(platform.equals("vita")){
                    this.platform.setText("VITA");
                }else {
                    if(platform.equals("ps3")){
                        this.platform.setText("PS3");
                    }else {
                        if(platform.equals("ps4")){
                            this.platform.setText("PS4");
                        }
                    }
                }
            }
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.selectGame(game);
                }
            });
        }
    }

    public interface SearchGameAdapterListener{
        void selectGame(Game game);
    }
}
