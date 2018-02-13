package com.marbit.hobbytrophies.market.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.market.model.Rate;
import com.marbit.hobbytrophies.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RatesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Rate> ratesList;
    private Context context;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = firebaseStorage.getReference();

    public RatesAdapter(Context context) {
        ratesList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item_rate, parent, false);
        viewHolder = new RateViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RateViewHolder rankingViewHolder = (RateViewHolder) holder;
        Rate rate = ratesList.get(position);
        rankingViewHolder.bindRate(rate);
    }

    @Override
    public int getItemCount() {
        return ratesList.size();
    }

    public void setList(List<Rate> list) {
        this.ratesList.addAll(list);
    }

    public void clearAll() {
        this.ratesList.clear();
    }

    private class RateViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageItem;
        private TextView rateType;
        private TextView comment;
        private TextView date;
        private RatingBar ratingBar;

        public RateViewHolder(View itemView) {
            super(itemView);
            this.imageItem = itemView.findViewById(R.id.image_item);
            this.rateType = itemView.findViewById(R.id.text_view_rate_type);
            this.comment = itemView.findViewById(R.id.text_view_comment);
            this.date = itemView.findViewById(R.id.text_view_date);
            this.ratingBar = itemView.findViewById(R.id.rating_bar);
        }

        public void bindRate(Rate rate) {
            if(rate.getType().equals(Constants.BUY_TYPE)){
                rateType.setText("Compraste");
            }else {
                rateType.setText("Vendiste");
            }
            comment.setText(rate.getComment());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = formatter.format(new Date(rate.getCreatedTimestampLong()));
            date.setText(dateString);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(storageRef.child("/item-images/" + rate.getItemId() + "/image_00.jpg"))
                    .placeholder(R.drawable.placeholder_item)
                    .centerCrop()
                    .into(imageItem);
            this.ratingBar.setRating(rate.getValue());
        }
    }


}