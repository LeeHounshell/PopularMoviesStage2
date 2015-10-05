package com.smartvariables.lee.popularmovies;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartvariables.lee.popularmovies.util.CircleFragment;
import com.smartvariables.lee.popularmovies.util.NetworkUtilities;
import com.smartvariables.lee.popularmovies.util.Tmdb_API_KEY;

import java.util.ArrayList;


/**
 * A list fragment representing the main list of Movies. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link DetailActivityFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MainActivityFragment
        extends CircleFragment
        implements AdapterView.OnItemClickListener,
        PerformMovieSearchContext
{
    private final static String TAG = "LEE: <" + MainActivityFragment.class.getSimpleName() + ">";

    private final static String MOVIE_LIST_KEY = "movieList";
    private static MainActivityFragment sMainActivityFragment;
    private static MovieListAdapter sMovieListAdapter;

    private boolean mFirstTime = true;
    private boolean mFirstItemClick = true;
    private boolean mDirtyMovieList;
    private MovieInfoList mRecoveryList;
    private GridView mGridView;
    private ImageView mNoDataImageView;
    private TextView mNoDataTextView;

    //--------------------------------------------------------------------------------
    // for master-detail..
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = GridView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String position) {
            getTheFragment().setActivatedPosition(Integer.valueOf(position));
        }
    };
    //--------------------------------------------------------------------------------

    public MainActivityFragment() {
        Log.v(TAG, "MainActivityFragment");
    }

    public static MainActivityFragment getTheFragment() {
        return sMainActivityFragment;
    }

    public static MovieInfoList getMovieList() {
        if (sMovieListAdapter == null) {
            return null;
        }
        return sMovieListAdapter.getMovieList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        sMainActivityFragment = this;
        setHasOptionsMenu(true);
        mDirtyMovieList = false;
        mRecoveryList = new MovieInfoList();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIE_LIST_KEY)) {
                try {
                    ArrayList<MovieInfo> savedMovieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
                    Log.v(TAG, "*** RECOVER *** using savedMovieList=" + savedMovieList);
                    mRecoveryList = (MovieInfoList) savedMovieList;
                } catch (OutOfMemoryError e) {
                    Log.w(TAG, "*** out of memory while trying to load movie list from parcelable..");
                    mRecoveryList = new MovieInfoList();
                }
            } else {
                Log.v(TAG, "no 'movieList' in savedInstanceState.");
            }
        }
        mNoDataImageView = null;
        mNoDataTextView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
        PerformAsyncMovieSearchTask.reset();
        sMainActivityFragment = null;
        sMovieListAdapter = null;
        mRecoveryList = null;
        mGridView = null;
        mNoDataImageView = null;
        mNoDataTextView = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        MovieInfoList theMovieList = getMovieList();
        if (theMovieList != null) {
            Log.v(TAG, "onSaveInstanceState: SAVE theMovieList");
            outState.putParcelableArrayList(MOVIE_LIST_KEY, theMovieList);
        }
        // master-detail..
        if (mActivatedPosition != GridView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            Log.v(TAG, "onSaveInstanceState: SAVE mActivatedPosition="+mActivatedPosition);
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        super.onSaveInstanceState(outState);
        Log.v(TAG, "--> onSaveInstanceState");
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) view.findViewById(R.id.poster_gridview);
        if (mGridView != null) {

            boolean didSelect = false;
            if (MainActivity.getTheActivity() != null) {
                Log.v(TAG, "onCreateView: create MovieListAdapter - mRecoveryList.size()=" + mRecoveryList.size());
                sMovieListAdapter = new MovieListAdapter(MainActivity.getTheActivity(), R.layout.grid_item_movie_view, mRecoveryList);
                mGridView.setAdapter(sMovieListAdapter);
                mGridView.setOnItemClickListener(this);
                if (mRecoveryList.size() == 0) {
                    Log.v(TAG, "onCreateView: create MovieListAdapter - empty mRecoveryList, so loadMovies..");
                    loadMovies();
                }
                if (MainActivity.isTwoPane() && mActivatedPosition != GridView.INVALID_POSITION) {
                    Log.v(TAG, "onCreateView: *** AUTO-SELECT MOVIE GRID ITEM: - mActivatedPosition=" + mActivatedPosition);
                    mGridView.setSelection(mActivatedPosition); // select the element passed via STATE_ACTIVATED_POSITION
                    Log.v(TAG, "onCreateView: *** mGridView.smoothScrollToPosition(mActivatedPosition="+mActivatedPosition+");");
                    mGridView.smoothScrollToPosition(mActivatedPosition);
                    didSelect = true;
                }
            } else {
                Log.e(TAG, "onCreateView: *** problem accessing GridView");
            }

            mNoDataImageView = (ImageView) view.findViewById(R.id.nodata_image);
            mNoDataTextView = (TextView) view.findViewById(R.id.nodata_text);
        }
        else {
            Log.w(TAG, "onCreateView: *** THE GRID-VIEW is null!");
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        if (getMovieAdapter() != null) {
            if (getMovieAdapter().getCount() == 0 && !NetworkUtilities.isConnected()) {
                Log.v(TAG, "onResume: no data cached, and no Internet.. need to complain");
                if (MainActivity.getTheActivity() != null) {
                    MainActivity.getTheActivity().checkIfNeedToDisplayNoInternetDialog();
                }
            }
        }
        if (mDirtyMovieList) {
            Log.v(TAG, "onResume: possible dirty movie list, we need to reload it.. loadMovies");
            loadMovies();
        }
    }

    public void loadMovies() {
        Log.v(TAG, "loadMovies (Thread)");
        Thread loadMoviesThread = new Thread() {
            @Override
            public void run() {
                MainActivity activity = MainActivity.getTheActivity();
                if (activity != null && activity.getHandler() != null) {
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
                    String defaultSortDesc = getResources().getString(R.string.pref_popular);
                    String defaultSortCriteria = getResources().getString(R.string.sort_popular);
                    final String sortDesc = sharedPrefs.getString("sortDesc", defaultSortDesc);
                    final String sortOrder = sharedPrefs.getString("sortBy", defaultSortCriteria);

                    activity.getHandler()
                            .postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.v(TAG, "run loadMovies in Thread");
                                            loadMovies(sortDesc, sortOrder, false);
                                        }
                                    }, 1000);

                }
            }
        };
        loadMoviesThread.start();
    }

    protected boolean isOneMinuteSinceLastLoadMovies() {
        long now = System.currentTimeMillis();
        long lastSearch = PerformAsyncMovieSearchTask.getLastSearchTime();
        long timeDiff = now - lastSearch;
        long ONE_MINUTE = 60000L; // in milliseconds
        return (timeDiff < ONE_MINUTE);
    }

    public synchronized void loadMovies(
            final String sortDesc,
            final String sortKey,
            boolean autoSelectFirstItem) {
        Log.v(TAG, "loadMovies: sortDesc=" + sortDesc + ", sortKey=" + sortKey + ", autoSelectFirstItem=" + autoSelectFirstItem);
        if (isOneMinuteSinceLastLoadMovies() && sortKey.equals(PerformAsyncMovieSearchTask.getLastSearchOrder())) {
            Log.w(TAG, "WE DID THE 'loadMovies' FOR THIS SORT=" + sortDesc + " UNDER A MINUTE AGO! - SKIP IT.");
            return;
        }
        // use AsyncTask to check Internet and populate the grid
        // first try to load from SQLite.. then Internet
        PerformAsyncMovieSearchTask loadMovieDbTask = new PerformAsyncMovieSearchTask((PerformMovieSearchContext) this, getView());
        loadMovieDbTask.execute(Tmdb_API_KEY.getKey(), sortKey);
        final MainActivity activity = MainActivity.getTheActivity();
        if (activity != null && activity.getHandler() != null) {

            if (autoSelectFirstItem) {
                mFirstTime = true;
            }
            activity.getHandler().post(
                    new Runnable() {
                        @Override
                        public void run() {
                            Log.v(TAG, "*** NODATA TEXT is GONE");
                            mNoDataTextView.setVisibility(View.GONE);
                            Log.v(TAG, "*** NODATA IMAGE is GONE");
                            mNoDataImageView.setVisibility(View.GONE);
                            Log.v(TAG, "update MainActivity title");
                            String newTitle = activity.getResources().getString(R.string.app_name) + ": " + sortDesc;
                            activity.setTitle(newTitle);
                            if (!NetworkUtilities.isConnected()) {
                                Log.w(TAG, "loadMovies: there is no Internet..");
                                activity.checkIfNeedToDisplayNoInternetDialog();
                            }
                            if (mActivatedPosition != GridView.INVALID_POSITION) {
                                Log.v(TAG, "loadMovies: *** mGridView.smoothScrollToPosition(mActivatedPosition=" + mActivatedPosition + ");");
                                mGridView.smoothScrollToPosition(mActivatedPosition);
                            }
                        }
                    });

        }
        Log.v(TAG, "loadMovies finished");
    }

    @Override
    public void onItemClick(
            AdapterView<?> parent,
            View view,
            int position,
            long id) {
        Log.v(TAG, "onItemClick - position=" + position);
        if (position >= getMovieList().size()) {
            Log.e(TAG, "no movie for index position=" + position + ", getMovieList.size()=" + getMovieList().size());
            return;
        }
        if (mActivatedPosition == position && ! mFirstItemClick) {
            Log.v(TAG, "we are already at position="+position);
            return;
        }
        mFirstItemClick = false;

        mActivatedPosition = position;
        Log.v(TAG, "onItemClick: mActivatedPosition=" + mActivatedPosition);
        setActivatedPosition(mActivatedPosition);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(String.valueOf(mActivatedPosition));

        if (MainActivity.isTwoPane()) {
            Log.v(TAG, "occasionally the list poster image is not properly updated.  if two-pane, sync it here.");
            getMovieAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu,
            MenuInflater inflater) {
        Log.v(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main_poster_grid, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        int id = item.getItemId();

        switch (id) {
            case R.id.home: {
                Log.v(TAG, "case R.id.home - UP PRESSED");
                getActivity().onBackPressed();
                return true;
            }
            case R.id.pref_allcached: {
                Log.v(TAG, "case R.id.pref_allcached");
                final String allCachedDesc = getResources().getString(R.string.pref_allcached);
                final String byAllCached = getResources().getString(R.string.sort_allcached);
                editor.putString("sortDesc", allCachedDesc);
                editor.putString("sortBy", byAllCached);
                editor.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mActivatedPosition = 0;
                        mFirstItemClick = true;
                        Log.v(TAG, "onOptionsItemSelected: mActivatedPosition=" + mActivatedPosition);
                        loadMovies(allCachedDesc, byAllCached, true);
                    }
                }).start();

                return true;
            }
            case R.id.pref_favorites: {
                Log.v(TAG, "case R.id.pref_favorites");
                final String favoritesDesc = getResources().getString(R.string.pref_favorites);
                final String byFavorites = getResources().getString(R.string.sort_favorites);
                editor.putString("sortDesc", favoritesDesc);
                editor.putString("sortBy", byFavorites);
                editor.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mActivatedPosition = 0;
                        mFirstItemClick = true;
                        Log.v(TAG, "onOptionsItemSelected: mActivatedPosition=" + mActivatedPosition);
                        loadMovies(favoritesDesc, byFavorites, true);
                    }
                }).start();

                return true;
            }
            case R.id.pref_popular: {
                Log.v(TAG, "case R.id.pref_popular");
                final String popularityDesc = getResources().getString(R.string.pref_popular);
                final String byPopularity = getResources().getString(R.string.sort_popular);
                editor.putString("sortDesc", popularityDesc);
                editor.putString("sortBy", byPopularity);
                editor.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mActivatedPosition = 0;
                        mFirstItemClick = true;
                        Log.v(TAG, "onOptionsItemSelected: mActivatedPosition=" + mActivatedPosition);
                        loadMovies(popularityDesc, byPopularity, true);
                    }
                }).start();

                return true;
            }
            case R.id.pref_rated: {
                Log.v(TAG, "case R.id.pref_rated");
                final String ratedDesc = getResources().getString(R.string.pref_rated);
                final String byRated = getResources().getString(R.string.sort_rated);
                editor.putString("sortDesc", ratedDesc);
                editor.putString("sortBy", byRated);
                editor.commit();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mActivatedPosition = 0;
                        mFirstItemClick = true;
                        Log.v(TAG, "onOptionsItemSelected: mActivatedPosition=" + mActivatedPosition);
                        loadMovies(ratedDesc, byRated, true);
                    }
                }).start();

                return true;
            }
/*
 * everything works. uncomment here and in res/menu/menu_main_poster_grid.xml to include Future Releases.
 *
            case R.id.pref_release: {
                Log.v(TAG, "case R.id.pref_release");
                final String releaseDesc = getResources().getString(R.string.pref_release);
                final String byRelease = getResources().getString(R.string.sort_release);
                editor.putString("sortDesc", releaseDesc);
                editor.putString("sortBy", byRelease);
                editor.commit();
                loadMovies(releaseDesc, byRelease, true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mActivatedPosition = 0;
                        mFirstItemClick = true;
                        Log.v(TAG, "onOptionsItemSelected: mActivatedPosition=" + mActivatedPosition);
                        loadMovies(releaseDesc, byRelease, true);
                    }
                }).start();

                return true;
            }
*/
        }

        return super.onOptionsItemSelected(item);
    }

    public static MovieListAdapter getMovieAdapter() {
        return sMovieListAdapter;
    }

    // 'PerformMovieSearchContext' interface method
    @Override
    public void setMovieList(MovieInfoList movieList) {
        Log.v(TAG, "setMovieList");
        if (getMovieAdapter() == null) {
            Log.v(TAG, "setMovieList: the MovieAdapter is null!");
        } else {
            Log.v(TAG, "setMovieList");
            MovieInfoList oldMovieList = getMovieAdapter().getMovieList();
            if (!movieList.equals(oldMovieList)) {
                Log.v(TAG, "setMovieList: *** the MovieInfoList has changed!");
                MovieInfoList emptyMovieList = new MovieInfoList();
                getMovieAdapter().setMovieData(emptyMovieList); // clear it all out first
                getMovieAdapter().setMovieData(movieList); // now set the new movie list
                if (mFirstTime) {
                    mFirstTime = false;
                    selectAppropriateItemFromList();
                }
            } else {
                Log.v(TAG, "setMovieList: *** hmm.. refresh the MovieInfoList dataset");
                getMovieAdapter().notifyDataSetChanged();
            }
        }
    }

    public void selectAppropriateItemFromList() {
        Log.v(TAG, "selectAppropriateItemFromList");
        if (! MainActivity.isTwoPane()) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                MainActivity activity = MainActivity.getTheActivity();
                if (activity != null && activity.getHandler() != null) {

                    activity.getHandler()
                            .postDelayed(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mActivatedPosition == GridView.INVALID_POSITION) {
                                                Log.v(TAG, "found GridView.INVALID_POSITION");
                                                mActivatedPosition = 0;
                                                Log.v(TAG, "selectAppropriateItemFromList: new mActivatedPosition=" + mActivatedPosition);
                                            }

                                            Log.v(TAG, "*** AUTO-SELECT A MOVIE GRID ITEM: *** - mActivatedPosition=" + mActivatedPosition);
                                            mGridView.setSelection(mActivatedPosition); // select the element passed via STATE_ACTIVATED_POSITION
                                            Log.v(TAG, "selectAppropriateItemFromList: *** mGridView.smoothScrollToPosition(mActivatedPosition=" + mActivatedPosition + ");");
                                            mGridView.smoothScrollToPosition(mActivatedPosition);

                                            Log.v(TAG, "CLICK IT - the first item or the saved mActivatedPostion=" + mActivatedPosition);
                                            mGridView.performItemClick(
                                                    mGridView.getAdapter().getView(mActivatedPosition, null, null),
                                                    mActivatedPosition,
                                                    mGridView.getAdapter().getItemId(mActivatedPosition));
                                        }
                                    }, 1234);

                }
            }
        }.start();
    }

    // 'PerformMovieSearchContext' interface method
    @Override
    public CircleFragment getCircleFragment() {
        return this;
    }

    // 'PerformMovieSearchContext' interface method
    @Override
    public MovieInfoList getDefaultMovieList() {
        return mRecoveryList;
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        Log.v(TAG, "setActivatedOnItemClick - *** setChoiceMode *** <------- activateOnItemClick="+activateOnItemClick);
/*
 * NOTE: the choiceMode is set using styles.  see 'movielist_style'
 *       working code below is unused but left here for personal reference
 *
        // When setting CHOICE_MODE_SINGLE, GridView will automatically
        // give items the 'activated' state when touched.

        getGridView().setChoiceMode(activateOnItemClick
                ? GridView.CHOICE_MODE_SINGLE
                : GridView.CHOICE_MODE_NONE);
 */
    }

    private GridView getGridView() {
        return mGridView;
    }

    private void setActivatedPosition(int position) {
        Log.v(TAG, "setActivatedPosition");
        if (position == GridView.INVALID_POSITION) {
            getGridView().setItemChecked(mActivatedPosition, false);
        } else {
            getGridView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
        Log.v(TAG, "setActivatedPosition: now mActivatedPosition="+mActivatedPosition);
    }

    public void setmDirtyMovieList(boolean mDirtyMovieList) {
        this.mDirtyMovieList = mDirtyMovieList;
    }

    @Override
    // the the Fragment view - also destroy it in onDestroy
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // in two-pane mode, a favorite may be removed from the list.. update the list here.
    public void refresh() {
        if (isOneMinuteSinceLastLoadMovies() || mDirtyMovieList) {
            Log.v(TAG, "*** refresh (do loadMovies) ***");
            loadMovies();
            selectAppropriateItemFromList();
        }
    }

}
