package com.marbit.hobbytrophies.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marbit.hobbytrophies.R;

public class ImagesItemPagerAdapter extends PagerAdapter {

    private final int amount;
    private final String itemId;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = firebaseStorage.getReference();

    public ImagesItemPagerAdapter(Context context, String itemId,  int amount) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemId = itemId;
        this.amount = amount;
    }

    @Override
    public int getCount() {
        return amount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item_image, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_item_detail);
        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(storageRef.child("/item-images/" + itemId + "/image_0" + position +".jpg"))
                .centerCrop()
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}