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

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.smartvariables.lee.popularmovies.data.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code trailers} table.
 */
public class TrailersContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return TrailersColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where           The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable TrailersSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where           The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable TrailersSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public TrailersContentValues putFieldMovieId(long value) {
        mContentValues.put(TrailersColumns.FIELD_MOVIE_ID, value);
        return this;
    }


    /**
     * trailer URL
     */
    public TrailersContentValues putFieldTrailerUrl(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("fieldTrailerUrl must not be null");
        mContentValues.put(TrailersColumns.FIELD_TRAILER_URL, value);
        return this;
    }

}
