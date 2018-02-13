package com.marbit.hobbytrophies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.marbit.hobbytrophies.dao.bodies.LocationUser;
import com.marbit.hobbytrophies.model.Game;
import com.marbit.hobbytrophies.model.meeting.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Preferences {

    public static void saveString(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SP.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String getString(Context context, String preferenceName) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String valor = SP.getString(preferenceName, "null");
        return valor;
    }

    public static void saveInt(Context context, String preferenceName, int preferenceValue) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SP.edit();
        editor.putInt(preferenceName, preferenceValue);
        editor.apply();
    }

    public static int getInt(Context context, String preferenceName) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        return SP.getInt(preferenceName, -1);
    }

    private static void saveDouble(Context context, double preferenceValue, String preferenceName) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SP.edit();
        editor.putString(preferenceName, String.valueOf(preferenceValue));
        editor.apply();
    }

    public static void saveLong(Context context, String preferenceName, long preferenceValue) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SP.edit();
        editor.putLong(preferenceName, preferenceValue);
        editor.apply();
    }

    public static long getLong(Context context, String preferenceName) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        return SP.getLong(preferenceName, -1);
    }

    public static void saveBoolean(Context context, String preferenceName, boolean b) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SP.edit();
        editor.putBoolean(preferenceName, b);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String preferenceName) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        return SP.getBoolean(preferenceName, false);
    }

    public static void logOut(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SP.edit();
        editor.clear().apply();
    }

    /** USER **/

    public static void saveUserName(Context context, String userName) {
        saveString(context, Constants.PREFERENCE_USER_NAME, userName);
    }

    public static String getUserName(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String userName = SP.getString(Constants.PREFERENCE_USER_NAME, "Invitado");
        return userName;
    }

    public static void saveUserId(Context context, String userId) {
        saveString(context, Constants.PREFERENCE_USER_ID, userId);
    }

    public static String getUserId(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String userName = SP.getString(Constants.PREFERENCE_USER_ID, "null");
        return userName;
    }

    public static String getAvatar(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String avatar = SP.getString(Constants.PREFERENCE_USER_AVATAR, "null");
        return avatar;
    }

    public static String[] getGameTitle(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonUserGames = SP.getString(Constants.STATS_USER_GAMES, "null");
        String[] gameNamesArray = null;
        try {
            JSONArray jsonArrayGames = new JSONArray(jsonUserGames);
            gameNamesArray = new String[jsonArrayGames.length()];
            for (int i=0; i<jsonArrayGames.length(); i++){
                JSONObject jsonObjectGame = (JSONObject) jsonArrayGames.get(i);// Si manu se pone pesado con psp2, hacer el if acá
                gameNamesArray[i] =  jsonObjectGame.getString("title") + " - " + convertPlatform(jsonObjectGame.getString("platform"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gameNamesArray ;
    }

    private static String convertPlatform(String platform){
        switch (platform.toLowerCase()){
            case "psp2":
                return "VITA";
            case "ps3":
                return "PS3";
            case "ps4":
                return "PS4";
            default:
                return "";
        }
    }

    public static List<Game> getGameList(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonUserGames = SP.getString(Constants.STATS_USER_GAMES, "null");
        ArrayList<Game> gameArrayList = new ArrayList<>();
        try {
            JSONArray jsonArrayGames = new JSONArray(jsonUserGames);
            for (int i=0; i<jsonArrayGames.length(); i++){
                JSONObject jsonObjectGame = (JSONObject) jsonArrayGames.get(i);
                Game game = new Game();
                game.setId(jsonObjectGame.getString("npcommid"));
                game.setName(jsonObjectGame.getString("title"));
                game.setImg("http://hobbytrophies.com/i/j/" + jsonObjectGame.getString("npcommid") + ".png");
                String[] platformsArray = jsonObjectGame.getString("platform").split(",");
                List<String> platforms = new ArrayList<>();
                Collections.addAll(platforms, platformsArray);
                game.setPlatform(platforms);
                gameArrayList.add(game);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gameArrayList ;
    }

    public static Game getGame(Context context, String gameId) {
        List<Game> gameArrayList = getGameList(context);
        for(int i = 0; i < gameArrayList.size(); i++){
            if(gameId.equals(gameArrayList.get(i).getId())){
                return gameArrayList.get(i);
            }
        }
        return new Game();
    }

    public static String getUserGamesMeetings(Context context) throws JSONException {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonMeetings = SP.getString(Constants.STATS_MEETINGS, "null");
        JSONObject jsonObject = new JSONObject(jsonMeetings);
        List<Game> userGames = getGameList(context);
        JSONArray jsonArrayResponse = new JSONArray();
        try {
            JSONArray jsonArrayMeetings = jsonObject.getJSONArray("meetings");
            for (int i=0; i<jsonArrayMeetings.length(); i++){
                JSONObject jsonObjectMeeting = (JSONObject) jsonArrayMeetings.get(i);
                Game game = new Game(jsonObjectMeeting.getString("game_id"));
                if(userGames.contains(game)){
                    jsonArrayResponse.put(jsonObjectMeeting);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObjectResponse = new JSONObject();
        jsonObjectResponse.put("meetings", jsonArrayResponse);
        return jsonObjectResponse.toString();
    }

    public static boolean isLoadRemoteRanking(Context context){
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        long lastDayUpdate = Preferences.getLong(context, Constants.PREFERENCE_DAY_LAST_UPDATE_RANKING);
        if(currentDayOfMonth != lastDayUpdate || lastDayUpdate == -1){
            return true;
        }
        return false;
    }
    public static boolean isLoadRemoteProfile(Context context){
        Calendar calendar = Calendar.getInstance();
        int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        long lastDayUpdate = Preferences.getLong(context, Constants.PREFERENCE_DAY_LAST_UPDATE_PROFILE);
        if(currentDayOfMonth != lastDayUpdate || lastDayUpdate == -1){
            return true;
        }
        return false;
    }

    public static void saveUserLocation(Context context, LocationUser locationUser) {
        Preferences.saveString(context, locationUser.getLocality(), Constants.PREFERENCE_USER_LOCALITY);
        Preferences.saveDouble(context, locationUser.getLatitud(), Constants.PREFERENCE_USER_LATITUDE);
        Preferences.saveDouble(context, locationUser.getLongitud(), Constants.PREFERENCE_USER_LONGITUDE);
    }

    public static Location getUserLocation(Context context){
        if(!Preferences.getString(context, Constants.PREFERENCE_USER_LATITUDE).equals("null")){
            return new Location(Preferences.getString(context, Constants.PREFERENCE_USER_LOCALITY),
                    Double.valueOf(Preferences.getString(context, Constants.PREFERENCE_USER_LATITUDE)),
                    Double.valueOf(Preferences.getString(context, Constants.PREFERENCE_USER_LONGITUDE)));
        }else {
            return new Location("Sin ubicación", 0, 0);
        }
    }
}