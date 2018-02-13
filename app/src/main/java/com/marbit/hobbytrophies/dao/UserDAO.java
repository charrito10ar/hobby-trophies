package com.marbit.hobbytrophies.dao;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marbit.hobbytrophies.dao.bodies.LocationUser;
import com.marbit.hobbytrophies.model.User;
import com.marbit.hobbytrophies.model.meeting.Location;
import com.marbit.hobbytrophies.utilities.DataBaseConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class UserDAO implements UserDAOInterface {

    private RequestQueue requestQueue;
    private Context context;
    private final FirebaseDatabase database;
    private final DatabaseReference databaseReference;

    public UserDAO(Context context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference();
    }
    @Override
    public void getUserBasicProfile(String userId, ListenerUserDAO listenerUserDAO) {
        requestQueue.add(getStringPostNewMessage(userId, listenerUserDAO)).setShouldCache(true);
    }

    @Override
    public void getUserLocation(String userId, final ListenerUserLocationDAO listenerUserLocationDAO) {
        databaseReference.child(DataBaseConstants.COLUMN_USER_LOCATION).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LocationUser location = dataSnapshot.getValue(LocationUser.class);
                listenerUserLocationDAO.loadUserLocationSuccessful(location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerUserLocationDAO.loadUserLocationError(databaseError.getMessage());
            }
        });
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

    public interface ListenerUserLocationDAO{
        void loadUserLocationSuccessful(LocationUser location);
        void loadUserLocationError(String errorMessage);
    }
}
