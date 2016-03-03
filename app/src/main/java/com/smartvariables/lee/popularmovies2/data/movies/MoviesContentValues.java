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
package com.smartvariables.lee.popularmovies2.data.movies;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.smartvariables.lee.popularmovies2.data.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code movies} table.
 */
public class MoviesContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return MoviesColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable MoviesSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable MoviesSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * tmdb movie ID
     */
    public MoviesContentValues putFieldTmdbMovieId(@Nullable Integer value) {
        mContentValues.put(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public MoviesContentValues putFieldTmdbMovieIdNull() {
        mContentValues.putNull(MoviesColumns.FIELD_TMDB_MOVIE_ID);
        return this;
    }

    /**
     * the movie title
     */
    public MoviesContentValues putFieldMovieTitle(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("fieldMovieTitle must not be null");
        mContentValues.put(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }


    /**
     * path to the movie poster
     */
    public MoviesContentValues putFieldMoviePosterPath(@Nullable String value) {
        mContentValues.put(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public MoviesContentValues putFieldMoviePosterPathNull() {
        mContentValues.putNull(MoviesColumns.FIELD_MOVIE_POSTER_PATH);
        return this;
    }

    /**
     * the movie poster
     */
    public MoviesContentValues putFieldMoviePoster(@Nullable byte[] value) {
        mContentValues.put(MoviesColumns.FIELD_MOVIE_POSTER, value);
        return this;
    }

    public MoviesContentValues putFieldMoviePosterNull() {
        mContentValues.putNull(MoviesColumns.FIELD_MOVIE_POSTER);
        return this;
    }

    /**
     * movie popularity 0-10
     */
    public MoviesContentValues putFieldMoviePopularity(@Nullable Integer value) {
        mContentValues.put(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public MoviesContentValues putFieldMoviePopularityNull() {
        mContentValues.putNull(MoviesColumns.FIELD_MOVIE_POPULARITY);
        return this;
    }

    /**
     * movie vote average 0-10
     */
    public MoviesContentValues putFieldMovieVoteAverage(@Nullable Integer value) {
        mContentValues.put(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public MoviesContentValues putFieldMovieVoteAverageNull() {
        mContentValues.putNull(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE);
        return this;
    }

    /**
     * movie vote count
     */
    public MoviesContentValues putFieldMovieVoteCount(int value) {
        mContentValues.put(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }


    /**
     * movie release date
     */
    public MoviesContentValues putFieldMovieReleaseDate(@Nullable Date value) {
        mContentValues.put(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value == null ? null : value.getTime());
        return this;
    }

    public MoviesContentValues putFieldMovieReleaseDateNull() {
        mContentValues.putNull(MoviesColumns.FIELD_MOVIE_RELEASE_DATE);
        return this;
    }

    public MoviesContentValues putFieldMovieReleaseDate(@Nullable Long value) {
        mContentValues.put(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    /**
     * the movie overview
     */
    public MoviesContentValues putFieldMovieOverview(@Nullable String value) {
        mContentValues.put(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public MoviesContentValues putFieldMovieOverviewNull() {
        mContentValues.putNull(MoviesColumns.FIELD_MOVIE_OVERVIEW);
        return this;
    }
}
