package com.marbit.hobbytrophies.market;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.interfaces.market.EditItemActivityView;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.presenters.market.EditItemActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditItemActivity extends AppCompatActivity implements EditItemActivityView,  View.OnClickListener {

    private CheckBox checkBoxDigital;
    private CheckBox checkBoxBarter;
    private TextView titleGame;
    private EditText priceItem;
    private EditText descriptionItem;
    private TextView categoryItem;
    private EditItemActivityPresenter presenter;
    private Item item;
    private String[] itemCategories;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageRef = firebaseStorage.getReference();

    @BindView(R.id.image_first_photo) ImageView imageFirsPhoto;
    @BindView(R.id.image_second_photo) ImageView imageSecondPhoto;
    @BindView(R.id.image_third_photo) ImageView imageThirdPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        ButterKnife.bind(this);
        this.item = getIntent().getParcelableExtra("ITEM");
        this.itemCategories = getResources().getStringArray(R.array.item_types);
        this.presenter = new EditItemActivityPresenter(getApplicationContext(), this);
        this.checkBoxDigital = findViewById(R.id.checkbox_digital_version);
        this.checkBoxBarter = (CheckBox) findViewById(R.id.checkbox_is_barter);
        this.titleGame = (TextView) findViewById(R.id.title_item_game);
        this.titleGame.setOnClickListener(this);
        this.priceItem = (EditText) findViewById(R.id.item_price);
        this.descriptionItem = (EditText) findViewById(R.id.item_description);
        this.categoryItem = (TextView) findViewById(R.id.text_view_category);

    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.setItem(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPrice(double price) {
        this.priceItem.setText(String.valueOf(price));
    }

    @Override
    public void setTitle(String title) {
        this.titleGame.setText(title);
    }

    @Override
    public void setDescription(String description) {
        this.descriptionItem.setText(description);
    }

    @Override
    public void setDigital(boolean digital) {
        this.checkBoxDigital.setChecked(digital);
    }

    @Override
    public void setBarter(boolean barter) {
        this.checkBoxBarter.setChecked(barter);
    }

    @Override
    public void setCategory(int category) {
        this.categoryItem.setText("Categoria: " + itemCategories[category]);
    }

    @Override
    public void setPhotos(String itemId) {
        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(storageRef.child("/item-images/" + itemId + "/image_0" + 0 +".jpg"))
                .fitCenter()
                .into(imageFirsPhoto);

        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(storageRef.child("/item-images/" + itemId + "/image_0" + 1 +".jpg"))
                .error(R.drawable.placeholder_item)
                .fitCenter()
                .into(imageSecondPhoto);

        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(storageRef.child("/item-images/" + itemId + "/image_0" + 2 +".jpg"))
                .error(R.drawable.placeholder_item)
                .fitCenter()
                .into(imageThirdPhoto);
    }

    @Override
    public void editItemSuccess() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("ITEM",item);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void updateItem(View view) {
        item.setDescription(descriptionItem.getText().toString());
        item.setPrice(Double.valueOf(priceItem.getText().toString()));
        item.setBarter(checkBoxBarter.isChecked());
        item.setDigital(checkBoxDigital.isChecked());
        presenter.updateItem(item);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getUserId()){
//            case R.id.
//        }
    }
}
