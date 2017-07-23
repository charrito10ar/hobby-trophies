package com.marbit.hobbytrophies.market;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.marbit.hobbytrophies.EditItemActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.ImagesItemPagerAdapter;
import com.marbit.hobbytrophies.chat.ChatActivity;
import com.marbit.hobbytrophies.interfaces.market.ItemDetailActivityView;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.presenters.market.ItemDetailActivityPresenter;
import com.marbit.hobbytrophies.utilities.Preferences;

public class ItemDetailActivity extends AppCompatActivity implements ItemDetailActivityView{
    private Item item;
    private ViewPager viewPager;
    private ImagesItemPagerAdapter imagesItemPageAdapter;
    private TextView price;
    private TextView title;
    private TextView description;
    private TextView textViewBarter;
    private Menu menu;
    private FrameLayout labelSold;
    private Button buttonChat;
    private ItemDetailActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        this.presenter = new ItemDetailActivityPresenter(getApplicationContext(), this);
        this.item = getIntent().getParcelableExtra("ITEM");

        this.viewPager = (ViewPager) findViewById(R.id.pager_images_item);
        this.imagesItemPageAdapter = new ImagesItemPagerAdapter(getApplicationContext(),item.getId(),  item.getImageAmount());
        this.viewPager.setAdapter(this.imagesItemPageAdapter);
        this.textViewBarter = (TextView) findViewById(R.id.text_view_barter);
        this.labelSold = (FrameLayout) findViewById(R.id.label_sold);
        this.buttonChat = (Button) findViewById(R.id.button_chat);

        this.price = (TextView) findViewById(R.id.item_detail_price);
        this.price.setText(item.getPrice() + " €");
        this.title = (TextView) findViewById(R.id.text_view_item_detail_title);
        this.title.setText(item.getTitle());
        this.description = (TextView) findViewById(R.id.text_view_item_detail_description);
        this.description.setText(item.getDescription());
        if(!item.isBarter()) this.textViewBarter.setVisibility(View.INVISIBLE);
        this.setButtonChat();
    }

    private void setButtonChat() { // Si no soy el dueño y esta aún en venta, muestro el boton de chat.
        if(!Preferences.getUserName(getApplicationContext()).equals(item.getUserId()) && item.getStatus() == 0){
            showButtonChat();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_detail, menu);
        this.menu = menu;
        if(!item.getUserId().equals(Preferences.getUserName(getApplication()))){
            this.menu.clear();
        }
        this.setStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit_item:
                Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                intent.putExtra("ITEM", this.item);
                startActivity(intent);
                return true;
            case R.id.action_sold:
                this.presenter.markAsSold(this.item);
                return true;
            case R.id.action_unmark_sold:
                this.presenter.unmarkAsSold(this.item);
                return true;
            case R.id.action_delete:
                this.presenter.delete(this.item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openChat(View view) {
        Intent intentChat = new Intent(getApplicationContext(), ChatActivity.class);
        intentChat.putExtra(ChatActivity.PARAM_ITEM_ID, item.getId());
        intentChat.putExtra(ChatActivity.PARAM_SELLER, item.getUserId());
        intentChat.putExtra(ChatActivity.PARAM_BUYER, Preferences.getUserName(getApplicationContext()));
        startActivity(intentChat);
    }

    @Override
    public void markAsSold() {
        this.menu.getItem(1).setVisible(false);
        this.menu.getItem(2).setVisible(true);
        this.labelSold.setVisibility(View.VISIBLE);
    }

    @Override
    public void unmarkAsSold() {
        this.menu.getItem(1).setVisible(true);
        this.menu.getItem(2).setVisible(false);
        this.labelSold.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setStatus() {
        if(item.getStatus() == 1){
            markAsSold();
        }
    }

    @Override
    public void deleteItemSuccess() {
        finish();
    }

    @Override
    public void hideButtonChat() {
        this.buttonChat.setVisibility(View.GONE);
    }

    @Override
    public void showButtonChat() {
        this.buttonChat.setVisibility(View.VISIBLE);
    }
}
