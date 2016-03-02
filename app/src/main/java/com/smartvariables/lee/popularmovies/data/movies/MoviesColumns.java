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
package com.smartvariables.lee.popularmovies.data.movies;

import android.net.Uri;
import android.provider.BaseColumns;

import com.smartvariables.lee.popularmovies.data.PopularMoviesProvider;
import com.smartvariables.lee.popularmovies.data.favorites.FavoritesColumns;
import com.smartvariables.lee.popularmovies.data.movies.MoviesColumns;
import com.smartvariables.lee.popularmovies.data.reviews.ReviewsColumns;
import com.smartvariables.lee.popularmovies.data.trailers.TrailersColumns;

/**
 * information about a movie.
 */
public class MoviesColumns implements BaseColumns {
    public static final String TABLE_NAME = "movies";
    public static final Uri CONTENT_URI = Uri.parse(PopularMoviesProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    /**
     * tmdb movie ID
     */
    public static final String FIELD_TMDB_MOVIE_ID = "field_tmdb_movie_id";

    /**
     * the movie title
     */
    public static final String FIELD_MOVIE_TITLE = "field_movie_title";

    /**
     * path to the movie poster
     */
    public static final String FIELD_MOVIE_POSTER_PATH = "field_movie_poster_path";

    /**
     * the movie poster
     */
    public static final String FIELD_MOVIE_POSTER = "field_movie_poster";

    /**
     * movie popularity 0-10
     */
    public static final String FIELD_MOVIE_POPULARITY = "field_movie_popularity";

    /**
     * movie vote average 0-10
     */
    public static final String FIELD_MOVIE_VOTE_AVERAGE = "field_movie_vote_average";

    /**
     * movie vote count
     */
    public static final String FIELD_MOVIE_VOTE_COUNT = "field_movie_vote_count";

    /**
     * movie release date
     */
    public static final String FIELD_MOVIE_RELEASE_DATE = "field_movie_release_date";

    /**
     * the movie overview
     */
    public static final String FIELD_MOVIE_OVERVIEW = "field_movie_overview";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            FIELD_TMDB_MOVIE_ID,
            FIELD_MOVIE_TITLE,
            FIELD_MOVIE_POSTER_PATH,
            FIELD_MOVIE_POSTER,
            FIELD_MOVIE_POPULARITY,
            FIELD_MOVIE_VOTE_AVERAGE,
            FIELD_MOVIE_VOTE_COUNT,
            FIELD_MOVIE_RELEASE_DATE,
            FIELD_MOVIE_OVERVIEW
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(FIELD_TMDB_MOVIE_ID) || c.contains("." + FIELD_TMDB_MOVIE_ID)) return true;
            if (c.equals(FIELD_MOVIE_TITLE) || c.contains("." + FIELD_MOVIE_TITLE)) return true;
            if (c.equals(FIELD_MOVIE_POSTER_PATH) || c.contains("." + FIELD_MOVIE_POSTER_PATH)) return true;
            if (c.equals(FIELD_MOVIE_POSTER) || c.contains("." + FIELD_MOVIE_POSTER)) return true;
            if (c.equals(FIELD_MOVIE_POPULARITY) || c.contains("." + FIELD_MOVIE_POPULARITY)) return true;
            if (c.equals(FIELD_MOVIE_VOTE_AVERAGE) || c.contains("." + FIELD_MOVIE_VOTE_AVERAGE)) return true;
            if (c.equals(FIELD_MOVIE_VOTE_COUNT) || c.contains("." + FIELD_MOVIE_VOTE_COUNT)) return true;
            if (c.equals(FIELD_MOVIE_RELEASE_DATE) || c.contains("." + FIELD_MOVIE_RELEASE_DATE)) return true;
            if (c.equals(FIELD_MOVIE_OVERVIEW) || c.contains("." + FIELD_MOVIE_OVERVIEW)) return true;
        }
        return false;
    }

}
