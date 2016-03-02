//
//===========================================================================================================
// THIS IS GENERATED CODE! - do not edit
//
// To edit, change JSON configs under 'com/smartvariables/lee/popularmovies/generate_data_contentprovider/'
// Then run the bash script './generate-data-contentprovider.sh' in that same directory
// to create new Java code under 'com/smartvariables/lee/popularmovies/data/' + note old 'data' is deleted!
//
// THIS IS GENERATED CODE! - do not edit
//===========================================================================================================
//
package com.smartvariables.lee.popularmovies.data.trailers;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.smartvariables.lee.popularmovies.data.base.AbstractSelection;
import com.smartvariables.lee.popularmovies.data.movies.*;

/**
 * Selection for the {@code trailers} table.
 */
public class TrailersSelection extends AbstractSelection<TrailersSelection> {
    @Override
    protected Uri baseUri() {
        return TrailersColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TrailersCursor} object, which is positioned before the first entry, or null.
     */
    public TrailersCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TrailersCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public TrailersCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code TrailersCursor} object, which is positioned before the first entry, or null.
     */
    public TrailersCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new TrailersCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public TrailersCursor query(Context context) {
        return query(context, null);
    }


    public TrailersSelection id(long... value) {
        addEquals("trailers." + TrailersColumns._ID, toObjectArray(value));
        return this;
    }

    public TrailersSelection idNot(long... value) {
        addNotEquals("trailers." + TrailersColumns._ID, toObjectArray(value));
        return this;
    }

    public TrailersSelection orderById(boolean desc) {
        orderBy("trailers." + TrailersColumns._ID, desc);
        return this;
    }

    public TrailersSelection orderById() {
        return orderById(false);
    }

    public TrailersSelection fieldMovieId(long... value) {
        addEquals(TrailersColumns.FIELD_MOVIE_ID, toObjectArray(value));
        return this;
    }

    public TrailersSelection fieldMovieIdNot(long... value) {
        addNotEquals(TrailersColumns.FIELD_MOVIE_ID, toObjectArray(value));
        return this;
    }

    public TrailersSelection fieldMovieIdGt(long value) {
        addGreaterThan(TrailersColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection fieldMovieIdGtEq(long value) {
        addGreaterThanOrEquals(TrailersColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection fieldMovieIdLt(long value) {
        addLessThan(TrailersColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection fieldMovieIdLtEq(long value) {
        addLessThanOrEquals(TrailersColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection orderByFieldMovieId(boolean desc) {
        orderBy(TrailersColumns.FIELD_MOVIE_ID, desc);
        return this;
    }

    public TrailersSelection orderByFieldMovieId() {
        orderBy(TrailersColumns.FIELD_MOVIE_ID, false);
        return this;
    }

    public TrailersSelection moviesFieldTmdbMovieId(Integer... value) {
        addEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection moviesFieldTmdbMovieIdNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection moviesFieldTmdbMovieIdGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection moviesFieldTmdbMovieIdGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection moviesFieldTmdbMovieIdLt(int value) {
        addLessThan(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection moviesFieldTmdbMovieIdLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public TrailersSelection orderByMoviesFieldTmdbMovieId(boolean desc) {
        orderBy(MoviesColumns.FIELD_TMDB_MOVIE_ID, desc);
        return this;
    }

    public TrailersSelection orderByMoviesFieldTmdbMovieId() {
        orderBy(MoviesColumns.FIELD_TMDB_MOVIE_ID, false);
        return this;
    }

    public TrailersSelection moviesFieldMovieTitle(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieTitleNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieTitleLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieTitleContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieTitleStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieTitleEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieTitle(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_TITLE, desc);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieTitle() {
        orderBy(MoviesColumns.FIELD_MOVIE_TITLE, false);
        return this;
    }

    public TrailersSelection moviesFieldMoviePosterPath(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePosterPathNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePosterPathLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePosterPathContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePosterPathStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePosterPathEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMoviePosterPath(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER_PATH, desc);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMoviePosterPath() {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER_PATH, false);
        return this;
    }

    public TrailersSelection moviesFieldMoviePoster(byte[]... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POSTER, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePosterNot(byte[]... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POSTER, value);
        return this;
    }


    public TrailersSelection orderByMoviesFieldMoviePoster(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER, desc);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMoviePoster() {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER, false);
        return this;
    }

    public TrailersSelection moviesFieldMoviePopularity(Integer... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePopularityNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePopularityGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePopularityGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePopularityLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public TrailersSelection moviesFieldMoviePopularityLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMoviePopularity(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POPULARITY, desc);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMoviePopularity() {
        orderBy(MoviesColumns.FIELD_MOVIE_POPULARITY, false);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteAverage(Integer... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteAverageNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteAverageGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteAverageGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteAverageLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteAverageLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieVoteAverage(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, desc);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieVoteAverage() {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, false);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteCount(int... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteCountNot(int... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteCountGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteCountGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteCountLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieVoteCountLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieVoteCount(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, desc);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieVoteCount() {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, false);
        return this;
    }

    public TrailersSelection moviesFieldMovieReleaseDate(Date... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieReleaseDateNot(Date... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieReleaseDate(Long... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieReleaseDateAfter(Date value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieReleaseDateAfterEq(Date value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieReleaseDateBefore(Date value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieReleaseDateBeforeEq(Date value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieReleaseDate(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, desc);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieReleaseDate() {
        orderBy(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, false);
        return this;
    }

    public TrailersSelection moviesFieldMovieOverview(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieOverviewNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieOverviewLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieOverviewContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieOverviewStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public TrailersSelection moviesFieldMovieOverviewEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieOverview(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_OVERVIEW, desc);
        return this;
    }

    public TrailersSelection orderByMoviesFieldMovieOverview() {
        orderBy(MoviesColumns.FIELD_MOVIE_OVERVIEW, false);
        return this;
    }

    public TrailersSelection fieldTrailerUrl(String... value) {
        addEquals(TrailersColumns.FIELD_TRAILER_URL, value);
        return this;
    }

    public TrailersSelection fieldTrailerUrlNot(String... value) {
        addNotEquals(TrailersColumns.FIELD_TRAILER_URL, value);
        return this;
    }

    public TrailersSelection fieldTrailerUrlLike(String... value) {
        addLike(TrailersColumns.FIELD_TRAILER_URL, value);
        return this;
    }

    public TrailersSelection fieldTrailerUrlContains(String... value) {
        addContains(TrailersColumns.FIELD_TRAILER_URL, value);
        return this;
    }

    public TrailersSelection fieldTrailerUrlStartsWith(String... value) {
        addStartsWith(TrailersColumns.FIELD_TRAILER_URL, value);
        return this;
    }

    public TrailersSelection fieldTrailerUrlEndsWith(String... value) {
        addEndsWith(TrailersColumns.FIELD_TRAILER_URL, value);
        return this;
    }

    public TrailersSelection orderByFieldTrailerUrl(boolean desc) {
        orderBy(TrailersColumns.FIELD_TRAILER_URL, desc);
        return this;
    }

    public TrailersSelection orderByFieldTrailerUrl() {
        orderBy(TrailersColumns.FIELD_TRAILER_URL, false);
        return this;
    }
}
