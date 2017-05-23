package com.marbit.hobbytrophies;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.adapters.TrophyAdapter;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.HeaderListTrophy;
import com.marbit.hobbytrophies.model.Trophy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameDetailActivity extends AppCompatActivity {
    private Game game;
    private ImageView imageGameDetail;
    private RequestQueue requestQueue;
    private RecyclerView recyclerViewTrophies;
    private TrophyAdapter trophyAdapter;
    private List<Trophy> trophies;
    private List<Object> genericList;
    private SwipeRefreshLayout swipeContainer;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_datail);

        this.recyclerViewTrophies = (RecyclerView) findViewById(R.id.recycler_view_trophies);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerViewTrophies.setLayoutManager(mLayoutManager);
        this.swipeContainer = (SwipeRefreshLayout) findViewById(R.id.game_detail_swipe_refresh);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.game = getIntent().getParcelableExtra("GAME");
        toolbar.setTitle(game.getName());

        this.trophyAdapter = new TrophyAdapter(GameDetailActivity.this, this.game.getId());
        this.recyclerViewTrophies.setAdapter(trophyAdapter);
        this.imageGameDetail = (ImageView) findViewById(R.id.image_game_detail);
        this.trophies = new ArrayList<>();
        this.genericList = new ArrayList<>();

        Picasso.with(getApplicationContext()).load(game.getImg()).fit().into(this.imageGameDetail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(getApplicationContext());


        stringRequest = new StringRequest(Request.Method.GET,
                "http://hobbytrophies.com/foros/ps3/get-trofeos.php?id=" + game.getId(),
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
                                String idDlc = jsonObjectTrophy.getString("id_dlc");
                                if(!idDlc.equals("null")){
                                    trophy.setIdDlc(idDlc);
                                    if(!trophies.get(i-1).getIdDlc().equals(idDlc)){
                                        JSONObject jsonArrayDlcs = jsonObject.getJSONObject("dlcs");
                                        JSONObject jsonObjectDcl = jsonArrayDlcs.getJSONObject(idDlc);
                                        HeaderListTrophy headerListTrophy = new HeaderListTrophy();
                                        headerListTrophy.setId(idDlc);
                                        headerListTrophy.setName(jsonObjectDcl.getString("titol"));
                                        headerListTrophy.setUrl(jsonObjectDcl.getString("imatge"));
                                        genericList.add(headerListTrophy);
                                    }
                                }
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
                Toast.makeText(GameDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        this.trophyAdapter.clearAll();
        this.trophyAdapter.setList(genericList);
        this.trophyAdapter.notifyDataSetChanged();
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
