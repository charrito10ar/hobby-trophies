package com.marbit.hobbytrophies.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.marbit.hobbytrophies.model.market.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Utilities {

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatNumber(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatNumber(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatNumber(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean itemIsFavourite(String id) {
        return false;
    }

    public static void addFavourite(Context context, Item item) {
        List favorites = loadFavorites(context);
        if (favorites == null)
            favorites = new ArrayList();
        favorites.add(item);
        storeFavorites(context, favorites);
    }

    public static void removeFavorite(Context context, Item item) {
        ArrayList favorites = loadFavorites(context);
        if (favorites != null) {
            favorites.remove(item);
            storeFavorites(context, favorites);
        }
    }


    private static void storeFavorites(Context context, List favorites) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SP.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(Constants.PREFERENCE_FAVORITES_LIST, jsonFavorites);
        editor.apply();
    }

    public static ArrayList loadFavorites(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        List favorites;
        if (SP.contains(Constants.PREFERENCE_FAVORITES_LIST)) {
            String jsonFavorites = SP.getString(Constants.PREFERENCE_FAVORITES_LIST, null);
            Gson gson = new Gson();
            Item[] favoriteItems = gson.fromJson(jsonFavorites,Item[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList(favorites);
        } else
            return new ArrayList();
        return (ArrayList) favorites;
    }

    public static boolean isFavorite(Context context, Item item) {
        ArrayList favorites = loadFavorites(context);
        return favorites != null && favorites.contains(item);
    }

    public static String getItemTypeString(int itemType) {
        switch (itemType){
            case Constants.PREFERENCE_ITEM_CATEGORY_GAME:
                return "Mira este juego que encontré en HobbyTrophies: ";
            case Constants.PREFERENCE_ITEM_CATEGORY_CONSOLE:
                return "Mira esta consola que encontré en HobbyTrophies: ";
            case Constants.PREFERENCE_ITEM_CATEGORY_ACCESSORIES:
                return "Mira este accesorio que encontré en HobbyTrophies: ";
            default:
                return "Mira esto que encontré en HobbyTrophies: ";
        }
    }
}
