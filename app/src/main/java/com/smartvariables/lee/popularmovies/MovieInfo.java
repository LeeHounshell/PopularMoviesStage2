package com.smartvariables.lee.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.smartvariables.lee.popularmovies.data.MovieDbHelper;
import com.smartvariables.lee.popularmovies.data.trailers.TrailersColumns;
import com.smartvariables.lee.popularmovies.data_helper.MoviesContract;
import com.smartvariables.lee.popularmovies.util.BitmapUtility;
import com.smartvariables.lee.popularmovies.util.BitmapUtilityException;
import com.smartvariables.lee.popularmovies.util.DateUtility;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import info.movito.themoviedbapi.model.MovieDb;

// manage a memory-instance of Movie meta-data, including database operations.
// each MovieViewHolder contains one of these for the Movie instance being managed.
public class MovieInfo implements Parcelable {
    private final static String TAG = "LEE: <" + MovieInfo.class.getSimpleName() + ">";

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel in) {
            Log.v(TAG, "createFromParcel");
            return new MovieInfo(in);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            Log.v(TAG, "newArray");
            return new MovieInfo[size];
        }
    };

    private String mTitle;
    private String mPosterPath;
    private Float mPopularity;
    private Float mVoteAverage;
    private Integer mVoteCount;
    private String mReleaseDate;
    private String mOverview;
    private Long mTmdbMovieId;
    private Long mMovieRowId;
    private boolean mDidSearchFavorites;
    private boolean mFavorite;
    private String mReviews;
    private boolean mLoadedParcelable;
    private Integer mPosterWidth;
    private Integer mPosterHeight;
    private Bitmap mPoster;
    private MovieViewHolder mMovieViewHolder = null;
    private boolean mExistingPosterImage;
    private boolean mDidSqlitePosterImageUpdate;

    public void reset() {
        if (getMovieViewHolder() != null && getMovieViewHolder().getPosterImageView() != null) {
            getMovieViewHolder().getPosterImageView().notifyOn(getMovieViewHolder());
        }
        mDidSearchFavorites = false;
        mDidSqlitePosterImageUpdate = false;
    }

    public MovieInfo(String argItemId) {
        Log.v(TAG, "-DUMMY-INSTNACE- MovieInfo(argItemId="+argItemId+")");
        mLoadedParcelable = false;
        mExistingPosterImage = false;
        mDidSqlitePosterImageUpdate = false;
        mTitle = argItemId; // dummy instance will get the named Movie from local SQLite
        mPosterPath = null;
        mPopularity = null;
        mVoteAverage = null;
        mVoteCount = 0;
        mReleaseDate = null;
        mOverview = null;
        mDidSearchFavorites = false;
        mTmdbMovieId = 0L;
        mMovieRowId = -1L; // no row
        mFavorite = false;
        mReviews = null;
        mPosterWidth = 0;
        mPosterHeight = 0;
        mPoster = null;
        querySqliteMovieInfo(mTitle);
    }

    public MovieInfo(MovieDb movie) {
        Log.v(TAG, "MovieInfo(MovieDb)");
        mLoadedParcelable = false;
        mExistingPosterImage = false;
        mDidSqlitePosterImageUpdate = false;
        mTitle = movie.getTitle();
        mPosterPath = movie.getPosterPath();
        mPopularity = movie.getPopularity();
        mVoteAverage = movie.getVoteAverage();
        mVoteCount = movie.getVoteCount();
        mReleaseDate = movie.getReleaseDate();
        mOverview = movie.getOverview();
        mDidSearchFavorites = false;
        mTmdbMovieId = (long) movie.getId();
        mMovieRowId = -1L; // set by isFavorite() if movie exists in local DB, else -1L
        mFavorite = isFavorite();
        mReviews = null;
        mPosterWidth = 0;
        mPosterHeight = 0;
        mPoster = null;
        if (mMovieRowId == -1L) { // not in Sqlite
            Log.v(TAG, "adding movie ID=" + mTmdbMovieId + " titled '" + mTitle + "' to database for offline use");
            insertSqliteMovie();
        } else {
            Log.v(TAG, "loading movie ID=" + mTmdbMovieId + " titled '" + mTitle + "' from database");
            querySqliteMovieInfo(mMovieRowId);
        }
    }

    public MovieInfo(String title,
                     String posterPath,
                     Float populatity,
                     Float voteAverage,
                     Integer voteCount,
                     String releaseDate,
                     String overview,
                     Long tmdbMovieId,
                     Long movieRowId,
                     Integer didSearchFavorites,
                     Integer favorite,
                     String reviews,
                     Integer posterWidth,
                     Integer posterHeight,
                     Parcel in
    ) {
        Log.v(TAG, "MovieInfo(...) <--");
        mLoadedParcelable = true;
        mExistingPosterImage = false;
        mDidSqlitePosterImageUpdate = false;
        mTitle = title;
        mPosterPath = posterPath;
        mPopularity = populatity;
        mVoteAverage = voteAverage;
        mVoteCount = voteCount;
        mReleaseDate = releaseDate;
        mOverview = overview;
        mTmdbMovieId = tmdbMovieId;
        mMovieRowId = movieRowId;
        mDidSearchFavorites = didSearchFavorites != 0 ? true : false;
        mFavorite = favorite != 0 ? true : false;
        mReviews = reviews;
        mPosterWidth = posterWidth;
        mPosterHeight = posterHeight;
        if (! readPosterImage(in)) {
            mPoster = null;
        }
    }

    public MovieInfo(Cursor cursor) {
        if (cursor.getCount() > 0) {
            getMovieDbInfoAtCursor(cursor);
        }
    }

    protected MovieInfo(Parcel in) {
        Log.v(TAG, "MovieInfo(Parcel) <--");
        mLoadedParcelable = true;
        mExistingPosterImage = false;
        mDidSqlitePosterImageUpdate = false;
        mTitle = in.readString();
        mPosterPath = in.readString();
        mPopularity = in.readFloat();
        mVoteAverage = in.readFloat();
        mVoteCount = in.readInt();
        mReleaseDate = in.readString();
        mOverview = in.readString();
        mTmdbMovieId = new Long(in.readLong());
        mMovieRowId = new Long(in.readLong());
        mDidSearchFavorites = in.readInt() != 0 ? true : false;
        mFavorite = in.readInt() != 0 ? true : false;
        mReviews = in.readString();
        mPosterWidth = in.readInt();
        mPosterHeight = in.readInt();
        if (! readPosterImage(in)) {
            mPoster = null;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v(TAG, "writeToParcel");
        dest.writeString(getTitle());
        dest.writeString(getPosterPath());
        dest.writeFloat(getPopularity());
        dest.writeFloat(getVoteAverage());
        dest.writeInt(getVoteCount());
        dest.writeString(getReleaseDate());
        dest.writeString(getOverview());
        dest.writeLong(getTmdbMovieId());
        dest.writeLong(getMovieRowId());
        dest.writeInt(getDidSearchFavorites() ? new Integer(1) : new Integer(0));
        if (getDidSearchFavorites()) {
            dest.writeInt(getFavorite() ? new Integer(1) : new Integer(0));
        } else {
            dest.writeInt(new Integer(0));
        }
        dest.writeString(getReviews());
        dest.writeInt(getPosterWidth()); // poster width
        dest.writeInt(getPosterHeight()); // poster height

        // CAREFUL OOM: passing the images (even compressed) takes up lots of memory, unfortunately.
        Log.v(TAG, "*** POSTER-IMAGE INSIDE PARCEL ***");
        byte[] posterBytes = BitmapUtility.getBytes(getPoster(), MovieViewHolder.MIN_IMAGE_SIZE);
        if (posterBytes != null && posterBytes.length > 0) {
            dest.writeInt(new Integer(posterBytes.length)); // poster size
            dest.writeByteArray(posterBytes); // the poster image
        } else {
            dest.writeInt(new Integer(0));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected boolean readPosterImage(Parcel in) {
        Log.v(TAG, "readPosterImage(Parcel in)");
        boolean success = false;
        mPoster = null;
        try {
            int posterSize = in.readInt();
            if (posterSize == 0) {
                // load the previously saved the poster image from disk
                Log.v(TAG, "readOneBitmapFromFlash for width=" + mPosterWidth + " height=" + mPosterHeight);
                mPoster = BitmapUtility.readOneBitmapFromFlash(mPosterPath, mPosterWidth, mPosterHeight);
                if (mPoster != null) {
                    mExistingPosterImage = true;
                    success = true;
                }
            } else {
                Log.v(TAG, "readPosterImage: posterSize=" + posterSize);
                // CAREFUL OOM: passing the images (even compressed) takes up lots of memory, unfortunately.
                // the poster image is included.. read it
                byte[] posterBytes = new byte[posterSize];
                in.readByteArray(posterBytes);
                if (posterBytes.length > 0) {
                    mPoster = BitmapUtility.getImage(posterBytes);
                    mExistingPosterImage = true;
                    success = true;
                }
            }
        } catch (BitmapUtilityException e) {
            Log.w(TAG, "unable to readOneBitmapFromFlash: error=" + e);
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "OUT OF MEMORY: unable to readOneBitmapFromFlash: error=" + e);
        }
        return success;
    }

    protected void insertSqliteMovie() {
        Log.v(TAG, "insertSqliteMovie: - movie=" + getTitle());
        Context context = PopularMoviesStage2Application.getAppContext();
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues movieValues = new ContentValues();
        if (mReleaseDate != null && mReleaseDate.length() > 0) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date released = format.parse(mReleaseDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(released);
                if (calendar.get(Calendar.YEAR) > 1900 && calendar.get(Calendar.YEAR) < 2100) {
                    Log.v(TAG, "insertSqliteMovie: Date released=" + released + ", time=" + released.getTime());
                    movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE, MoviesContract.normalizeDate(released.getTime()));
                    Log.v(TAG, "insertSqliteMovie: normalizeDate=" + MoviesContract.normalizeDate(released.getTime()));
                } else {
                    Log.v(TAG, "insesrtSqliteMoive: Invalid release year. calendar=" + calendar);
                }
            } catch (ParseException e) {
                Log.e(TAG, "unable to convert mReleaseDate='" + mReleaseDate + "' to java Date");
            }
        }
        movieValues.put(MoviesContract.MoviesEntry.FIELD_TMDB_MOVIE_ID, mTmdbMovieId);
        movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE, mTitle);
        movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH, mPosterPath);
        movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY, mPopularity);
        movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE, mVoteAverage);
        movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT, mVoteCount);
        movieValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW, mOverview);
        Log.v(TAG, "===> SQL INSERT MOVIE - movie=" + mTitle);
        try {
            mMovieRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, movieValues);
            Log.v(TAG, "SQLite INSERT NEW MOVIE SUCCESS! - movieRowId=" + mMovieRowId + " - movie=" + mTitle);
            mDidSearchFavorites = true; // not really, but this is a new movie and can't be 'favorited' yet
            mExistingPosterImage = false;
            mDidSqlitePosterImageUpdate = false;
        } catch (SQLiteConstraintException e) {
            Log.e(TAG, "CONSTRAINT EXCEPTION: SQLITE UNABLE to INSERT movieValues=" + movieValues);
        } catch (SQLiteException e) {
            Log.v(TAG, "SQLITE EXCEPTION: during with INSERT for MOVIE '" + mTitle + "' exception=" + e);
        }
    }

    protected void querySqliteMovieInfo() {
        querySqliteMovieInfo(mTitle);
    }

    protected void querySqliteMovieInfo(String title) {
        if (title != null) {
            Context context = PopularMoviesStage2Application.getAppContext();
            Log.v(TAG, "SQLITE querySqliteMovieInfo: SQLITE QUERY TITLE=" + title);
            Cursor cursor = context.getContentResolver().query(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    null, // all columns
                    MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE + "=?", // where
                    new String[]{title}, // Values for the "where" clause
                    null  // sort order
            );
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                getMovieDbInfoAtCursor(cursor);
            }
            cursor.close();
        }
        else {
            Log.v(TAG, "querySqliteMovieInfo: the title is null!");
        }
    }

    protected void querySqliteMovieInfo(long movieRowId) {
        if (movieRowId == -1L) {
            Log.v(TAG, "SQLITE querySqliteMovieInfo: but movieRowId == -1!");
            return;
        }
        Context context = PopularMoviesStage2Application.getAppContext();
        Log.v(TAG, "SQLITE querySqliteMovieInfo: SQLITE QUERY ID=" + movieRowId);
        Cursor cursor = context.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,  // Table to Query
                null, // all columns
                MoviesContract.MoviesEntry._ID + "=?", // where
                new String[]{Long.toString(movieRowId)}, // Values for the "where" clause
                null // sort order
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            getMovieDbInfoAtCursor(cursor);
        }
        cursor.close();
    }

    // see if we can query the poster image in SQLite..
    public synchronized boolean querySqliteMoviePoster(MovieViewHolder holder) {
        Log.v(TAG, "querySqliteMoviePoster: movie=" + getTitle());
        Context context = PopularMoviesStage2Application.getAppContext();
        MovieDbHelper dbHelper = MovieDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.v(TAG, "SQLITE querySqliteMoviePoster: SQLITE QUERY POSTER IMAGE - movie=" + getTitle());
        Cursor cursor = db.query(
                MoviesContract.MoviesEntry.TABLE_NAME, // Table to Query
                new String[]{MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER}, // specific column.
                MoviesContract.MoviesEntry._ID + "=?", // where
                new String[]{Long.toString(getMovieRowId())}, // Values for the "where" clause
                null,      // columns to group by
                null,      // columns to filter by row groups
                null       // sort order
        );
        boolean querySuccess = false;
        if (cursor.getCount() > 0) {
            querySuccess = true;
            Log.v(TAG, "querySqliteMoviePoster: querySuccess=" + querySuccess);
            Log.v(TAG, "querySqliteMoviePoster: found " + cursor.getCount() + " poster - movie=" + getTitle());
            cursor.moveToFirst();
            querySuccess = processPosterImageData(cursor);
        }
        cursor.close();
        Log.v(TAG, "querySqliteMoviePoster: ==> querySuccess=" + querySuccess + " - movie=" + getTitle() + " <==");
        return querySuccess;
    }

    public static synchronized boolean updateSqliteMoviePoster(MovieViewHolder holder, Bitmap posterImage) {
        MovieInfo movieInfo = holder.getMovie();
        if (movieInfo.mDidSqlitePosterImageUpdate) {
            Log.v(TAG, "updateSqliteMoviePoster: ==> SQL updateSqliteMoviePoster FAIL because POSTER ALREADY UPDATED - movie=" + movieInfo.getTitle());
            return true; // pretend the update succeeded, in this case.
        }
        Log.v(TAG, "SQL updateSqliteMoviePoster - movie=" + movieInfo.getTitle());
        boolean synchronizedToSQLite = false;
        byte[] image = BitmapUtility.getBytes(posterImage, MovieViewHolder.MIN_IMAGE_SIZE);
        if (image == null) {
            Log.v(TAG, "updateSqliteMoviePoster: ==> FAIL because INVALID IMAGE RECEIVED - movie=" + movieInfo.getTitle());
            return false;
        }
        holder.setMovieImageViewState(MovieViewHolder.MovieImageViewState.SQLITE_STORE);
        ContentValues updateValues = new ContentValues();
        updateValues.put(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER, image);
        Log.v(TAG, "SQLITE updateSqliteMoviePoster: UPDATING POSTER IMAGE in SQLite - movie=" + movieInfo.getTitle());
        int count = holder.getActivityBase().getContentResolver().update(
                MoviesContract.MoviesEntry.CONTENT_URI,
                updateValues,
                MoviesContract.MoviesEntry._ID + "=?",
                new String[]{Long.toString(movieInfo.getMovieRowId())});
        if (count > 0) {
            Log.v(TAG, "updateSqliteMoviePoster: SQL UPDATE SUCCESS! - updated " + count + " rows in " + MoviesContract.MoviesEntry.CONTENT_URI + " with new POSTER IMAGE");
            movieInfo.mDidSqlitePosterImageUpdate = true;
            movieInfo.mExistingPosterImage = true;
            holder.setNoImage(false);
            holder.setMovieImageViewState(MovieViewHolder.MovieImageViewState.IMAGE_OK);
            Log.v(TAG, "updateSqliteMoviePoster: ==> state=IMAGE_OK <== - movie=" + movieInfo.getTitle());
            synchronizedToSQLite = true;
        } else {
            Log.w(TAG, "updateSqliteMoviePoster: SQL UPDATE FAILED.. - movie=" + movieInfo.getTitle());
        }
        return synchronizedToSQLite;
    }

    protected void getMovieDbInfoAtCursor(Cursor cursor) {
        int idx = cursor.getColumnIndex(MoviesContract.MoviesEntry._ID);
        mMovieRowId = cursor.getLong(idx);
        idx = cursor.getColumnIndex(MoviesContract.MoviesEntry.FIELD_TMDB_MOVIE_ID);
        mTmdbMovieId = cursor.getLong(idx);
        idx = cursor.getColumnIndex(MoviesContract.MoviesEntry.FIELD_MOVIE_TITLE);
        mTitle = cursor.getString(idx);
        idx = cursor.getColumnIndex(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER_PATH);
        mPosterPath = cursor.getString(idx);
        mPoster = null;
        idx = cursor.getColumnIndex(MoviesContract.MoviesEntry.FIELD_MOVIE_POPULARITY);
        mPopularity = cursor.getFloat(idx);
        idx = cursor.getColumnIndex(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_AVERAGE);
        mVoteAverage = cursor.getFloat(idx);
        idx = cursor.getColumnIndex(MoviesContract.MoviesEntry.FIELD_MOVIE_VOTE_COUNT);
        mVoteCount = cursor.getInt(idx);
        idx = cursor.getColumnIndex(MoviesContract.MoviesEntry.FIELD_MOVIE_OVERVIEW);
        mOverview = cursor.getString(idx);
        idx = cursor.getColumnIndex(MoviesContract.MoviesEntry.FIELD_MOVIE_RELEASE_DATE);
        mReleaseDate = MoviesContract.tmdbDate(cursor.getLong(idx));
        processPosterImageData(cursor);
    }

    // we have PosterImage data at this Cursor
    protected boolean processPosterImageData(Cursor cursor) {
        boolean querySuccess = true;
        int idx = cursor.getColumnIndex(MoviesContract.MoviesEntry.FIELD_MOVIE_POSTER);
        byte[] posterBytes = cursor.getBlob(idx);
        if (posterBytes == null) {
            Log.w(TAG, "processPosterImageData: EMPTY posterBytes - THIS IMAGE HAS NOT BEEN SAVED TO SLQITE YET! - movie="+getTitle());
            querySuccess = false;
            mExistingPosterImage = false;
            Log.w(TAG, "*** BOTH SQL POSTER-BYTES AND THE MOVIE-VIEW-HOLDER ARE NULL *** - movie="+getTitle());
        } else {
            Log.v(TAG, "processPosterImageData: *** SET THE POSTER IMAGE FROM SQLITE BITMAP *** - movie=" + getTitle());
            try {
                Log.v(TAG, "processPosterImageData: ==> state SQLITE_FOUND <==");
                mExistingPosterImage = true;
                Bitmap posterImage = BitmapUtility.getImage(posterBytes);
                setPoster(posterImage);
                setPosterWidth(posterImage.getWidth());
                setPosterHeight(posterImage.getHeight());
                if (getMovieViewHolder() != null) {
                    getMovieViewHolder().setMovieImageViewState(MovieViewHolder.MovieImageViewState.SQLITE_FOUND);
                    Log.w(TAG, "processPosterImageData: state=SQLITE_FOUND - movie=" + getTitle());
                    getMovieViewHolder().setNoImage(false);
                }
                else {
                    Log.w(TAG, "*** THE MOVIE-VIEW-HOLDER IS NULL *** - movie="+getTitle());
                }
                Log.v(TAG, "=============================================================================");
                Log.v(TAG, "NEED TO UPDATE DISPLAYED POSTER IMAGE - movie=" + getTitle());
                Log.v(TAG, "=============================================================================");
            } catch (OutOfMemoryError e) {
                Log.w(TAG, "processPosterImageData: *** out of memory - SQLite update posterImage - movie"+getTitle());
                querySuccess = false;
                mExistingPosterImage = false;
            }
        }
        return querySuccess;
    }

    public long getTmdbMovieId() {
        return mTmdbMovieId;
    }

    public long getMovieRowId() {
        return mMovieRowId;
    }

    public void setMovieRowId(long movieRowId) {
        mMovieRowId = movieRowId;
    }

    public Bitmap getPoster() {
        return mPoster;
    }

    public void setPoster(Bitmap poster) {
        mPoster = poster;
    }

    public boolean isExistingPosterImage() {
        return mExistingPosterImage;
    }

    public boolean isLoadedParcelable() {
        return mLoadedParcelable;
    }

    public String getTitle() {
        return mTitle;
    }

    public Float getPopularity() {
        return mPopularity;
    }

    public Float getVoteAverage() {
        return mVoteAverage;
    }

    public Integer getVoteCount() {
        return mVoteCount;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public String setReviews(String reviews) {
        mReviews = reviews;
        return getReviews();
    }

    public String getReviews() {
        return (mReviews == null) ? "" : mReviews;
    }

    public boolean getDidSearchFavorites() {
        return mDidSearchFavorites;
    }

    public boolean getFavorite() {
        Log.v(TAG, "getFavorite");
        if (! mDidSearchFavorites) {
            Log.v(TAG, "getFavorite: did not search yet..");
            mFavorite = isFavorite(); // load the value from SQLite
        }
        Log.v(TAG, "getFavorite: return="+mFavorite);
        return mFavorite;
    }

    protected boolean isFavorite() {
        Log.v(TAG, "isFavorite");
        if (mDidSearchFavorites) {
            Log.v(TAG, "isFavorite: already searched. mFavorite="+mFavorite);
            return mFavorite; // after 1st lookup, cache result
        }
        mDidSearchFavorites = true;
        if (mTitle == null || mTitle.length() == 0) {
            mFavorite = false;
            mTmdbMovieId = -1L;
            mMovieRowId = -1L;
            Log.e(TAG, "isFavorite: INVALID Title! - mFavorite=" + mFavorite);
            return mFavorite;
        }
        if (mMovieRowId == -1L) {
            querySqliteMovieInfo();
            if (mMovieRowId == -1L) {
                Log.w(TAG, "isFavorite: *** UNABLE TO LOCATE MOVIE ROW! *** - movie="+getTitle());
                mFavorite = false;
                return mFavorite;
            }
        }
        // now we have the mMovieRowId, we can get the favorite_star
        Context context = PopularMoviesStage2Application.getAppContext();
        Log.v(TAG, "isFavorite: SQLITE querySqliteMovieInfo: SQLITE QUERY FAVORITES");
        Cursor favoritesCursor = context.getContentResolver().query(
                MoviesContract.FavoritesEntry.CONTENT_URI,  // Table to Query
                null, // all columns
                MoviesContract.FavoritesEntry.FIELD_MOVIE_ID + "=?", // where
                new String[]{Long.toString(mMovieRowId)}, // Values for the "where" clause
                null // sort order
        );
        mFavorite = (favoritesCursor.getCount() > 0);
        favoritesCursor.close();
        Log.v(TAG, "isFavorite: return mFavorite=" + mFavorite);
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        Log.v(TAG, "setFavorite(" + favorite + ")");
        boolean oldFavorite = mFavorite;
        if (!mDidSearchFavorites) {
            oldFavorite = isFavorite(); // also set mMovieRowId, if necessary
        }
        if (mMovieRowId == -1L) {
            Log.w(TAG, "setFavorite: *** UNABLE TO LOCATE MOVIE ROW! *** - movie=" + getTitle());
            return;
        }
        Context context = PopularMoviesStage2Application.getAppContext();
        if (favorite && !oldFavorite) {
            MovieDbHelper dbHelper = MovieDbHelper.getInstance(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues favoriteValues = new ContentValues();
            favoriteValues.put(MoviesContract.FavoritesEntry.FIELD_MOVIE_ID, mMovieRowId);
            try {
                long favoriteRowId = db.insert(MoviesContract.FavoritesEntry.TABLE_NAME, null, favoriteValues);
                Log.v(TAG, "setFavorite: SQLite INSERT new FAVORITES row - ID=" + favoriteRowId + " for movie ID=" + mMovieRowId + " (" + mTitle + ")");
            } catch (SQLiteConstraintException e) {
                Log.e(TAG, "setFavorite: CONSTRAINT EXCEPTION: SQLITE unable to INSERT favoriteValues=" + favoriteValues);
            } catch (SQLiteException e) {
                Log.v(TAG, "setFavorite: SQLITE EXCEPTION: problem with INSERT for FAVORITES '" + mTitle + "' exception=" + e);
            }
        } else if (!favorite && oldFavorite) {
            Log.v(TAG, "setFavorite: SQLite delete existing Favorites row for movie ID=" + mMovieRowId + " (" + mTitle + ")");
            String whereClause = MoviesContract.FavoritesEntry.FIELD_MOVIE_ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(mMovieRowId)};
            try {
                context.getContentResolver().delete(MoviesContract.FavoritesEntry.buildFavoritesUri(), whereClause, whereArgs);
                Log.v(TAG, "setFavorite: deleted movie=" + mMovieRowId + " (" + mTitle + ")");
            } catch (SQLiteException e) {
                Log.e(TAG, "setFavorite: SQLITE EXCEPTION: unable to delete movie=" + mMovieRowId + " error=" + e);
            }
        }
        mFavorite = favorite;
    }

    public void setPosterWidth(int posterWidth) {
        Log.v(TAG, "setPosterWidth=" + posterWidth);
        mPosterWidth = posterWidth;
    }

    public int getPosterWidth() {
        if (mPosterWidth == null) {
            mPosterWidth = new Integer(0);
        }
        return mPosterWidth;
    }

    public void setPosterHeight(int posterHeight) {
        Log.v(TAG, "setPosterHeight=" + posterHeight);
        mPosterWidth = posterHeight;
    }

    public int getPosterHeight() {
        if (mPosterHeight == null) {
            mPosterHeight = new Integer(0);
        }
        return mPosterHeight;
    }

    // Note that mMovieRowId will already be set via the MovieInfo constructor
    public void addTrailer(String trailerUrl) {
        Log.v(TAG, "SQLITE addTrailer url=" + trailerUrl + " - movie=" + getTitle());
        // Insert trailer ContentValues into database and get a trailer row ID back
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(MoviesContract.TrailersEntry.FIELD_MOVIE_ID, mMovieRowId);
        trailerValues.put(MoviesContract.TrailersEntry.FIELD_TRAILER_URL, trailerUrl);
        Context context = PopularMoviesStage2Application.getAppContext();
        // we use the Sqlite contraint mechanism to prevent trailer duplicates
        try {
            Uri trailerUri = context.getContentResolver().insert(MoviesContract.TrailersEntry.buildTrailersUri(), trailerValues);
            long trailerRowId = ContentUris.parseId(trailerUri);
            Log.v(TAG, "addTrailer: SQLITE INSERT TRAILER - trailerRowId=" + trailerRowId + " mMovieRowId=" + mMovieRowId + " TRAILER=" + trailerUrl);
        } catch (SQLiteConstraintException e) {
            Log.v(TAG, "CONSTRAINT EXCEPTION: TRAILER '" + trailerUrl + "' is already in the database.");
        } catch (SQLiteException e) {
            Log.v(TAG, "SQLITE EXCEPTION: problem with INSERT for TRAILER '" + trailerUrl + "' exception=" + e);
        }
    }

    public final String[] getTrailers() {
        Context context = PopularMoviesStage2Application.getAppContext();
        Log.v(TAG, "SQLITE getTrailers: SQLITE QUERY TRAILERS - movie=" + getTitle());
        Cursor cursor = context.getContentResolver().query(
                TrailersColumns.CONTENT_URI, // table
                null, // all columns.
                MoviesContract.FavoritesEntry.FIELD_MOVIE_ID + "=?", // where
                new String[]{Long.toString(mMovieRowId)}, // Values for the "where" clause
                null  // order by
        );
        if (cursor.getCount() > 0) {
            Log.v(TAG, "found " + cursor.getCount() + " trailers");
            cursor.moveToFirst();
            final String trailerUrls[] = new String[cursor.getCount()];
            int index = 0;
            while (cursor.isAfterLast() == false) {
                int idx = cursor.getColumnIndex(MoviesContract.TrailersEntry.FIELD_TRAILER_URL);
                String trailerUrl = cursor.getString(idx);
                Log.v(TAG, "*** trailerUrl=" + trailerUrl);
                trailerUrls[index] = trailerUrl;
                ++index;
                cursor.moveToNext();
            }
            cursor.close();
            return trailerUrls;
        }
        cursor.close();
        return null;
    }

    @Override
    public String toString() {
        Context context = PopularMoviesStage2Application.getAppContext();
        String overview_head = context.getResources().getString(R.string.overview_head);
        String release_head = context.getResources().getString(R.string.release_head);
        String movieInfo = "\n<p>Title: " + mTitle + "</p>\n" + release_head + "\n" + DateUtility.getReadableDate(mReleaseDate) + "</p>\n<br>\n<br>" + mVoteAverage / (float) 2.0 + " Stars (out of 5)\n<br>\n<br>\n\n" + overview_head + "\n<br>" + mOverview + "\n<br>\n";
        return movieInfo;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31) // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                .append(mTitle)
                .append(mPosterPath)
                .append(mPopularity)
                .append(mVoteAverage)
                .append(mVoteCount)
                .append(mReleaseDate)
                .append(mOverview)
                .append(mTmdbMovieId)
                .append(mMovieRowId)
                .append(mFavorite)
                .append(mReviews)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MovieInfo)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        MovieInfo rhs = (MovieInfo) obj;
        return new EqualsBuilder()
                // if deriving: appendSuper(super.equals(obj)).
                .append(mTitle, rhs.mTitle)
                .append(mPosterPath, rhs.mPosterPath)
                .append(mPopularity, rhs.mPopularity)
                .append(mVoteAverage, rhs.mVoteAverage)
                .append(mVoteCount, rhs.mVoteCount)
                .append(mReleaseDate, rhs.mReleaseDate)
                .append(mOverview, rhs.mOverview)
                .append(mTmdbMovieId, rhs.mTmdbMovieId)
                .append(mMovieRowId, rhs.mMovieRowId)
                .append(mFavorite, rhs.mFavorite)
                .append(mReviews, rhs.mReviews)
                .isEquals();
    }

    public void setMovieViewHolder(MovieViewHolder movieViewHolder) {
        this.mMovieViewHolder = movieViewHolder;
    }

    public MovieViewHolder getMovieViewHolder() {
        return this.mMovieViewHolder;
    }

}
