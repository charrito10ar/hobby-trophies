package com.marbit.hobbytrophies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.marbit.hobbytrophies.interfaces.market.EditItemActivityView;
import com.marbit.hobbytrophies.model.market.Item;
import com.marbit.hobbytrophies.presenters.market.EditItemActivityPresenter;

public class EditItemActivity extends AppCompatActivity implements EditItemActivityView, AdapterView.OnItemSelectedListener {

    private CheckBox checkBoxDigital;
    private CheckBox checkBoxBarter;
    private TextView titleGame;
    private EditText priceItem;
    private EditText descriptionItem;
    private Spinner categoryItem;
    private EditItemActivityPresenter presenter;
    private Item item;
    private String[] itemCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        this.item = getIntent().getParcelableExtra("ITEM");
        this.itemCategories = getResources().getStringArray(R.array.item_types);
        this.presenter = new EditItemActivityPresenter(getApplicationContext(), this);
        this.checkBoxDigital = (CheckBox) findViewById(R.id.checkbox_digital_version);
        this.checkBoxBarter = (CheckBox) findViewById(R.id.checkbox_is_barter);
        this.titleGame = (TextView) findViewById(R.id.title_item_game);
        this.priceItem = (EditText) findViewById(R.id.item_price);
        this.descriptionItem = (EditText) findViewById(R.id.item_description);
        this.categoryItem = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, itemCategories);
        this.categoryItem.setAdapter(adapter);
        this.categoryItem.setOnItemSelectedListener(this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        this.categoryItem.setSelection(category);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        this.categoryItem.setSelection(item.getItemCategory()+1);
    }

    public void updateItem(View view) {

    }
}
