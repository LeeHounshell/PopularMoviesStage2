/*
 * PopularMoviesStage2 from https://github.com/LeeHounshell/PopularMoviesStage2
 *
 * This PopularMoviesStage2 app is based off PopularMoviesStage1 from https://github.com/LeeHounshell/PopularMoviesStage1
 * PopularMoviesStage1 code patterned from: http://javatechig.com/android/download-and-display-image-in-android-gridview
 *
 * List of changes from the PopularMoviesStage1 app:
 * - Allow users to view and play trailers.
 *   App uses a share Intent to expose the external youtube URL for the trailer.
 * - Allow users to read reviews of a selected movie.
 * - Allow users to mark a movie as a favorite in the details view by tapping a button(star).
 *   This is for a local movies collection that is maintained and does not require an API request.
 * - Sorting criteria for the main view now includes an additional pivot to show the favorites collection.
 * - Movie Details layout contains a section for displaying trailer videos and user reviews.
 *   Details View includes an Action Bar item to share the first trailer video URL from the list of trailers
 * - Tablet UI uses a Master-Detail layout implemented using fragments. The left fragment is for discovering movies.
 *   The right fragment displays the movie details view for the currently selected movie.
 * - App saves a 'Favorited' movie to the SQLite 'Favorties' table using the movie's ID.
 * - When the 'favorites' setting option is selected, the main view displays the entire favorites collection based on
 *   movie IDs stored in SQLite's 'Favorites' table, with matching content from the 'Movies', 'Reviews', and 'Trailers' tables.
 * - A ContentProvider manages all access to the database (online or offline).
 * - Optimized the app experience for tablet.
 *   When a poster thumbnail is selected, the details screen is launched [Phone] or displayed in a fragment [Tablet].
 * - Optionally use 'Glide' instead of 'Picasso' for image loading
 * - Refactor, with many changes - including creation of a PosterImageView that can notify when drawn on
 * - Added Unit-Test Suite for generated database valadation.
 *
 * API detail:
 * 1. To fetch trailers we make a request to the /movie/{id}/videos endpoint
 *    in a background thread and display those details when the user selects a movie.
 * 2. To fetch reviews we make a request to the /movie/{id}/reviews endpoint
 *    in a background thread and display those details when the user selects a movie.
 * 3. Use an Intent to open a youtube link in either the native app or a web browser of choice.
 */
package com.smartvariables.lee.popularmovies;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.smartvariables.lee.popularmovies.util.AppCompatActivityBase;
import com.smartvariables.lee.popularmovies.util.NetworkUtilities;


public class MainActivity
        extends AppCompatActivityBase
        implements MainActivityFragment.Callbacks
{
    private final static String TAG = "LEE: <" + MainActivity.class.getSimpleName() + ">";

    public final static String DETAIL_FRAGMENT_TAG = "DFTAG";
    private static MainActivity sMainActivity;
    private static volatile boolean sInternetDialogActive;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static boolean sTwoPane;

    private BroadcastReceiver mInternetReceiver;
    private AlertDialog.Builder mNoInternetDialog;

    public static MainActivity getTheActivity() {
        return sMainActivity;
    }

    public static boolean isTwoPane() {
        return sTwoPane;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        sMainActivity = this;
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_movie_detail) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            sTwoPane = true;
            Log.v(TAG, "---*** TWO-PANE MODE ***---");

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((MainActivityFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.movie_list))
                    .setActivateOnItemClick(true);
        }
        else {
            sTwoPane = false;
            Log.v(TAG, "---*** NOT TWO-PANE MODE ***---");
        }

        NetworkUtilities.init(); // will determine if we are online in a background thead..
        mNoInternetDialog = null;
        sInternetDialogActive = false;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        // if we lose Internet then reconnect, this BroadcastReceiver will detect the change and reload the movieList..
        final boolean wasConnected = NetworkUtilities.isConnected();
        mInternetReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v(TAG, "onReceive - intent=" + intent);
                boolean loadMovies = false;
                if (wasConnected == false && NetworkUtilities.isConnected() == true && MainActivity.getTheActivity() != null) {
                    Log.v(TAG, "*** NETWORK STATE CHANGED *** - isConnected()=" + NetworkUtilities.isConnected());
                    loadMovies = true;
                } else {
                    MovieInfoList movieList = MovieListAdapter.getMovieList();
                    if (movieList == null || movieList.size() == 0) {
                        Log.v(TAG, "*** INITIALIZING ***");
                        loadMovies = true;
                    }
                }
                if (loadMovies) {
                    MainActivityFragment activityFragment = (MainActivityFragment) MainActivityFragment.getTheFragment();
                    if (activityFragment != null) {
                        Log.v(TAG, "found the MainActivityFragment - loadMovies");
                        activityFragment.loadMovies();
                    }
                }
            }
        };
        registerReceiver(mInternetReceiver, filter);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        NetworkUtilities.isNetworkAvailable();
                    }
                }).start();

        // TODO: If exposing deep links into app, handle intents here.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        unregisterReceiver(mInternetReceiver);
        if (mNoInternetDialog != null) {
            mNoInternetDialog = null;
            sInternetDialogActive = false;
        }
        sMainActivity = null;
        mInternetReceiver = null;
        mNoInternetDialog = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "==> onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // not used. see MainActivityFragment.onCreate(...)
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "==> onRestoreInstanceState (unused)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        NetworkUtilities.isNetworkAvailable();
                    }
                }).start();
    }

    public void checkIfNeedToDisplayNoInternetDialog() {
        Log.v(TAG, "checkIfNeedToDisplayNoInternetDialog");
        if (!NetworkUtilities.isConnected() && sInternetDialogActive == false && MainActivity.getTheActivity() != null) {
            getTheActivity().getHandler()
                    .post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Log.v(TAG, "oops, no Internet connection!");
                                    showNoInternetDialog();
                                }
                            });
        }
    }

    public void showNoInternetDialog() {
        sInternetDialogActive = true;
        Log.v(TAG, "showNoInternetDialog");
        if (getTheActivity() != null) {
            mNoInternetDialog = new AlertDialog.Builder(getTheActivity());
            mNoInternetDialog.setTitle(
                    getResources().getString(R.string.offline));
            String app_name = getResources().getString(
                    R.string.app_name);
            String message = getResources().getString(
                    R.string.no_internet);
            if (isAirplaneModeOn()) {
                message = getResources().getString(
                        R.string.airplane_mode);
            }
            message = String.format(message, app_name);
            mNoInternetDialog.setMessage(message);
            mNoInternetDialog.setIcon(R.mipmap.ic_launcher);

            mNoInternetDialog.setPositiveButton(
                    isAirplaneModeOn() ? getResources().getString(
                            R.string.airplane) : getResources().getString(
                            R.string.connect),
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog,
                                int id) {
                            dialog.dismiss();
                            sInternetDialogActive = false;
                            if (isAirplaneModeOn()) {
                                Log.v(
                                        TAG,
                                        "open airplane mode settings");
                                Intent i = new Intent(
                                        Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                                i.putExtra(
                                        ":android:show_fragment",
                                        "com.android.settings.AirplaneModeSettings");
                                i.putExtra(
                                        ":android:no_headers",
                                        true);
                                startActivity(i);
                            } else {
                                Log.v(TAG, "open wifi settings");
                                Intent i = new Intent(
                                        Settings.ACTION_WIFI_SETTINGS);
                                startActivity(i);
                            }
                        }
                    });

            mNoInternetDialog.setNegativeButton(
                    getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog,
                                int id) {
                            dialog.dismiss();
                            sInternetDialogActive = false;
                        }
                    });

            mNoInternetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.v(TAG, "onBackPressed within Dialog..");
                    dialog.dismiss();
                    sInternetDialogActive = false;
                }
            });

            if (!NetworkUtilities.isConnected()) {
                mNoInternetDialog.show();
            }
        }
    }

    /**
     * Gets the state of Airplane Mode.
     * from: http://stackoverflow.com/questions/4319212/how-can-one-detect-airplane-mode-on-android
     *
     * @param context
     * @return true if enabled.
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected boolean isAirplaneModeOn() {
        final Context context = (Context) getTheActivity();
        if (context == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(
                    context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(
                    context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    @Override
    public void onItemSelected(String positionStr) {
        Log.v(TAG, "*** CLICK! onItemSelected *** positionStr="+ positionStr);
        // handle master-detail - load fragment with detail for movie ID=id
        MovieInfo movieInfo = MainActivityFragment.getMovieList().get(Integer.valueOf(positionStr));

        if (isTwoPane()) {
            Log.v(TAG, "using two-pane - *** ATTACH THE DETAIL FRAGMENT ***");
            // Create new fragment and transaction
            Bundle arguments = new Bundle();
            int position = Integer.valueOf(positionStr);
            MovieInfo movieInfo1 = MainActivityFragment.getMovieList().get(position);
            arguments.putParcelable(DetailActivityFragment.MOVIE_DETAIL_KEY, movieInfo);
            Fragment newFragment = new DetailActivityFragment();
            newFragment.setArguments(arguments);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            String tag = null;

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.fragment_movie_detail, newFragment, DETAIL_FRAGMENT_TAG);
            transaction.addToBackStack(tag);

            // Commit the transaction
            transaction.commit();
        }
        else {
            Log.v(TAG, "not using two-pane - start the Detail Activity..");
            MainActivity mainActivity = MainActivity.getTheActivity();
            if (mainActivity != null) {
                Intent intent = new Intent(mainActivity, DetailActivity.class);
                intent.putExtra("movie", movieInfo);
                mainActivity.startActivity(intent);
            }
        }
    }

}
