package com.smartvariables.lee.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.smartvariables.lee.popularmovies.util.AppCompatActivityBase;


public class DetailActivity
        extends AppCompatActivityBase {
    private final static String TAG = "LEE: <" + DetailActivity.class.getSimpleName() + ">";

    private static DetailActivity sDetailActivity;
    private static MovieInfo sMovie;

    public static DetailActivity getTheActivity() {
        //Log.v(TAG, "getTheActivity");
        return sDetailActivity;
    }

    public static void setMovie(MovieInfo movie) {
        //Log.v(TAG, "setMovie");
        sMovie = movie;
    }

    public static MovieInfo getMovie() {
        //Log.v(TAG, "getMovie");
        return sMovie;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        sDetailActivity = this;
        setContentView(R.layout.fragment_movie_detail);

        // show the up button in the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        try {
            sMovie = (MovieInfo) getIntent().getParcelableExtra("movie");
            Log.d(TAG, "onCreate detail: LOADED PARCELABLE EXTRA - sMovie=" + sMovie);
        } catch (OutOfMemoryError e) {
            Log.w(TAG, "*** out of memory trying to load 'movie' from Parcelable..");
            sDetailActivity = null;
            sMovie = null;
            onBackPressed();
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            Log.v(TAG, "*** CREATE THE DETAIL FRAGMENT *** and add it to the activity using a fragment transaction..");
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.MOVIE_DETAIL_KEY, getIntent().getData());
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_movie_detail, fragment, MainActivity.DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        sDetailActivity = null;
        sMovie = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "==> onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // not used. see MainActivityDetailsFragment.onCreate(...)
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "==> onRestoreInstanceState (unused)");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "==> onOptionsItemSelected");
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.v(TAG, "==> HOME");
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
