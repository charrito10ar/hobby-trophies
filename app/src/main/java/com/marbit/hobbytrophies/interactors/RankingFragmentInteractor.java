package com.marbit.hobbytrophies.interactors;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.interfaces.RankingPresenterInterface;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.utilities.Constants;
import com.marbit.hobbytrophies.utilities.Network;
import com.marbit.hobbytrophies.utilities.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RankingFragmentInteractor {
    private Context context;
    private RankingPresenterInterface presenter;
    private RequestQueue requestQueue;
    private Calendar calendar = Calendar.getInstance();


    public RankingFragmentInteractor(Context context, RankingPresenterInterface presenter) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.presenter = presenter;
    }

    public void loadRemoteRanking() {
        int month = calendar.get(Calendar.MONTH);
        requestQueue.add(getStringRequest(month + 1));
    }

    private StringRequest getStringRequest(int month){
        String url = "http://www.hobbytrophies.com/foros/ps3/get-ranking.php?year=2017&month=" + month;
        return new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Preferences.saveString(context, Constants.PREFERENCE_RANKING_JSON, response);
                            Preferences.saveLong(context, Constants.PREFERENCE_DAY_LAST_UPDATE_RANKING, calendar.get(Calendar.DAY_OF_MONTH));
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayLastWinner = jsonObject.getJSONArray("last-podium");
                            JSONArray jsonArrayRankingList = jsonObject.getJSONArray("ranking");
                            List<User> lastPodium = getLastPodium(jsonArrayLastWinner);
                            List<User> rankingList = getRankingList(jsonArrayRankingList);
                            presenter.onSuccessLoadRanking(lastPodium, rankingList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error en el formato de la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = Network.getErrorMessage(volleyError);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<User> getLastPodium(JSONArray jsonArrayLastPodium) throws JSONException {
        List<User> rankingList = new ArrayList<>();
        for(int i = 0; i<jsonArrayLastPodium.length(); i++) {
            User user = new User();
            try {
                JSONObject jsonUser = (JSONObject) jsonArrayLastPodium.get(i);
                user.setPsnId(jsonUser.getString("id_usuari"));
                user.setAvatarUrl(jsonUser.getString("avatar"));
                user.setPoints(Integer.valueOf(jsonUser.getString("punts")));
                rankingList.add(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rankingList;
    }

    public List<User> getRankingList(JSONArray jsonArrayRankingList) {
        List<User> rankingList = new ArrayList<>();
        for(int i = 0; i<jsonArrayRankingList.length(); i++){
            User user = new User();
            try {
                JSONObject jsonUser = (JSONObject) jsonArrayRankingList.get(i);
                user.setPsnId(jsonUser.getString("id_usuari"));
                user.setAvatarUrl(jsonUser.getString("avatar"));
                user.setPoints(Integer.valueOf(jsonUser.getString("punts")));
                rankingList.add(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rankingList;
    }

    public void loadLocalRanking() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(Preferences.getString(context, Constants.PREFERENCE_RANKING_JSON));
            JSONArray jsonArrayLastWinner = jsonObject.getJSONArray("last-podium");
            JSONArray jsonArrayRankingList = jsonObject.getJSONArray("ranking");
            List<User> lastPodium = getLastPodium(jsonArrayLastWinner);
            List<User> rankingList = getRankingList(jsonArrayRankingList);
            presenter.onSuccessLoadRanking(lastPodium, rankingList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
