package com.smartvariables.lee.popularmovies2;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.smartvariables.lee.popularmovies2.data_helper.MoviesContract;
import com.smartvariables.lee.popularmovies2.util.CircleViewHelper;
import com.smartvariables.lee.popularmovies2.util.NetworkUtilities;

import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Reviews;

public class PerformAsyncReviewSearchTask
        extends AsyncTask<String, Void, MovieReviewInfoList> {
    private final static String TAG = "LEE: <" + PerformAsyncReviewSearchTask.class.getSimpleName() + ">";

    private final static int NUMBER_OF_REVIEWS_TO_DISPLAY = 20;
    private final static String LANGUAGE_DEFAULT = "";
    private final PerformReviewSearchContext mTheContext;
    private MovieReviewInfoList mReviewList;
    private boolean mScrollOk;
    private boolean mShowCircleView;

    public PerformAsyncReviewSearchTask(PerformReviewSearchContext theContext, CircleProgressView circleView, boolean firstTime) {
        Log.v(TAG, "new PerformAsyncReviewSearchTask");
        mScrollOk = false;
        mTheContext = theContext;
        mShowCircleView = !firstTime && circleView != null;
        if (mShowCircleView) {
            Log.v(TAG, "PerformAsyncReviewSearchTask: *** SHOW CIRCLE-VIEW PROGRESS");
            mTheContext.getCircleFragment().initCircleView(circleView);
            CircleViewHelper.showCircleView(CircleViewHelper.CircleViewTarget.DETAIL);
        }
    }

    @Override
    protected void onPreExecute() {
        Log.v(TAG, "onPreExecute");
    }

    @Override
    protected MovieReviewInfoList doInBackground(String... params) {
        Log.v(TAG, "doInBackground");
        assert params[0] != null : "invalid API key!";
        assert params[1] != null : "invalid tmdbMovieId!";
        assert params[2] != null : "invalid movieRowId!";
        assert params[3] != null : "invalid 'scrollok' | 'NoScroll'!";
        mScrollOk = params[3].equals("scrollok"); // default
        mReviewList = new MovieReviewInfoList();
        Context context = PopularMoviesStage2Application.getAppContext();
        long tmdbMovieId = Long.valueOf(params[1]);
        mReviewList = mTheContext.getDefaultReviewListForMovie(tmdbMovieId);
        if (NetworkUtilities.isConnected()) {
            Log.v(TAG, "we should have Internet");
            loadInternetReviews(params);
            if (mReviewList != null && mReviewList.size() == 0) {
                // My Nvidia Shield Tablet (32G with Simm) reports both wireless and mobile.
                // HOWEVER - the tablet has NO DATA PLAN and can't use Internet when wireless is off!
                // but the Android API reports Internet as 'available' because it sees the simm card.
                Log.v(TAG, "nothing loaded from (connected) Internet - try loading any from SQLite..");
                loadSqliteReviews(params[2]); // try to load from SQLite
            }
        } else {
            Log.v(TAG, "no Internet - try loading from SQLite..");
            loadSqliteReviews(params[2]); // try to load the other way
        }
        Log.v(TAG, "doInBackground finished. - mReviewList=" + mReviewList);
        return mReviewList;
    }

    protected void loadInternetReviews(String[] params) {
        Log.v(TAG, "loadInternetReviews");
        try {
            mReviewList = new MovieReviewInfoList();
            final TmdbApi tmdb = new TmdbApi(params[0]);
            if (tmdb != null) {
                Log.v(TAG, "tmdb=" + tmdb + ", tmdbMovieId=" + params[1] + ", movieRowId=" + params[2]);
                try {
                    long tmdbMovieId = Long.valueOf(params[1]);
                    long movieRowId = Long.valueOf(params[2]);
                    List<Reviews> theReviewList = tmdb.getReviews().getReviews((int) tmdbMovieId, LANGUAGE_DEFAULT, 0).getResults();
                    if (theReviewList != null && theReviewList.size() > 0) {
                        Log.v(TAG, "processing " + theReviewList.size() + " reviews..");
                        if (mShowCircleView) {
                            CircleViewHelper.initializeCircleViewValue((float) theReviewList.size());
                        }
                        int count = 0;
                        for (Reviews review : theReviewList) {
                            Log.v(TAG, "review=" + review);
                            if (mShowCircleView) {
                                CircleViewHelper.setCircleViewValue((float) ++count);
                            }
                            final MovieReviewInfo movieReviewInfo = new MovieReviewInfo(review, movieRowId);
                            mReviewList.add(movieReviewInfo);
                            Log.v(TAG, "ADD to mReviewList: movieReviewInfo=" + movieReviewInfo + " for movie=" + movieRowId);
                        }
                    } else {
                        Log.v(TAG, "theReviewList is empty!");
                    }
                } catch (NullPointerException e) {
                    Log.e(TAG, "*** problem accessing tmdb. tmdbMovieId=" + params[1] + " - error=" + e);
                }
            } else {
                Log.e(TAG, "unable to access TmdbApi - is the API key correct?");
            }
        } catch (Exception e) {
            Log.e(TAG, "something unexpected happened. unable to load reviews via Internet. error=" + e);
            loadSqliteReviews(params[2]); // try to load from SQLite
        }
    }

    @Override
    protected void onPostExecute(MovieReviewInfoList reviewList) {
        // update detail UI with reviews..
        if (reviewList != null) {
            Log.v(TAG, "*** onPostExecute: reviewList.size()=" + reviewList.size());
            if (mShowCircleView) {
                CircleViewHelper.hideCircleView(true, reviewList.size() == 0);
            }
            mTheContext.setReviewsList(reviewList, mScrollOk);
        } else {
            Log.v(TAG, "*** onPostExecute: reviewList=null");
            if (mShowCircleView) {
                CircleViewHelper.hideCircleView(true, true); // short delay, even if no data
            }
        }
    }

    protected void loadSqliteReviews(String movieRowId) {
        // no Internet - show movies using cache from SQLite database.
        Log.v(TAG, "loadSqliteReviews for movieRowId=" + movieRowId);
        mReviewList = new MovieReviewInfoList();
        Context context = PopularMoviesStage2Application.getAppContext();
        Cursor reviewCursor = null;
        // query all SQLite reviews - order by reviews date
        String orderBy = "FIELD_CRITIC_NAME ASC";
        reviewCursor = context.getContentResolver().query(
                MoviesContract.ReviewsEntry.CONTENT_URI,
                null, // all columns.
                MoviesContract.ReviewsEntry.FIELD_MOVIE_ID + "=?", // where
                new String[]{movieRowId}, // Values for the "where" clause
                orderBy); // ORDER BY CRITIC NAME ASC
        if (reviewCursor != null) {
            reviewCursor.moveToFirst();
            Log.i(TAG, "query found " + reviewCursor.getCount() + " reviews cached in SQLite");
            if (reviewCursor.getCount() == 0) {
                reviewCursor.close();
            } else {
                while (reviewCursor.isAfterLast() == false) {
                    MovieReviewInfo movieReviewInfo = new MovieReviewInfo(reviewCursor);
                    mReviewList.add(movieReviewInfo);
                    reviewCursor.moveToNext();
                }
                reviewCursor.close();
            }
        } else {
            Log.w(TAG, "the reviewCursor is null!");
        }
    }

}
