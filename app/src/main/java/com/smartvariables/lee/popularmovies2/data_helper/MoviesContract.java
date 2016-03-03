/*
 * This is a helper class for the generated Sqlite 'data' package.
 * This class is not generated, and must be manually updated when
 * database tables are added or removed.  See 'generate_data_contentprovider'
 */
package com.smartvariables.lee.popularmovies2.data_helper;

import android.content.ContentUris;
import android.net.Uri;
import android.text.format.Time;
import android.util.Log;

import com.smartvariables.lee.popularmovies2.data.PopularMoviesProvider;
import com.smartvariables.lee.popularmovies2.data.favorites.FavoritesColumns;
import com.smartvariables.lee.popularmovies2.data.movies.MoviesColumns;
import com.smartvariables.lee.popularmovies2.data.reviews.ReviewsColumns;
import com.smartvariables.lee.popularmovies2.data.trailers.TrailersColumns;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MoviesContract {
    private final static String TAG = "LEE: <" + MoviesContract.class.getSimpleName() + ">";

    // Reference the AUTHORITY generated from 'generate-data-provider.sh'
    public static final String CONTENT_AUTHORITY = PopularMoviesProvider.AUTHORITY;

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static String tmdbDate(long startDate) {
        if (startDate != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startDate);
            if (calendar.get(Calendar.YEAR) > 1900 && calendar.get(Calendar.YEAR) < 2100) {
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                String tmdbDate = dt1.format(calendar.getTime());
                return tmdbDate;
            } else {
                Log.w(TAG, "invalid release date! - calendar=" + calendar);
            }
        }
        return "";
    }

    /* Inner class that defines the table contents of the movies table */
    public static final class MoviesEntry extends MoviesColumns {

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesUri() {
            return CONTENT_URI;
        }

    }

    /* Inner class that defines the table contents of the favorites table */
    public static final class FavoritesEntry extends FavoritesColumns {

        public static Uri buildFavoriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFavoritesUri() {
            return CONTENT_URI;
        }

    }

    /* Inner class that defines the table contents of the trailers table */
    public static final class TrailersEntry extends TrailersColumns {

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTrailersUri() {
            return CONTENT_URI;
        }

    }

    /* Inner class that defines the table contents of the reviews table */
    public static final class ReviewsEntry extends ReviewsColumns {

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewsUri() {
            return CONTENT_URI;
        }

    }

}
