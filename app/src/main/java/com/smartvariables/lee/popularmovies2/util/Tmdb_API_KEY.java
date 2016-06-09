package com.smartvariables.lee.popularmovies2.util;

public class Tmdb_API_KEY {
    private final static String TAG = "LEE: <" + Tmdb_API_KEY.class.getSimpleName() + ">";
    final private static String TMDB_BASE_URL = "http://image.tmdb.org/t/p/";

    final private static String API_KEY = "ee543441648038963595e9e2c0058c0f";

    public static String getBaseUrl() {
        return TMDB_BASE_URL;
    }

    public static String getKey() {
        return API_KEY;
    }
}
