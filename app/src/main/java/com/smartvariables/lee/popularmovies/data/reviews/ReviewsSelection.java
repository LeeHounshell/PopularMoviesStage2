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
package com.smartvariables.lee.popularmovies.data.reviews;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.smartvariables.lee.popularmovies.data.base.AbstractSelection;
import com.smartvariables.lee.popularmovies.data.movies.*;

/**
 * Selection for the {@code reviews} table.
 */
public class ReviewsSelection extends AbstractSelection<ReviewsSelection> {
    @Override
    protected Uri baseUri() {
        return ReviewsColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code ReviewsCursor} object, which is positioned before the first entry, or null.
     */
    public ReviewsCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new ReviewsCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public ReviewsCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code ReviewsCursor} object, which is positioned before the first entry, or null.
     */
    public ReviewsCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new ReviewsCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public ReviewsCursor query(Context context) {
        return query(context, null);
    }


    public ReviewsSelection id(long... value) {
        addEquals("reviews." + ReviewsColumns._ID, toObjectArray(value));
        return this;
    }

    public ReviewsSelection idNot(long... value) {
        addNotEquals("reviews." + ReviewsColumns._ID, toObjectArray(value));
        return this;
    }

    public ReviewsSelection orderById(boolean desc) {
        orderBy("reviews." + ReviewsColumns._ID, desc);
        return this;
    }

    public ReviewsSelection orderById() {
        return orderById(false);
    }

    public ReviewsSelection fieldMovieId(long... value) {
        addEquals(ReviewsColumns.FIELD_MOVIE_ID, toObjectArray(value));
        return this;
    }

    public ReviewsSelection fieldMovieIdNot(long... value) {
        addNotEquals(ReviewsColumns.FIELD_MOVIE_ID, toObjectArray(value));
        return this;
    }

    public ReviewsSelection fieldMovieIdGt(long value) {
        addGreaterThan(ReviewsColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection fieldMovieIdGtEq(long value) {
        addGreaterThanOrEquals(ReviewsColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection fieldMovieIdLt(long value) {
        addLessThan(ReviewsColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection fieldMovieIdLtEq(long value) {
        addLessThanOrEquals(ReviewsColumns.FIELD_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection orderByFieldMovieId(boolean desc) {
        orderBy(ReviewsColumns.FIELD_MOVIE_ID, desc);
        return this;
    }

    public ReviewsSelection orderByFieldMovieId() {
        orderBy(ReviewsColumns.FIELD_MOVIE_ID, false);
        return this;
    }

    public ReviewsSelection moviesFieldTmdbMovieId(Integer... value) {
        addEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection moviesFieldTmdbMovieIdNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection moviesFieldTmdbMovieIdGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection moviesFieldTmdbMovieIdGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection moviesFieldTmdbMovieIdLt(int value) {
        addLessThan(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection moviesFieldTmdbMovieIdLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_TMDB_MOVIE_ID, value);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldTmdbMovieId(boolean desc) {
        orderBy(MoviesColumns.FIELD_TMDB_MOVIE_ID, desc);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldTmdbMovieId() {
        orderBy(MoviesColumns.FIELD_TMDB_MOVIE_ID, false);
        return this;
    }

    public ReviewsSelection moviesFieldMovieTitle(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieTitleNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieTitleLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieTitleContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieTitleStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieTitleEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_TITLE, value);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieTitle(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_TITLE, desc);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieTitle() {
        orderBy(MoviesColumns.FIELD_MOVIE_TITLE, false);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePosterPath(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePosterPathNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePosterPathLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePosterPathContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePosterPathStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePosterPathEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_POSTER_PATH, value);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMoviePosterPath(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER_PATH, desc);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMoviePosterPath() {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER_PATH, false);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePoster(byte[]... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POSTER, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePosterNot(byte[]... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POSTER, value);
        return this;
    }


    public ReviewsSelection orderByMoviesFieldMoviePoster(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER, desc);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMoviePoster() {
        orderBy(MoviesColumns.FIELD_MOVIE_POSTER, false);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePopularity(Integer... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePopularityNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePopularityGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePopularityGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePopularityLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public ReviewsSelection moviesFieldMoviePopularityLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_POPULARITY, value);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMoviePopularity(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_POPULARITY, desc);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMoviePopularity() {
        orderBy(MoviesColumns.FIELD_MOVIE_POPULARITY, false);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteAverage(Integer... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteAverageNot(Integer... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteAverageGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteAverageGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteAverageLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteAverageLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, value);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieVoteAverage(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, desc);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieVoteAverage() {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_AVERAGE, false);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteCount(int... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteCountNot(int... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, toObjectArray(value));
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteCountGt(int value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteCountGtEq(int value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteCountLt(int value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieVoteCountLtEq(int value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, value);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieVoteCount(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, desc);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieVoteCount() {
        orderBy(MoviesColumns.FIELD_MOVIE_VOTE_COUNT, false);
        return this;
    }

    public ReviewsSelection moviesFieldMovieReleaseDate(Date... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieReleaseDateNot(Date... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieReleaseDate(Long... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieReleaseDateAfter(Date value) {
        addGreaterThan(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieReleaseDateAfterEq(Date value) {
        addGreaterThanOrEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieReleaseDateBefore(Date value) {
        addLessThan(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieReleaseDateBeforeEq(Date value) {
        addLessThanOrEquals(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, value);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieReleaseDate(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, desc);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieReleaseDate() {
        orderBy(MoviesColumns.FIELD_MOVIE_RELEASE_DATE, false);
        return this;
    }

    public ReviewsSelection moviesFieldMovieOverview(String... value) {
        addEquals(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieOverviewNot(String... value) {
        addNotEquals(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieOverviewLike(String... value) {
        addLike(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieOverviewContains(String... value) {
        addContains(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieOverviewStartsWith(String... value) {
        addStartsWith(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public ReviewsSelection moviesFieldMovieOverviewEndsWith(String... value) {
        addEndsWith(MoviesColumns.FIELD_MOVIE_OVERVIEW, value);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieOverview(boolean desc) {
        orderBy(MoviesColumns.FIELD_MOVIE_OVERVIEW, desc);
        return this;
    }

    public ReviewsSelection orderByMoviesFieldMovieOverview() {
        orderBy(MoviesColumns.FIELD_MOVIE_OVERVIEW, false);
        return this;
    }

    public ReviewsSelection fieldCriticName(String... value) {
        addEquals(ReviewsColumns.FIELD_CRITIC_NAME, value);
        return this;
    }

    public ReviewsSelection fieldCriticNameNot(String... value) {
        addNotEquals(ReviewsColumns.FIELD_CRITIC_NAME, value);
        return this;
    }

    public ReviewsSelection fieldCriticNameLike(String... value) {
        addLike(ReviewsColumns.FIELD_CRITIC_NAME, value);
        return this;
    }

    public ReviewsSelection fieldCriticNameContains(String... value) {
        addContains(ReviewsColumns.FIELD_CRITIC_NAME, value);
        return this;
    }

    public ReviewsSelection fieldCriticNameStartsWith(String... value) {
        addStartsWith(ReviewsColumns.FIELD_CRITIC_NAME, value);
        return this;
    }

    public ReviewsSelection fieldCriticNameEndsWith(String... value) {
        addEndsWith(ReviewsColumns.FIELD_CRITIC_NAME, value);
        return this;
    }

    public ReviewsSelection orderByFieldCriticName(boolean desc) {
        orderBy(ReviewsColumns.FIELD_CRITIC_NAME, desc);
        return this;
    }

    public ReviewsSelection orderByFieldCriticName() {
        orderBy(ReviewsColumns.FIELD_CRITIC_NAME, false);
        return this;
    }

    public ReviewsSelection fieldCriticUrl(String... value) {
        addEquals(ReviewsColumns.FIELD_CRITIC_URL, value);
        return this;
    }

    public ReviewsSelection fieldCriticUrlNot(String... value) {
        addNotEquals(ReviewsColumns.FIELD_CRITIC_URL, value);
        return this;
    }

    public ReviewsSelection fieldCriticUrlLike(String... value) {
        addLike(ReviewsColumns.FIELD_CRITIC_URL, value);
        return this;
    }

    public ReviewsSelection fieldCriticUrlContains(String... value) {
        addContains(ReviewsColumns.FIELD_CRITIC_URL, value);
        return this;
    }

    public ReviewsSelection fieldCriticUrlStartsWith(String... value) {
        addStartsWith(ReviewsColumns.FIELD_CRITIC_URL, value);
        return this;
    }

    public ReviewsSelection fieldCriticUrlEndsWith(String... value) {
        addEndsWith(ReviewsColumns.FIELD_CRITIC_URL, value);
        return this;
    }

    public ReviewsSelection orderByFieldCriticUrl(boolean desc) {
        orderBy(ReviewsColumns.FIELD_CRITIC_URL, desc);
        return this;
    }

    public ReviewsSelection orderByFieldCriticUrl() {
        orderBy(ReviewsColumns.FIELD_CRITIC_URL, false);
        return this;
    }

    public ReviewsSelection fieldReviewText(String... value) {
        addEquals(ReviewsColumns.FIELD_REVIEW_TEXT, value);
        return this;
    }

    public ReviewsSelection fieldReviewTextNot(String... value) {
        addNotEquals(ReviewsColumns.FIELD_REVIEW_TEXT, value);
        return this;
    }

    public ReviewsSelection fieldReviewTextLike(String... value) {
        addLike(ReviewsColumns.FIELD_REVIEW_TEXT, value);
        return this;
    }

    public ReviewsSelection fieldReviewTextContains(String... value) {
        addContains(ReviewsColumns.FIELD_REVIEW_TEXT, value);
        return this;
    }

    public ReviewsSelection fieldReviewTextStartsWith(String... value) {
        addStartsWith(ReviewsColumns.FIELD_REVIEW_TEXT, value);
        return this;
    }

    public ReviewsSelection fieldReviewTextEndsWith(String... value) {
        addEndsWith(ReviewsColumns.FIELD_REVIEW_TEXT, value);
        return this;
    }

    public ReviewsSelection orderByFieldReviewText(boolean desc) {
        orderBy(ReviewsColumns.FIELD_REVIEW_TEXT, desc);
        return this;
    }

    public ReviewsSelection orderByFieldReviewText() {
        orderBy(ReviewsColumns.FIELD_REVIEW_TEXT, false);
        return this;
    }
}
