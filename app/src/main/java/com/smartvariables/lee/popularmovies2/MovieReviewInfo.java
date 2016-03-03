package com.smartvariables.lee.popularmovies2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.smartvariables.lee.popularmovies2.data.MovieDbHelper;
import com.smartvariables.lee.popularmovies2.data.reviews.ReviewsColumns;
import com.smartvariables.lee.popularmovies2.data_helper.MoviesContract;

import info.movito.themoviedbapi.model.Reviews;

public class MovieReviewInfo {
    private final static String TAG = "LEE: <" + MovieReviewInfo.class.getSimpleName() + ">";

    private Long mReviewRowId;
    private Long mMovieRowId;
    private String mCriticName;
    private String mCriticUrl;
    private String mReviewText;

    public MovieReviewInfo(Reviews review, long movieRowId) {
        Log.v(TAG, "MovieReviewInfo(MovieInfo)");
        mReviewRowId = -1L;
        mMovieRowId = movieRowId;
        mCriticName = review.getAuthor();
        mCriticUrl = review.getUrl();
        mReviewText = review.getContent();
        // note: there is a bug here, in that new edits to existing reviews won't be saved.
        // for this purpose, that is fine - as the 1st revision of a posted review is valid.
        if (!loadMovieReviewInfo()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    insertSqliteReview();
                }
            }).start();
        }
    }

    public MovieReviewInfo(Cursor cursor) {
        if (cursor.getCount() > 0) {
            getMovieReviewInfoAtCursor(cursor);
        }
    }

    protected boolean loadMovieReviewInfo() {
        boolean found = false;
        Context context = PopularMoviesStage2Application.getAppContext();
        Cursor cursor = context.getContentResolver().query(
                MoviesContract.ReviewsEntry.CONTENT_URI,  // Table to Query
                null, // all columns
                MoviesContract.ReviewsEntry.FIELD_MOVIE_ID + "=? AND "
                        + MoviesContract.ReviewsEntry.FIELD_CRITIC_NAME + "=? AND "
                        + MoviesContract.ReviewsEntry.FIELD_CRITIC_URL + "=?", // where
                new String[]{Long.toString(mMovieRowId), mCriticName, mCriticUrl}, // Values for the "where" clause
                null // sort order
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            getMovieReviewInfoAtCursor(cursor);
            found = true;
        }
        cursor.close();
        return found;
    }

    protected void getMovieReviewInfoAtCursor(Cursor cursor) {
        int idx = cursor.getColumnIndex(MoviesContract.MoviesEntry._ID);
        mReviewRowId = cursor.getLong(idx);
        idx = cursor.getColumnIndex(MoviesContract.ReviewsEntry.FIELD_MOVIE_ID);
        mMovieRowId = cursor.getLong(idx);
        idx = cursor.getColumnIndex(MoviesContract.ReviewsEntry.FIELD_CRITIC_NAME);
        mCriticName = cursor.getString(idx);
        idx = cursor.getColumnIndex(MoviesContract.ReviewsEntry.FIELD_CRITIC_URL);
        mCriticUrl = cursor.getString(idx);
        idx = cursor.getColumnIndex(MoviesContract.ReviewsEntry.FIELD_REVIEW_TEXT);
        mReviewText = cursor.getString(idx);
    }

    public long getMovieRowId() {
        return mMovieRowId;
    }

    public void setMovieRowId(long movieRowId) {
        mMovieRowId = movieRowId;
    }

    private void insertSqliteReview() {
        Context context = PopularMoviesStage2Application.getAppContext();
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues reviewValues = getContentValues();
        try {
            mReviewRowId = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, reviewValues);
            Log.v(TAG, "SQLite insert new Review row - ID=" + mReviewRowId + " for movie #" + mMovieRowId);
        } catch (SQLiteConstraintException e) {
            Log.e(TAG, "CONSTRAINT EXCEPTION: sqlite unable to insert reviewValues=" + reviewValues);
        } catch (SQLiteException e) {
            Log.v(TAG, "SQLITE EXCEPTION: problem inserting Review for movie #" + mMovieRowId + " exception=" + e);
        }
    }

    @NonNull
    protected ContentValues getContentValues() {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(MoviesContract.ReviewsEntry.FIELD_MOVIE_ID, mMovieRowId);
        reviewValues.put(MoviesContract.ReviewsEntry.FIELD_CRITIC_NAME, mCriticName);
        reviewValues.put(MoviesContract.ReviewsEntry.FIELD_CRITIC_URL, mCriticUrl);
        reviewValues.put(MoviesContract.ReviewsEntry.FIELD_REVIEW_TEXT, mReviewText);
        return reviewValues;
    }

    // Note that mMovieRowId will already be set via the MovieReviewInfo constructor
    public void addReview(String reviewUrl) {
        // Insert review ContentValues into database and get a review row ID back
        Context context = PopularMoviesStage2Application.getAppContext();
        ContentValues reviewValues = getContentValues();
        // we use the Sqlite contraint mechanism to prevent review duplicates
        try {
            Uri reviewUri = context.getContentResolver().insert(MoviesContract.ReviewsEntry.buildReviewsUri(), reviewValues);
            mReviewRowId = ContentUris.parseId(reviewUri);
            Log.v(TAG, "addReview: mReviewRowId=" + mReviewRowId + " mMovieRowId=" + mMovieRowId + " REVIEW=" + reviewUrl);
        } catch (SQLiteConstraintException e) {
            Log.v(TAG, "CONSTRAINT EXCEPTION: Review '" + reviewUrl + "' is already in the database.");
        } catch (SQLiteException e) {
            Log.v(TAG, "SQLITE EXCEPTION: problem inserting Review '" + reviewUrl + "' exception=" + e);
        }
    }

    public final String[] getReviews() {
        Context context = PopularMoviesStage2Application.getAppContext();
        Cursor cursor = context.getContentResolver().query(
                ReviewsColumns.CONTENT_URI, // table
                null, // all columns.
                MoviesContract.FavoritesEntry.FIELD_MOVIE_ID + "=?", // where
                new String[]{Long.toString(mMovieRowId)}, // Values for the "where" clause
                null  // order by
        );
        if (cursor.getCount() > 0) {
            Log.v(TAG, "found " + cursor.getCount() + " reviews");
            cursor.moveToFirst();
            final String reviewUrls[] = new String[cursor.getCount()];
            int index = 0;
            while (cursor.isAfterLast() == false) {
                getMovieReviewInfoAtCursor(cursor);
                reviewUrls[index] = this.toString(); // save the entire review at this index
                ++index;
                cursor.moveToNext();
            }
            cursor.close();
            return reviewUrls;
        }
        cursor.close();
        return null;
    }

    @Override
    public String toString() {
        String reviewInfo = "<br /><h2>by: " + mCriticName + "</h2>" +
                "<a href=\"" + mCriticUrl + "\">(" + mCriticUrl + ") </a>" +
                "<br /><br />" +
                mReviewText + "<br /><br />";
        return reviewInfo;
    }

}
