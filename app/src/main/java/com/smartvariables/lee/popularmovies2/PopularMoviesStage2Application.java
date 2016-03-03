package com.smartvariables.lee.popularmovies2;

import android.app.Application;
import android.content.Context;

public class PopularMoviesStage2Application extends Application {
    private final static String TAG = "LEE: <" + PopularMoviesStage2Application.class.getSimpleName() + ">";

    private static Context sContext;

    public void onCreate() {
        super.onCreate();
        PopularMoviesStage2Application.sContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return PopularMoviesStage2Application.sContext;
    }

}

