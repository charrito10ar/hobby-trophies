package com.marbit.hobbytrophies.dao;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDAO implements UserDAOInterface {

    private RequestQueue requestQueue;
    private Context context;

    public UserDAO(Context context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }
    @Override
    public void getUserBasicProfile(String userId, ListenerUserDAO listenerUserDAO) {
        requestQueue.add(getStringPostNewMessage(userId, listenerUserDAO)).setShouldCache(true);
    }

    private StringRequest getStringPostNewMessage(String userId, final ListenerUserDAO listenerUserDAO){
        return new StringRequest("http://www.hobbytrophies.com/foros/ps3/class/psnAPI.php?method=psnGetUser&sPSNID=" + userId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String avatar = jsonObject.getString("avatar");
                    User user = new User();
                    user.setAvatarUrl(avatar);
                    listenerUserDAO.loadUserBasicProfileSuccessful(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public interface ListenerUserDAO{
        void loadUserBasicProfileSuccessful(User user);
    }
}
