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
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.smartvariables.lee.popularmovies.data.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code reviews} table.
 */
public class ReviewsContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return ReviewsColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable ReviewsSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable ReviewsSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public ReviewsContentValues putFieldMovieId(long value) {
        mContentValues.put(ReviewsColumns.FIELD_MOVIE_ID, value);
        return this;
    }


    /**
     * critic's name
     */
    public ReviewsContentValues putFieldCriticName(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("fieldCriticName must not be null");
        mContentValues.put(ReviewsColumns.FIELD_CRITIC_NAME, value);
        return this;
    }


    /**
     * critic's url
     */
    public ReviewsContentValues putFieldCriticUrl(@Nullable String value) {
        mContentValues.put(ReviewsColumns.FIELD_CRITIC_URL, value);
        return this;
    }

    public ReviewsContentValues putFieldCriticUrlNull() {
        mContentValues.putNull(ReviewsColumns.FIELD_CRITIC_URL);
        return this;
    }

    /**
     * the movie review
     */
    public ReviewsContentValues putFieldReviewText(@NonNull String value) {
        if (value == null) throw new IllegalArgumentException("fieldReviewText must not be null");
        mContentValues.put(ReviewsColumns.FIELD_REVIEW_TEXT, value);
        return this;
    }

}
