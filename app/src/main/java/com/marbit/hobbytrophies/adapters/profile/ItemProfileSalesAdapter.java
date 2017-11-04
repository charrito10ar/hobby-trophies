package com.marbit.hobbytrophies.adapters.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.model.market.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemProfileSalesAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> itemList;
    private Context context;
    private ItemDetailAdapterListener mListener;

    public ItemProfileSalesAdapter(Context context, ItemDetailAdapterListener mListener) {
        this.context = context;
        this.itemList = new ArrayList<>();
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.list_item_profile_sales, parent, false);
        viewHolder = new ItemViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Item item = itemList.get(position);
        itemViewHolder.bindItem(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemList(List<Item> itemList) {
        this.itemList.addAll(itemList);
    }

    public void clearAll() {
        this.itemList.clear();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView titleItem;
        private TextView descriptionItem;
        private TextView priceItem;
        private ImageView mainImage;
        private String image;
        private TextView soldLabel;
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();

        private ItemViewHolder(View itemView) {
            super(itemView);
            this.soldLabel = (TextView) itemView.findViewById(R.id.sold_label);
            this.titleItem = (TextView) itemView.findViewById(R.id.text_view_item_title);
            this.descriptionItem = (TextView) itemView.findViewById(R.id.text_view_item_description);
            this.priceItem = (TextView) itemView.findViewById(R.id.text_view_item_price);
            this.mainImage = (ImageView) itemView.findViewById(R.id.item_main_image);
        }

        public void bindItem(final Item item) {
            this.titleItem.setText(item.getTitle());
            this.descriptionItem.setText(item.getDescription());
            this.priceItem.setText(item.getPrice() + " €");
            this.setImage(item.getId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.openDetailActivity(item, "LOCAL");
                }
            });
            if(item.getStatus() == 1){
                soldLabel.setVisibility(View.VISIBLE);
            }else {
                soldLabel.setVisibility(View.GONE);
            }
        }

        private void setImage(String id) {
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(storageRef.child("/item-images/" + id + "/image_00.jpg"))
                    .centerCrop()
                    .into(mainImage);
        }
    }

    public interface ItemDetailAdapterListener{
        void openDetailActivity(Item item, String from);
    }
}
