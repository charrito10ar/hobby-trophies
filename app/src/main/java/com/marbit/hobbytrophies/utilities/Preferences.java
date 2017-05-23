package com.marbit.hobbytrophies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.marbit.hobbytrophies.model.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by marcelo on 14/12/16.
 */


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

    public static String getUserName(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String userName = SP.getString(Constants.PREFERENCE_USER_NAME, "Invitado");
        return userName;
    }

    public static String getAvatar(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String userName = SP.getString(Constants.PREFERENCE_USER_AVATAR, "null");
        return userName;
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
                game.setImg(jsonObjectGame.getString("image"));
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
}
