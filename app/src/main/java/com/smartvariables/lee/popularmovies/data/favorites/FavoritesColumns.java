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
package com.smartvariables.lee.popularmovies.data.favorites;

import android.net.Uri;
import android.provider.BaseColumns;

import com.smartvariables.lee.popularmovies.data.PopularMoviesProvider;
import com.smartvariables.lee.popularmovies.data.movies.MoviesColumns;

/**
 * movie favorites.
 */
public class FavoritesColumns implements BaseColumns {
    public static final String TABLE_NAME = "favorites";
    public static final Uri CONTENT_URI = Uri.parse(PopularMoviesProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String FIELD_MOVIE_ID = "field_movie_id";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." + _ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[]{
            _ID,
            FIELD_MOVIE_ID
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(FIELD_MOVIE_ID) || c.contains("." + FIELD_MOVIE_ID)) return true;
        }
        return false;
    }

    public static final String PREFIX_MOVIES = TABLE_NAME + "__" + MoviesColumns.TABLE_NAME;
}
