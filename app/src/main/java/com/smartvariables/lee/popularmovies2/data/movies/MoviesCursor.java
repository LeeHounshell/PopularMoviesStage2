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

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.smartvariables.lee.popularmovies2.data.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code movies} table.
 */
public class MoviesCursor extends AbstractCursor implements MoviesModel {
    public MoviesCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(MoviesColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * tmdb movie ID
     * Can be {@code null}.
     */
    @Nullable
    public Integer getFieldTmdbMovieId() {
        Integer res = getIntegerOrNull(MoviesColumns.FIELD_TMDB_MOVIE_ID);
        return res;
    }

    /**
     * the movie title
     * Cannot be {@code null}.
     */
    @NonNull
    public String getFieldMovieTitle() {
        String res = getStringOrNull(MoviesColumns.FIELD_MOVIE_TITLE);
        if (res == null)
            throw new NullPointerException("The value of 'field_movie_title' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * path to the movie poster
     * Can be {@code null}.
     */
    @Nullable
    public String getFieldMoviePosterPath() {
        String res = getStringOrNull(MoviesColumns.FIELD_MOVIE_POSTER_PATH);
        return res;
    }

    /**
     * the movie poster
     * Can be {@code null}.
     */
    @Nullable
    public byte[] getFieldMoviePoster() {
        byte[] res = getBlobOrNull(MoviesColumns.FIELD_MOVIE_POSTER);
        return res;
    }

    /**
     * movie popularity 0-10
     * Can be {@code null}.
     */
    @Nullable
    public Integer getFieldMoviePopularity() {
        Integer res = getIntegerOrNull(MoviesColumns.FIELD_MOVIE_POPULARITY);
        return res;
    }

    /**
     * movie vote average 0-10
     * Can be {@code null}.
     */
    @Nullable
    public Integer getFieldMovieVoteAverage() {
        Integer res = getIntegerOrNull(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE);
        return res;
    }

    /**
     * movie vote count
     */
    public int getFieldMovieVoteCount() {
        Integer res = getIntegerOrNull(MoviesColumns.FIELD_MOVIE_VOTE_COUNT);
        if (res == null)
            throw new NullPointerException("The value of 'field_movie_vote_count' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * movie release date
     * Can be {@code null}.
     */
    @Nullable
    public Date getFieldMovieReleaseDate() {
        Date res = getDateOrNull(MoviesColumns.FIELD_MOVIE_RELEASE_DATE);
        return res;
    }

    /**
     * the movie overview
     * Can be {@code null}.
     */
    @Nullable
    public String getFieldMovieOverview() {
        String res = getStringOrNull(MoviesColumns.FIELD_MOVIE_OVERVIEW);
        return res;
    }
}
