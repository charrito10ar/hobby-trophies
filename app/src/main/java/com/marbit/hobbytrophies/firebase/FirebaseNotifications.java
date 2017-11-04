package com.marbit.hobbytrophies.firebase;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marbit.hobbytrophies.firebase.dao.FirebaseNotificationDAO;
import com.marbit.hobbytrophies.firebase.interfaces.FirebaseNotificationsInterface;
import com.marbit.hobbytrophies.model.market.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FirebaseNotifications implements FirebaseNotificationsInterface {

    private RequestQueue requestQueue;
    private Context context;


    public FirebaseNotifications(Context context){
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public void sendNotificationMarketChat(final String userSender, String userTo, final String itemTitle, final String message, final String itemId, final String chatId) throws JSONException {

        FirebaseNotificationDAO firebaseNotificationDAO = new FirebaseNotificationDAO();
        firebaseNotificationDAO.getRegistrationTokens(userTo, new FirebaseNotificationDAO.FirebaseNotificationDAOListener() {
            @Override
            public void loadRegistrationTokensSuccess(JSONArray registrationTokens) throws JSONException {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user-sender", userSender);
                jsonObject.put("text", message);
                jsonObject.put("chatId", chatId);
                jsonObject.put("itemId", itemId);
                jsonObject.put("itemTitle", itemTitle);
                jsonObject.put("tokensArray", registrationTokens);
                requestQueue.add(getStringPostNewMessage(jsonObject));
            }
        });
    }

    private JsonObjectRequest getStringPostNewMessage(JSONObject jsonParams){
        return new JsonObjectRequest(Request.Method.POST,"http://www.hobbytrophies.com/foros/ps3/inc/new-chat-message.php", jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("response-code");
                            int messageId = response.getInt("message-id");
                            if(responseCode == 1){
                                Log.d("Notificacion", "Correcta: " + messageId);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
    }

    @Override
    public void sendNotificationWishListMatch(List<String> userToList, final Item item) throws JSONException {
        FirebaseNotificationDAO firebaseNotificationDAO = new FirebaseNotificationDAO();
        firebaseNotificationDAO.getRegistrationTokens(userToList, new FirebaseNotificationDAO.FirebaseNotificationDAOListener() {
            @Override
            public void loadRegistrationTokensSuccess(JSONArray registrationTokens) throws JSONException {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("itemId", item.getId());
                jsonObject.put("itemTitle", item.getTitle());
                jsonObject.put("tokensArray", registrationTokens);
                requestQueue.add(getStringPostWishMatch(jsonObject));
            }
        });
    }

    private JsonObjectRequest getStringPostWishMatch(JSONObject jsonParams) {
        return new JsonObjectRequest(Request.Method.POST,"http://www.hobbytrophies.com/foros/ps3/inc/wish-match.php", jsonParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int responseCode = response.getInt("response-code");
                            if(responseCode == 1){
                                Log.d("Notificacion", "Correcta: ");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("onErrorResponse: ", error.getMessage());
            }
        });
    }


}

