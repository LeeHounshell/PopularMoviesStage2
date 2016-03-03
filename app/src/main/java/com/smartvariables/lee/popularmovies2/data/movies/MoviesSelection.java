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
import android.database.Cursor;
import android.net.Uri;

import com.smartvariables.lee.popularmovies2.data.base.AbstractSelection;

/**
 * Selection for the {@code movies} table.
 */
public class MoviesSelection extends AbstractSelection<MoviesSelection> {
    @Override
    protected Uri baseUri() {
        return MoviesColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MoviesCursor} object, which is positioned before the first entry, or null.
     */
    public MoviesCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MoviesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public MoviesCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code MoviesCursor} object, which is positioned before the first entry, or null.
     */
    public MoviesCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new MoviesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public MoviesCursor query(Context context) {
        return query(context, null);
    }


    public MoviesSelection id(long... value) {
        addEquals("movies." + MoviesColumns._ID, toObjectArray(value));
        return this;
    }

    public MoviesSelection idNot(long... value) {
        addNotEquals("movies." + MoviesColumns._ID, toObjectArray(value));
        return this;
    }

    public MoviesSelection orderById(boolean desc) {
        orderBy("movies." + MoviesColumns._ID, desc);
        return this;
    }

    public MoviesSelection orderById() {
        return orderById(false);
    }

    public MoviesSelection fieldTmdbMovieId(Integer... value) {
        addEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public MoviesSelection fieldTmdbMovieIdNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public MoviesSelection fieldTmdbMovieIdGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public MoviesSelection fieldTmdbMovieIdGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public MoviesSelection fieldTmdbMovieIdLt(int value) {
        addLessThan(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public MoviesSelection fieldTmdbMovieIdLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public MoviesSelection orderByFieldTmdbMovieId(boolean desc) {
        orderBy(MoviesColumns.FIELD_TMDB_MOVIE_ID, desc);
        return this;
    }

    public MoviesSelection orderByFieldTmdbMovieId() {
        orderBy(MoviesColumns.FIELD_TMDB_MOVIE_ID, false);
        return this;
    }

    public MoviesSelection fieldMovieTitle(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public MoviesSelection fieldMovieTitleNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public MoviesSelection fieldMovieTitleLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public MoviesSelection fieldMovieTitleContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public MoviesSelection fieldMovieTitleStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public MoviesSelection fieldMovieTitleEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public MoviesSelection orderByFieldMovieTitle(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_TITLE, desc);
        return this;
    }

    public MoviesSelection orderByFieldMovieTitle() {
        orderBy(MoviesColumns.FIELD_MOVIE_TITLE, false);
        return this;
    }

    public MoviesSelection fieldMoviePosterPath(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public MoviesSelection fieldMoviePosterPathNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public MoviesSelection fieldMoviePosterPathLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public MoviesSelection fieldMoviePosterPathContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public MoviesSelection fieldMoviePosterPathStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public MoviesSelection fieldMoviePosterPathEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public MoviesSelection orderByFieldMoviePosterPath(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER_PATH, desc);
        return this;
    }

    public MoviesSelection orderByFieldMoviePosterPath() {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER_PATH, false);
        return this;
    }

    public MoviesSelection fieldMoviePoster(byte[]... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POSTER, value);
        return this;
    }

    public MoviesSelection fieldMoviePosterNot(byte[]... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POSTER, value);
        return this;
    }


    public MoviesSelection orderByFieldMoviePoster(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER, desc);
        return this;
    }

    public MoviesSelection orderByFieldMoviePoster() {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER, false);
        return this;
    }

    public MoviesSelection fieldMoviePopularity(Integer... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public MoviesSelection fieldMoviePopularityNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public MoviesSelection fieldMoviePopularityGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public MoviesSelection fieldMoviePopularityGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public MoviesSelection fieldMoviePopularityLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public MoviesSelection fieldMoviePopularityLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public MoviesSelection orderByFieldMoviePopularity(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POPULARITY, desc);
        return this;
    }

    public MoviesSelection orderByFieldMoviePopularity() {
        orderBy(MoviesColumns.FIELD_MOVIE_POPULARITY, false);
        return this;
    }

    public MoviesSelection fieldMovieVoteAverage(Integer... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public MoviesSelection fieldMovieVoteAverageNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public MoviesSelection fieldMovieVoteAverageGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public MoviesSelection fieldMovieVoteAverageGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public MoviesSelection fieldMovieVoteAverageLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public MoviesSelection fieldMovieVoteAverageLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public MoviesSelection orderByFieldMovieVoteAverage(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, desc);
        return this;
    }

    public MoviesSelection orderByFieldMovieVoteAverage() {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, false);
        return this;
    }

    public MoviesSelection fieldMovieVoteCount(int... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public MoviesSelection fieldMovieVoteCountNot(int... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public MoviesSelection fieldMovieVoteCountGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public MoviesSelection fieldMovieVoteCountGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public MoviesSelection fieldMovieVoteCountLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public MoviesSelection fieldMovieVoteCountLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public MoviesSelection orderByFieldMovieVoteCount(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, desc);
        return this;
    }

    public MoviesSelection orderByFieldMovieVoteCount() {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, false);
        return this;
    }

    public MoviesSelection fieldMovieReleaseDate(Date... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public MoviesSelection fieldMovieReleaseDateNot(Date... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public MoviesSelection fieldMovieReleaseDate(Long... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public MoviesSelection fieldMovieReleaseDateAfter(Date value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public MoviesSelection fieldMovieReleaseDateAfterEq(Date value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public MoviesSelection fieldMovieReleaseDateBefore(Date value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public MoviesSelection fieldMovieReleaseDateBeforeEq(Date value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public MoviesSelection orderByFieldMovieReleaseDate(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, desc);
        return this;
    }

    public MoviesSelection orderByFieldMovieReleaseDate() {
        orderBy(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, false);
        return this;
    }

    public MoviesSelection fieldMovieOverview(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public MoviesSelection fieldMovieOverviewNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public MoviesSelection fieldMovieOverviewLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public MoviesSelection fieldMovieOverviewContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public MoviesSelection fieldMovieOverviewStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public MoviesSelection fieldMovieOverviewEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public MoviesSelection orderByFieldMovieOverview(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_OVERVIEW, desc);
        return this;
    }

    public MoviesSelection orderByFieldMovieOverview() {
        orderBy(MoviesColumns.FIELD_MOVIE_OVERVIEW, false);
        return this;
    }
}
