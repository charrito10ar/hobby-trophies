package com.marbit.hobbytrophies.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marcelo on 6/12/16.
 */

public class Constants {
    public final static String TROPHY_TYPE_BRONZE = "bronze";
    public final static String TROPHY_TYPE_SILVER = "silver";
    public final static String TROPHY_TYPE_GOLD = "gold";
    public final static String TROPHY_TYPE_PLATINUM = "platinum";

    /*** PREFERENCES ***/
    public final static String PREFERENCE_USER_NAME = "USER-NAME";
    public static final String PREFERENCE_RANDOM_PSN_CODE = "RANDOM-PSN-CODE";
    public static final String PREFERENCE_IS_PSN_CODE_GENERATED = "PSN-CODE-GENERATED";
    public static final String PREFERENCE_IS_PSN_CODE_OK = "PSN-CODE-OK";
    public static final String PREFERENCE_IS_USER_LOGIN = "IS-USER-LOGIN";
    public static final String PREFERENCE_USER_AVATAR = "USER-AVATAR";
    public static final String PREFERENCE_USER_PROFILE_JSON = "USER-PROFILE";


    /** STATS ***/
    public static final String STATS_USER_GAMES = "STATS-USER-GAMES";
    public static final String STATS_MEETINGS = "STATS-MEETINGS";

    /** COUNT DOWN **/
    public final static long MS_EN_ANIO_MEDIO = 47335350000L;
    public final static long MS_PER_SEGUNDO = 1000;
    public final static long MS_PER_MINUTO = 60000;
    public final static long MS_PER_HORA = 3600000;
    public final static long MS_PER_DIA = 86400000;

    /** RANKING **/
    public final static String PREFERENCE_DAY_LAST_UPDATE_RANKING = "TIME_LAST_UPDATE_RANKING";
    public final static String PREFERENCE_RANKING_JSON = "RANKING_JSON";

    /** Market - Item **/
    public final static int PREFERENCE_ITEM_CATEGORY_GAME = 0;
    public final static int PREFERENCE_ITEM_CATEGORY_CONSOLE = 1;
    public final static int PREFERENCE_ITEM_CATEGORY_ACCESSORIES = 2;
    public final static int PREFERENCE_ITEM_CATEGORY_OTHER= 3;
    public static final String PREFERENCE_DAY_LAST_UPDATE_PROFILE = "TIME_LAST_UPDATE_PROFILE";
    public static final String PREFERENCE_FAVORITES_LIST = "LOCAL-FAVOURITES-ITEMS";

    public final static int PREFERENCE_CONSOLE_PS = 0;
    public final static int PREFERENCE_CONSOLE_PSP = 1;
    public final static int PREFERENCE_CONSOLE_PS2 = 2;
    public final static int PREFERENCE_CONSOLE_PS3 = 3;
    public final static int PREFERENCE_CONSOLE_PS4 = 4;
    public final static int PREFERENCE_CONSOLE_VITA = 5;
    public final static int PREFERENCE_CONSOLE_VR = 6;
    public static final String PREFERENCE_USER_ID = "USER-ID";
    public static final String BUY_TYPE = "BUYER";
    public static final String SELL_TYPE = "SELLER";
    public static final String PREFERENCE_USER_LOCALITY = "LOCALITY";
    public static final String PREFERENCE_USER_LATITUDE = "LATITUDE";
    public static final String PREFERENCE_USER_LONGITUDE = "LONGITUDE";
    public static final float TEN_KM = 10000;
}
