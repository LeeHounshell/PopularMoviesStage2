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
package com.smartvariables.lee.popularmovies2.data.trailers;

import com.smartvariables.lee.popularmovies2.data.base.BaseModel;

import android.support.annotation.NonNull;

/**
 * movie trailers. each movie may have 0-n trailers.
 */
public interface TrailersModel extends BaseModel {

    /**
     * Get the {@code field_movie_id} value.
     */
    long getFieldMovieId();

    /**
     * trailer URL
     * Cannot be {@code null}.
     */
    @NonNull
    String getFieldTrailerUrl();
}
