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

import com.smartvariables.lee.popularmovies.data.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
