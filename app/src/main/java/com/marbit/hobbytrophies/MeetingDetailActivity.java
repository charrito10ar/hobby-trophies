package com.marbit.hobbytrophies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.marbit.hobbytrophies.adapters.TrophyMeetingDetailAdapter;
import com.marbit.hobbytrophies.dialogs.DialogAlertLogin;
import com.marbit.hobbytrophies.dialogs.DialogEditMeeting;
import com.marbit.hobbytrophies.dialogs.DialogGeneric;
import com.marbit.hobbytrophies.interfaces.MeetingDetailView;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.Meeting;
import com.marbit.hobbytrophies.model.MessageMeeting;
import com.marbit.hobbytrophies.model.Trophy;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.model.meeting.Location;
import com.marbit.hobbytrophies.presenters.MeetingDetailPresenter;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.DateUtils;
import com.marbit.hobbytrophies.utilities.Network;
import com.marbit.hobbytrophies.utilities.Preferences;
import com.marbit.hobbytrophies.utilities.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingDetailActivity extends AppCompatActivity implements DialogEditMeeting.OnDialogEditMeetingInteractionListener, TrophyMeetingDetailAdapter.OnListenerEndListMessage,
        MeetingDetailView, DialogGeneric.OnDialogGenericInteractionListener {

    private static final int OWNER = 0;
    private static final int PLAYER = 1;
    private static final int NOT_PLAYER = 2;
    private static int STATE_PLAYER = 2;
    private Meeting meeting;
    @BindView(R.id.meeting_detail_swipe_refresh) SwipeRefreshLayout swipeContainer;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private List<User> listPlayers;
    private List<Trophy> listTrophies;
    @BindView(R.id.image_meeting_detail) ImageView imageGame;
    @BindView(R.id.text_view_description_meeting_detail) TextView description;

    private TrophyMeetingDetailAdapter trophyAdapter;
    @BindView(R.id.recycler_view_meeting_trophies) RecyclerView recyclerViewTrophies;
    private Menu menu;
    private boolean isPlayerNotOwner;
    @BindView(R.id.edit_text_comment) EditText editTextMessage;

    private LinearLayoutManager mLayoutManagerTrophies;
    private List<Object> messageMeetingList;
    private int pageNumber = 1;
    private int pageAmount;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean firstTime = true;
    @BindView(R.id.button_send_comment) ImageButton buttomSend;
    @BindView(R.id.text_view_timer_meeting_detail) TextView timer;
    private MeetingDetailPresenter presenter;
    private String from;
    @BindView(R.id.map_view) MapView mMapView;
    private LatLng LOCATION_MEETING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.from = getIntent().getStringExtra("FROM");
        this.buttomSend.setEnabled(false);
        this.setupEditTextMessage();
        this.listPlayers = new ArrayList<>();
        this.listTrophies = new ArrayList<>();
        this.messageMeetingList = new ArrayList<>();
        this.mLayoutManagerTrophies = new LinearLayoutManager(getApplicationContext());
        this.recyclerViewTrophies.setLayoutManager(mLayoutManagerTrophies);
        this.trophyAdapter = new TrophyMeetingDetailAdapter(getApplicationContext(), this);
        this.recyclerViewTrophies.setAdapter(trophyAdapter);
        this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        this.swipeContainer.setRefreshing(true);
        this.presenter = new MeetingDetailPresenter(getApplicationContext(), this);
        mMapView.onCreate(Bundle.EMPTY);
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
        switch (this.from){
            case "LOCAL":
                this.meeting = getIntent().getParcelableExtra("MEETING");
                this.stringRequest = this.getStringRequest(meeting.getId());
                swipeContainer.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            requestQueue.add(stringRequest);
                                        }
                                    }
                );
                //this.setInitialData();
                //loadRemoteItemSuccess(this.meeting);
                break;
            case "DEEP-LINK":
                int meetingId = Integer.valueOf(getIntent().getStringExtra("meetingId"));
                this.meeting = new Meeting();
                this.meeting.setId(meetingId);
                this.stringRequest = this.getStringRequest(meetingId);
                requestQueue.add(this.stringRequest);
                break;
        }
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestQueue.add(stringRequest);
            }
        });
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

    private void setupEditTextMessage() {
        this.editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(charSequence.length() > 0){
                    buttomSend.setEnabled(true);
                    buttomSend.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                }else {
                    buttomSend.setEnabled(false);
                    buttomSend.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.material_grey_600));
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void setInitialData() {
        Picasso.with(getApplicationContext()).load("http://hobbytrophies.com/i/j/" + this.meeting.getGame().getId()+".png").fit().into(this.imageGame);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_meeting) {
            DialogEditMeeting dialogEditMeeting = DialogEditMeeting.newInstance(this.meeting);
            dialogEditMeeting.show(getSupportFragmentManager(), "Dialog Edit");
            return true;
        }
        if (id == R.id.action_join_meeting) {
            if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN)){
                if(meeting.getLimitMembers() > listPlayers.size()){
                    this.sendRequestJoinMeeting();
                }else {
                    DialogGeneric dialogGeneric = DialogGeneric.newInstance("Meeting completa", "Puedes crear otra meeting y escoger el número de participantes que desees", "Crear meeting", 0);
                    dialogGeneric.show(getSupportFragmentManager(), "dialogAlertMeetingFull");
                }
            }else {
                DialogAlertLogin dialogAlertLogin = DialogAlertLogin.newInstance();
                dialogAlertLogin.show(getSupportFragmentManager(), "dialogAlertLogin");
            }
            return true;
        }
        if (id == R.id.action_exit_meeting) {
            this.sendRequestExitMeeting();
            return true;
        }
        if (id == R.id.action_share_meeting) {
            presenter.shareMeeting(meeting);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meeting_detail, menu);
        this.menu = menu;
        return true;
    }

    public void setStatePlayer() {
        if(isPlayerNotOwner){
            STATE_PLAYER = PLAYER;
        }else {
            if(meeting.getUserOwner().equals(Preferences.getUserName(getApplicationContext()))){
                STATE_PLAYER = OWNER;
            }else {
                STATE_PLAYER = NOT_PLAYER;
            }
        }
    }

    private void setMainAction(){
        switch (STATE_PLAYER){
            case OWNER:
                menu.getItem(0).setVisible(true);
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(false);
                break;
            case PLAYER:
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(true);
                break;
            case NOT_PLAYER:
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(true);
                menu.getItem(2).setVisible(false);
        }
    }

    public StringRequest getStringRequest(final int meetingId) {
        return new StringRequest(Request.Method.GET,
                "http://www.hobbytrophies.com/foros/ps3/get-meeting.php?id=" + meetingId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        listPlayers.clear();
                        listTrophies.clear();
                        isPlayerNotOwner = false;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONObject jsonObjectMeeting = jsonObject.getJSONObject("meeting");
                            meeting.setDescription(jsonObjectMeeting.getString("description"));
                            Game game = new Game();
                            game.setId(jsonObjectMeeting.getString("gameId"));
                            meeting.setGame(game);
                            meeting.setType(jsonObjectMeeting.getInt("type"));
                            if(meeting.getType() == 5){
                                mMapView.setVisibility(View.VISIBLE);
                                JSONObject jsonObjectLocation = jsonObjectMeeting.getJSONObject("location");
                                Location location = new Location(jsonObjectLocation.getString("description"), Double.valueOf(jsonObjectLocation.getString("latitud")), Double.valueOf(jsonObjectLocation.getString("longitud")));
                                LOCATION_MEETING = new LatLng(location.getLatitude(), location.getLongitude());
                                meeting.setLocation(location);
                                setLocation();
                            }
                            meeting.setLimitMembers(jsonObjectMeeting.getInt("places"));
                            meeting.setDate(DateUtils.getInstance().convertToLocalTimeDate(jsonObjectMeeting.getString("date")));
                            JSONArray jsonArrayPlayers = jsonObjectMeeting.getJSONArray("players");
                            for (int i = 0; i < jsonArrayPlayers.length(); i++){
                                JSONObject jsonObjectPlayer = (JSONObject) jsonArrayPlayers.get(i);
                                User user = new User();
                                user.setPsnId(jsonObjectPlayer.getString("user"));
                                user.setAvatarUrl(jsonObjectPlayer.getString("img"));
                                user.setTrophiesWonInMeeting(jsonObjectPlayer.getInt("amount-trophies"));
                                user.setRolMeeting(jsonObjectPlayer.getInt("rol"));
                                int rol = jsonObjectPlayer.getInt("rol");
                                if (rol == 1 && user.getPsnId().equals(Preferences.getUserName(getApplicationContext()))){
                                    isPlayerNotOwner = true;
                                }
                                if(rol == 0){
                                    meeting.setUserOwner(user.getPsnId());
                                    meeting.setUserOwnerAvatar(user.getAvatarUrl());
                                }
                                listPlayers.add(user);
                            }

                            JSONArray jsonArrayTrophies = jsonObjectMeeting.getJSONArray("trophies");
                            for (int i = 0; i < jsonArrayTrophies.length(); i++){
                                JSONObject jsonObjectTrohy = (JSONObject) jsonArrayTrophies.get(i);
                                Trophy trophy = new Trophy();
                                trophy.setId(jsonObjectTrohy.getInt("id"));
                                trophy.setTitle(jsonObjectTrohy.getString("title"));
                                trophy.setDescription(jsonObjectTrohy.getString("description"));
                                trophy.setImg("http://hobbytrophies.com/i/t/"+ meeting.getGame().getId() + "/" + trophy.getId() + ".png");
                                trophy.setType(jsonObjectTrohy.getString("type"));
                                listTrophies.add(trophy);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeContainer.setRefreshing(false);
                            Toast.makeText(getApplicationContext(), "Error en el formato de la respuesta", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        refreshContent(listTrophies, listPlayers, meeting);
                        swipeContainer.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeContainer.setRefreshing(false);
                String message = Network.getErrorMessage(volleyError);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshContent(List listTrophies, List listPlayers, Meeting meeting) {
        this.trophyAdapter.clearAll();
        this.trophyAdapter.setMeeting(meeting);
        this.trophyAdapter.setList(listTrophies);
        this.trophyAdapter.clearAllPlayers();
        this.trophyAdapter.setListPlayers(listPlayers);
        this.trophyAdapter.notifyDataSetChanged();
        this.meeting.setTrophyList(listTrophies);
        this.presenter.start(meeting);
        this.setStatePlayer();
        this.setMainAction();
        this.setInitialData();
        this.setEndList();
    }

    private void setLocation() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions().position(LOCATION_MEETING));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_MEETING, 13f));
            }
        });
    }

    private void sendRequestJoinMeeting() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user-id", Preferences.getUserName(getApplicationContext()));
            jsonObject.put("meeting-id", this.meeting.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue.add(getStringPostJoinMeeting(jsonObject)).setShouldCache(false);
    }

    private JsonObjectRequest getStringPostJoinMeeting(JSONObject jsonParams){
        return (JsonObjectRequest) new JsonObjectRequest(Request.Method.PUT,"http://www.hobbytrophies.com/foros/ps3/inc/join-meeting.php", jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("response-code");
                            if(responseCode == 1){
                                Toast.makeText(getApplicationContext(), "Te has unido a la meeting", Toast.LENGTH_LONG).show();
                                FirebaseMessaging.getInstance().subscribeToTopic("meeting-" + meeting.getId());
                                joinMeeting();
                            }else {
                                Toast.makeText(getApplicationContext(), "Error al crearla", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(MeetingDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }).setShouldCache(false);
    }

    private void joinMeeting() {
        User user = new User(Preferences.getUserName(getApplicationContext()));
        user.setAvatarUrl(Preferences.getAvatar(getApplicationContext()));
        this.listPlayers.add(user);
        STATE_PLAYER = PLAYER;
        refreshContentPlayers(this.listPlayers);
    }

    private void sendRequestExitMeeting() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user-id", Preferences.getUserName(getApplicationContext()));
            jsonObject.put("meeting-id", this.meeting.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue.add(getStringPostDeleteMeeting(jsonObject)).setShouldCache(false);
    }

    private JsonObjectRequest getStringPostDeleteMeeting(JSONObject jsonParams) {
        return (JsonObjectRequest) new JsonObjectRequest(Request.Method.PUT,"http://www.hobbytrophies.com/foros/ps3/inc/exit-meeting.php", jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("response-code");
                            if(responseCode == 1){
                                Toast.makeText(getApplicationContext(), "Has salido de la meeting", Toast.LENGTH_LONG).show();
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("meeting-" + meeting.getId());
                                deleterPlayer();
                            }else {
                                Toast.makeText(getApplicationContext(), "Error al salir de la meeting", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(MeetingDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }).setShouldCache(false);
    }

    private void deleterPlayer() {
        for(int i =0; i < listPlayers.size(); i++){
            if(listPlayers.get(i).getPsnId().equals(Preferences.getUserName(getApplicationContext()))){
                listPlayers.remove(i);
            }
        }
        this.STATE_PLAYER = NOT_PLAYER;
        refreshContentPlayers(listPlayers);
    }

    private void refreshContentPlayers(List listPlayers) {
        this.trophyAdapter.clearAllPlayers();
        this.trophyAdapter.setListPlayers(listPlayers);
        this.trophyAdapter.notifyDataSetChanged();
        this.setMainAction();
    }

    @Override
    public void onDialogEditMeetingInteraction(int code) {
        switch (code){
            case DialogEditMeeting.DELETE_MEETING_ERROR_CODE:
                Toast.makeText(getApplicationContext(), "Error al eliminar la meeting", Toast.LENGTH_SHORT).show();
                break;
            case DialogEditMeeting.DELETE_MEETING_CODE:
                Toast.makeText(getApplicationContext(), "Meeting eliminada", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case DialogEditMeeting.GENERAL_EDIT_MEETING_CODE:
                presenter.editGeneralInfoMeeting();
                break;
        }
    }

    public void clickSendMessage(View view){
        if(Preferences.getBoolean(getApplicationContext(), Constants.PREFERENCE_IS_USER_LOGIN)){
            String messageText = this.editTextMessage.getText().toString();
            String user = Preferences.getUserName(getApplicationContext());
            int meetingId = this.meeting.getId();
            this.editTextMessage.setText("");
            this.editTextMessage.onEditorAction(EditorInfo.IME_ACTION_DONE);
            this.sendRequestNewMessage(messageText, user, meetingId);
        }else {
            DialogAlertLogin dialogAlertLogin = DialogAlertLogin.newInstance();
            dialogAlertLogin.show(getSupportFragmentManager(), "dialogAlertLogin");
        }
    }

    private void sendRequestNewMessage(String message, String user, int meetingId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("meeting-id", meetingId);
            jsonObject.put("user-psn-id", user);
            jsonObject.put("text", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MessageMeeting messageMeeting = new MessageMeeting();
        User userMessage = new User();
        userMessage.setAvatarUrl(Preferences.getAvatar(getApplicationContext()));
        userMessage.setPsnId(Preferences.getUserName(getApplicationContext()));
        messageMeeting.setUser(userMessage);
        messageMeeting.setText(message);
        DateUtils dateUtils = new DateUtils();
        messageMeeting.setDate(dateUtils.getDataBaseTime());
        addMessage(messageMeeting);
        Toast.makeText(getApplicationContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
        requestQueue.add(getStringPostNewMessage(jsonObject, messageMeeting)).setShouldCache(false);
    }

    private JsonObjectRequest getStringPostNewMessage(JSONObject jsonParams, final MessageMeeting message){
        return (JsonObjectRequest) new JsonObjectRequest(Request.Method.PUT,"http://www.hobbytrophies.com/foros/ps3/inc/new-message.php", jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("response-code");
                            int messageId = response.getInt("message-id");
                            if(responseCode == 1){
                                message.setId(messageId);
                                FirebaseMessaging.getInstance().subscribeToTopic("meeting-" + meeting.getId());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(MeetingDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }).setShouldCache(false);
    }

    private void setEndList() {
        recyclerViewTrophies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) //check for scroll down{
                    visibleItemCount = mLayoutManagerTrophies.getChildCount();
                totalItemCount = mLayoutManagerTrophies.getItemCount();
                pastVisiblesItems = mLayoutManagerTrophies.findFirstVisibleItemPosition();

                if (loading) {
                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount - 5) {
                        loading = false;
                        loadNextDataFromApi();
                    }
                }
            }
        });
    }

    public void loadNextDataFromApi() {
        if(pageNumber <= pageAmount || firstTime){
            requestQueue.add(getStringRequestMessages(meeting.getId()));
            pageNumber++;
            firstTime = false;
        }
    }

    private StringRequest getStringRequestMessages(final int meetingId){
        return new StringRequest(Request.Method.GET,
                "http://www.hobbytrophies.com/foros/ps3/get-messages.php?id=" + meetingId + "&page=" + this.pageNumber,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArrayMessages = jsonObject.getJSONArray("messages");
                            int messageAmount = jsonObject.getInt("count");
                            pageAmount =  jsonObject.getInt("count") / 10 + 1;
                            messageMeetingList.clear();
                            for (int i=0; i<jsonArrayMessages.length(); i++) {
                                JSONObject jsonObjectMessage = jsonArrayMessages.getJSONObject(i);
                                MessageMeeting messageMeeting = new MessageMeeting();
                                messageMeeting.setId(jsonObjectMessage.getInt("id"));
                                messageMeeting.setUser(new User(jsonObjectMessage.getString("user_psn_id"), jsonObjectMessage.getString("avatar")));
                                messageMeeting.setText(jsonObjectMessage.getString("text"));
                                messageMeeting.setDate(jsonObjectMessage.getString("date"));
                                messageMeetingList.add(messageMeeting);
                            }
                            refreshContentMessages(messageMeetingList, messageAmount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                Toast.makeText(MeetingDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshContentMessages(List messageMeetingList, int messageAmount) {
        this.trophyAdapter.setMessageAmount(messageAmount);
        this.trophyAdapter.setListMessages(messageMeetingList);
        this.trophyAdapter.notifyDataSetChanged();
        this.trophyAdapter.setLoading(true);
    }

    private void addMessage(MessageMeeting messageMeeting) {
        this.trophyAdapter.addMessage(messageMeeting);
        this.trophyAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreMessage() {
        loadNextDataFromApi();
    }

    @Override
    public void setDescriptionMeeting(String description) {
        this.description.setText(description);
    }

    @Override
    public void setTimerMeeting(long days, long hours, long minutes, long seconds) {
        this.timer.setText(getApplicationContext().getString(R.string.count_down_meeting, days, hours, minutes, seconds));
    }

    @Override
    public void openEditMeeting() {
        Intent intent = new Intent(this, EditMeetingActivity.class);
        intent.putExtra("MEETING", this.meeting);
        startActivity(intent);
    }

    @Override
    public void shareMeetingLink(Meeting meeting, String longMeetingLink) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Unámosnos a esta quedada en Hobby Trophies: " + longMeetingLink);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Elija aplicación"));
    }

    @Override
    public void onDialogGenericInteraction(int code) {// Open New Meeting cuando no hay más lugar en la actual.
        Intent intent = new Intent(this, NewMeetingActivity.class);
        startActivity(intent);
    }
}