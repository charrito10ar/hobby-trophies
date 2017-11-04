package com.marbit.hobbytrophies.wishes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.profile.ItemProfileSalesAdapter;
import com.marbit.hobbytrophies.market.ItemDetailActivity;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.wishes.interfaces.DetailWishActivityView;
import com.marbit.hobbytrophies.wishes.model.Wish;
import com.marbit.hobbytrophies.wishes.presenters.DetailWishActivityPresenter;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PS;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PS2;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PS3;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PS4;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_PSP;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_VITA;
import static com.marbit.hobbytrophies.utilities.Constants.PREFERENCE_CONSOLE_VR;

public class DetailWishActivity extends AppCompatActivity implements DetailWishActivityView, ItemProfileSalesAdapter.ItemDetailAdapterListener {
    private Wish wish;
    private DetailWishActivityPresenter presenter;
    private ImageView imageViewWish;
    private TextView textViewTitle;
    private TextView textViewPrice;
    private ItemProfileSalesAdapter itemProfileSalesAdapter;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private RelativeLayout layoutEmptyItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.wish = getIntent().getExtras().getParcelable("WISH");
        this.presenter = new DetailWishActivityPresenter(getApplicationContext(), this);
        setContentView(R.layout.activity_detail_wish);
        this.layoutEmptyItems = (RelativeLayout) findViewById(R.id.layout_empty_items);
        this.imageViewWish = (ImageView) findViewById(R.id.image_wish_detail);
        this.textViewTitle = (TextView) findViewById(R.id.text_view_title_wish);
        this.textViewPrice = (TextView) findViewById(R.id.text_view_price_wish);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_view_items_of_wish);
        this.itemProfileSalesAdapter = new ItemProfileSalesAdapter(getApplicationContext(), this);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemProfileSalesAdapter);
        this.initWish(wish);
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.loadItemsByWish(wish);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete_wish:
                presenter.deleteWish(wish);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wish_detail, menu);
        return true;
    }

    @Override
    public void initWish(Wish wish) {
        this.textViewTitle.setText(wish.getName());
        this.textViewPrice.setText("Precio: " + wish.getMinPrice() + " - " + wish.getMaxPrice() + " euros");

        if(wish.getItemType() == Constants.PREFERENCE_ITEM_CATEGORY_GAME){
            Picasso.with(getApplicationContext()).load("http://hobbytrophies.com/i/j/" + wish.getGameId() + ".png").placeholder(R.drawable.avatar).into(this.imageViewWish);
        }else {
            switch (wish.getConsoleId()){
                case PREFERENCE_CONSOLE_PS:
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_ps1).placeholder(R.drawable.avatar).into(this.imageViewWish);
                    break;
                case PREFERENCE_CONSOLE_PSP:
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_psp).placeholder(R.drawable.avatar).into(this.imageViewWish);
                    break;
                case PREFERENCE_CONSOLE_PS2:
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_ps2).placeholder(R.drawable.avatar).into(this.imageViewWish);
                    break;
                case PREFERENCE_CONSOLE_PS3:
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_ps3).placeholder(R.drawable.avatar).into(this.imageViewWish);
                    break;
                case PREFERENCE_CONSOLE_PS4:
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_ps4).placeholder(R.drawable.avatar).into(this.imageViewWish);
                    break;
                case PREFERENCE_CONSOLE_VITA:
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_psvita).placeholder(R.drawable.avatar).into(this.imageViewWish);
                    break;
                case PREFERENCE_CONSOLE_VR:
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_vr).placeholder(R.drawable.avatar).into(this.imageViewWish);
                    break;
            }
        }
    }

    @Override
    public void loadItemsByWishSuccessful(List<Item> itemList) {
        itemProfileSalesAdapter.clearAll();
        itemProfileSalesAdapter.setItemList(itemList);
        itemProfileSalesAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteWishSuccessful() {
        finish();
    }

    @Override
    public void showEmptyLayout() {
        layoutEmptyItems.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyLayout() {
        layoutEmptyItems.setVisibility(View.GONE);
    }

    @Override
    public void openDetailActivity(Item item, String from) {
        Intent itemIntent = new Intent(getApplicationContext(), ItemDetailActivity.class);
        itemIntent.putExtra("ITEM", item);
        itemIntent.putExtra("FROM", from);
        startActivity(itemIntent);
    }
}
