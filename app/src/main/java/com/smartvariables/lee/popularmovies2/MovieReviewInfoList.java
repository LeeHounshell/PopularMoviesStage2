package com.smartvariables.lee.popularmovies2;

import android.util.Log;

import java.util.ArrayList;

// placeholder class to use the same pattern as MovieInfo and MovieInfoList
// there is no need at this time to make this object's data Parcelable
//
public class MovieReviewInfoList extends ArrayList<MovieReviewInfo> {
    private final static String TAG = "LEE: <" + MovieReviewInfoList.class.getSimpleName() + ">";

    public MovieReviewInfoList() {
        Log.v(TAG, "MovieReviewInfoList");
    }

}
