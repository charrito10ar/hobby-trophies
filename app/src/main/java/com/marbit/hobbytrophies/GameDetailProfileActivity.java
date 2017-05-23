package com.marbit.hobbytrophies;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.adapters.TrophyProfileAdapter;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.Trophy;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.overwrite.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameDetailProfileActivity extends AppCompatActivity {

    private Game game;
    private ImageView imageGameDetail;
    private ImageView imageUserProfile;
    private RequestQueue requestQueue;
    private RecyclerView recyclerViewTrophies;
    private TrophyProfileAdapter trophyProfileAdapter;
    private List<Trophy> trophies;
    private List<Object> genericList;
    private SwipeRefreshLayout swipeContainer;
    private StringRequest stringRequest;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail_profile);
        this.game = getIntent().getParcelableExtra("GAME");
        this.user = getIntent().getParcelableExtra("USER");

        this.recyclerViewTrophies = (RecyclerView) findViewById(R.id.recycler_view_trophies_profile);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerViewTrophies.setLayoutManager(mLayoutManager);
        this.swipeContainer = (SwipeRefreshLayout) findViewById(R.id.game_detail_profile_swipe_refresh);

        this.trophyProfileAdapter = new TrophyProfileAdapter(GameDetailProfileActivity.this, this.game.getId());
        this.recyclerViewTrophies.setAdapter(trophyProfileAdapter);
        this.trophies = new ArrayList<>();
        this.genericList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar.setTitle(game.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.imageGameDetail = (ImageView) findViewById(R.id.image_game_detail_profile);
        this.imageUserProfile = (ImageView) findViewById(R.id.image_user_profile);
        Picasso.with(getApplicationContext()).load(game.getImg()).fit().into(this.imageGameDetail);
        Picasso.with(getApplicationContext()).load(user.getAvatarUrl()).fit()
                .centerCrop()
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .transform(new CircleTransform())
                .into(this.imageUserProfile);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        stringRequest = new StringRequest(Request.Method.GET,
                "http://hobbytrophies.com/foros/ps3/get-juego-usuario.php?sPSNID=" + user.getPsnId() + "&id=" + game.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeContainer.setRefreshing(false);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray jsonArrayTrophies = jsonObject.getJSONArray("trophies");

                            trophies.clear();
                            genericList.clear();
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
                                genericList.add(trophy);
                            }
                            game.setTrophies(trophies);
                            refreshContent(genericList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeContainer.setRefreshing(false);
                Log.d("ERROR", error.toString());
                Toast.makeText(GameDetailProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

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

    }

    private void refreshContent(List<Object> genericList) {
        this.trophyProfileAdapter.clearAll();
        this.trophyProfileAdapter.setList(genericList);
        this.trophyProfileAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
