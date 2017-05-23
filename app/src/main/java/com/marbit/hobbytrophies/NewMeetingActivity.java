package com.marbit.hobbytrophies;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.marbit.hobbytrophies.adapters.TrophyMeetingAdapter;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.Trophy;
import com.marbit.hobbytrophies.utilities.DatePickerFragment;
import com.marbit.hobbytrophies.utilities.DateUtils;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.marbit.hobbytrophies.utilities.TimePickerFragment;
import com.marbit.hobbytrophies.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewMeetingActivity extends AppCompatActivity implements TimePickerFragment.TimeDatePickerListener, TextWatcher {

    private TextView gameNameTextView;
    private TextView gameTypeTextView;
    private TextView dateTimeTextView;
    private TextView amountMembersTextView;
    private EditText descriptionEditText;
    private int gameTypeSelected;
    private int gameSelected;
    private String stringDateSelected;
    private MenuItem menuItemSend;
    private boolean dateTimeLoad = false;
    private boolean gameLoad = false;
    private boolean typeLoad = false;
    private List<Game> userGamesList;
    private String[] userGamesName;
    private View layoutStepOne;
    private SwipeRefreshLayout layoutStepTwo;
    private RecyclerView recyclerViewTrophies;
    private RequestQueue requestQueue;
    private List<Trophy> trophies;
    private TrophyMeetingAdapter trophyMeetingAdapter;
    private int step;
    private DateUtils dateUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_new);
        this.dateUtils = new DateUtils();
        this.gameNameTextView = (TextView) findViewById(R.id.text_view_name_game);
        this.gameTypeTextView = (TextView) findViewById(R.id.text_view_type_game);
        this.dateTimeTextView = (TextView) findViewById(R.id.text_view_time_meeting);
        this.amountMembersTextView = (TextView) findViewById(R.id.text_view_amount_people);
        this.descriptionEditText = (EditText) findViewById(R.id.text_view_description_meeting);
        this.descriptionEditText.addTextChangedListener(this);
        this.userGamesList = Preferences.getGameList(getApplicationContext());
        this.userGamesName = Preferences.getGameTitle(getApplicationContext());
        this.layoutStepOne = (ScrollView) findViewById(R.id.scroll_layout_step_one);
        this.layoutStepTwo = (SwipeRefreshLayout) findViewById(R.id.new_meeting_swipe_refresh);
        this.recyclerViewTrophies = (RecyclerView) findViewById(R.id.recycler_view_trophies_new_meeting);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerViewTrophies.setLayoutManager(mLayoutManager);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        this.step = 1;
        this.trophies = new ArrayList<>();
        this.trophyMeetingAdapter = new TrophyMeetingAdapter(getApplicationContext(), true);
        this.recyclerViewTrophies.setAdapter(trophyMeetingAdapter);

        layoutStepTwo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestQueue.add(getStringRequestTrophies(Preferences.getUserName(getApplicationContext()), userGamesList.get(gameSelected).getId()));
            }
        });
    }

    private void refreshContent(List<Trophy> trophyList) {
        this.trophyMeetingAdapter.clearAll();
        this.trophyMeetingAdapter.setList(trophyList);
        this.trophyMeetingAdapter.notifyDataSetChanged();
    }

    private StringRequest getStringRequestTrophies(String psnId, String gameId){
        return new StringRequest(Request.Method.GET,
                "http://hobbytrophies.com/foros/ps3/get-juego-usuario.php?sPSNID=" + psnId + "&id=" + gameId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        layoutStepTwo.setRefreshing(false);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArrayTrophies = jsonObject.getJSONArray("trophies");

                            trophies.clear();
                            for (int i=0; i<jsonArrayTrophies.length(); i++){
                                JSONObject jsonObjectTrophy = jsonArrayTrophies.getJSONObject(i);
                                Trophy trophy = new Trophy();
                                trophy.setId(jsonObjectTrophy.getInt("id_trofeu"));
                                trophy.setTitle(jsonObjectTrophy.getString("titol"));
                                trophy.setDescription(jsonObjectTrophy.getString("descripcio"));
                                trophy.setImg(jsonObjectTrophy.getString("imatge"));
                                trophy.setType(jsonObjectTrophy.getString("tipus"));
                                trophy.setDateGet(jsonObjectTrophy.getString("data_aconseguit"));
                                trophies.add(trophy);
                            }
                            refreshContent(trophies);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                layoutStepTwo.setRefreshing(false);
                Log.d("ERROR", error.toString());
                Toast.makeText(NewMeetingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting_new, menu);
        this.menuItemSend = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(this.step == 1){
                onBackPressed();
            }else {
                this.step = 1;
                this.layoutStepOne.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInLeft)
                        .duration(600)
                        .playOn(this.layoutStepOne);

                YoYo.with(Techniques.FadeOutRight)
                        .duration(600)
                        .playOn(this.layoutStepTwo);
            }
            return true;
        }
        if (id == R.id.action_send_meeting) {
            if(this.step == 1){
                this.layoutStepTwo.setRefreshing(true);
                this.requestQueue.add(getStringRequestTrophies(Preferences.getUserName(getApplicationContext()), this.userGamesList.get(this.gameSelected).getId()));
                this.step = 2;
                YoYo.with(Techniques.FadeOutLeft)
                        .duration(600)
                        .playOn(this.layoutStepOne);

                this.layoutStepTwo.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.FadeInRight)
                        .duration(600)
                        .playOn(this.layoutStepTwo);
                Utilities.hideKeyboard(this);
            }else {
                if(this.trophyMeetingAdapter.getSelectedTrophiesList().size() > 0){
                    this.sendRequestNewMeeting();
                }else {
                    Toast.makeText(getApplicationContext(), "Debes seleccionar al menos un trofeo", Toast.LENGTH_LONG).show();
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendRequestNewMeeting() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user-admin", Preferences.getUserName(getApplicationContext()));
            jsonObject.put("game-id", this.userGamesList.get(this.gameSelected).getId());
            jsonObject.put("description", descriptionEditText.getText().toString());
            jsonObject.put("date", stringDateSelected);
            jsonObject.put("type", gameTypeSelected);
            jsonObject.put("places", amountMembersTextView.getText());
            jsonObject.put("trophies", new JSONArray(trophyMeetingAdapter.getSelectedTrophiesList()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue.add(getStringPostNewMeeting(jsonObject)).setShouldCache(false);
    }

    private JsonObjectRequest getStringPostNewMeeting(JSONObject jsonParams){
        return (JsonObjectRequest) new JsonObjectRequest(Request.Method.PUT,"http://www.hobbytrophies.com/foros/ps3/inc/new-meeting.php", jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("response-code");
                            int meetingId = response.getInt("meeting-id");
                            if(responseCode == 1){
                                Toast.makeText(getApplicationContext(), "Meeting creada exitosamente", Toast.LENGTH_LONG).show();
                                FirebaseMessaging.getInstance().subscribeToTopic("meeting-" + meetingId);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(NewMeetingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }).setShouldCache(false);

    }

    /**
     *  Listener XML
     */

    public void onClickGame(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona juego")
                .setItems(userGamesName, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        gameSelected = which;
                        gameNameTextView.setText(userGamesList.get(which).getName());
                        gameLoad = true;
                        onTextChanged(descriptionEditText.getText(), 0, 0, 0);
                    }
                });
        builder.show();
    }

    public void onClickDateTimeMeeting(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onClickLessMembers(View view){
        int amount = Integer.valueOf(amountMembersTextView.getText().toString()) - 1;
        if(amount >= 0){
            this.amountMembersTextView.setText(String.valueOf(amount));
        }
    }

    public void onClickMoreMembers(View view){
        int amount = Integer.valueOf(amountMembersTextView.getText().toString()) + 1;
        this.amountMembersTextView.setText(String.valueOf(amount));
    }

    public void onClickType(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tipos de quedadas")
                .setItems(R.array.games_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        gameTypeSelected = which;
                        String[] gamesTypeArray = getResources().getStringArray(R.array.games_type);
                        gameTypeTextView.setText(gamesTypeArray[which]);
                        typeLoad = true;
                        onTextChanged(descriptionEditText.getText(), 0, 0, 0);
                    }
                });
        builder.show();
    }

    @Override
    public void onDateTimeInteraction(Calendar calendar) {
        try {
            this.stringDateSelected = dateUtils.convertToServerTime(calendar.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strDateFormat = getResources().getString(R.string.date_time_meeting);
        String[] dayOfWeekArray = getResources().getStringArray(R.array.dayOfWeek);
        String[] monthsArray = getResources().getStringArray(R.array.months);
        String dayString = dayOfWeekArray[calendar.get(Calendar.DAY_OF_WEEK)-1];
        String monthString = monthsArray[calendar.get(Calendar.MONTH)+1];
        String strDateMsg = String.format(strDateFormat, calendar.get(Calendar.DAY_OF_MONTH), dayString, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), monthString);
        this.dateTimeTextView.setText(strDateMsg);
        this.dateTimeLoad = true;
        onTextChanged(this.descriptionEditText.getText(), 0, 0, 0);
    }

    private boolean fieldsComplete() {
        return this.dateTimeLoad && this.typeLoad && this.gameLoad;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0 && fieldsComplete()){
            this.menuItemSend.setEnabled(true);
            this.menuItemSend.setIcon(R.drawable.ic_action_send);
        }else {
            this.menuItemSend.setEnabled(false);
            this.menuItemSend.setIcon(R.drawable.ic_action_send_disable);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
