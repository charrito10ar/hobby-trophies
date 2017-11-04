package com.marbit.hobbytrophies.wishes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.wishes.DetailWishActivity;
import com.marbit.hobbytrophies.wishes.model.Wish;
import com.marbit.hobbytrophies.utilities.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PS;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PS2;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PS3;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PS4;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PSP;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_VITA;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_VR;


public class WishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Wish> wishList;
    private Context context;

    public WishAdapter(Context context) {
        wishList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.list_item_wish, parent, false);
        viewHolder = new WishViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WishViewHolder rankingViewHolder = (WishViewHolder) holder;
        Wish wish = wishList.get(position);
        rankingViewHolder.bindWish(wish);
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

    public void setList(List<Wish> list) {
        this.wishList.addAll(list);
    }

    public void clearAll() {
        this.wishList.clear();
    }

    private class WishViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNameWish;
        private TextView textViewPrice;
        private TextView textViewBarter;
        private TextView textViewDigital;
        private ImageView imageWish;


        public WishViewHolder(View itemView) {
            super(itemView);
            imageWish = (ImageView) itemView.findViewById(R.id.ic_wish);
            textViewPrice = (TextView) itemView.findViewById(R.id.text_view_range_price);
            textViewNameWish = (TextView) itemView.findViewById(R.id.text_view_name_wish);
            textViewBarter = (TextView) itemView.findViewById(R.id.text_view_barter);
            textViewDigital = (TextView) itemView.findViewById(R.id.text_view_digital);
        }

        public void bindWish(final Wish wish) {
            this.textViewNameWish.setText(wish.getName());
            textViewPrice.setText("Precio: " + wish.getMinPrice() + " - " + wish.getMaxPrice() + " euros");
            if(wish.isBarter()){
                textViewBarter.setVisibility(View.VISIBLE);
            }else {
                textViewBarter.setVisibility(View.GONE);
            }
            if(wish.isDigital()){
                textViewDigital.setVisibility(View.VISIBLE);
            }else{
                textViewDigital.setVisibility(View.GONE);
            }
            if(wish.getItemType() == Constants.PREFERENCE_ITEM_CATEGORY_GAME){
                Picasso.with(context).load("http://hobbytrophies.com/i/j/" + wish.getGameId() + ".png").placeholder(R.drawable.avatar).into(this.imageWish);
            }else {
                switch (wish.getConsoleId()){
                    case PREFERENCE_CONSOLE_PS:
                        Picasso.with(context).load(R.drawable.ic_ps1).placeholder(R.drawable.avatar).into(this.imageWish);
                        break;
                    case PREFERENCE_CONSOLE_PSP:
                        Picasso.with(context).load(R.drawable.ic_psp).placeholder(R.drawable.avatar).into(this.imageWish);
                        break;
                    case PREFERENCE_CONSOLE_PS2:
                        Picasso.with(context).load(R.drawable.ic_ps2).placeholder(R.drawable.avatar).into(this.imageWish);
                        break;
                    case PREFERENCE_CONSOLE_PS3:
                        Picasso.with(context).load(R.drawable.ic_ps3).placeholder(R.drawable.avatar).into(this.imageWish);
                        break;
                    case PREFERENCE_CONSOLE_PS4:
                        Picasso.with(context).load(R.drawable.ic_ps4).placeholder(R.drawable.avatar).into(this.imageWish);
                        break;
                    case PREFERENCE_CONSOLE_VITA:
                        Picasso.with(context).load(R.drawable.ic_psvita).placeholder(R.drawable.avatar).into(this.imageWish);
                        break;
                    case PREFERENCE_CONSOLE_VR:
                        Picasso.with(context).load(R.drawable.ic_vr).placeholder(R.drawable.avatar).into(this.imageWish);
                        break;
                }
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailWishActivity.class);
                    intent.putExtra("WISH", wish);
                    context.startActivity(intent);
                }
            });
        }
    }
}


