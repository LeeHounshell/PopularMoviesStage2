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
package com.smartvariables.lee.popularmovies2.data.favorites;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.smartvariables.lee.popularmovies2.data.base.AbstractSelection;
import com.smartvariables.lee.popularmovies2.data.movies.*;

/**
 * Selection for the {@code favorites} table.
 */
public class FavoritesSelection extends AbstractSelection<FavoritesSelection> {
    @Override
    protected Uri baseUri() {
        return FavoritesColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code FavoritesCursor} object, which is positioned before the first entry, or null.
     */
    public FavoritesCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new FavoritesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public FavoritesCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code FavoritesCursor} object, which is positioned before the first entry, or null.
     */
    public FavoritesCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new FavoritesCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public FavoritesCursor query(Context context) {
        return query(context, null);
    }


    public FavoritesSelection id(long... value) {
        addEquals("favorites." + FavoritesColumns._ID, toObjectArray(value));
        return this;
    }

    public FavoritesSelection idNot(long... value) {
        addNotEquals("favorites." + FavoritesColumns._ID, toObjectArray(value));
        return this;
    }

    public FavoritesSelection orderById(boolean desc) {
        orderBy("favorites." + FavoritesColumns._ID, desc);
        return this;
    }

    public FavoritesSelection orderById() {
        return orderById(false);
    }

    public FavoritesSelection fieldMovieId(long... value) {
        addEquals(FavoritesColumns.FIELD_MOVIE_ID, toObjectArray(value));
        return this;
    }

    public FavoritesSelection fieldMovieIdNot(long... value) {
        addNotEquals(FavoritesColumns.FIELD_MOVIE_ID, toObjectArray(value));
        return this;
    }

    public FavoritesSelection fieldMovieIdGt(long value) {
        addGreaterThan(FavoritesColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection fieldMovieIdGtEq(long value) {
        addGreaterThanOrEquals(FavoritesColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection fieldMovieIdLt(long value) {
        addLessThan(FavoritesColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection fieldMovieIdLtEq(long value) {
        addLessThanOrEquals(FavoritesColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection orderByFieldMovieId(boolean desc) {
        orderBy(FavoritesColumns.FIELD_MOVIE_ID, desc);
        return this;
    }

    public FavoritesSelection orderByFieldMovieId() {
        orderBy(FavoritesColumns.FIELD_MOVIE_ID, false);
        return this;
    }

    public FavoritesSelection moviesFieldTmdbMovieId(Integer... value) {
        addEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection moviesFieldTmdbMovieIdNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection moviesFieldTmdbMovieIdGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection moviesFieldTmdbMovieIdGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection moviesFieldTmdbMovieIdLt(int value) {
        addLessThan(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection moviesFieldTmdbMovieIdLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldTmdbMovieId(boolean desc) {
        orderBy(MoviesColumns.FIELD_TMDB_MOVIE_ID, desc);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldTmdbMovieId() {
        orderBy(MoviesColumns.FIELD_TMDB_MOVIE_ID, false);
        return this;
    }

    public FavoritesSelection moviesFieldMovieTitle(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieTitleNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieTitleLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieTitleContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieTitleStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieTitleEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieTitle(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_TITLE, desc);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieTitle() {
        orderBy(MoviesColumns.FIELD_MOVIE_TITLE, false);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePosterPath(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePosterPathNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePosterPathLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePosterPathContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePosterPathStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePosterPathEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMoviePosterPath(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER_PATH, desc);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMoviePosterPath() {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER_PATH, false);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePoster(byte[]... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POSTER, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePosterNot(byte[]... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POSTER, value);
        return this;
    }


    public FavoritesSelection orderByMoviesFieldMoviePoster(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER, desc);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMoviePoster() {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER, false);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePopularity(Integer... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePopularityNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePopularityGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePopularityGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePopularityLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public FavoritesSelection moviesFieldMoviePopularityLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMoviePopularity(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POPULARITY, desc);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMoviePopularity() {
        orderBy(MoviesColumns.FIELD_MOVIE_POPULARITY, false);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteAverage(Integer... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteAverageNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteAverageGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteAverageGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteAverageLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteAverageLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieVoteAverage(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, desc);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieVoteAverage() {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, false);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteCount(int... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteCountNot(int... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteCountGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteCountGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteCountLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieVoteCountLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieVoteCount(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, desc);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieVoteCount() {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, false);
        return this;
    }

    public FavoritesSelection moviesFieldMovieReleaseDate(Date... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieReleaseDateNot(Date... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieReleaseDate(Long... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieReleaseDateAfter(Date value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieReleaseDateAfterEq(Date value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieReleaseDateBefore(Date value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieReleaseDateBeforeEq(Date value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieReleaseDate(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, desc);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieReleaseDate() {
        orderBy(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, false);
        return this;
    }

    public FavoritesSelection moviesFieldMovieOverview(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieOverviewNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieOverviewLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieOverviewContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieOverviewStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public FavoritesSelection moviesFieldMovieOverviewEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieOverview(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_OVERVIEW, desc);
        return this;
    }

    public FavoritesSelection orderByMoviesFieldMovieOverview() {
        orderBy(MoviesColumns.FIELD_MOVIE_OVERVIEW, false);
        return this;
    }
}
