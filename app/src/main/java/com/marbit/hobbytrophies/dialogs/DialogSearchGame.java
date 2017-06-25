package com.marbit.hobbytrophies.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.R;
import com.marbit.hobbytrophies.dialogs.adapter.SearchGameAdapter;
import com.marbit.hobbytrophies.fragments.market.FragmentItemDetail;
import com.marbit.hobbytrophies.model.Game;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogSearchGame extends DialogFragment implements View.OnClickListener, SearchGameAdapter.SearchGameAdapterListener {

    private RequestQueue requestQueue;
    private SearchGameAdapter gameAdapter;
    private List<Game> gameList;

    private StringRequest stringRequest;
    private int pageNumber = 1;
    private String textToSearch = "";

    private int pageAmount;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerViewGames;
    private EditText editTextGame;
    private ImageButton buttonSearch;
    private DialogSearchGameListener mListener;

    public static DialogSearchGame newInstance() {
        DialogSearchGame dialogSearchGame = new DialogSearchGame();
        return dialogSearchGame;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_search_game, null);
        builder.setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        this.editTextGame = (EditText) view.findViewById(R.id.edit_text_search_game);
        this.buttonSearch = (ImageButton) view.findViewById(R.id.button_search_game);
        this.buttonSearch.setOnClickListener(this);
        this.recyclerViewGames =  (RecyclerView) view.findViewById(R.id.recycler_view_games);
        this.mLayoutManager = new LinearLayoutManager(getContext());
        this.recyclerViewGames.setLayoutManager(mLayoutManager);

        this.gameList = new ArrayList<>();

        this.gameAdapter = new SearchGameAdapter(getContext(), this);
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


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                textToSearch = "";
                requestQueue.add(stringRequest);
            }
        });


        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getTargetFragment() instanceof DialogSearchGameListener) {
            mListener = (DialogSearchGameListener) getTargetFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DialogSearchGameListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    @Override
    public void onClick(View v) {
        this.textToSearch(this.editTextGame.getText().toString());
    }

    private void textToSearch(String s) {
        this.textToSearch = s;
        this.swipeContainer.setRefreshing(true);
        this.gameList.clear();
        this.pageNumber = 1;
        this.requestQueue.add(this.getStringRequest());
    }

    @Override
    public void selectGame(Game game) {
        mListener.selectGame(game);
        dismiss();
    }

    public interface DialogSearchGameListener{
        void selectGame(Game gameId);
    }
}
