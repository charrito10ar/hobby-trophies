package com.marbit.hobbytrophies.adapters;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.TrophyDetailActivity;
import com.marbit.hobbytrophies.model.HeaderListTrophy;
import com.marbit.hobbytrophies.model.Trophy;
import com.marbit.hobbytrophies.utilities.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrophyProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> genericList;
    private Context context;
    private final String gameId;

    public TrophyProfileAdapter(Context context, String gameId) {
        this.genericList = new ArrayList<>();
        this.context = context;
        this.gameId = gameId;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 0:
                View itemViewTrophy = inflater.inflate(R.layout.list_item_trophy_profile, parent, false);
                viewHolder = new TrophyViewHolder(itemViewTrophy);
                break;
            default:
                View itemViewHeader = inflater.inflate(R.layout.list_item_header_trophy, parent, false);
                viewHolder = new TrophyHeaderViewHolder(itemViewHeader);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Object object = genericList.get(position);
        if(object instanceof Trophy){
            TrophyViewHolder trophyViewHolder = (TrophyViewHolder) holder;
            trophyViewHolder.bindTrophy((Trophy)object);
        } else {
            TrophyHeaderViewHolder trophyHeaderViewHolder= (TrophyHeaderViewHolder) holder;
            trophyHeaderViewHolder.onBindHeader((HeaderListTrophy)object);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = this.genericList.get(position);
        if (Trophy.class.isInstance(object)) {
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

    private class TrophyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView title;
        private ImageView imageTrophyType;
        private TextView description;
        private ImageView imageTrophy;
        private String trophyType;
        private TextView dateGet;
        private View layoutDisable;


        public TrophyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.title = (TextView) itemView.findViewById(R.id.text_title_trophy);
            this.imageTrophyType = (ImageView) itemView.findViewById(R.id.ic_medal_type);
            this.description = (TextView) itemView.findViewById(R.id.text_description_trophy);
            this.imageTrophy = (ImageView) itemView.findViewById(R.id.ic_trophy);
            this.dateGet = (TextView) itemView.findViewById(R.id.text_view_date_get);
            this.layoutDisable = (RelativeLayout) itemView.findViewById(R.id.layout_disable);
        }

        public void bindTrophy(final Trophy trophy) {

            Picasso.with(context).load(trophy.getImg()).placeholder(R.drawable.ic_placeholder_trophy).into(this.imageTrophy);
            this.title.setText(trophy.getTitle());
            this.description.setText(trophy.getDescription());
            this.setTrophyType(trophy.getType());
            if(trophy.getDateGet().equals("null")){
                this.layoutDisable.setVisibility(View.VISIBLE);
                this.dateGet.setText("Trofeo no conseguido");
            }else {
                this.layoutDisable.setVisibility(View.GONE);
                this.dateGet.setText("Fecha de obtención: " + trophy.getDateGet());
            }

            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), TrophyDetailActivity.class);

                    intent.putExtra("TROPHY", trophy);
                    intent.putExtra("GAME-ID", gameId);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                                new Pair<View, String>(view.findViewById(R.id.ic_trophy),
                                        context.getString(R.string.transition_image_trophy)),
                                new Pair<View, String>(view.findViewById(R.id.text_title_trophy),
                                        context.getString(R.string.transition_title_trophy))
                        );

                        view.getContext().startActivity(intent, options.toBundle());

                    } else {
                        view.getContext().startActivity(intent);
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

    private class TrophyHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView nameDlc;
        private ImageView imageHeader;

        public TrophyHeaderViewHolder(View itemView) {
            super(itemView);
            this.nameDlc = (TextView) itemView.findViewById(R.id.text_view_dlc_name);
            this.imageHeader = (ImageView) itemView.findViewById(R.id.image_dlc);
        }

        public void onBindHeader(HeaderListTrophy headerListTrophy){
            this.nameDlc.setText(context.getString(R.string.title_header_trophy_dlc, headerListTrophy.getName()));
            Picasso.with(context).load(headerListTrophy.getUrl()).placeholder(R.drawable.placeholder).fit().into(this.imageHeader);
        }
    }
}
