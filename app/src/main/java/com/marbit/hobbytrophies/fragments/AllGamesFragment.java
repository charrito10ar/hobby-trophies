package com.marbit.hobbytrophies.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.adapters.GameAdapter;
import com.marbit.hobbytrophies.dialogs.DialogAbcdiario;
import com.marbit.hobbytrophies.model.Game;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllGamesFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RequestQueue requestQueue;
    private GameAdapter gameAdapter;
    private List<Game> gameList;

    private StringRequest stringRequest;
    private int pageNumber = 1;
    private String textToSearch = "";
    private SearchView searchView;


    private int pageAmount;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerViewGames;

    private DialogAbcdiario dialogAbcdiario;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AllGamesFragment() {
        // Required empty public constructor
    }

    public static AllGamesFragment newInstance(String param1, String param2) {
        AllGamesFragment fragment = new AllGamesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_games, container, false);

        this.recyclerViewGames =  (RecyclerView) view.findViewById(R.id.recycler_view_games);
        this.mLayoutManager = new LinearLayoutManager(getContext());
        this.recyclerViewGames.setLayoutManager(mLayoutManager);

        this.gameList = new ArrayList<>();

        this.gameAdapter = new GameAdapter(getContext());
        this.recyclerViewGames.setAdapter(this.gameAdapter);
        this.swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.main_swipe_refresh);

                recyclerViewGames.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if(dy > 0) //check for scroll down{
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount - 5) {
                            loading = false;
                            loadNextDataFromApi();
                        }
                    }
                }
        });

        this.requestQueue = Volley.newRequestQueue(getContext());

        this.stringRequest = this.getStringRequest();

        swipeContainer.setRefreshing(true);
        swipeContainer.post(new Runnable() {
                                @Override
                                public void run() {
                                    pageNumber = 1;
                                    textToSearch = "";
                                    requestQueue.add(stringRequest);
                                }
                            }
        );

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                textToSearch = "";
                requestQueue.add(stringRequest);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatin_action_new_meeting);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAbcdiario = DialogAbcdiario.newInstance();
                dialogAbcdiario.show(getFragmentManager(), "dialog");
            }
        });

        return view;
    }


    private StringRequest getStringRequest(){
        return new StringRequest(Request.Method.POST,
                "http://hobbytrophies.com/foros/ps3/inc/cercar_joc.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeContainer.setRefreshing(false);
                        Log.d("onResponse", textToSearch);
                        Log.d("response", response);
                        if(pageNumber == 1){
                            gameList.clear();
                        }

                        Document doc = Jsoup.parse(response);

                        for (Element table : doc.select("table")) {
                            for (Element row : table.select("tr")) {
                                Elements tds = row.select("td");
                                if (tds.size() > 5) {
                                    Game game = new Game();
                                    //game.setId();
                                    String srcImage = tds.get(0).getElementsByTag("img").attr("src");
                                    game.setId(srcImage.replace("/i/j/", "").replace(".png", ""));
                                    game.setImg("http://hobbytrophies.com" + tds.get(0).getElementsByTag("img").attr("src"));
                                    game.setName(tds.get(1).getElementsByTag("a").text());
                                    game.setTrophiesUrl("http://hobbytrophies.com" + tds.get(1).getElementsByTag("a").attr("href"));

                                    Elements hijos = tds.get(2).children();
                                    List<String> platforms = new ArrayList<>();
                                    for(Element child: hijos){
                                        String platform = child.getElementsByTag("img").attr("src").substring(15).replace(".png", "");
                                        platforms.add(platform);
                                    }
                                    game.setPlatform(platforms);

                                    Elements trophies = tds.get(4).child(0).children();
                                    game.setPlatinumAmount(trophies.get(0).child(1).html());
                                    game.setGoldenAmount(trophies.get(1).child(1).html());
                                    game.setSilverAmount(trophies.get(2).child(1).html());
                                    game.setBronzeAmount(trophies.get(3).child(1).html());

                                    gameList.add(game);
                                }
                            }
                        }

                        pageAmount = doc.getElementsByTag("ul").select("li").size();


                        refreshContent(gameList);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeContainer.setRefreshing(false);
                Log.d("ERROR", error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cerca", textToSearch);
                params.put("plataforma", "");
                params.put("pag", String.valueOf(pageNumber)    );
                return params;
            };

        };
    }

    public void loadNextDataFromApi() {
        if(pageNumber < pageAmount){
            pageNumber++;
            requestQueue.add(stringRequest);
        }
    }


    private void refreshContent(List<Game> gameList) {
        this.gameAdapter.clearAll();
        this.gameAdapter.setList(gameList);
        this.gameAdapter.notifyDataSetChanged();
        this.loading = true;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void clickLetter(String letter){
        this.textToSearch = letter;
        this.swipeContainer.setRefreshing(true);
        this.gameList.clear();
        this.pageNumber = 1;
        this.requestQueue.add(this.getStringRequest());
        this.dialogAbcdiario.dismiss();
    }

    public void textToSearch(String query){
        this.textToSearch = query;
        this.swipeContainer.setRefreshing(true);
        this.gameList.clear();
        this.pageNumber = 1;
        this.requestQueue.add(this.getStringRequest());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.textToSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
