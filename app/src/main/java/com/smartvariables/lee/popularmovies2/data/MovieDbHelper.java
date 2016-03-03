//
//===========================================================================================================
// THIS IS GENERATED CODE! - do not edit
//
// To edit, change JSON configs under 'com/smartvariables/lee/popularmovies2/generate_data_contentprovider/'
// Then run the bash script './generate-data-contentprovider.sh' in that same directory
// to create new Java code under 'com/smartvariables/lee/popularmovies2/data/' + note old 'data' is deleted!
//
// THIS IS GENERATED CODE! - do not edit
//===========================================================================================================
//
package com.smartvariables.lee.popularmovies2.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.smartvariables.lee.popularmovies2.BuildConfig;
import com.smartvariables.lee.popularmovies2.data.favorites.FavoritesColumns;
import com.smartvariables.lee.popularmovies2.data.movies.MoviesColumns;
import com.smartvariables.lee.popularmovies2.data.reviews.ReviewsColumns;
import com.smartvariables.lee.popularmovies2.data.trailers.TrailersColumns;

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String TAG = MovieDbHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private static MovieDbHelper sInstance;
    private final Context mContext;
    private final MovieDbHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_FAVORITES = "CREATE TABLE IF NOT EXISTS "
            + FavoritesColumns.TABLE_NAME + " ( "
            + FavoritesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FavoritesColumns.FIELD_MOVIE_ID + " INTEGER NOT NULL "
            + ", CONSTRAINT fk_field_movie_id FOREIGN KEY (" + FavoritesColumns.FIELD_MOVIE_ID + ") REFERENCES movies (_id) ON DELETE CASCADE"
            + " );";

    public static final String SQL_CREATE_TABLE_MOVIES = "CREATE TABLE IF NOT EXISTS "
            + MoviesColumns.TABLE_NAME + " ( "
            + MoviesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MoviesColumns.FIELD_TMDB_MOVIE_ID + " INTEGER, "
            + MoviesColumns.FIELD_MOVIE_TITLE + " TEXT NOT NULL, "
            + MoviesColumns.FIELD_MOVIE_POSTER_PATH + " TEXT, "
            + MoviesColumns.FIELD_MOVIE_POSTER + " BLOB, "
            + MoviesColumns.FIELD_MOVIE_POPULARITY + " INTEGER, "
            + MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE + " INTEGER, "
            + MoviesColumns.FIELD_MOVIE_VOTE_COUNT + " INTEGER NOT NULL DEFAULT 0, "
            + MoviesColumns.FIELD_MOVIE_RELEASE_DATE + " INTEGER, "
            + MoviesColumns.FIELD_MOVIE_OVERVIEW + " TEXT "
            + ", CONSTRAINT field_movie_title UNIQUE (field_movie_title) ON CONFLICT ABORT"
            + " );";

    public static final String SQL_CREATE_TABLE_REVIEWS = "CREATE TABLE IF NOT EXISTS "
            + ReviewsColumns.TABLE_NAME + " ( "
            + ReviewsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ReviewsColumns.FIELD_MOVIE_ID + " INTEGER NOT NULL, "
            + ReviewsColumns.FIELD_CRITIC_NAME + " TEXT NOT NULL, "
            + ReviewsColumns.FIELD_CRITIC_URL + " TEXT, "
            + ReviewsColumns.FIELD_REVIEW_TEXT + " TEXT NOT NULL "
            + ", CONSTRAINT fk_field_movie_id FOREIGN KEY (" + ReviewsColumns.FIELD_MOVIE_ID + ") REFERENCES movies (_id) ON DELETE CASCADE"
            + " );";

    public static final String SQL_CREATE_TABLE_TRAILERS = "CREATE TABLE IF NOT EXISTS "
            + TrailersColumns.TABLE_NAME + " ( "
            + TrailersColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TrailersColumns.FIELD_MOVIE_ID + " INTEGER NOT NULL, "
            + TrailersColumns.FIELD_TRAILER_URL + " TEXT NOT NULL "
            + ", CONSTRAINT fk_field_movie_id FOREIGN KEY (" + TrailersColumns.FIELD_MOVIE_ID + ") REFERENCES movies (_id) ON DELETE CASCADE"
            + ", CONSTRAINT field_trailer_url UNIQUE (field_trailer_url) ON CONFLICT ABORT"
            + " );";

    // @formatter:on

    public static MovieDbHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static MovieDbHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static MovieDbHelper newInstancePreHoneycomb(Context context) {
        return new MovieDbHelper(context);
    }

    private MovieDbHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new MovieDbHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static MovieDbHelper newInstancePostHoneycomb(Context context) {
        return new MovieDbHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private MovieDbHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new MovieDbHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_FAVORITES);
        db.execSQL(SQL_CREATE_TABLE_MOVIES);
        db.execSQL(SQL_CREATE_TABLE_REVIEWS);
        db.execSQL(SQL_CREATE_TABLE_TRAILERS);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
