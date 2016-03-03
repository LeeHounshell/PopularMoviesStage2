package com.smartvariables.lee.popularmovies2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.smartvariables.lee.popularmovies2.data.MovieDbHelper;
import com.smartvariables.lee.popularmovies2.data_helper.MoviesContract;
import com.smartvariables.lee.popularmovies2.util.CircleViewHelper;
import com.smartvariables.lee.popularmovies2.util.NetworkUtilities;

import java.util.Date;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Video;

public class PerformAsyncMovieSearchTask
        extends AsyncTask<String, Void, MovieInfoList> {
    private final static String TAG = "LEE: <" + PerformAsyncMovieSearchTask.class.getSimpleName() + ">";

    private final static int NUMBER_OF_MOVIES_TO_DISPLAY = 20; // except for 'All Cached' - which shows them all
    private static long sLastSearchTime = 0L;
    private static String sLastSearchOrder = "";

    private final PerformMovieSearchContext mTheContext;
    private MovieInfoList mMovieList;
    private boolean mShowCircleView;

    public PerformAsyncMovieSearchTask(PerformMovieSearchContext theContext, View view) {
        Log.v(TAG, "new PerformAsyncMovieSearchTask");
        mTheContext = theContext;
        mMovieList = mTheContext.getDefaultMovieList();
        mShowCircleView = view != null;
        if (mShowCircleView) {
            Log.v(TAG, "PerformAsyncMovieSearchTask: *** SHOW CIRCLE-VIEW PROGRESS");
            mTheContext.getCircleFragment().initCircleView(view);
            CircleViewHelper.showCircleView(CircleViewHelper.CircleViewTarget.MASTER);
        }
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute");
    }

    @Override
    protected MovieInfoList doInBackground(String... params) {
        Log.v(TAG, "doInBackground");
        assert params[0] != null : "invalid API key!";
        assert params[1] != null : "invalid sort order!";
        mMovieList = new MovieInfoList();
        sLastSearchTime = System.currentTimeMillis();
        sLastSearchOrder = params[1];
        Context context = PopularMoviesStage2Application.getAppContext();
        final String byAllCached = context.getResources().getString(R.string.sort_allcached);
        final String byFavorites = context.getResources().getString(R.string.sort_favorites);
        if (params[1].equals(byAllCached) || params[1].equals(byFavorites)) {
            // only SQLite access for "all cached" or "favorites"
            Log.v(TAG, "query=" + params[1] + " - only loadable from SQLite..");
            loadSqliteMovies(params[1]);
        } else {
            if (NetworkUtilities.isConnected()) {
                Log.v(TAG, "we should have Internet");
                loadInternetMovies(params);
                if (mMovieList != null && mMovieList.size() == 0) {
                    // My Nvidia Shield Tablet (32G with Simm) reports both wireless and mobile.
                    // HOWEVER - the tablet has NO DATA PLAN and can't use Internet when wireless is off!
                    // but the Android API reports Internet as 'available' because it sees the simm card.
                    Log.v(TAG, "nothing loaded from (connected) Internet - try loading any from SQLite..");
                    loadSqliteMovies(params[1]); // try to load from SQLite
                }
            } else {
                Log.v(TAG, "no Internet - try loading from SQLite..");
                loadSqliteMovies(params[1]); // try to load the other way
            }
        }
        Log.v(TAG, "doInBackground finished. - mMovieList=" + mMovieList);
        return mMovieList;
    }

    protected void loadInternetMovies(String[] params) {
        try {
            final TmdbApi tmdb = new TmdbApi(params[0]);
            if (tmdb != null) {
                Log.v(TAG, "tmdb=" + tmdb + ", sortBy=" + params[1]);
                Discover discover = new Discover();
                discover.sortBy(params[1]);
                try {
                    List<MovieDb> theMovieList = tmdb.getDiscover()
                            .getDiscover(discover)
                            .getResults();
                    if (theMovieList != null && theMovieList.size() > 0) {
                        if (mShowCircleView) {
                            CircleViewHelper.initializeCircleViewValue((float) theMovieList.size());
                        }
                        int count = 0;
                        for (MovieDb movie : theMovieList) {
                            if (mShowCircleView) {
                                CircleViewHelper.setCircleViewValue((float) ++count);
                            }
                            if (count >= NUMBER_OF_MOVIES_TO_DISPLAY) {
                                continue; // max only display the NUMBER_OF_MOVIES_TO_DISPLAY
                            }
                            final MovieInfo movieInfo = new MovieInfo(movie);
                            final long movieRowId = movieInfo.getMovieRowId();
                            mMovieList.add(movieInfo);
                            Log.v(TAG, "ADD to mMovieList: movieInfo=" + movieInfo.getTitle() + " at ROW=" + movieRowId);
                            getMovieTrailers(tmdb, movieRowId, movieInfo, movie);
                        }
                    } else {
                        Log.v(TAG, "theMovieList is empty!");
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, "*** problem accessing tmdb. sortBy=" + params[1] + " - error=" + e);
                }
            } else {
                Log.e(TAG, "unable to access TmdbApi - is the API key correct?");
            }
        } catch (Exception e) {
            Log.e(TAG, "something unexpected happened. unable to load movies via Internet. error=" + e);
            loadSqliteMovies(params[1]); // try to load from SQLite
        }
    }

    @Override
    protected void onPostExecute(MovieInfoList movieList) {
        // update grid UI with movies..
        if (movieList != null) {
            Log.v(TAG, "*** onPostExecute: movieList.size()=" + movieList.size());
            if (mShowCircleView) {
                CircleViewHelper.hideCircleView(movieList.size() > 0, movieList.size() == 0);
            }
            mTheContext.setMovieList(movieList);
        } else {
            Log.v(TAG, "*** onPostExecute: movieList=null");
            if (mShowCircleView) {
                CircleViewHelper.hideCircleView(false, true);
            }
        }
    }

    protected void loadSqliteMovies(String sortOrder) {
        // no Internet - show movies using cache from SQLite database.
        Log.v(TAG, "loadSqliteMovies");
        mMovieList = new MovieInfoList();
        Context context = PopularMoviesStage2Application.getAppContext();
        Cursor movieCursor = null;
        final String byAllCached = context.getResources().getString(R.string.sort_allcached);
        final String byFavorites = context.getResources().getString(R.string.sort_favorites);
        final String byPopularity = context.getResources().getString(R.string.sort_popular);
        final String byRated = context.getResources().getString(R.string.sort_rated);
        final String byRelease = context.getResources().getString(R.string.sort_release);
        if (sortOrder.equals(byAllCached)) {
            // query all SQLite movies - order by release date
            Log.v(TAG, "query SQLite release_date.desc");
            String orderBy = "FIELD_MOVIE_TITLE ASC";
            movieCursor = context.getContentResolver().query(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null, // all columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    orderBy); // ORDER BY RELEASE_DATE ASC
        } else if (sortOrder.equals(byFavorites)) {
            // query all SQLite favorite_star movies - order by release date
            Log.v(TAG, "query SQLite favorites.desc");
            String orderBy = "FIELD_MOVIE_RELEASE_DATE DESC";
            movieCursor = context.getContentResolver().query(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null, // all columns.
                    MoviesContract.MoviesEntry._ID // ID must exist in Favorites table
                            + " IN (SELECT "
                            + MoviesContract.FavoritesEntry.FIELD_MOVIE_ID
                            + " FROM "
                            + MoviesContract.FavoritesEntry.TABLE_NAME
                            + ")",
                    null, // values for "where" clause
                    orderBy); // ORDER BY RELEASE_DATE ASC
        } else if (sortOrder.equals(byPopularity)) {
            // query 20 SQLite popular movies - order by release date
            Log.v(TAG, "query SQLite popularity.desc");
            String orderBy = "FIELD_MOVIE_POPULARITY DESC LIMIT " + NUMBER_OF_MOVIES_TO_DISPLAY;
            movieCursor = context.getContentResolver().query(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null, // all columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    orderBy); // ORDER BY RELEASE_DATE ASC
        } else if (sortOrder.equals(byRated)) {
            // query 20 SQLite vote_average movies - order by release date
            Log.v(TAG, "query SQLite vote_average.desc");
            String orderBy = "FIELD_MOVIE_VOTE_AVERAGE DESC LIMIT " + NUMBER_OF_MOVIES_TO_DISPLAY;
            movieCursor = context.getContentResolver().query(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null, // all columns.
                    null, // cols for "where" clause
                    null, // values for "where" clause
                    orderBy); // ORDER BY RELEASE_DATE ASC
        } else if (sortOrder.equals(byRelease)) {
            // query 20 future SQLite movies - order by release date
            Log.v(TAG, "query SQLite release_date.desc");
            String orderBy = "FIELD_MOVIE_RELEASE_DATE DESC LIMIT " + NUMBER_OF_MOVIES_TO_DISPLAY;
            Date now = new Date();
            long currentDate = MoviesContract.normalizeDate(now.getTime());
            movieCursor = context.getContentResolver().query(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null, // all columns.
                    MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE + ">=?", // cols for "where" clause
                    new String[]{Long.toString(currentDate)}, // values for the "where" clause
                    orderBy); // ORDER BY RELEASE_DATE ASC
        } else {
            Log.w(TAG, "unknown sort directive: '" + sortOrder + "'");
        }
        // Make sure we get the correct cursor out of the database
        if (movieCursor != null) {
            movieCursor.moveToFirst();
            Log.i(TAG, "query found " + movieCursor.getCount() + " movies cached in SQLite");
            while (movieCursor.isAfterLast() == false) {
                MovieInfo movieInfo = new MovieInfo(movieCursor);
                mMovieList.add(movieInfo);
                movieCursor.moveToNext();
            }
            movieCursor.close();
        } else {
            Log.w(TAG, "the movieCursor is null!");
        }
    }

    // load the movie trailers too..
    protected void getMovieTrailers(TmdbApi tmdb, long movieRowId, MovieInfo movieInfo, MovieDb movie) {
        Log.v(TAG, "getMovieTrailers");
        if (movieRowId > 0) {
            Context context = PopularMoviesStage2Application.getAppContext();
            MovieDbHelper dbHelper = MovieDbHelper.getInstance(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor trailerCursor = db.query(
                    MoviesContract.TrailersEntry.TABLE_NAME, // Table to Query
                    null,      // leaving "columns" null just returns all the columns.
                    MoviesContract.TrailersEntry.FIELD_MOVIE_ID + "=?", // where
                    new String[]{Long.toString(movieRowId)}, // values for the "where" clause
                    null,      // columns to group by
                    null,      // columns to filter by row groups
                    null       // sort order
            );
            if (trailerCursor.getCount() == 0) {
                Log.v(TAG, "no trailers found for movie ID=" + movieRowId);
                List<Video> trailers = tmdb.getMovies().getVideos(movie.getId(), "");
                for (Video trailer : trailers) {
                    if (trailer.getSite().equals("YouTube")) {
                        String trailerUrl = "https://www.youtube.com/watch?v=" + trailer.getKey();
                        movieInfo.addTrailer(trailerUrl);
                    } else {
                        Log.w(TAG, "UNUSED TRAILER: " + trailer.getKey() + " " + trailer.getType() + " " + trailer.getSite());
                    }
                }
            }
            trailerCursor.close();
        }
    }

    public static long getLastSearchTime() {
        return sLastSearchTime;
    }

    public static String getLastSearchOrder() {
        return sLastSearchOrder;
    }

    public static void reset() {
        sLastSearchOrder = "";
        sLastSearchTime = 0L;
    }

}
