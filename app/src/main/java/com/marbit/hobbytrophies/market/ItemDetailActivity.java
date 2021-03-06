package com.marbit.hobbytrophies.market;

import android.app.Activity;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.ImagesItemPagerAdapter;
import com.marbit.hobbytrophies.chat.ChatActivity;
import com.marbit.hobbytrophies.dialogs.DialogAlertLogin;
import com.marbit.hobbytrophies.interfaces.market.ItemDetailActivityView;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.presenters.market.ItemDetailActivityPresenter;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.marbit.hobbytrophies.utilities.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailActivity extends AppCompatActivity implements ItemDetailActivityView {
    private static final int REQUEST_CODE_EDIT_ITEM = 11;
    private static final int REQUEST_CODE_MARK_AS_SOLD = 99;
    private Item item;
    @BindView(R.id.pager_images_item) ViewPager viewPager;
    private ImagesItemPagerAdapter imagesItemPageAdapter;
    @BindView(R.id.item_detail_price) TextView price;
    @BindView(R.id.text_view_item_detail_title) TextView title;
    @BindView(R.id.text_view_item_detail_description) TextView description;
    @BindView(R.id.text_view_barter) TextView textViewBarter;
    private Menu menu;
    @BindView(R.id.label_sold) FrameLayout labelSold;
    @BindView(R.id.button_chat) Button buttonChat;
    private ItemDetailActivityPresenter presenter;
    private String from;
    @BindView(R.id.map_view) MapView mMapView;
    private LatLng LOCATION_ITEM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");
        this.presenter = new ItemDetailActivityPresenter(getApplicationContext(), this);
        this.from = getIntent().getStringExtra("FROM");
        if(from != null)
            switch (this.from){
                case "LOCAL":
                    this.item = getIntent().getParcelableExtra("ITEM");
                    loadRemoteItemSuccess(this.item);
                    break;
                case "DEEP-LINK":
                    presenter.loadRemoteItem(getIntent().getStringExtra("itemId"));
                    break;
            }
        mMapView.onCreate(Bundle.EMPTY);
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_CODE_EDIT_ITEM: {
                if (resultCode == Activity.RESULT_OK) {
                    Item item = data.getParcelableExtra("ITEM");
                    loadRemoteItemSuccess(item);
                }
                break;
            }
            case REQUEST_CODE_MARK_AS_SOLD:
                if (resultCode == Activity.RESULT_OK) {
                    markAsSold();
                }else {
                    Item item = data.getParcelableExtra("ITEM");
                    loadRemoteItemSuccess(item);
                }
                break;
        }
    }

    private void setButtonChat() { // Si no soy el dueño y esta aún en venta, muestro el boton de chat.
        if(!Preferences.getUserId(getApplicationContext()).equals(item.getUserId()) && item.getStatus() == 0){
            showButtonChat();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_detail, menu);
        this.menu = menu;
        if(item != null){
            if(!item.getUserId().equals(Preferences.getUserId(getApplication()))){
                this.menu.findItem(R.id.action_edit_item).setVisible(false);
                this.menu.findItem(R.id.action_sold).setVisible(false);
                this.menu.findItem(R.id.action_delete).setVisible(false);
            }else {
                this.menu.findItem(R.id.action_edit_item).setVisible(true);
                this.menu.findItem(R.id.action_sold).setVisible(true);
                this.menu.findItem(R.id.action_delete).setVisible(true);
            }
            this.setFavourite();
            this.setStatus();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_favourite:
                this.presenter.addFavourite(this.item);
                break;
            case R.id.action_delete_favourite:
                this.presenter.deleteFavourite(this.item);
                break;
            case R.id.action_edit_item:
                Intent intent = new Intent(getApplicationContext(), EditItemActivity.class);
                intent.putExtra("ITEM", this.item);
                startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);
                return true;
            case R.id.action_sold:
                this.presenter.markAsSold(this.item);
                return true;
            case R.id.action_delete:
                this.presenter.delete(this.item);
                return true;
            case R.id.action_share_item:
                this.presenter.shareItem(this.item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openChat(View view) {
        if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN)){
            Intent intentChat = new Intent(getApplicationContext(), ChatActivity.class);
            intentChat.putExtra(ChatActivity.PARAM_ITEM_ID, item.getId());
            intentChat.putExtra(ChatActivity.PARAM_SELLER, item.getUserId());
            intentChat.putExtra(ChatActivity.PARAM_SELLER_NAME, item.getUserName());
            intentChat.putExtra(ChatActivity.PARAM_ITEM_TITLE, item.getTitle());
            intentChat.putExtra(ChatActivity.PARAM_BUYER, Preferences.getUserId(getApplicationContext()));
            intentChat.putExtra(ChatActivity.PARAM_BUYER_NAME, Preferences.getUserName(getApplicationContext()));
            startActivity(intentChat);
        }else {
            DialogAlertLogin dialogAlertLogin = DialogAlertLogin.newInstance();
            dialogAlertLogin.show(getSupportFragmentManager(), "dialogAlertLogin");
        }
    }

    @Override
    public void markAsSold() {
        this.menu.findItem(R.id.action_sold).setVisible(false);
        this.menu.findItem(R.id.action_edit_item).setVisible(false);
        this.menu.findItem(R.id.action_delete).setVisible(false);
        this.labelSold.setVisibility(View.VISIBLE);
    }


    @Override
    public void markFavourite() {
        this.menu.findItem(R.id.action_add_favourite).setVisible(false);
        this.menu.findItem(R.id.action_delete_favourite).setVisible(true);
    }

    @Override
    public void unmarkFavourite() {
        this.menu.findItem(R.id.action_add_favourite).setVisible(true);
        this.menu.findItem(R.id.action_delete_favourite).setVisible(false);
    }

    @Override
    public void setStatus() {
        if(item.getUserId().equals(Preferences.getUserId(getApplication()))) {
            if(item.getStatus() == 1){
                markAsSold();
            }
        }
    }

    @Override
    public void setFavourite() {
        if(!item.getUserId().equals(Preferences.getUserId(getApplication()))) {
            if (Utilities.isFavorite(getApplicationContext(), item)) {
                markFavourite();
            } else {
                unmarkFavourite();
            }
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

    @Override
    public void shareItemLink(Item item, String longDynamicLink) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String itemTypeString = Utilities.getItemTypeString(item.getItemCategory());
        sendIntent.putExtra(Intent.EXTRA_TEXT, itemTypeString + longDynamicLink );
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Elija aplicación"));
    }

    @Override
    public void loadRemoteItemError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadRemoteItemSuccess(Item item) {
        this.item = item;
        invalidateOptionsMenu();
        this.imagesItemPageAdapter = new ImagesItemPagerAdapter(getApplicationContext(), item.getId(),  item.getImageAmount());
        this.viewPager.setAdapter(this.imagesItemPageAdapter);
        this.price.setText(item.getPrice() + " €");
        this.title.setText(item.getTitle());
        this.description.setText(item.getDescription());
        if(!item.isBarter()) this.textViewBarter.setVisibility(View.INVISIBLE);
        this.setButtonChat();
        this.setLocation();
    }

    @Override
    public void openItemSoldActivity() {
        Intent intent = new Intent(getApplicationContext(), ItemSoldActivity.class);
        intent.putExtra("ITEM", item);
        startActivityForResult(intent, REQUEST_CODE_MARK_AS_SOLD);
    }

    private void setLocation() {
        if(item.getLocation() != null && !item.getLocation().getLocality().equals("")){
            LOCATION_ITEM = new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude());
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    googleMap.addMarker(new MarkerOptions().position(LOCATION_ITEM));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_ITEM, 13f));
                }
            });
        }else {
            mMapView.setVisibility(View.GONE);
        }
    }
}