package com.marbit.hobbytrophies.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> rankingList;
    private Context context;

    public RankingAdapter(Context context) {
        rankingList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.list_item_ranking, parent, false);
        viewHolder = new RankingViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RankingViewHolder rankingViewHolder = (RankingViewHolder) holder;
        User user = rankingList.get(position);
        rankingViewHolder.bindRanking(user, position);
    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    public void setList(List<User> list) {
        this.rankingList.addAll(list);
    }

    public void clearAll() {
        this.rankingList.clear();
    }

    private class RankingViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPosition;
        private ImageView imageUser;
        private TextView userName;;
        private TextView userPoints;
        private RelativeLayout layout;

        public RankingViewHolder(View itemView) {
            super(itemView);
            this.layout = (RelativeLayout) itemView.findViewById(R.id.layout_row_ranking);
            this.textViewPosition = (TextView) itemView.findViewById(R.id.text_view_position);
            this.imageUser = (ImageView) itemView.findViewById(R.id.image_user_ranking);
            this.userName = (TextView) itemView.findViewById(R.id.text_view_user_name_ranking);
            this.userPoints = (TextView) itemView.findViewById(R.id.text_view_points_ranking);
        }

        public void bindRanking(User user, int position) {
            this.textViewPosition.setText(String.valueOf(position + 1));
            Picasso.with(context).load(user.getAvatarUrl()).placeholder(R.drawable.avatar).transform(new CircleTransform()).into(this.imageUser);
            this.userName.setText(user.getPsnId());
            this.userPoints.setText(String.valueOf(user.getPoints()));
            if(user.getPsnId().equals(Preferences.getUserName(context))){
                this.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.background_ranking));
                this.textViewPosition.setTextColor(ContextCompat.getColor(context, R.color.material_white));
                this.userName.setTextColor(ContextCompat.getColor(context, R.color.material_white));
                this.userPoints.setTextColor(ContextCompat.getColor(context, R.color.material_white));
            }
        }
    }
}

