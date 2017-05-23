package com.marbit.hobbytrophies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.Trophy;
import com.marbit.hobbytrophies.utilities.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class TrophyMeetingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Trophy> trophyList;
    private Context context;
    private HashSet<Integer> selectedTrophiesList= new HashSet<>();
    private boolean isToEdit;

    public TrophyMeetingAdapter(Context context, boolean isToEdit) {
        this.trophyList = new ArrayList<>();
        this.context = context;
        this.isToEdit = isToEdit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemViewTrophy = inflater.inflate(R.layout.list_item_trophy_meeting, parent, false);
        viewHolder = new TrophyViewHolder(itemViewTrophy);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Trophy trophy = trophyList.get(position);
        TrophyViewHolder trophyViewHolder = (TrophyViewHolder) holder;
        trophyViewHolder.onBindTrophy(trophy);
    }

    @Override
    public int getItemCount() {
        return trophyList.size();
    }
    public void setList(List<Trophy> list) {
        this.trophyList.addAll(list);
    }

    public void clearAll() {
        this.trophyList.clear();
    }

    public HashSet<Integer> getSelectedTrophiesList() {

        return selectedTrophiesList;
    }

    public void setSelectedTrophiesList(List<Trophy> list) {
        for(int i = 0; i < list.size(); i++){
            this.selectedTrophiesList.add(list.get(i).getId());
        }
    }


    private class TrophyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private ImageView imageTrophy;
        private ImageView imageTrophyType;
        private CheckBox checkBox;

        public TrophyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.text_title_trophy);
            this.description = (TextView) itemView.findViewById(R.id.text_description_trophy);
            this.imageTrophy = (ImageView) itemView.findViewById(R.id.ic_trophy);
            this.imageTrophyType = (ImageView) itemView.findViewById(R.id.ic_medal_type);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_trophy_meeting);
        }

        public void onBindTrophy(final Trophy trophy){
            this.title.setText(trophy.getTitle());
            this.description.setText(trophy.getDescription());
            Picasso.with(context).load(trophy.getImg()).placeholder(R.drawable.ic_placeholder_trophy).fit().into(this.imageTrophy);
            this.setTrophyType(trophy.getType());

            if(selectedTrophiesList.contains(trophy.getId())) {
                this.checkBox.setChecked(true);
            }else {
                this.checkBox.setChecked(false);
            }

            if(!isToEdit)
                this.checkBox.setVisibility(View.GONE);

            this.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkBox.isChecked()) {
                        checkBox.setChecked(true);
                        selectedTrophiesList.add(trophy.getId());
                    }else {
                        checkBox.setChecked(false);
                        selectedTrophiesList.remove(trophy.getId());
                    }
                }
            });
        }

        public void setTrophyType(String trophyType) {
            switch (trophyType){
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
}
