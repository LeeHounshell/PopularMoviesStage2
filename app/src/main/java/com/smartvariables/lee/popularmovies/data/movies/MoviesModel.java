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

import com.smartvariables.lee.popularmovies.data.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * information about a movie.
 */
public interface MoviesModel extends BaseModel {

    /**
     * tmdb movie ID
     * Can be {@code null}.
     */
    @Nullable
    Integer getFieldTmdbMovieId();

    /**
     * the movie title
     * Cannot be {@code null}.
     */
    @NonNull
    String getFieldMovieTitle();

    /**
     * path to the movie poster
     * Can be {@code null}.
     */
    @Nullable
    String getFieldMoviePosterPath();

    /**
     * the movie poster
     * Can be {@code null}.
     */
    @Nullable
    byte[] getFieldMoviePoster();

    /**
     * movie popularity 0-10
     * Can be {@code null}.
     */
    @Nullable
    Integer getFieldMoviePopularity();

    /**
     * movie vote average 0-10
     * Can be {@code null}.
     */
    @Nullable
    Integer getFieldMovieVoteAverage();

    /**
     * movie vote count
     */
    int getFieldMovieVoteCount();

    /**
     * movie release date
     * Can be {@code null}.
     */
    @Nullable
    Date getFieldMovieReleaseDate();

    /**
     * the movie overview
     * Can be {@code null}.
     */
    @Nullable
    String getFieldMovieOverview();
}
