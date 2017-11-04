package com.marbit.hobbytrophies.fragments.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.profile.GameProfileAdapter;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.Network;
import com.marbit.hobbytrophies.utilities.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class ProfileTrophiesFragment extends Fragment {

    private static final String ARG_PARAM1 = "user-name";
    private GameProfileAdapter gameAdapter;
    private SwipeRefreshLayout swipeContainer;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private String userName;
    private OnListenerProfileTrophiesFragment mListener;

    public static ProfileTrophiesFragment newInstance(String userPsnId) {
        ProfileTrophiesFragment fragment = new ProfileTrophiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userPsnId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getFragmentManager().findFragmentByTag("Profile") instanceof OnListenerProfileTrophiesFragment) {
            mListener = (OnListenerProfileTrophiesFragment) getFragmentManager().findFragmentByTag("Profile");
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListenerProfileTrophiesFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_profile_trophies, container, false);
        this.gameAdapter = new GameProfileAdapter(getContext());
        RecyclerView recyclerViewGames = (RecyclerView) view.findViewById(R.id.recycler_view_games_profile);
        recyclerViewGames.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewGames.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewGames.getContext(), mLayoutManager.getOrientation());
        recyclerViewGames.addItemDecoration(dividerItemDecoration);
        recyclerViewGames.setAdapter(this.gameAdapter);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        this.requestQueue = Volley.newRequestQueue(getContext());
        this.stringRequest = this.getStringRequest(userName);
        this.swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.fragment_profile_swipe_refresh);
        swipeContainer.setRefreshing(true);
        swipeContainer.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Preferences.isLoadRemoteProfile(getContext()) || !Preferences.getUserName(getContext()).equals(userName)){
                                        requestQueue.add(stringRequest);
                                    }else {
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(Preferences.getString(getContext(), Constants.PREFERENCE_USER_PROFILE_JSON));
                                            User user = new User();
                                            user.setPsnId(jsonObject.getString("psn_id"));
                                            user.setCountry(jsonObject.getString("country"));
                                            user.setAvatarUrl(jsonObject.getString("avatar"));
                                            user.setColor(jsonObject.getString("color"));
                                            if (jsonObject.getString("psnplus").equals("1"))
                                                user.setPsnPlus(true);
                                            user.setPoints(jsonObject.getInt("points"));
                                            user.setLevel(jsonObject.getInt("level"));
                                            user.setProgress(jsonObject.getInt("progress"));
                                            user.setPlatinum(jsonObject.getInt("real_platinum"));
                                            user.setGold(jsonObject.getInt("real_gold"));
                                            user.setSilver(jsonObject.getInt("real_silver"));
                                            user.setBronze(jsonObject.getInt("real_bronze"));
                                            user.setTotal(jsonObject.getInt("real_total"));
                                            user.setPercentTrophiesCompleted(jsonObject.getDouble("completion_percentage"));
                                            JSONArray jsonArrayUserGames = jsonObject.getJSONArray("games");
                                            user.setGamesList(getGameListFromJson(jsonArrayUserGames));
                                            if(userName.equals(Preferences.getUserName(getContext()))){
                                                Preferences.saveString(getContext(), Constants.STATS_USER_GAMES, jsonArrayUserGames.toString());
                                                Preferences.saveString(getContext(), Constants.PREFERENCE_USER_AVATAR, user.getAvatarUrl());
                                            }
                                            refreshContentGames(user);
                                            refreshContentUser(user);
                                            swipeContainer.setRefreshing(false);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }                                    }
                                }
                            }
        );

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        requestQueue.cancelAll(stringRequest);
    }

    private StringRequest getStringRequest(String user){
        return new StringRequest(Request.Method.POST,
                "http://www.hobbytrophies.com/foros/ps3/class/psnAPI.php?method=psnGetUserGames&sPSNID=" + user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        User user = new User();
                        try {
                            if(getContext() != null) {
                                Calendar calendar = Calendar.getInstance();
                                jsonObject = new JSONObject(response);
                                user.setPsnId(jsonObject.getString("psn_id"));
                                user.setCountry(jsonObject.getString("country"));
                                user.setAvatarUrl(jsonObject.getString("avatar"));
                                user.setColor(jsonObject.getString("color"));
                                if (jsonObject.getString("psnplus").equals("1"))
                                    user.setPsnPlus(true);
                                user.setPoints(jsonObject.getInt("points"));
                                user.setLevel(jsonObject.getInt("level"));
                                user.setProgress(jsonObject.getInt("progress"));
                                user.setPlatinum(jsonObject.getInt("real_platinum"));
                                user.setGold(jsonObject.getInt("real_gold"));
                                user.setSilver(jsonObject.getInt("real_silver"));
                                user.setBronze(jsonObject.getInt("real_bronze"));
                                user.setTotal(jsonObject.getInt("real_total"));
                                user.setPercentTrophiesCompleted(jsonObject.getDouble("completion_percentage"));
                                JSONArray jsonArrayUserGames = jsonObject.getJSONArray("games");
                                user.setGamesList(getGameListFromJson(jsonArrayUserGames));
                                if (userName.equals(Preferences.getUserName(getContext()))) {
                                    Preferences.saveString(getContext(), Constants.STATS_USER_GAMES, jsonArrayUserGames.toString());
                                    Preferences.saveString(getContext(), Constants.PREFERENCE_USER_AVATAR, user.getAvatarUrl());
                                    Preferences.saveString(getContext(), Constants.PREFERENCE_USER_PROFILE_JSON, response);
                                    Preferences.saveLong(getContext(), Constants.PREFERENCE_DAY_LAST_UPDATE_PROFILE, calendar.get(Calendar.DAY_OF_MONTH));
                                }

                                refreshContentGames(user);
                                refreshContentUser(user);
                                swipeContainer.setRefreshing(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeContainer.setRefreshing(false);
                            Toast.makeText(getContext(), "Error en el formato de la respuesta", Toast.LENGTH_SHORT).show();
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

    private void refreshContentGames(User user) {
        setUser(user);
    }

    private void refreshContentUser(User user) {
        mListener.refreshContentUser(user);
    }

    private List<Game> getGameListFromJson(JSONArray jsonArrayUserGames) throws JSONException {
        List<Game> gameList = new ArrayList<>();
        for (int i = 0; i<jsonArrayUserGames.length(); i++){
            JSONObject gameJson = (JSONObject) jsonArrayUserGames.get(i);
            Game game = new Game();
            game.setId(gameJson.getString("npcommid"));
            game.setImg("http://hobbytrophies.com/i/j/" + gameJson.getString("npcommid") + ".png");
            game.setName(gameJson.getString("title"));
            game.setPlatinumAmount(gameJson.getString("platinum"));
            game.setGoldenAmount(gameJson.getString("gold"));
            game.setSilverAmount(gameJson.getString("silver"));
            game.setBronzeAmount(gameJson.getString("bronze"));
            game.setPercentComplete(gameJson.getDouble("percentage"));
            game.setLastUpdate(gameJson.getInt("last_updated"));
            String[] platformsArray = gameJson.getString("platform").split(",");
            List<String> platforms = new ArrayList<>();
            Collections.addAll(platforms, platformsArray);
            game.setPlatform(platforms);
            gameList.add(game);
        }
        return gameList;
    }

    public void setUser(User user) {
        this.gameAdapter.clearAll();
        this.gameAdapter.setUserProfile(user);
        this.gameAdapter.notifyDataSetChanged();
    }

    public interface OnListenerProfileTrophiesFragment{
        void refreshContentUser(User user);
    }
}