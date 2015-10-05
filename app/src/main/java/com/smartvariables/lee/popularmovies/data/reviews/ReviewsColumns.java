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

import android.net.Uri;
import android.provider.BaseColumns;

import com.smartvariables.lee.popularmovies.data.PopularMoviesProvider;
import com.smartvariables.lee.popularmovies.data.movies.MoviesColumns;

/**
 * movie reviews. each movie may have 0-n reviews.
 */
public class ReviewsColumns implements BaseColumns {
    public static final String TABLE_NAME = "reviews";
    public static final Uri CONTENT_URI = Uri.parse(PopularMoviesProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String FIELD_MOVIE_ID = "field_movie_id";

    /**
     * critic's name
     */
    public static final String FIELD_CRITIC_NAME = "field_critic_name";

    /**
     * critic's url
     */
    public static final String FIELD_CRITIC_URL = "field_critic_url";

    /**
     * the movie review
     */
    public static final String FIELD_REVIEW_TEXT = "field_review_text";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." + _ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[]{
            _ID,
            FIELD_MOVIE_ID,
            FIELD_CRITIC_NAME,
            FIELD_CRITIC_URL,
            FIELD_REVIEW_TEXT
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(FIELD_MOVIE_ID) || c.contains("." + FIELD_MOVIE_ID)) return true;
            if (c.equals(FIELD_CRITIC_NAME) || c.contains("." + FIELD_CRITIC_NAME)) return true;
            if (c.equals(FIELD_CRITIC_URL) || c.contains("." + FIELD_CRITIC_URL)) return true;
            if (c.equals(FIELD_REVIEW_TEXT) || c.contains("." + FIELD_REVIEW_TEXT)) return true;
        }
        return false;
    }

    public static final String PREFIX_MOVIES = TABLE_NAME + "__" + MoviesColumns.TABLE_NAME;
}
