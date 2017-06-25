package com.marbit.hobbytrophies.market;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.ImagesItemPagerAdapter;
import com.marbit.hobbytrophies.model.market.Item;

public class ItemDetailActivity extends AppCompatActivity {
    private Item item;
    private ViewPager viewPager;
    private ImagesItemPagerAdapter imagesItemPageAdapter;
    private TextView price;
    private TextView title;
    private TextView description;
    private TextView textViewBarter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        this.item = getIntent().getParcelableExtra("ITEM");

        this.viewPager = (ViewPager) findViewById(R.id.pager_images_item);
        this.imagesItemPageAdapter = new ImagesItemPagerAdapter(getApplicationContext(),item.getId(),  item.getImageAmount());
        this.viewPager.setAdapter(this.imagesItemPageAdapter);
        this.textViewBarter = (TextView) findViewById(R.id.text_view_barter);

        this.price = (TextView) findViewById(R.id.item_detail_price);
        this.price.setText(item.getPrice() + " €");
        this.title = (TextView) findViewById(R.id.text_view_item_detail_title);
        this.title.setText(item.getDescription());
        this.description = (TextView) findViewById(R.id.text_view_item_detail_description);
        this.description.setText(item.getDescription());
        if(!item.isBarter()) this.textViewBarter.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
