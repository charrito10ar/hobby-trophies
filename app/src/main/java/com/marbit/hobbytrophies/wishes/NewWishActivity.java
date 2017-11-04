package com.marbit.hobbytrophies.wishes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.dialogs.DialogSearchGame;
import com.marbit.hobbytrophies.interfaces.wishes.NewWishActivityView;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.wishes.model.Wish;
import com.marbit.hobbytrophies.presenters.wishes.NewWishActivityPresenter;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.Preferences;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class NewWishActivity extends AppCompatActivity implements NewWishActivityView, View.OnClickListener, DialogSearchGame.DialogSearchGameListener, CompoundButton.OnCheckedChangeListener, RangeSeekBar.OnRangeSeekBarChangeListener {

    private CheckBox checkBoxDigital;
    private CheckBox checkBoxBarter;
    private TextView buttonSelectGame;
    private Spinner buttonSelectConsole;
    private NewWishActivityPresenter presenter;
    private RangeSeekBar rangeSeekBarPrice;
    private TextView textViewPrice;
    private RadioButton radioButtonConsole;
    private Button buttonAccept;
    private String[] consolesArray;
    private Wish wish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wish);
        this.wish = new Wish();
        this.wish.setUserId(Preferences.getUserName(getApplicationContext()));
        this.presenter = new NewWishActivityPresenter(getApplicationContext(), this);
        this.consolesArray  = getResources().getStringArray(R.array.consoles);
        this.checkBoxDigital = (CheckBox) findViewById(R.id.checkbox_digital);
        this.checkBoxDigital.setOnCheckedChangeListener(this);
        this.checkBoxBarter = (CheckBox) findViewById(R.id.checkbox_barter);
        this.checkBoxBarter.setOnCheckedChangeListener(this);
        this.buttonSelectConsole = (Spinner) findViewById(R.id.button_select_console);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, consolesArray);
        buttonSelectConsole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wish.setConsoleId(position - 1);
                wish.setName(consolesArray[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                wish.setConsoleId(-1);
            }
        });
        this.buttonSelectConsole.setAdapter(adapter);
        this.buttonSelectGame = (TextView) findViewById(R.id.button_select_game);
        this.buttonSelectGame.setOnClickListener(this);
        RadioButton radioButtonGame = (RadioButton) findViewById(R.id.radio_button_game);
        radioButtonGame.setOnCheckedChangeListener(this);
        this.radioButtonConsole = (RadioButton)findViewById(R.id.radio_button_console);
        this.radioButtonConsole.setOnCheckedChangeListener(this);
        this.buttonAccept = (Button) findViewById(R.id.button_apply_add_wish);
        this.buttonAccept.setOnClickListener(this);
        this.rangeSeekBarPrice = (RangeSeekBar) findViewById(R.id.seekbar_range_price);
        this.rangeSeekBarPrice.setOnRangeSeekBarChangeListener(this);
        this.textViewPrice = (TextView) findViewById(R.id.text_view_price);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_select_game:
                DialogSearchGame dialogSearchGame = DialogSearchGame.newInstance();
                dialogSearchGame.show(getSupportFragmentManager(), "DialogSearchGame");
                break;
            case R.id.button_apply_add_wish:
                if(itemIsValid()){
                    presenter.addWish(wish);
                }else {
                    Toast.makeText(getApplicationContext(), "Complete los campos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean itemIsValid() {
        switch (wish.getItemType()){
            case Constants.PREFERENCE_ITEM_CATEGORY_GAME:
                if(wish.getGameId() != null){
                    return true;
                }else {
                    return false;
                }
            default:
                if(wish.getConsoleId() != 0){
                    return true;
                }
                return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectGame(Game game) {
        wish.setGameId(game.getId());
        wish.setName(game.getName());
        this.setGameSelected(game.getName());
    }

    @Override
    public void setGameSelected(String gameSelected) {
        this.buttonSelectGame.setText(gameSelected);
    }

    @Override
    public void setConsoleSelected(String consoleSelected) {

    }

    @Override
    public void gameSelected() {
        wish.setItemType(Constants.PREFERENCE_ITEM_CATEGORY_GAME);
        showCheckBoxDigital();
        buttonSelectGame.setVisibility(View.VISIBLE);
        buttonSelectConsole.setVisibility(View.GONE);
    }

    @Override
    public void consoleSelected() {
        wish.setItemType(Constants.PREFERENCE_ITEM_CATEGORY_CONSOLE);
        hideCheckBoxDigital();
        buttonSelectGame.setVisibility(View.GONE);
        buttonSelectConsole.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCheckBoxDigital() {
        this.checkBoxDigital.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCheckBoxDigital() {
        this.checkBoxDigital.setVisibility(View.INVISIBLE);
    }

    @Override
    public void addWishSuccessful() {
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.radio_button_game:
                if(isChecked){
                    gameSelected();
                }
                break;
            case R.id.radio_button_console:
                if(isChecked){
                    consoleSelected();
                }
                break;
            case R.id.checkbox_barter:
                wish.setBarter(isChecked);
                break;
            case R.id.checkbox_digital:
                wish.setDigital(isChecked);
        }
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
        this.textViewPrice.setText(minValue.toString() + " - " + maxValue.toString() + " Euros");
        wish.setMinPrice((Integer) minValue);
        wish.setMaxPrice((Integer) maxValue);
    }
}
