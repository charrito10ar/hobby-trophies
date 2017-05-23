package com.marbit.hobbytrophies.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.GameProfileAdapter;
import com.marbit.hobbytrophies.interfaces.ProfileFragmentView;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.overwrite.SecondaryMediumTextView;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.Network;
import com.marbit.hobbytrophies.utilities.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements ProfileFragmentView{

    private static final String ARG_PARAM1 = "USER-PSN-ID";
    private static final String ARG_PARAM2 = "IS-ANOTHER-PROFILE";

    private String userPsnId;

    private RecyclerView recyclerViewGames;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar levelProgressBar;
    private SecondaryMediumTextView levelTextView;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private ProfileOnFragmentInteractionListener mListener;
    private GameProfileAdapter gameAdapter;
    private boolean isFragmentActive = true;
    private boolean isAnotherProfile;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String userPsnId, boolean isAnotherProfile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userPsnId);
        args.putBoolean(ARG_PARAM2, isAnotherProfile);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfileFragment newInstance(String userPsnId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userPsnId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userPsnId = getArguments().getString(ARG_PARAM1);
            isAnotherProfile = getArguments().getBoolean(ARG_PARAM2, false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        this.recyclerViewGames = (RecyclerView) view.findViewById(R.id.recycler_view_games_profile);
        recyclerViewGames.setHasFixedSize(true);
        this.mLayoutManager = new LinearLayoutManager(getContext());
        this.recyclerViewGames.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewGames.getContext(), mLayoutManager.getOrientation());
        this.recyclerViewGames.addItemDecoration(dividerItemDecoration);
        this.swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.fragment_profile_swipe_refresh);

        this.gameAdapter = new GameProfileAdapter(getContext());
        this.recyclerViewGames.setAdapter(this.gameAdapter);

        String userName = userPsnId;
        this.requestQueue = Volley.newRequestQueue(getContext());
        this.stringRequest = this.getStringRequest(userName);

        swipeContainer.setRefreshing(true);
        swipeContainer.post(new Runnable() {
                                @Override
                                public void run() {
                                    requestQueue.add(stringRequest);
                                }
                            }
        );

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestQueue.add(stringRequest);
            }
        });

        this.levelProgressBar = (ProgressBar) view.findViewById(R.id.profile_progress_bar_level);
        this.levelTextView = (SecondaryMediumTextView) view.findViewById(R.id.profile_level_text);

        return view;
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
                            if(!isAnotherProfile){
                                Preferences.saveString((Activity)mListener, Constants.STATS_USER_GAMES, jsonArrayUserGames.toString());
                                Preferences.saveString((Activity)mListener, Constants.PREFERENCE_USER_AVATAR, user.getAvatarUrl());
                            }
                            refreshContentGames(user);
                            refreshContentUser(user);
                            swipeContainer.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeContainer.setRefreshing(false);
                            Toast.makeText((Activity)mListener, "Error en el formato de la respuesta", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                swipeContainer.setRefreshing(false);
                String message = Network.getErrorMessage(volleyError);
                if(isFragmentActive)
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void refreshContentUser(User user) {
        this.levelProgressBar.setProgress(user.getProgress());
        this.levelTextView.setText("Level: " + user.getLevel());

    }

    private void refreshContentGames(User user) {
        this.gameAdapter.clearAll();
        this.gameAdapter.setUserProfile(user);
        this.gameAdapter.notifyDataSetChanged();
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
            for(int j=0; j<platformsArray.length; j++){
                String platform = platformsArray[0];
                platforms.add(platform);
            }
            game.setPlatform(platforms);
            gameList.add(game);
        }
        return gameList;
    }

    @Override
    public void onPause(){
        super.onPause();
        this.isFragmentActive = false;
        this.requestQueue.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.isFragmentActive = true;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.profileOnFragmentInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileOnFragmentInteractionListener) {
            mListener = (ProfileOnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProfileOnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public interface ProfileOnFragmentInteractionListener {
        void profileOnFragmentInteractionListener(Uri uri);
    }
}
