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
package com.smartvariables.lee.popularmovies2.data.reviews;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.smartvariables.lee.popularmovies2.data.base.AbstractCursor;
import com.smartvariables.lee.popularmovies2.data.movies.*;

/**
 * Cursor wrapper for the {@code reviews} table.
 */
public class ReviewsCursor extends AbstractCursor implements ReviewsModel {
    public ReviewsCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(ReviewsColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code field_movie_id} value.
     */
    public long getFieldMovieId() {
        Long res = getLongOrNull(ReviewsColumns.FIELD_MOVIE_ID);
        if (res == null)
            throw new NullPointerException("The value of 'field_movie_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * tmdb movie ID
     * Can be {@code null}.
     */
    @Nullable
    public Integer getMoviesFieldTmdbMovieId() {
        Integer res = getIntegerOrNull(MoviesColumns.FIELD_TMDB_MOVIE_ID);
        return res;
    }

    /**
     * the movie title
     * Cannot be {@code null}.
     */
    @NonNull
    public String getMoviesFieldMovieTitle() {
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
    public String getMoviesFieldMoviePosterPath() {
        String res = getStringOrNull(MoviesColumns.FIELD_MOVIE_POSTER_PATH);
        return res;
    }

    /**
     * the movie poster
     * Can be {@code null}.
     */
    @Nullable
    public byte[] getMoviesFieldMoviePoster() {
        byte[] res = getBlobOrNull(MoviesColumns.FIELD_MOVIE_POSTER);
        return res;
    }

    /**
     * movie popularity 0-10
     * Can be {@code null}.
     */
    @Nullable
    public Integer getMoviesFieldMoviePopularity() {
        Integer res = getIntegerOrNull(MoviesColumns.FIELD_MOVIE_POPULARITY);
        return res;
    }

    /**
     * movie vote average 0-10
     * Can be {@code null}.
     */
    @Nullable
    public Integer getMoviesFieldMovieVoteAverage() {
        Integer res = getIntegerOrNull(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE);
        return res;
    }

    /**
     * movie vote count
     */
    public int getMoviesFieldMovieVoteCount() {
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
    public Date getMoviesFieldMovieReleaseDate() {
        Date res = getDateOrNull(MoviesColumns.FIELD_MOVIE_RELEASE_DATE);
        return res;
    }

    /**
     * the movie overview
     * Can be {@code null}.
     */
    @Nullable
    public String getMoviesFieldMovieOverview() {
        String res = getStringOrNull(MoviesColumns.FIELD_MOVIE_OVERVIEW);
        return res;
    }

    /**
     * critic's name
     * Cannot be {@code null}.
     */
    @NonNull
    public String getFieldCriticName() {
        String res = getStringOrNull(ReviewsColumns.FIELD_CRITIC_NAME);
        if (res == null)
            throw new NullPointerException("The value of 'field_critic_name' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * critic's url
     * Can be {@code null}.
     */
    @Nullable
    public String getFieldCriticUrl() {
        String res = getStringOrNull(ReviewsColumns.FIELD_CRITIC_URL);
        return res;
    }

    /**
     * the movie review
     * Cannot be {@code null}.
     */
    @NonNull
    public String getFieldReviewText() {
        String res = getStringOrNull(ReviewsColumns.FIELD_REVIEW_TEXT);
        if (res == null)
            throw new NullPointerException("The value of 'field_review_text' in the database was null, which is not allowed according to the model definition");
        return res;
    }
}
