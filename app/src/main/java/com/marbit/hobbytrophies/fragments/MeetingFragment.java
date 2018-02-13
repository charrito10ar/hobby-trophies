package com.marbit.hobbytrophies.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.fabtransitionactivity.SheetLayout;
import com.marbit.hobbytrophies.NewMeetingActivity;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.MeetingAdapter;
import com.marbit.hobbytrophies.dialogs.DialogAlertLogin;
import com.marbit.hobbytrophies.dialogs.DialogFilterMeeting;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.Meeting;
import com.marbit.hobbytrophies.model.meeting.Location;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.DateUtils;
import com.marbit.hobbytrophies.utilities.Network;
import com.marbit.hobbytrophies.utilities.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class MeetingFragment extends Fragment implements SheetLayout.OnFabAnimationEndListener, DialogFilterMeeting.OnDialogFilterMeetingInteractionListener {


    private static final String ARG_COLUMN_COUNT = "column-count";
    @BindView(R.id.head_meeting) TextView  headMeeting;
    @BindView(R.id.layout_meeting_list) LinearLayout layoutMeetingList;
    @BindView(R.id.layout_empty_list) LinearLayout layoutEmptyList;
    private  SwipeRefreshLayout swipeContainer;
    private SheetLayout mSheetLayout;
    private int mColumnCount = 1;
    private MeetingInteractionListener mListener;
    private ArrayList<Object> genericList;
    private MeetingAdapter meetingAdapter;
    private static final int REQUEST_CODE = 1;
    private RequestQueue requestQueue;
    private int typeRequest = DialogFilterMeeting.ALL_MEETINGS;;
    private FloatingActionButton floatingActionButton;
    private Unbinder unbinder;
    private ArrayList<Object> meetingListNearby;

    public MeetingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.genericList = new ArrayList<>();
        this.meetingListNearby = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_meeting);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        this.meetingAdapter = new MeetingAdapter(mListener, getContext());
        recyclerView.setAdapter(this.meetingAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = linearLayoutManager.findFirstVisibleItemPosition();
                if(position != NO_POSITION){
                    Object object = genericList.get(position);
                    if(object instanceof Meeting){
                        headMeeting.setText(((Meeting) object).getDate(getContext()));
                        headMeeting.setBackgroundColor(((Meeting) object).getColor());
                    }
                }else {
                    headMeeting.setText("No hay quedadas");
                }
            }
        });
        this.floatingActionButton = view.findViewById(R.id.floatin_action_new_meeting);
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Preferences.getBoolean(getContext(), Constants.PREFERENCE_IS_USER_LOGIN) &&
                        Preferences.getBoolean(getContext(), Constants.PREFERENCE_IS_PSN_CODE_OK)){
                    mSheetLayout.expandFab();
                }else {
                    DialogAlertLogin dialogAlertLogin = DialogAlertLogin.newInstance();
                    dialogAlertLogin.show(getFragmentManager(), "dialogAlertLogin");
                }
            }
        });
        this.mSheetLayout = view.findViewById(R.id.bottom_sheet);
        this.mSheetLayout.setFab(this.floatingActionButton);
        this.mSheetLayout.setFabAnimationEndListener(this);
        this.requestQueue = Volley.newRequestQueue(getContext());
        this.swipeContainer = view.findViewById(R.id.fragment_meeting_swipe_refresh);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestQueue.add(getStringRequest(typeRequest));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.swipeContainer.setRefreshing(true);
        swipeContainer.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestQueue.add(getStringRequest(typeRequest));
                                }
                            }
        );
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private StringRequest getStringRequest(final int typeRequest){
        String url = "http://www.hobbytrophies.com/foros/ps3/get-meetings.php";
        if(typeRequest == DialogFilterMeeting.MY_MEETINGS){
            url = url + "?psnId=" + Preferences.getUserId(getContext());
        }
        return new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(typeRequest == DialogFilterMeeting.ALL_MEETINGS){
                                genericList.clear();
                            }
                            genericList = buildListMeeting(response);
                            refreshContent(genericList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeContainer.setRefreshing(false);
                            Toast.makeText(getContext(), "Error en el formato de la respuesta", Toast.LENGTH_SHORT).show();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeContainer.setRefreshing(false);
                String message = Network.getErrorMessage(volleyError);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ArrayList<Object> buildListMeeting(String response) throws JSONException, ParseException {
        if(typeRequest == DialogFilterMeeting.ALL_MEETINGS){
            Preferences.saveString(getContext(), Constants.STATS_MEETINGS, response);
            meetingListNearby.clear();
        }
        ArrayList<Object> list = new ArrayList();
        JSONObject jsonObject;
        jsonObject = new JSONObject(response);
        JSONArray jsonArrayMeetings = jsonObject.getJSONArray("meetings");
        for (int i = 0; i < jsonArrayMeetings.length(); i++){
            JSONObject jsonObjectMeeting = (JSONObject) jsonArrayMeetings.get(i);
            Meeting meeting = new Meeting();
            meeting.setId((jsonObjectMeeting.getInt("id")));
            meeting.setUserOwner(jsonObjectMeeting.getString("user_psn_id"));
            meeting.setUserOwnerAvatar(jsonObjectMeeting.getString("avatar"));
            meeting.setDescription((jsonObjectMeeting.getString("description")));
            meeting.setType((jsonObjectMeeting.getInt("type")));
            meeting.setReserved(jsonObjectMeeting.getInt("reserved"));
            meeting.setLimitMembers(jsonObjectMeeting.getInt("places"));
            meeting.setState(jsonObjectMeeting.getInt("state"));
            meeting.setDate(DateUtils.getInstance().convertToLocalTimeDate(jsonObjectMeeting.getString("date")));
            meeting.setGame(new Game(jsonObjectMeeting.getString("game_id")));
            Location location = new Location(jsonObjectMeeting.getString("address"), jsonObjectMeeting.getDouble("latitud"), jsonObjectMeeting.getDouble("longitud"));
            meeting.setLocation(location);
            if(i>0){
                DateFormat formatDay = new SimpleDateFormat("dd",Locale.ENGLISH);
                String currentDayString = formatDay.format(meeting.getDate());
                Object object = list.get(list.size()-1);
                String previousDatString = formatDay.format(((Meeting) object).getDate());
                if(!currentDayString.equals(previousDatString)){
                    list.add(meeting.getDate(getContext()));
                }
            }if(meeting.getType() == 5 && typeRequest == DialogFilterMeeting.ALL_MEETINGS){
                if(meetingIsNear(meeting.getLocation())){
                    meetingListNearby.add(meeting);
                }
            }
            list.add(meeting);
        }
        return list;
    }

    private boolean meetingIsNear(Location location) {
        float[] results = new float[1];
        Location userLocation = Preferences.getUserLocation(getContext());
        android.location.Location.distanceBetween(location.getLatitude(), location.getLongitude(),
                userLocation.getLatitude(), userLocation.getLongitude(),
                results);
         return results[0] < Constants.TEN_KM;
    }

    private void refreshContent(ArrayList<Object> genericList) {
        this.meetingAdapter.clearAll();
        this.meetingAdapter.setList(genericList);
        this.meetingAdapter.notifyDataSetChanged();
        this.swipeContainer.setRefreshing(false);
        this.checkEmptyList();
    }

    private void checkEmptyList() {
        if(meetingAdapter.getItemCount() == 0){
            this.layoutMeetingList.setVisibility(View.GONE);
            this.layoutEmptyList.setVisibility(View.VISIBLE);
        }else {
            this.layoutMeetingList.setVisibility(View.VISIBLE);
            this.layoutEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MeetingInteractionListener) {
            mListener = (MeetingInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDialogFilterMeetingInteraction(int code) {
        swipeContainer.setRefreshing(true);
        switch (code){
            case DialogFilterMeeting.ALL_MEETINGS:
                typeRequest = DialogFilterMeeting.ALL_MEETINGS;
                requestQueue.add(getStringRequest(DialogFilterMeeting.ALL_MEETINGS));
                break;
            case DialogFilterMeeting.MY_GAMES_MEETINGS:
                typeRequest = DialogFilterMeeting.MY_GAMES_MEETINGS;
                genericList.clear();
                try {
                    genericList = buildListMeeting(Preferences.getUserGamesMeetings(getContext()));
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                refreshContent(genericList);
                break;
            case DialogFilterMeeting.MY_MEETINGS:
                typeRequest = DialogFilterMeeting.MY_MEETINGS;
                requestQueue.add(getStringRequest(DialogFilterMeeting.MY_MEETINGS));
                break;
            case DialogFilterMeeting.NEARBY_MEETINGS:
                typeRequest = DialogFilterMeeting.NEARBY_MEETINGS;
                refreshContent(meetingListNearby);
        }
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent = new Intent(getActivity(), NewMeetingActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            mSheetLayout.contractFab();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_meeting_dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter_meeting) {
            DialogFilterMeeting dialogFilterMeeting = DialogFilterMeeting.newInstance(this, typeRequest);
            dialogFilterMeeting.show(getFragmentManager(), "DialogFilterMeetings");
        }
        return true;
    }

    public interface MeetingInteractionListener {
        void meetingOnFragmentInteraction(Meeting item);
    }
}