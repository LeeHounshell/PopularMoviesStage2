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
package com.smartvariables.lee.popularmovies2.data.reviews;

import com.smartvariables.lee.popularmovies2.data.base.BaseModel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * movie reviews. each movie may have 0-n reviews.
 */
public interface ReviewsModel extends BaseModel {

    /**
     * Get the {@code field_movie_id} value.
     */
    long getFieldMovieId();

    /**
     * critic's name
     * Cannot be {@code null}.
     */
    @NonNull
    String getFieldCriticName();

    /**
     * critic's url
     * Can be {@code null}.
     */
    @Nullable
    String getFieldCriticUrl();

    /**
     * the movie review
     * Cannot be {@code null}.
     */
    @NonNull
    String getFieldReviewText();
}
