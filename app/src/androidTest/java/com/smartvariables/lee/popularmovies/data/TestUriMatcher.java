/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartvariables.lee.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.smartvariables.lee.popularmovies.data.movies.MoviesColumns;
import com.smartvariables.lee.popularmovies.data.reviews.ReviewsColumns;
import com.smartvariables.lee.popularmovies.data.trailers.TrailersColumns;
import com.smartvariables.lee.popularmovies.data_helper.MoviesContract;

/*
    this class utilizes constants declared with package protection inside of the UriMatcher.
    This is why the test must be in the same data package as the Android app code.
    It is a compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {

    // content://com.smartvariables.lee.popularmovies.data.popularmoviesprovider/movies
    private static final Uri TEST_MOVIE_DIR = MoviesColumns.CONTENT_URI;
    private static final Uri TEST_TRAILERS_DIR = TrailersColumns.CONTENT_URI;
    private static final Uri TEST_REVIEWS_DIR = ReviewsColumns.CONTENT_URI;

    // note: these must match the generated data/PopularMoviesProvider.java
    private static final int URI_TYPE_MOVIES = 2;
    private static final int URI_TYPE_MOVIES_ID = 3;

    private static final int URI_TYPE_REVIEWS = 4;
    private static final int URI_TYPE_REVIEWS_ID = 5;

    private static final int URI_TYPE_TRAILERS = 6;
    private static final int URI_TYPE_TRAILERS_ID = 7;

    // Test that the UriMatcher returns the correct integer value
    // for each of the Uri types the ContentProvider can handle.
    public void testUriMatcher() {
        UriMatcher testMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        testMatcher.addURI(PopularMoviesProvider.AUTHORITY, MoviesColumns.TABLE_NAME, URI_TYPE_MOVIES);
        testMatcher.addURI(PopularMoviesProvider.AUTHORITY, MoviesColumns.TABLE_NAME + "/#", URI_TYPE_MOVIES_ID);
        testMatcher.addURI(PopularMoviesProvider.AUTHORITY, ReviewsColumns.TABLE_NAME, URI_TYPE_REVIEWS);
        testMatcher.addURI(PopularMoviesProvider.AUTHORITY, ReviewsColumns.TABLE_NAME + "/#", URI_TYPE_REVIEWS_ID);
        testMatcher.addURI(PopularMoviesProvider.AUTHORITY, TrailersColumns.TABLE_NAME, URI_TYPE_TRAILERS);
        testMatcher.addURI(PopularMoviesProvider.AUTHORITY, TrailersColumns.TABLE_NAME + "/#", URI_TYPE_TRAILERS_ID);

        assertEquals("Error: The MOVIES URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), testMatcher.match(MoviesContract.MoviesEntry.buildMoviesUri()));
        assertEquals("Error: The TRAILERS URI was matched incorrectly.",
                testMatcher.match(TEST_TRAILERS_DIR), testMatcher.match(MoviesContract.TrailersEntry.buildTrailersUri()));
        assertEquals("Error: The REVIEWS URI was matched incorrectly.",
                testMatcher.match(TEST_REVIEWS_DIR), testMatcher.match(MoviesContract.ReviewsEntry.buildReviewsUri()));
    }

}
